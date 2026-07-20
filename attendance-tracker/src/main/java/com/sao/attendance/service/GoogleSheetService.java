package com.sao.attendance.service;

import com.sao.attendance.dto.SheetRowDTO;

import java.util.List;

/**
 * Integration boundary with the connected Google Sheet.
 * Implementations must translate every failure mode (auth, network,
 * malformed data) into {@link com.sao.attendance.exception.GoogleSheetSyncException}.
 */
public interface GoogleSheetService {

    List<SheetRowDTO> fetchAttendanceRows();

    /**
     * Reads the sheet and upserts each valid row into the local database.
     * @return the number of rows successfully synced
     */
    int syncAttendanceFromSheet();
}
