package com.proyecto.dto.attendance;

import com.proyecto.entity.Participation;
import com.proyecto.validation.ValidDateRange;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@ValidDateRange(startField = "checkIn", endField = "checkOut")
public class AttendanceRequest {

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long userId;

    @NotNull(message = "El ID del proyecto es obligatorio")
    private Long projectId;

    @NotNull(message = "La hora de entrada es obligatoria")
    private LocalDateTime checkIn;

    @NotNull(message = "La hora de salida es obligatoria")
    private LocalDateTime checkOut;

    private Participation.RegistrationMethod registrationMethod
            = Participation.RegistrationMethod.MANUAL;

    public AttendanceRequest() {}

    public Long getUserId() { return userId; }
    public Long getProjectId() { return projectId; }
    public LocalDateTime getCheckIn() { return checkIn; }
    public LocalDateTime getCheckOut() { return checkOut; }
    public Participation.RegistrationMethod getRegistrationMethod() { return registrationMethod; }

    public void setUserId(Long userId) { this.userId = userId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    public void setCheckIn(LocalDateTime checkIn) { this.checkIn = checkIn; }
    public void setCheckOut(LocalDateTime checkOut) { this.checkOut = checkOut; }
    public void setRegistrationMethod(Participation.RegistrationMethod m) { this.registrationMethod = m; }
}