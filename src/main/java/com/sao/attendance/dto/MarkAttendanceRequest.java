package com.sao.attendance.dto;

import com.sao.attendance.entity.AttendanceStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * Request body for POST /api/attendance/mark and PUT /api/attendance/update.
 */
public class MarkAttendanceRequest {

    @NotBlank(message = "rollNumber is required")
    private String rollNumber;

    @NotNull(message = "date is required")
    private LocalDate date;

    @NotNull(message = "status is required")
    private AttendanceStatus status;

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public AttendanceStatus getStatus() {
        return status;
    }

    public void setStatus(AttendanceStatus status) {
        this.status = status;
    }
}
