package com.sao.attendance.service.impl;

import com.sao.attendance.dto.AttendanceStatusDTO;
import com.sao.attendance.dto.DashboardSummaryDTO;
import com.sao.attendance.entity.AttendanceRecord;
import com.sao.attendance.entity.AttendanceStatus;
import com.sao.attendance.entity.Student;
import com.sao.attendance.exception.AttendanceNotFoundException;
import com.sao.attendance.exception.DuplicateAttendanceException;
import com.sao.attendance.exception.StudentNotFoundException;
import com.sao.attendance.repository.AttendanceRecordRepository;
import com.sao.attendance.repository.StudentRepository;
import com.sao.attendance.service.AttendanceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    private final StudentRepository studentRepository;
    private final AttendanceRecordRepository attendanceRecordRepository;

    public AttendanceServiceImpl(StudentRepository studentRepository,
                                  AttendanceRecordRepository attendanceRecordRepository) {
        this.studentRepository = studentRepository;
        this.attendanceRecordRepository = attendanceRecordRepository;
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Optional<Student> findStudentByRollNumber(String rollNumber) {
        return studentRepository.findByRollNumber(rollNumber);
    }

    @Override
    public Student getStudentByRollNumber(String rollNumber) {
        return studentRepository.findByRollNumber(rollNumber)
                .orElseThrow(() -> StudentNotFoundException.forRollNumber(rollNumber));
    }

    @Override
    @Transactional
    public Student createStudent(String rollNumber, String name, String guild) {
        if (studentRepository.existsByRollNumber(rollNumber)) {
            throw new IllegalArgumentException("Roll number already exists: " + rollNumber);
        }
        return studentRepository.save(new Student(name, rollNumber, guild));
    }

    @Override
    @Transactional
    public Student updateStudent(String rollNumber, String name, String guild) {
        Student student = getStudentByRollNumber(rollNumber);
        student.setName(name);
        student.setGuild(guild);
        return studentRepository.save(student);
    }

    @Override
    @Transactional
    public void deleteStudent(String rollNumber) {
        Student student = getStudentByRollNumber(rollNumber);
        studentRepository.delete(student);
    }

    @Override
    public AttendanceStatusDTO getStatus(String rollNumber, LocalDate date) {
        Student student = getStudentByRollNumber(rollNumber);

        Optional<AttendanceRecord> record =
                attendanceRecordRepository.findByStudentAndAttendanceDate(student, date);

        if (record.isEmpty()) {
            return new AttendanceStatusDTO(rollNumber, student.getName(), date, false, AttendanceStatus.NOT_MARKED);
        }

        return new AttendanceStatusDTO(rollNumber, student.getName(), date, true, record.get().getStatus());
    }

    @Override
    public List<AttendanceRecord> getHistory(String rollNumber) {
        Student student = getStudentByRollNumber(rollNumber);
        return attendanceRecordRepository.findByStudentOrderByAttendanceDateDesc(student);
    }

    @Override
    @Transactional
    public AttendanceRecord markAttendance(String rollNumber, LocalDate date, AttendanceStatus status) {
        Student student = getStudentByRollNumber(rollNumber);

        boolean alreadyMarked = attendanceRecordRepository
                .findByStudentAndAttendanceDate(student, date)
                .isPresent();

        if (alreadyMarked) {
            throw DuplicateAttendanceException.forStudentAndDate(rollNumber, date);
        }

        AttendanceRecord record = new AttendanceRecord(student, date, status);
        return attendanceRecordRepository.save(record);
    }

    @Override
    @Transactional
    public AttendanceRecord updateAttendance(String rollNumber, LocalDate date, AttendanceStatus status) {
        Student student = getStudentByRollNumber(rollNumber);

        AttendanceRecord record = attendanceRecordRepository
                .findByStudentAndAttendanceDate(student, date)
                .orElseThrow(() -> AttendanceNotFoundException.forStudentAndDate(rollNumber, date));

        record.setStatus(status);
        return attendanceRecordRepository.save(record);
    }

    @Override
    @Transactional
    public AttendanceRecord saveAttendance(String rollNumber, LocalDate date, AttendanceStatus status) {
        Student student = getStudentByRollNumber(rollNumber);

        Optional<AttendanceRecord> existing = attendanceRecordRepository.findByStudentAndAttendanceDate(student, date);
        if (existing.isPresent()) {
            AttendanceRecord record = existing.get();
            record.setStatus(status);
            return attendanceRecordRepository.save(record);
        }

        AttendanceRecord record = new AttendanceRecord(student, date, status);
        return attendanceRecordRepository.save(record);
    }

    @Override
    public DashboardSummaryDTO getDashboardSummary(LocalDate date) {
        long total = studentRepository.count();
        long present = attendanceRecordRepository.countByAttendanceDateAndStatus(date, AttendanceStatus.PRESENT);
        long absent = attendanceRecordRepository.countByAttendanceDateAndStatus(date, AttendanceStatus.ABSENT);
        long late = attendanceRecordRepository.countByAttendanceDateAndStatus(date, AttendanceStatus.LATE);
        long marked = attendanceRecordRepository.findByAttendanceDate(date).size();
        long notMarked = Math.max(0, total - marked);

        return new DashboardSummaryDTO(date, total, present, absent, late, notMarked);
    }
}
