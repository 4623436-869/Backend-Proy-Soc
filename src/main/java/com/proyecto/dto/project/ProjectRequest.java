package com.proyecto.dto.project;

import com.proyecto.entity.Project;
import com.proyecto.validation.ValidDateRange;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@ValidDateRange(startField = "startDate", endField = "endDate")
public class ProjectRequest {

    @NotBlank(message = "El nombre del proyecto es obligatorio")
    @Size(max = 150, message = "El nombre no puede superar 150 caracteres")
    private String name;

    @Size(max = 500, message = "La descripción no puede superar 500 caracteres")
    private String description;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate startDate;

    private LocalDate endDate;

    @NotNull(message = "El estado es obligatorio")
    private Project.ProjectStatus status;

    private Long coordinatorId;

    public ProjectRequest() {}

    public String getName() { return name; }
    public String getDescription() { return description; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public Project.ProjectStatus getStatus() { return status; }
    public Long getCoordinatorId() { return coordinatorId; }

    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public void setStatus(Project.ProjectStatus status) { this.status = status; }
    public void setCoordinatorId(Long coordinatorId) { this.coordinatorId = coordinatorId; }
}