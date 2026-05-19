package com.proyecto.dto.attendance;

public class AttendanceSummaryResponse {

    private Long userId;
    private String userFullName;
    private Long projectId;
    private String projectName;
    private Double totalHours;
    private Long totalSessions;

    public AttendanceSummaryResponse() {}

    // Getters
    public Long getUserId() { return userId; }
    public String getUserFullName() { return userFullName; }
    public Long getProjectId() { return projectId; }
    public String getProjectName() { return projectName; }
    public Double getTotalHours() { return totalHours; }
    public Long getTotalSessions() { return totalSessions; }

    // Setters
    public void setUserId(Long userId) { this.userId = userId; }
    public void setUserFullName(String userFullName) { this.userFullName = userFullName; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    public void setProjectName(String projectName) { this.projectName = projectName; }
    public void setTotalHours(Double totalHours) { this.totalHours = totalHours; }
    public void setTotalSessions(Long totalSessions) { this.totalSessions = totalSessions; }

    // Builder
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long userId;
        private String userFullName;
        private Long projectId;
        private String projectName;
        private Double totalHours;
        private Long totalSessions;

        public Builder userId(Long v) { this.userId = v; return this; }
        public Builder userFullName(String v) { this.userFullName = v; return this; }
        public Builder projectId(Long v) { this.projectId = v; return this; }
        public Builder projectName(String v) { this.projectName = v; return this; }
        public Builder totalHours(Double v) { this.totalHours = v; return this; }
        public Builder totalSessions(Long v) { this.totalSessions = v; return this; }

        public AttendanceSummaryResponse build() {
            AttendanceSummaryResponse r = new AttendanceSummaryResponse();
            r.userId = this.userId; r.userFullName = this.userFullName;
            r.projectId = this.projectId; r.projectName = this.projectName;
            r.totalHours = this.totalHours; r.totalSessions = this.totalSessions;
            return r;
        }
    }
}