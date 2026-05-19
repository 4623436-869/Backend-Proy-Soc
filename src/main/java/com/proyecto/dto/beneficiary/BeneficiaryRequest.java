package com.proyecto.dto.beneficiary;

import com.proyecto.entity.Beneficiary;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class BeneficiaryRequest {

    @NotBlank(message = "El nombre completo es obligatorio")
    @Size(max = 100)
    private String fullName;

    @Size(max = 20, message = "El número de documento no puede superar 20 caracteres")
    private String documentNumber;

    @Pattern(regexp = "^[0-9+\\-\\s]{7,15}$", message = "Teléfono inválido")
    private String phone;

    private String address;
    private LocalDate birthDate;

    @NotNull(message = "El estado es obligatorio")
    private Beneficiary.BeneficiaryStatus status;

    @NotNull(message = "El proyecto es obligatorio")
    private Long projectId;

    public BeneficiaryRequest() {}

    public String getFullName() { return fullName; }
    public String getDocumentNumber() { return documentNumber; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public LocalDate getBirthDate() { return birthDate; }
    public Beneficiary.BeneficiaryStatus getStatus() { return status; }
    public Long getProjectId() { return projectId; }

    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setDocumentNumber(String documentNumber) { this.documentNumber = documentNumber; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setAddress(String address) { this.address = address; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    public void setStatus(Beneficiary.BeneficiaryStatus status) { this.status = status; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
}