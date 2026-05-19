package com.proyecto.dto.qr;

import java.time.LocalDateTime;

public class QrGenerateResponse {

    private String token;
    private String qrBase64;
    private Long userId;
    private Long projectId;
    private LocalDateTime generatedAt;
    private LocalDateTime expiresAt;

    public QrGenerateResponse() {}

    // Getters
    public String getToken() { return token; }
    public String getQrBase64() { return qrBase64; }
    public Long getUserId() { return userId; }
    public Long getProjectId() { return projectId; }
    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public LocalDateTime getExpiresAt() { return expiresAt; }

    // Setters
    public void setToken(String token) { this.token = token; }
    public void setQrBase64(String qrBase64) { this.qrBase64 = qrBase64; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String token;
        private String qrBase64;
        private Long userId;
        private Long projectId;
        private LocalDateTime generatedAt;
        private LocalDateTime expiresAt;

        public Builder token(String v) { this.token = v; return this; }
        public Builder qrBase64(String v) { this.qrBase64 = v; return this; }
        public Builder userId(Long v) { this.userId = v; return this; }
        public Builder projectId(Long v) { this.projectId = v; return this; }
        public Builder generatedAt(LocalDateTime v) { this.generatedAt = v; return this; }
        public Builder expiresAt(LocalDateTime v) { this.expiresAt = v; return this; }

        public QrGenerateResponse build() {
            QrGenerateResponse r = new QrGenerateResponse();
            r.token = this.token; r.qrBase64 = this.qrBase64;
            r.userId = this.userId; r.projectId = this.projectId;
            r.generatedAt = this.generatedAt; r.expiresAt = this.expiresAt;
            return r;
        }
    }
}