package com.proyecto.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

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
    private LocalDate startDate;

    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ProjectStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coordinator_id")
    private User coordinator;

    public enum ProjectStatus { ACTIVO, INACTIVO, FINALIZADO }

    public Project() {}

    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public ProjectStatus getStatus() { return status; }
    public User getCoordinator() { return coordinator; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public void setStatus(ProjectStatus status) { this.status = status; }
    public void setCoordinator(User coordinator) { this.coordinator = coordinator; }

    // Builder
    public static ProjectBuilder builder() { return new ProjectBuilder(); }

    public static class ProjectBuilder {
        private Long id;
        private String name;
        private String description;
        private LocalDate startDate;
        private LocalDate endDate;
        private ProjectStatus status;
        private User coordinator;

        public ProjectBuilder id(Long id) { this.id = id; return this; }
        public ProjectBuilder name(String name) { this.name = name; return this; }
        public ProjectBuilder description(String d) { this.description = d; return this; }
        public ProjectBuilder startDate(LocalDate d) { this.startDate = d; return this; }
        public ProjectBuilder endDate(LocalDate d) { this.endDate = d; return this; }
        public ProjectBuilder status(ProjectStatus s) { this.status = s; return this; }
        public ProjectBuilder coordinator(User u) { this.coordinator = u; return this; }

        public Project build() {
            Project p = new Project();
            p.id = this.id; p.name = this.name;
            p.description = this.description;
            p.startDate = this.startDate; p.endDate = this.endDate;
            p.status = this.status; p.coordinator = this.coordinator;
            return p;
        }
    }
}