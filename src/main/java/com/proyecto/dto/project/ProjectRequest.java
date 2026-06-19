package com.proyecto.dto.project;

import com.proyecto.entity.Project;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public class ProjectRequest {

    @NotBlank(message = "El nombre del proyecto es obligatorio")
    @Size(max = 150, message = "El nombre no puede superar 150 caracteres")
    private String name;

    @Size(max = 500, message = "La descripción no puede superar 500 caracteres")
    private String description;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @NotNull(message = "El estado es obligatorio")
    private Project.ProjectStatus status;

    @NotBlank(message = "El ciclo académico es obligatorio")
    @Pattern(regexp = "^\\d{4}-[1-2]$", message = "El ciclo académico debe tener formato AAAA-N, ej: 2025-1")
    private String cicloAcademico;

    private Long coordinatorId;

    public ProjectRequest() {}

    public String getName() { return name; }
    public String getDescription() { return description; }
    public LocalDateTime getStartDate() { return startDate; }
    public LocalDateTime getEndDate() { return endDate; }
    public Project.ProjectStatus getStatus() { return status; }
    public String getCicloAcademico() { return cicloAcademico; }
    public Long getCoordinatorId() { return coordinatorId; }

    public void setName(String name) { this.name = name; }
    public void setDescription(String d) { this.description = d; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }
    public void setStatus(Project.ProjectStatus status) { this.status = status; }
    public void setCicloAcademico(String cicloAcademico) { this.cicloAcademico = cicloAcademico; }
    public void setCoordinatorId(Long coordinatorId) { this.coordinatorId = coordinatorId; }
}