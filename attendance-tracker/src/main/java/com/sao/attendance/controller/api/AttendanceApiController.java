package com.sao.attendance.controller.api;

import com.sao.attendance.dto.AttendanceStatusDTO;
import com.sao.attendance.dto.DashboardSummaryDTO;
import com.sao.attendance.dto.MarkAttendanceRequest;
import com.sao.attendance.entity.AttendanceRecord;
import com.sao.attendance.entity.Student;
import com.sao.attendance.service.AttendanceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * JSON REST API for the attendance system. See {@link ApiExceptionHandler}
 * for how thrown exceptions are turned into HTTP responses.
 */
@RestController
@RequestMapping("/api")
public class AttendanceApiController {

    private final AttendanceService attendanceService;

    public AttendanceApiController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @GetMapping("/students")
    public List<Student> getAllStudents() {
        return attendanceService.getAllStudents();
    }

    @GetMapping("/students/{rollNumber}")
    public Student getStudent(@PathVariable String rollNumber) {
        return attendanceService.getStudentByRollNumber(rollNumber);
    }

    @GetMapping("/attendance/status/{rollNumber}")
    public AttendanceStatusDTO getStatus(@PathVariable String rollNumber,
                                          @RequestParam(required = false)
                                          @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE)
                                          LocalDate date) {
        return attendanceService.getStatus(rollNumber, date != null ? date : LocalDate.now());
    }

    @GetMapping("/attendance/history/{rollNumber}")
    public List<AttendanceRecord> getHistory(@PathVariable String rollNumber) {
        return attendanceService.getHistory(rollNumber);
    }

    @PostMapping("/attendance/mark")
    public ResponseEntity<AttendanceRecord> markAttendance(@Valid @RequestBody MarkAttendanceRequest request) {
        AttendanceRecord record = attendanceService.markAttendance(
                request.getRollNumber(), request.getDate(), request.getStatus());
        return ResponseEntity.status(HttpStatus.CREATED).body(record);
    }

    @PutMapping("/attendance/update")
    public AttendanceRecord updateAttendance(@Valid @RequestBody MarkAttendanceRequest request) {
        return attendanceService.updateAttendance(
                request.getRollNumber(), request.getDate(), request.getStatus());
    }

    @GetMapping("/attendance/summary")
    public DashboardSummaryDTO getSummary(@RequestParam(required = false)
                                           @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE)
                                           LocalDate date) {
        return attendanceService.getDashboardSummary(date != null ? date : LocalDate.now());
    }
}
