package com.sao.attendance.entity;

/**
 * Possible states a single day's attendance record can be in.
 * {@code NOT_MARKED} is never actually persisted - it is the value the
 * service layer returns when no {@link AttendanceRecord} exists yet for a
 * given student/date pair, so callers can tell "absent" apart from
 * "nobody has taken attendance yet".
 */
public enum AttendanceStatus {
    PRESENT,
    ABSENT,
    LATE,
    NOT_MARKED
}
