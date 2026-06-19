package com.proyecto.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String originalFileName;

    @Column(nullable = false, length = 255)
    private String storedFileName;

    @Column(nullable = false, length = 500)
    private String filePath;

    @Column(nullable = false)
    private Long fileSize;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DocumentType tipo;

    @Column(name = "ciclo_academico", nullable = false, length = 20)
    private String cicloAcademico;

    @Column(nullable = false, length = 150)
    private String facultad;

    @Column(nullable = false, length = 150)
    private String escuela;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Project.Campus campus;

    @Column(length = 500)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by", nullable = false)
    private User uploadedBy;

    @Column(nullable = false)
    private LocalDateTime uploadedAt;

    public enum DocumentType { PLAN, INFORME }

    public Document() {}

    // Getters
    public Long getId() { return id; }
    public String getOriginalFileName() { return originalFileName; }
    public String getStoredFileName() { return storedFileName; }
    public String getFilePath() { return filePath; }
    public Long getFileSize() { return fileSize; }
    public DocumentType getTipo() { return tipo; }
    public String getCicloAcademico() { return cicloAcademico; }
    public String getFacultad() { return facultad; }
    public String getEscuela() { return escuela; }
    public Project.Campus getCampus() { return campus; }
    public String getDescription() { return description; }
    public User getUploadedBy() { return uploadedBy; }
    public LocalDateTime getUploadedAt() { return uploadedAt; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setOriginalFileName(String v) { this.originalFileName = v; }
    public void setStoredFileName(String v) { this.storedFileName = v; }
    public void setFilePath(String v) { this.filePath = v; }
    public void setFileSize(Long v) { this.fileSize = v; }
    public void setTipo(DocumentType v) { this.tipo = v; }
    public void setCicloAcademico(String v) { this.cicloAcademico = v; }
    public void setFacultad(String v) { this.facultad = v; }
    public void setEscuela(String v) { this.escuela = v; }
    public void setCampus(Project.Campus v) { this.campus = v; }
    public void setDescription(String v) { this.description = v; }
    public void setUploadedBy(User v) { this.uploadedBy = v; }
    public void setUploadedAt(LocalDateTime v) { this.uploadedAt = v; }

    public static DocumentBuilder builder() { return new DocumentBuilder(); }

    public static class DocumentBuilder {
        private String originalFileName;
        private String storedFileName;
        private String filePath;
        private Long fileSize;
        private DocumentType tipo;
        private String cicloAcademico;
        private String facultad;
        private String escuela;
        private Project.Campus campus;
        private String description;
        private User uploadedBy;
        private LocalDateTime uploadedAt;

        public DocumentBuilder originalFileName(String v) { this.originalFileName = v; return this; }
        public DocumentBuilder storedFileName(String v) { this.storedFileName = v; return this; }
        public DocumentBuilder filePath(String v) { this.filePath = v; return this; }
        public DocumentBuilder fileSize(Long v) { this.fileSize = v; return this; }
        public DocumentBuilder tipo(DocumentType v) { this.tipo = v; return this; }
        public DocumentBuilder cicloAcademico(String v) { this.cicloAcademico = v; return this; }
        public DocumentBuilder facultad(String v) { this.facultad = v; return this; }
        public DocumentBuilder escuela(String v) { this.escuela = v; return this; }
        public DocumentBuilder campus(Project.Campus v) { this.campus = v; return this; }
        public DocumentBuilder description(String v) { this.description = v; return this; }
        public DocumentBuilder uploadedBy(User v) { this.uploadedBy = v; return this; }
        public DocumentBuilder uploadedAt(LocalDateTime v) { this.uploadedAt = v; return this; }

        public Document build() {
            Document d = new Document();
            d.originalFileName = this.originalFileName;
            d.storedFileName = this.storedFileName;
            d.filePath = this.filePath;
            d.fileSize = this.fileSize;
            d.tipo = this.tipo;
            d.cicloAcademico = this.cicloAcademico;
            d.facultad = this.facultad;
            d.escuela = this.escuela;
            d.campus = this.campus;
            d.description = this.description;
            d.uploadedBy = this.uploadedBy;
            d.uploadedAt = this.uploadedAt;
            return d;
        }
    }
}