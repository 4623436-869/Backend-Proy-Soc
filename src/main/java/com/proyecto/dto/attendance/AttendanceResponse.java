package com.proyecto.dto.attendance;

import com.proyecto.entity.Participation;
import java.time.LocalDateTime;

public class AttendanceResponse {

    private Long id;
    private Long userId;
    private String userFullName;
    private Long projectId;
    private String projectName;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private Double hoursLogged;
    private String registrationMethod;

    public AttendanceResponse() {}

    // Getters
    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getUserFullName() { return userFullName; }
    public Long getProjectId() { return projectId; }
    public String getProjectName() { return projectName; }
    public LocalDateTime getCheckIn() { return checkIn; }
    public LocalDateTime getCheckOut() { return checkOut; }
    public Double getHoursLogged() { return hoursLogged; }
    public String getRegistrationMethod() { return registrationMethod; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setUserFullName(String userFullName) { this.userFullName = userFullName; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    public void setProjectName(String projectName) { this.projectName = projectName; }
    public void setCheckIn(LocalDateTime checkIn) { this.checkIn = checkIn; }
    public void setCheckOut(LocalDateTime checkOut) { this.checkOut = checkOut; }
    public void setHoursLogged(Double hoursLogged) { this.hoursLogged = hoursLogged; }
    public void setRegistrationMethod(String registrationMethod) { this.registrationMethod = registrationMethod; }

    public static AttendanceResponse fromEntity(Participation p) {
        AttendanceResponse r = new AttendanceResponse();
        r.id = p.getId();
        r.userId = p.getUser().getId();
        r.userFullName = p.getUser().getFullName();
        r.projectId = p.getProject().getId();
        r.projectName = p.getProject().getName();
        r.checkIn = p.getCheckIn();
        r.checkOut = p.getCheckOut();
        r.hoursLogged = p.getHoursLogged();
        r.registrationMethod = p.getRegistrationMethod().name();
        return r;
    }
}