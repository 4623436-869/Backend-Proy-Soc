package com.proyecto.dto.qr;

import jakarta.validation.constraints.NotBlank;

public class QrScanRequest {

    @NotBlank(message = "El token QR es obligatorio")
    private String token;

    public QrScanRequest() {}

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}