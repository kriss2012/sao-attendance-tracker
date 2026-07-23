package com.sao.attendance.exception;

import java.time.LocalDate;

/**
 * Thrown when an operation requires an existing attendance record
 * (e.g. editing today's mark) but none has been created yet.
 */
public class AttendanceNotFoundException extends RuntimeException {

    public AttendanceNotFoundException(String message) {
        super(message);
    }

    public static AttendanceNotFoundException forStudentAndDate(String rollNumber, LocalDate date) {
        return new AttendanceNotFoundException(
                "No attendance has been marked for student '" + rollNumber + "' on " + date + ".");
    }
}
