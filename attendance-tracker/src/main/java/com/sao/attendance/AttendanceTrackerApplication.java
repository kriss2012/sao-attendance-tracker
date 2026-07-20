package com.sao.attendance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the SAO Attendance Tracker.
 * <p>
 * A Spring Boot service that keeps a local database of students and their
 * daily attendance, exposing both a REST API and a "Link Start" themed
 * web dashboard.
 */
@SpringBootApplication
public class AttendanceTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AttendanceTrackerApplication.class, args);
    }
}
