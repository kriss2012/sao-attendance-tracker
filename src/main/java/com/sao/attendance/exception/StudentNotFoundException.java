package com.sao.attendance.exception;

/**
 * Thrown when a lookup by roll number or id does not match any known student.
 */
public class StudentNotFoundException extends RuntimeException {

    public StudentNotFoundException(String message) {
        super(message);
    }

    public static StudentNotFoundException forRollNumber(String rollNumber) {
        return new StudentNotFoundException("No student found with roll number '" + rollNumber + "'.");
    }
}
