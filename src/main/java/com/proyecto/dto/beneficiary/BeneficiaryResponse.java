package com.proyecto.dto.beneficiary;

import com.proyecto.entity.Beneficiary;
import java.time.LocalDate;

public class BeneficiaryResponse {

    private Long id;
    private String fullName;
    private String documentNumber;
    private String phone;
    private String address;
    private LocalDate birthDate;
    private String status;
    private Long projectId;
    private String projectName;

    public BeneficiaryResponse() {}

    // Getters
    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getDocumentNumber() { return documentNumber; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public LocalDate getBirthDate() { return birthDate; }
    public String getStatus() { return status; }
    public Long getProjectId() { return projectId; }
    public String getProjectName() { return projectName; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setDocumentNumber(String documentNumber) { this.documentNumber = documentNumber; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setAddress(String address) { this.address = address; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    public void setStatus(String status) { this.status = status; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    public void setProjectName(String projectName) { this.projectName = projectName; }

    public static BeneficiaryResponse fromEntity(Beneficiary b) {
        BeneficiaryResponse r = new BeneficiaryResponse();
        r.id = b.getId();
        r.fullName = b.getFullName();
        r.documentNumber = b.getDocumentNumber();
        r.phone = b.getPhone();
        r.address = b.getAddress();
        r.birthDate = b.getBirthDate();
        r.status = b.getStatus().name();
        r.projectId = b.getProject().getId();
        r.projectName = b.getProject().getName();
        return r;
    }
}