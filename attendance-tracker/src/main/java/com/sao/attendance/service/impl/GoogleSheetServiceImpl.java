package com.sao.attendance.service.impl;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.sao.attendance.dto.SheetRowDTO;
import com.sao.attendance.entity.AttendanceRecord;
import com.sao.attendance.entity.AttendanceStatus;
import com.sao.attendance.entity.Student;
import com.sao.attendance.exception.GoogleSheetSyncException;
import com.sao.attendance.exception.InvalidAttendanceRowException;
import com.sao.attendance.repository.AttendanceRecordRepository;
import com.sao.attendance.repository.StudentRepository;
import com.sao.attendance.service.GoogleSheetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads student/attendance rows from the connected Google Sheet and
 * reconciles them with the local database.
 * <p>
 * Expected sheet layout (row 1 = header, skipped):
 * <pre>
 * | RollNumber | Name        | Guild        | Date       | Status  |
 * | S001       | Kirito      | Knights of Blood | 2026-07-20 | PRESENT |
 * </pre>
 */
public class GoogleSheetServiceImpl implements GoogleSheetService {

    private static final Logger log = LoggerFactory.getLogger(GoogleSheetServiceImpl.class);
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final Sheets sheetsService;
    private final StudentRepository studentRepository;
    private final AttendanceRecordRepository attendanceRecordRepository;

    @Value("${googlesheets.spreadsheet-id}")
    private String spreadsheetId;

    @Value("${googlesheets.range}")
    private String range;

    public GoogleSheetServiceImpl(@Lazy Sheets sheetsService,
                                   StudentRepository studentRepository,
                                   AttendanceRecordRepository attendanceRecordRepository) {
        this.sheetsService = sheetsService;
        this.studentRepository = studentRepository;
        this.attendanceRecordRepository = attendanceRecordRepository;
    }

    @Override
    public List<SheetRowDTO> fetchAttendanceRows() {
        ValueRange response;
        try {
            response = sheetsService.spreadsheets().values()
                    .get(spreadsheetId, range)
                    .execute();
        } catch (IOException e) {
            throw new GoogleSheetSyncException(
                    "Failed to read data from Google Sheets. Check the spreadsheet id, the sharing "
                            + "permissions on the service account, and network connectivity. Cause: "
                            + e.getMessage(), e);
        }

        List<List<Object>> values = response.getValues();
        if (values == null || values.isEmpty()) {
            throw new GoogleSheetSyncException(
                    "The connected sheet returned no rows for range '" + range + "'.");
        }

        List<SheetRowDTO> rows = new ArrayList<>();
        // Row 0 is assumed to be the header, so parsing starts at index 1.
        for (int i = 1; i < values.size(); i++) {
            try {
                rows.add(parseRow(values.get(i)));
            } catch (InvalidAttendanceRowException e) {
                log.warn("Skipping sheet row {}: {}", i + 1, e.getMessage());
            }
        }
        return rows;
    }

    @Override
    @Transactional
    public int syncAttendanceFromSheet() {
        List<SheetRowDTO> rows = fetchAttendanceRows();
        int syncedCount = 0;

        for (SheetRowDTO row : rows) {
            try {
                Student student = studentRepository.findByRollNumber(row.getRollNumber())
                        .orElseGet(() -> studentRepository.save(
                                new Student(row.getName(), row.getRollNumber(), row.getGuild())));

                AttendanceRecord record = attendanceRecordRepository
                        .findByStudentAndAttendanceDate(student, row.getDate())
                        .orElse(new AttendanceRecord());

                record.setStudent(student);
                record.setAttendanceDate(row.getDate());
                record.setStatus(row.getStatus());
                record.setSyncedFromSheet(true);
                attendanceRecordRepository.save(record);
                syncedCount++;
            } catch (Exception e) {
                log.warn("Skipping row for roll number '{}' due to error: {}",
                        row.getRollNumber(), e.getMessage());
            }
        }
        return syncedCount;
    }

    private SheetRowDTO parseRow(List<Object> row) {
        if (row.size() < 5) {
            throw new InvalidAttendanceRowException("Expected 5 columns, got " + row.size());
        }

        String rollNumber = asString(row.get(0));
        String name = asString(row.get(1));
        String guild = asString(row.get(2));

        if (rollNumber.isBlank() || name.isBlank()) {
            throw new InvalidAttendanceRowException("RollNumber and Name are required");
        }

        LocalDate date;
        try {
            date = LocalDate.parse(asString(row.get(3)), DATE_FORMAT);
        } catch (DateTimeParseException e) {
            throw new InvalidAttendanceRowException("Unparseable date '" + row.get(3) + "', expected yyyy-MM-dd");
        }

        AttendanceStatus status;
        try {
            status = AttendanceStatus.valueOf(asString(row.get(4)).trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidAttendanceRowException(
                    "Unknown status '" + row.get(4) + "', expected PRESENT, ABSENT or LATE");
        }

        return new SheetRowDTO(rollNumber.trim(), name.trim(), guild, date, status);
    }

    private String asString(Object value) {
        return value == null ? "" : value.toString();
    }
}
