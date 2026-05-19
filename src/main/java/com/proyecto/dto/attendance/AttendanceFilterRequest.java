package com.proyecto.dto.attendance;

import com.proyecto.entity.Participation;
import java.time.LocalDateTime;

public class AttendanceFilterRequest {

    private Long userId;
    private Long projectId;
    private LocalDateTime from;
    private LocalDateTime to;
    private Participation.RegistrationMethod method;

    public AttendanceFilterRequest() {}
    public AttendanceFilterRequest(Long userId, Long projectId,
                                   LocalDateTime from, LocalDateTime to,
                                   Participation.RegistrationMethod method) {
        this.userId = userId;
        this.projectId = projectId;
        this.from = from;
        this.to = to;
        this.method = method;
    }

    public Long getUserId() { return userId; }
    public Long getProjectId() { return projectId; }
    public LocalDateTime getFrom() { return from; }
    public LocalDateTime getTo() { return to; }
    public Participation.RegistrationMethod getMethod() { return method; }

    public void setUserId(Long userId) { this.userId = userId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    public void setFrom(LocalDateTime from) { this.from = from; }
    public void setTo(LocalDateTime to) { this.to = to; }
    public void setMethod(Participation.RegistrationMethod method) { this.method = method; }
}