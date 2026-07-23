package com.sao.attendance.dto;

import java.time.LocalDate;

/**
 * Aggregate counts used to render the dashboard's radial stat gauges.
 */
public class DashboardSummaryDTO {

    private final LocalDate date;
    private final long totalStudents;
    private final long presentCount;
    private final long absentCount;
    private final long lateCount;
    private final long notMarkedCount;

    public DashboardSummaryDTO(LocalDate date, long totalStudents, long presentCount,
                                long absentCount, long lateCount, long notMarkedCount) {
        this.date = date;
        this.totalStudents = totalStudents;
        this.presentCount = presentCount;
        this.absentCount = absentCount;
        this.lateCount = lateCount;
        this.notMarkedCount = notMarkedCount;
    }

    public LocalDate getDate() {
        return date;
    }

    public long getTotalStudents() {
        return totalStudents;
    }

    public long getPresentCount() {
        return presentCount;
    }

    public long getAbsentCount() {
        return absentCount;
    }

    public long getLateCount() {
        return lateCount;
    }

    public long getNotMarkedCount() {
        return notMarkedCount;
    }

    public double getAttendanceRate() {
        if (totalStudents == 0) {
            return 0.0;
        }
        return (presentCount + lateCount) * 100.0 / totalStudents;
    }
}
