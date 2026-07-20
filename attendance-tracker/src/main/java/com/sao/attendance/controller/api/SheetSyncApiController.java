package com.sao.attendance.controller.api;

import com.sao.attendance.service.GoogleSheetService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Manual trigger for pulling the latest data from the connected Google Sheet.
 * On failure, {@link ApiExceptionHandler} converts the resulting
 * {@link com.sao.attendance.exception.GoogleSheetSyncException} into a 503.
 */
@RestController
@RequestMapping("/api/sheets")
public class SheetSyncApiController {

    private final GoogleSheetService googleSheetService;

    public SheetSyncApiController(GoogleSheetService googleSheetService) {
        this.googleSheetService = googleSheetService;
    }

    @PostMapping("/sync")
    public Map<String, Object> sync() {
        int synced = googleSheetService.syncAttendanceFromSheet();
        return Map.of(
                "syncedRows", synced,
                "message", "Synced " + synced + " row(s) from the connected sheet."
        );
    }
}
