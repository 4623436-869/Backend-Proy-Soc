package com.proyecto.dto.qr;

import java.time.LocalDateTime;

public class QrValidateResponse {

    private Boolean valid;
    private Long userId;
    private String userFullName;
    private Long projectId;
    private String projectName;
    private LocalDateTime scannedAt;
    private String message;

    public QrValidateResponse() {}

    // Getters
    public Boolean getValid() { return valid; }
    public Long getUserId() { return userId; }
    public String getUserFullName() { return userFullName; }
    public Long getProjectId() { return projectId; }
    public String getProjectName() { return projectName; }
    public LocalDateTime getScannedAt() { return scannedAt; }
    public String getMessage() { return message; }

    // Setters
    public void setValid(Boolean valid) { this.valid = valid; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setUserFullName(String userFullName) { this.userFullName = userFullName; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    public void setProjectName(String projectName) { this.projectName = projectName; }
    public void setScannedAt(LocalDateTime scannedAt) { this.scannedAt = scannedAt; }
    public void setMessage(String message) { this.message = message; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Boolean valid;
        private Long userId;
        private String userFullName;
        private Long projectId;
        private String projectName;
        private LocalDateTime scannedAt;
        private String message;

        public Builder valid(Boolean v) { this.valid = v; return this; }
        public Builder userId(Long v) { this.userId = v; return this; }
        public Builder userFullName(String v) { this.userFullName = v; return this; }
        public Builder projectId(Long v) { this.projectId = v; return this; }
        public Builder projectName(String v) { this.projectName = v; return this; }
        public Builder scannedAt(LocalDateTime v) { this.scannedAt = v; return this; }
        public Builder message(String v) { this.message = v; return this; }

        public QrValidateResponse build() {
            QrValidateResponse r = new QrValidateResponse();
            r.valid = this.valid; r.userId = this.userId;
            r.userFullName = this.userFullName; r.projectId = this.projectId;
            r.projectName = this.projectName; r.scannedAt = this.scannedAt;
            r.message = this.message;
            return r;
        }
    }
}