package com.proyecto.dto.project;

import com.proyecto.entity.Project;
import java.time.LocalDate;

public class ProjectResponse {

    private Long id;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private String coordinatorName;
    private String coordinatorEmail;

    public ProjectResponse() {}

    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public String getStatus() { return status; }
    public String getCoordinatorName() { return coordinatorName; }
    public String getCoordinatorEmail() { return coordinatorEmail; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public void setStatus(String status) { this.status = status; }
    public void setCoordinatorName(String coordinatorName) { this.coordinatorName = coordinatorName; }
    public void setCoordinatorEmail(String coordinatorEmail) { this.coordinatorEmail = coordinatorEmail; }

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