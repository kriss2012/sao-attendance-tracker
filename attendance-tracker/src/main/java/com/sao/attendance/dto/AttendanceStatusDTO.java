package com.sao.attendance.dto;

import com.sao.attendance.entity.AttendanceStatus;

import java.time.LocalDate;

/**
 * Answer to "is attendance marked for this student, and if so what is it".
 * {@code marked=false} means no record exists yet - the front end and API
 * consumers use this flag rather than inferring it from a null/enum trick.
 */
public class AttendanceStatusDTO {

    private final String rollNumber;
    private final String studentName;
    private final LocalDate date;
    private final boolean marked;
    private final AttendanceStatus status;

    public AttendanceStatusDTO(String rollNumber, String studentName, LocalDate date,
                                boolean marked, AttendanceStatus status) {
        this.rollNumber = rollNumber;
        this.studentName = studentName;
        this.date = date;
        this.marked = marked;
        this.status = status;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public String getStudentName() {
        return studentName;
    }

    public LocalDate getDate() {
        return date;
    }

    public boolean isMarked() {
        return marked;
    }

    public AttendanceStatus getStatus() {
        return status;
    }
}
