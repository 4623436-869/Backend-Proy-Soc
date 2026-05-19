package com.proyecto.dto.qr;

import jakarta.validation.constraints.NotNull;

public class QrCheckOutRequest {

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long userId;

    @NotNull(message = "El ID del proyecto es obligatorio")
    private Long projectId;

    public QrCheckOutRequest() {}

    public Long getUserId() { return userId; }
    public Long getProjectId() { return projectId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
}