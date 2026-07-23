package com.sao.attendance.controller.web;

import com.sao.attendance.dto.AttendanceStatusDTO;
import com.sao.attendance.entity.AttendanceRecord;
import com.sao.attendance.entity.AttendanceStatus;
import com.sao.attendance.entity.Student;
import com.sao.attendance.service.AttendanceService;
import com.sao.attendance.service.GoogleSheetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
public class DashboardController {

    private final AttendanceService attendanceService;
    private final GoogleSheetService googleSheetService;

    public DashboardController(AttendanceService attendanceService, GoogleSheetService googleSheetService) {
        this.attendanceService = attendanceService;
        this.googleSheetService = googleSheetService;
    }

    /** "Link Start" splash screen. */
    @GetMapping("/")
    public String linkStart() {
        return "index";
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
        return "student-profile";
    }

    @PostMapping("/dashboard/mark")
    public String markFromDashboard(@RequestParam String rollNumber,
                                     @RequestParam AttendanceStatus status,
                                     RedirectAttributes redirectAttributes) {
        attendanceService.markAttendance(rollNumber, LocalDate.now(), status);
        redirectAttributes.addFlashAttribute("successMsg", "Attendance recorded for " + rollNumber + ".");
        return "redirect:/dashboard";
    }

    @PostMapping("/dashboard/sync")
    public String syncFromSheet(RedirectAttributes redirectAttributes) {
        int synced = googleSheetService.syncAttendanceFromSheet();
        redirectAttributes.addFlashAttribute("successMsg", "Cardinal System sync complete: " + synced + " row(s) merged.");
        return "redirect:/dashboard";
    }
}
