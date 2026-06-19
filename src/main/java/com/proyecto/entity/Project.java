package com.proyecto.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ProjectStatus status;

    @Column(name = "ciclo_academico", length = 20)
    private String cicloAcademico;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Campus campus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coordinator_id")
    private User coordinator;

    public enum ProjectStatus { ACTIVO, INACTIVO, FINALIZADO }
    public enum Campus { LIMA, JULIACA, TARAPOTO }

    public Project() {}

    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public LocalDateTime getStartDate() { return startDate; }
    public LocalDateTime getEndDate() { return endDate; }
    public ProjectStatus getStatus() { return status; }
    public String getCicloAcademico() { return cicloAcademico; }
    public Campus getCampus() { return campus; }
    public User getCoordinator() { return coordinator; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }
    public void setStatus(ProjectStatus status) { this.status = status; }
    public void setCicloAcademico(String cicloAcademico) { this.cicloAcademico = cicloAcademico; }
    public void setCampus(Campus campus) { this.campus = campus; }
    public void setCoordinator(User coordinator) { this.coordinator = coordinator; }

    // Builder
    public static ProjectBuilder builder() { return new ProjectBuilder(); }

    public static class ProjectBuilder {
        private Long id;
        private String name;
        private String description;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private ProjectStatus status;
        private String cicloAcademico;
        private Campus campus;
        private User coordinator;

        public ProjectBuilder id(Long id) { this.id = id; return this; }
        public ProjectBuilder name(String name) { this.name = name; return this; }
        public ProjectBuilder description(String d) { this.description = d; return this; }
        public ProjectBuilder startDate(LocalDateTime d) { this.startDate = d; return this; }
        public ProjectBuilder endDate(LocalDateTime d) { this.endDate = d; return this; }
        public ProjectBuilder status(ProjectStatus s) { this.status = s; return this; }
        public ProjectBuilder cicloAcademico(String c) { this.cicloAcademico = c; return this; }
        public ProjectBuilder campus(Campus c) { this.campus = c; return this; }
        public ProjectBuilder coordinator(User u) { this.coordinator = u; return this; }

        public Project build() {
            Project p = new Project();
            p.id = this.id;
            p.name = this.name;
            p.description = this.description;
            p.startDate = this.startDate;
            p.endDate = this.endDate;
            p.status = this.status;
            p.cicloAcademico = this.cicloAcademico;
            p.campus = this.campus;
            p.coordinator = this.coordinator;
            return p;
        }
    }
}