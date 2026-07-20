package com.sao.attendance.config;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;

/**
 * Builds the {@link Sheets} API client used to read the connected
 * attendance spreadsheet.
 * <p>
 * The bean is {@code @Lazy}: it is only constructed the first time a sync
 * is actually requested, so the application can start up perfectly well
 * without any Google credentials present (useful for local development
 * and for running the seeded demo data). If the credentials file is
 * missing or invalid, construction fails with a {@link com.sao.attendance.exception.GoogleSheetSyncException}
 * that the caller of the sync endpoint sees as a clear error, rather than
 * the whole application refusing to boot.
 */
public class GoogleSheetsConfig {

    private final ResourceLoader resourceLoader;

    @Value("${googlesheets.credentials-path}")
    private String credentialsPath;

    @Value("${googlesheets.application-name}")
    private String applicationName;

    public GoogleSheetsConfig(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Bean
    @Lazy
    public Sheets sheetsService() {
        try {
            Resource resource = resourceLoader.getResource(credentialsPath);
            if (!resource.exists()) {
                throw new IllegalStateException(
                        "Google credentials file not found at '" + credentialsPath + "'. "
                                + "See README.md 'Connecting your Google Sheet' for setup steps.");
            }

            NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

            try (InputStream credentialsStream = resource.getInputStream()) {
                GoogleCredentials credentials = GoogleCredentials
                        .fromStream(credentialsStream)
                        .createScoped(Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY));

                return new Sheets.Builder(httpTransport, GsonFactory.getDefaultInstance(),
                        new HttpCredentialsAdapter(credentials))
                        .setApplicationName(applicationName)
                        .build();
            }
        } catch (IOException | GeneralSecurityException | IllegalStateException e) {
            throw new com.sao.attendance.exception.GoogleSheetSyncException(
                    "Could not initialize the Google Sheets client: " + e.getMessage(), e);
        }
    }
}
