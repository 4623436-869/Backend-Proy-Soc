package com.proyecto.dto.attendance;

import java.util.List;

public class StudentHoursSummaryResponse {

    private Long userId;
    private String fullName;
    private String codigoEstudiante;
    private Double totalHoursAllCycles;
    private List<ProjectCycleHours> details;

    public StudentHoursSummaryResponse() {}

    // Getters
    public Long getUserId() { return userId; }
    public String getFullName() { return fullName; }
    public String getCodigoEstudiante() { return codigoEstudiante; }
    public Double getTotalHoursAllCycles() { return totalHoursAllCycles; }
    public List<ProjectCycleHours> getDetails() { return details; }

    // Setters
    public void setUserId(Long userId) { this.userId = userId; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setCodigoEstudiante(String codigoEstudiante) { this.codigoEstudiante = codigoEstudiante; }
    public void setTotalHoursAllCycles(Double totalHoursAllCycles) { this.totalHoursAllCycles = totalHoursAllCycles; }
    public void setDetails(List<ProjectCycleHours> details) { this.details = details; }

    public static StudentHoursSummaryResponseBuilder builder() { return new StudentHoursSummaryResponseBuilder(); }

    public static class StudentHoursSummaryResponseBuilder {
        private Long userId;
        private String fullName;
        private String codigoEstudiante;
        private Double totalHoursAllCycles;
        private List<ProjectCycleHours> details;

        public StudentHoursSummaryResponseBuilder userId(Long userId) { this.userId = userId; return this; }
        public StudentHoursSummaryResponseBuilder fullName(String fullName) { this.fullName = fullName; return this; }
        public StudentHoursSummaryResponseBuilder codigoEstudiante(String c) { this.codigoEstudiante = c; return this; }
        public StudentHoursSummaryResponseBuilder totalHoursAllCycles(Double t) { this.totalHoursAllCycles = t; return this; }
        public StudentHoursSummaryResponseBuilder details(List<ProjectCycleHours> details) { this.details = details; return this; }

        public StudentHoursSummaryResponse build() {
            StudentHoursSummaryResponse r = new StudentHoursSummaryResponse();
            r.userId = this.userId;
            r.fullName = this.fullName;
            r.codigoEstudiante = this.codigoEstudiante;
            r.totalHoursAllCycles = this.totalHoursAllCycles;
            r.details = this.details;
            return r;
        }
    }

    public static class ProjectCycleHours {
        private Long projectId;
        private String projectName;
        private String cicloAcademico;
        private Double hours;

        public ProjectCycleHours() {}

        public ProjectCycleHours(Long projectId, String projectName, String cicloAcademico, Double hours) {
            this.projectId = projectId;
            this.projectName = projectName;
            this.cicloAcademico = cicloAcademico;
            this.hours = hours;
        }

        public Long getProjectId() { return projectId; }
        public String getProjectName() { return projectName; }
        public String getCicloAcademico() { return cicloAcademico; }
        public Double getHours() { return hours; }

        public void setProjectId(Long projectId) { this.projectId = projectId; }
        public void setProjectName(String projectName) { this.projectName = projectName; }
        public void setCicloAcademico(String cicloAcademico) { this.cicloAcademico = cicloAcademico; }
        public void setHours(Double hours) { this.hours = hours; }
    }
}