package com.proyecto.dto.dashboard;

import java.util.List;

public class DashboardSummaryResponse {

    private Long activeProjects;
    private Double totalHours;
    private Long totalBeneficiaries;
    private List<CampusSummary> byCampus;

    public DashboardSummaryResponse() {}

    public Long getActiveProjects() { return activeProjects; }
    public Double getTotalHours() { return totalHours; }
    public Long getTotalBeneficiaries() { return totalBeneficiaries; }
    public List<CampusSummary> getByCampus() { return byCampus; }

    public void setActiveProjects(Long v) { this.activeProjects = v; }
    public void setTotalHours(Double v) { this.totalHours = v; }
    public void setTotalBeneficiaries(Long v) { this.totalBeneficiaries = v; }
    public void setByCampus(List<CampusSummary> v) { this.byCampus = v; }

    public static DashboardSummaryResponseBuilder builder() { return new DashboardSummaryResponseBuilder(); }

    public static class DashboardSummaryResponseBuilder {
        private Long activeProjects;
        private Double totalHours;
        private Long totalBeneficiaries;
        private List<CampusSummary> byCampus;

        public DashboardSummaryResponseBuilder activeProjects(Long v) { this.activeProjects = v; return this; }
        public DashboardSummaryResponseBuilder totalHours(Double v) { this.totalHours = v; return this; }
        public DashboardSummaryResponseBuilder totalBeneficiaries(Long v) { this.totalBeneficiaries = v; return this; }
        public DashboardSummaryResponseBuilder byCampus(List<CampusSummary> v) { this.byCampus = v; return this; }

        public DashboardSummaryResponse build() {
            DashboardSummaryResponse r = new DashboardSummaryResponse();
            r.activeProjects = this.activeProjects;
            r.totalHours = this.totalHours;
            r.totalBeneficiaries = this.totalBeneficiaries;
            r.byCampus = this.byCampus;
            return r;
        }
    }

    public static class CampusSummary {
        private String campus;
        private Long projectCount;
        private Double totalHours;
        private Long beneficiaryCount;

        public CampusSummary() {}

        public CampusSummary(String campus, Long projectCount, Double totalHours, Long beneficiaryCount) {
            this.campus = campus;
            this.projectCount = projectCount;
            this.totalHours = totalHours;
            this.beneficiaryCount = beneficiaryCount;
        }

        public String getCampus() { return campus; }
        public Long getProjectCount() { return projectCount; }
        public Double getTotalHours() { return totalHours; }
        public Long getBeneficiaryCount() { return beneficiaryCount; }

        public void setCampus(String campus) { this.campus = campus; }
        public void setProjectCount(Long projectCount) { this.projectCount = projectCount; }
        public void setTotalHours(Double totalHours) { this.totalHours = totalHours; }
        public void setBeneficiaryCount(Long beneficiaryCount) { this.beneficiaryCount = beneficiaryCount; }
    }
}