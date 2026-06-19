package com.proyecto.dto.document;

import com.proyecto.entity.Document;
import java.time.LocalDateTime;

public class DocumentResponse {

    private Long id;
    private String originalFileName;
    private Long fileSize;
    private String tipo;
    private String cicloAcademico;
    private String facultad;
    private String escuela;
    private String campus;
    private String description;
    private String uploadedByName;
    private LocalDateTime uploadedAt;

    public DocumentResponse() {}

    public Long getId() { return id; }
    public String getOriginalFileName() { return originalFileName; }
    public Long getFileSize() { return fileSize; }
    public String getTipo() { return tipo; }
    public String getCicloAcademico() { return cicloAcademico; }
    public String getFacultad() { return facultad; }
    public String getEscuela() { return escuela; }
    public String getCampus() { return campus; }
    public String getDescription() { return description; }
    public String getUploadedByName() { return uploadedByName; }
    public LocalDateTime getUploadedAt() { return uploadedAt; }

    public void setId(Long v) { this.id = v; }
    public void setOriginalFileName(String v) { this.originalFileName = v; }
    public void setFileSize(Long v) { this.fileSize = v; }
    public void setTipo(String v) { this.tipo = v; }
    public void setCicloAcademico(String v) { this.cicloAcademico = v; }
    public void setFacultad(String v) { this.facultad = v; }
    public void setEscuela(String v) { this.escuela = v; }
    public void setCampus(String v) { this.campus = v; }
    public void setDescription(String v) { this.description = v; }
    public void setUploadedByName(String v) { this.uploadedByName = v; }
    public void setUploadedAt(LocalDateTime v) { this.uploadedAt = v; }

    public static DocumentResponse fromEntity(Document doc) {
        DocumentResponse r = new DocumentResponse();
        r.id = doc.getId();
        r.originalFileName = doc.getOriginalFileName();
        r.fileSize = doc.getFileSize();
        r.tipo = doc.getTipo().name();
        r.cicloAcademico = doc.getCicloAcademico();
        r.facultad = doc.getFacultad();
        r.escuela = doc.getEscuela();
        r.campus = doc.getCampus() != null ? doc.getCampus().name() : null;
        r.description = doc.getDescription();
        r.uploadedByName = doc.getUploadedBy() != null ? doc.getUploadedBy().getFullName() : null;
        r.uploadedAt = doc.getUploadedAt();
        return r;
    }
}