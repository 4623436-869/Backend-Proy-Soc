package com.proyecto.dto.project;

import com.proyecto.entity.Project;
import java.time.LocalDateTime;

public class ProjectResponse {

    private Long id;
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String status;
    private String coordinatorName;
    private String coordinatorEmail;

    public ProjectResponse() {}

    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public LocalDateTime getStartDate() { return startDate; }
    public LocalDateTime getEndDate() { return endDate; }
    public String getStatus() { return status; }
    public String getCoordinatorName() { return coordinatorName; }
    public String getCoordinatorEmail() { return coordinatorEmail; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }
    public void setStatus(String status) { this.status = status; }
    public void setCoordinatorName(String n) { this.coordinatorName = n; }
    public void setCoordinatorEmail(String e) { this.coordinatorEmail = e; }

    public static ProjectResponse fromEntity(Project project) {
        ProjectResponse r = new ProjectResponse();
        r.id = project.getId();
        r.name = project.getName();
        r.description = project.getDescription();
        r.startDate = project.getStartDate();
        r.endDate = project.getEndDate();
        r.status = project.getStatus().name();
        r.coordinatorName = project.getCoordinator() != null
                ? project.getCoordinator().getFullName() : null;
        r.coordinatorEmail = project.getCoordinator() != null
                ? project.getCoordinator().getEmail() : null;
        return r;
    }
}