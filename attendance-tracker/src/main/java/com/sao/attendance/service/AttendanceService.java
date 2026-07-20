package com.sao.attendance.service;

import com.sao.attendance.dto.AttendanceStatusDTO;
import com.sao.attendance.dto.DashboardSummaryDTO;
import com.sao.attendance.entity.AttendanceRecord;
import com.sao.attendance.entity.AttendanceStatus;
import com.sao.attendance.entity.Student;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Core business operations for students and their attendance.
 * Implementations are responsible for throwing the appropriate custom
 * exception (StudentNotFoundException, AttendanceNotFoundException,
 * DuplicateAttendanceException) rather than letting persistence-level
 * exceptions leak to callers.
 */
public interface AttendanceService {

    List<Student> getAllStudents();

    Optional<Student> findStudentByRollNumber(String rollNumber);

    Student getStudentByRollNumber(String rollNumber);

    Student createStudent(String rollNumber, String name, String guild);

    Student updateStudent(String rollNumber, String name, String guild);

    void deleteStudent(String rollNumber);

    /**
     * Answers "has attendance been marked for this student on this date, and if so what is it".
     */
    AttendanceStatusDTO getStatus(String rollNumber, LocalDate date);

    List<AttendanceRecord> getHistory(String rollNumber);

    AttendanceRecord markAttendance(String rollNumber, LocalDate date, AttendanceStatus status);

    AttendanceRecord updateAttendance(String rollNumber, LocalDate date, AttendanceStatus status);

    AttendanceRecord saveAttendance(String rollNumber, LocalDate date, AttendanceStatus status);

    DashboardSummaryDTO getDashboardSummary(LocalDate date);
}
