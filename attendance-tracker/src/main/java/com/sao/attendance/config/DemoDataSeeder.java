package com.sao.attendance.config;

import com.sao.attendance.entity.AttendanceRecord;
import com.sao.attendance.entity.AttendanceStatus;
import com.sao.attendance.entity.Student;
import com.sao.attendance.repository.AttendanceRecordRepository;
import com.sao.attendance.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

/**
 * Populates the in-memory H2 database with a handful of students on
 * startup so the dashboard is populated immediately, without requiring
 * a live Google Sheets connection. Safe to delete once you are syncing
 * real data - it only inserts if the students table is empty.
 */
@Configuration
public class DemoDataSeeder {

    @Bean
    public CommandLineRunner seedDemoData(StudentRepository studentRepository,
                                           AttendanceRecordRepository attendanceRecordRepository) {
        return args -> {
            if (studentRepository.count() > 0) {
                return;
            }

            Student kirito = studentRepository.save(new Student("Kazuto Kirigaya", "S001", "Knights of the Blood"));
            Student asuna = studentRepository.save(new Student("Asuna Yuuki", "S002", "Knights of the Blood"));
            Student klein = studentRepository.save(new Student("Klein", "S003", "Fuurinkazan"));
            Student silica = studentRepository.save(new Student("Silica", "S004", "Independent"));
            studentRepository.save(new Student("Lisbeth", "S005", "Independent"));

            LocalDate today = LocalDate.now();
            attendanceRecordRepository.save(new AttendanceRecord(kirito, today, AttendanceStatus.PRESENT));
            attendanceRecordRepository.save(new AttendanceRecord(asuna, today, AttendanceStatus.PRESENT));
            attendanceRecordRepository.save(new AttendanceRecord(klein, today, AttendanceStatus.LATE));
            attendanceRecordRepository.save(new AttendanceRecord(silica, today, AttendanceStatus.ABSENT));
            // Lisbeth intentionally left unmarked to demonstrate the "NOT_MARKED" state.

            attendanceRecordRepository.save(new AttendanceRecord(kirito, today.minusDays(1), AttendanceStatus.PRESENT));
            attendanceRecordRepository.save(new AttendanceRecord(asuna, today.minusDays(1), AttendanceStatus.PRESENT));
            attendanceRecordRepository.save(new AttendanceRecord(klein, today.minusDays(1), AttendanceStatus.ABSENT));
        };
    }
}
