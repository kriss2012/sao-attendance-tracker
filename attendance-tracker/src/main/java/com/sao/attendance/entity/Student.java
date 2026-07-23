package com.sao.attendance.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.util.ArrayList;
import java.util.List;

/**
 * A student (a "player", in the theme of the front end) tracked by the
 * system. {@code rollNumber} is the natural key used to match rows coming
 * back from the connected Google Sheet to a local record.
 */
@Entity
@Table(name = "students", uniqueConstraints = @UniqueConstraint(columnNames = "roll_number"))
public class Student extends AbstractAuditable {

    @Column(nullable = false, length = 120)
    private String name;

    @Column(name = "roll_number", nullable = false, length = 50)
    private String rollNumber;

    /** SAO-flavoured field: which class section / "guild" the student belongs to. */
    @Column(name = "guild", length = 120)
    private String guild;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<AttendanceRecord> attendanceRecords = new ArrayList<>();

    public Student() {
    }

    public Student(String name, String rollNumber, String guild) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.guild = guild;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getGuild() {
        return guild;
    }

    public void setGuild(String guild) {
        this.guild = guild;
    }

    public List<AttendanceRecord> getAttendanceRecords() {
        return attendanceRecords;
    }

    public void setAttendanceRecords(List<AttendanceRecord> attendanceRecords) {
        this.attendanceRecords = attendanceRecords;
    }
}
