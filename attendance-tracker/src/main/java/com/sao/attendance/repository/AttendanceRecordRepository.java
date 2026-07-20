package com.sao.attendance.repository;

import com.sao.attendance.entity.AttendanceRecord;
import com.sao.attendance.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Long> {

    Optional<AttendanceRecord> findByStudentAndAttendanceDate(Student student, LocalDate attendanceDate);

    List<AttendanceRecord> findByAttendanceDate(LocalDate attendanceDate);

    List<AttendanceRecord> findByStudentOrderByAttendanceDateDesc(Student student);

    long countByAttendanceDateAndStatus(LocalDate attendanceDate, com.sao.attendance.entity.AttendanceStatus status);
}
