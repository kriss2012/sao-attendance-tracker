package com.sao.attendance.dto;

import com.sao.attendance.entity.AttendanceStatus;

import java.time.LocalDate;

/**
 * A single parsed row from the connected Google Sheet, before it has
 * been reconciled against the local database. Kept separate from the
 * JPA entities so a malformed or partial sheet row never leaks into
 * persistence code.
 */
public class SheetRowDTO {

    private final String rollNumber;
    private final String name;
    private final String guild;
    private final LocalDate date;
    private final AttendanceStatus status;

    public SheetRowDTO(String rollNumber, String name, String guild, LocalDate date, AttendanceStatus status) {
        this.rollNumber = rollNumber;
        this.name = name;
        this.guild = guild;
        this.date = date;
        this.status = status;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public String getName() {
        return name;
    }

    public String getGuild() {
        return guild;
    }

    public LocalDate getDate() {
        return date;
    }

    public AttendanceStatus getStatus() {
        return status;
    }
}
