package com.proyecto.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "beneficiaries")
public class Beneficiary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String fullName;

    @Column(unique = true, length = 20)
    private String documentNumber;

    @Column(length = 15)
    private String phone;

    @Column(length = 150)
    private String address;

    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BeneficiaryStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    public enum BeneficiaryStatus { ACTIVO, INACTIVO }

    public Beneficiary() {}

    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getDocumentNumber() { return documentNumber; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public LocalDate getBirthDate() { return birthDate; }
    public BeneficiaryStatus getStatus() { return status; }
    public Project getProject() { return project; }

    public void setId(Long id) { this.id = id; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setDocumentNumber(String documentNumber) { this.documentNumber = documentNumber; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setAddress(String address) { this.address = address; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    public void setStatus(BeneficiaryStatus status) { this.status = status; }
    public void setProject(Project project) { this.project = project; }

    public static BeneficiaryBuilder builder() { return new BeneficiaryBuilder(); }

    public static class BeneficiaryBuilder {
        private String fullName;
        private String documentNumber;
        private String phone;
        private String address;
        private LocalDate birthDate;
        private BeneficiaryStatus status;
        private Project project;

        public BeneficiaryBuilder fullName(String v) { this.fullName = v; return this; }
        public BeneficiaryBuilder documentNumber(String v) { this.documentNumber = v; return this; }
        public BeneficiaryBuilder phone(String v) { this.phone = v; return this; }
        public BeneficiaryBuilder address(String v) { this.address = v; return this; }
        public BeneficiaryBuilder birthDate(LocalDate v) { this.birthDate = v; return this; }
        public BeneficiaryBuilder status(BeneficiaryStatus v) { this.status = v; return this; }
        public BeneficiaryBuilder project(Project v) { this.project = v; return this; }

        public Beneficiary build() {
            Beneficiary b = new Beneficiary();
            b.fullName = this.fullName;
            b.documentNumber = this.documentNumber;
            b.phone = this.phone;
            b.address = this.address;
            b.birthDate = this.birthDate;
            b.status = this.status;
            b.project = this.project;
            return b;
        }
    }
}