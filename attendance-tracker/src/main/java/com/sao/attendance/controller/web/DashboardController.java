package com.sao.attendance.controller.web;

import com.sao.attendance.dto.AttendanceStatusDTO;
import com.sao.attendance.entity.AttendanceRecord;
import com.sao.attendance.entity.AttendanceStatus;
import com.sao.attendance.entity.Student;
import com.sao.attendance.exception.DuplicateAttendanceException;
import com.sao.attendance.exception.StudentNotFoundException;
import com.sao.attendance.service.AttendanceService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Controller
public class DashboardController {

    private final AttendanceService attendanceService;

    public DashboardController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @GetMapping("/")
    public String loginPage(@RequestParam(required = false) String error,
                            @RequestParam(required = false) String success,
                            HttpSession session,
                            Model model) {
        if (session.getAttribute("userRoll") != null) {
            return "redirect:/profile";
        }
        if (error != null) {
            model.addAttribute("errorMsg", error);
        }
        if (success != null) {
            model.addAttribute("successMsg", success);
        }
        return "index";
    }

    @PostMapping("/login")
    public String login(@RequestParam String rollNumber,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {
        String normalized = rollNumber == null ? "" : rollNumber.trim();
        if (normalized.isBlank()) {
            redirectAttributes.addFlashAttribute("errorMsg", "Please enter your roll number.");
            return "redirect:/";
        }

        Optional<Student> student = attendanceService.findStudentByRollNumber(normalized);
        if (student.isPresent()) {
            session.setAttribute("userRoll", normalized);
            return "redirect:/profile";
        }

        redirectAttributes.addFlashAttribute("errorMsg",
                "No player found for roll number '" + normalized + "'. Please register first.");
        return "redirect:/register?rollNumber=" + normalized;
    }

    @GetMapping("/register")
    public String registerForm(@RequestParam(required = false) String rollNumber, Model model) {
        model.addAttribute("rollNumber", rollNumber == null ? "" : rollNumber);
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String rollNumber,
                           @RequestParam String name,
                           @RequestParam(required = false, defaultValue = "") String guild,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {
        String normalizedRoll = rollNumber == null ? "" : rollNumber.trim();
        String normalizedName = name == null ? "" : name.trim();

        if (normalizedRoll.isBlank() || normalizedName.isBlank()) {
            redirectAttributes.addFlashAttribute("errorMsg", "Roll number and name are required.");
            return "redirect:/register?rollNumber=" + normalizedRoll;
        }

        if (attendanceService.findStudentByRollNumber(normalizedRoll).isPresent()) {
            redirectAttributes.addFlashAttribute("errorMsg",
                    "Roll number '" + normalizedRoll + "' already exists. Please login.");
            return "redirect:/";
        }

        attendanceService.createStudent(normalizedRoll, normalizedName, guild.trim());
        session.setAttribute("userRoll", normalizedRoll);
        redirectAttributes.addFlashAttribute("successMsg", "Player registered successfully.");
        return "redirect:/profile";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        LocalDate today = LocalDate.now();
        List<Student> students = attendanceService.getAllStudents();

        List<AttendanceStatusDTO> statuses = students.stream()
                .map(s -> attendanceService.getStatus(s.getRollNumber(), today))
                .toList();

        model.addAttribute("today", today);
        model.addAttribute("summary", attendanceService.getDashboardSummary(today));
        model.addAttribute("statuses", statuses);
        model.addAttribute("statusOptions", AttendanceStatus.values());
        return "dashboard";
    }

    @GetMapping("/students/{rollNumber}")
    public String studentProfile(@PathVariable String rollNumber, Model model) {
        Student student = attendanceService.getStudentByRollNumber(rollNumber);
        List<AttendanceRecord> history = attendanceService.getHistory(rollNumber);
        model.addAttribute("student", student);
        model.addAttribute("history", history);
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("statusOptions", AttendanceStatus.values());
        return "student-profile";
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, RedirectAttributes redirectAttributes, Model model) {
        String rollNumber = (String) session.getAttribute("userRoll");
        if (rollNumber == null) {
            redirectAttributes.addFlashAttribute("errorMsg", "Please log in first.");
            return "redirect:/";
        }

        Student student = attendanceService.getStudentByRollNumber(rollNumber);
        List<AttendanceRecord> history = attendanceService.getHistory(rollNumber);
        AttendanceStatusDTO statusDto = attendanceService.getStatus(rollNumber, LocalDate.now());

        model.addAttribute("student", student);
        model.addAttribute("history", history);
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("statusOptions", AttendanceStatus.values());
        model.addAttribute("studentStatus", statusDto.getStatus());
        return "profile";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("successMsg", "You have been logged out.");
        return "redirect:/";
    }

    @PostMapping("/dashboard/mark")
    public String markFromDashboard(@RequestParam String rollNumber,
                                    @RequestParam AttendanceStatus status,
                                    RedirectAttributes redirectAttributes) {
        attendanceService.markAttendance(rollNumber, LocalDate.now(), status);
        redirectAttributes.addFlashAttribute("successMsg", "Attendance recorded for " + rollNumber + ".");
        return "redirect:/dashboard";
    }

    @GetMapping("/students/{rollNumber}/edit")
    public String editStudent(@PathVariable String rollNumber, Model model) {
        Student student = attendanceService.getStudentByRollNumber(rollNumber);
        model.addAttribute("student", student);
        return "student-edit";
    }

    @PostMapping("/students/{rollNumber}/edit")
    public String updateStudent(@PathVariable String rollNumber,
                                @RequestParam String name,
                                @RequestParam(required = false, defaultValue = "") String guild,
                                RedirectAttributes redirectAttributes) {
        attendanceService.updateStudent(rollNumber, name.trim(), guild.trim());
        redirectAttributes.addFlashAttribute("successMsg", "Profile updated for " + rollNumber + ".");
        return "redirect:/students/" + rollNumber;
    }

    @PostMapping("/students/{rollNumber}/delete")
    public String deleteStudent(@PathVariable String rollNumber, RedirectAttributes redirectAttributes) {
        attendanceService.deleteStudent(rollNumber);
        redirectAttributes.addFlashAttribute("successMsg", "Player " + rollNumber + " has been removed.");
        return "redirect:/dashboard";
    }

    @PostMapping("/students/{rollNumber}/attendance")
    public String saveAttendance(@PathVariable String rollNumber,
                                 @RequestParam String attendanceDate,
                                 @RequestParam AttendanceStatus status,
                                 RedirectAttributes redirectAttributes) {
        try {
            LocalDate date = LocalDate.parse(attendanceDate);
            attendanceService.saveAttendance(rollNumber, date, status);
            redirectAttributes.addFlashAttribute("successMsg",
                    "Attendance saved for " + rollNumber + " on " + date + ".");
            return "redirect:/students/" + rollNumber;
        } catch (DateTimeParseException ex) {
            redirectAttributes.addFlashAttribute("errorMsg", "Invalid date format. Use YYYY-MM-DD.");
            return "redirect:/students/" + rollNumber;
        } catch (StudentNotFoundException ex) {
            redirectAttributes.addFlashAttribute("errorMsg", ex.getMessage());
            return "redirect:/";
        } catch (DuplicateAttendanceException ex) {
            redirectAttributes.addFlashAttribute("successMsg", "Attendance record already exists and was updated.");
            return "redirect:/students/" + rollNumber;
        }
    }
}
