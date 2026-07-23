package com.sao.attendance.exception;

/**
 * Thrown while parsing a single row of the connected Google Sheet when
 * that row is malformed (missing columns, unparseable date, unknown
 * status value, etc). Kept distinct from GoogleSheetSyncException so the
 * sync process can catch it per-row and skip just that row instead of
 * aborting the whole sync.
 */
public class InvalidAttendanceRowException extends RuntimeException {

    public InvalidAttendanceRowException(String message) {
        super(message);
    }
}
