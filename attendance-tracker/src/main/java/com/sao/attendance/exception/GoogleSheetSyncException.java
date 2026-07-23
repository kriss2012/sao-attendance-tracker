package com.sao.attendance.exception;

/**
 * Wraps any failure that occurs while talking to the Google Sheets API
 * (missing/invalid credentials, network failure, malformed sheet data,
 * spreadsheet not shared with the service account, etc.) into a single
 * unchecked type the rest of the application can handle uniformly.
 */
public class GoogleSheetSyncException extends RuntimeException {

    public GoogleSheetSyncException(String message) {
        super(message);
    }

    public GoogleSheetSyncException(String message, Throwable cause) {
        super(message, cause);
    }
}
