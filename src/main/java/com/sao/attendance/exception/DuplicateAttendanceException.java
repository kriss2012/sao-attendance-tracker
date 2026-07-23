package com.sao.attendance.exception;

import java.time.LocalDate;

/**
 * Thrown when attempting to create a fresh attendance mark for a
 * student/date pair that already has one. Callers should use the
 * update endpoint instead of mark when this is thrown.
 */
public class DuplicateAttendanceException extends RuntimeException {

    public DuplicateAttendanceException(String message) {
        super(message);
    }

    public static DuplicateAttendanceException forStudentAndDate(String rollNumber, LocalDate date) {
        return new DuplicateAttendanceException(
                "Attendance for student '" + rollNumber + "' on " + date
                        + " is already marked. Use the update endpoint to change it.");
    }
}
