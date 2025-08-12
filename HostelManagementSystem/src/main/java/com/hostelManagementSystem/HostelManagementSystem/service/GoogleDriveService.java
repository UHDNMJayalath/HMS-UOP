package com.hostelManagementSystem.HostelManagementSystem.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
public class GoogleDriveService {

    private static final String APPLICATION_NAME = "HostelManagementSystem";
    private static final String SERVICE_ACCOUNT_KEY_PATH = "hms-project.json";
    private static final String FOLDER_ID = "1JzocDx14TPXmBDLyrB7mlEQYHaEFb93z"; // Shared Drive Folder ID

    // Step 1: Initialize Drive Service with Service Account
    private Drive getDriveService() throws IOException, GeneralSecurityException {
        GoogleCredential credential = GoogleCredential
                .fromStream(new ClassPathResource(SERVICE_ACCOUNT_KEY_PATH).getInputStream())
                .createScoped(Collections.singleton("https://www.googleapis.com/auth/drive"));

        return new Drive.Builder(
                com.google.api.client.googleapis.javanet.GoogleNetHttpTransport.newTrustedTransport(),
                com.google.api.client.json.jackson2.JacksonFactory.getDefaultInstance(),
                credential
        ).setApplicationName(APPLICATION_NAME).build();
    }

    // Step 2: Upload file to Google Drive (Shared Drive Folder)
    public String uploadFileToDrive(java.io.File file, String mimeType) throws Exception {
        if (file == null || !file.exists()) {
            throw new Exception("File does not exist or is null");
        }

        if (mimeType == null || mimeType.trim().isEmpty()) {
            mimeType = "application/octet-stream"; // default MIME
        }

        try {
            Drive driveService = getDriveService();

            File fileMetadata = new File();
            fileMetadata.setName(file.getName());
            fileMetadata.setParents(Collections.singletonList(FOLDER_ID));

            FileContent mediaContent = new FileContent(mimeType, file);

            File uploadedFile = driveService.files().create(fileMetadata, mediaContent)
                    .setSupportsAllDrives(true) // ✅ Required for Shared Drive
                    .setFields("id,webViewLink,name")
                    .execute();

            if (uploadedFile == null || uploadedFile.getId() == null) {
                throw new Exception("Upload failed: No file ID returned");
            }

            // Optional: Make file publicly viewable (can skip if not needed)
            try {
                Permission permission = new Permission()
                        .setType("anyone")
                        .setRole("reader");
                driveService.permissions()
                        .create(uploadedFile.getId(), permission)
                        .setSupportsAllDrives(true)
                        .execute();
            } catch (Exception e) {
                System.err.println("Warning: Failed to set public permission. " + e.getMessage());
            }

            // Return viewable Google Drive URL
            return "https://drive.google.com/uc?export=view&id=" + uploadedFile.getId();

        } catch (IOException | GeneralSecurityException e) {
            throw new Exception("Upload error: " + e.getMessage(), e);
        }
    }
}
