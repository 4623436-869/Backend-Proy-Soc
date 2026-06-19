package com.proyecto.service;

import com.proyecto.dto.document.DocumentResponse;
import com.proyecto.entity.Document;
import com.proyecto.entity.Project;
import com.proyecto.entity.User;
import com.proyecto.repository.DocumentRepository;
import com.proyecto.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentService {

    private static final long MAX_FILE_SIZE_BYTES = 20L * 1024 * 1024; // 20 MB

    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final DocumentStorageService storageService;

    public DocumentService(DocumentRepository documentRepository,
                            UserRepository userRepository,
                            DocumentStorageService storageService) {
        this.documentRepository = documentRepository;
        this.userRepository = userRepository;
        this.storageService = storageService;
    }

    @Transactional
    public DocumentResponse upload(MultipartFile file, String cicloAcademico, String facultad,
                                    String escuela, Document.DocumentType tipo,
                                    Project.Campus campus, String description, Long uploadedByUserId) {

        validateFile(file);

        User uploader = userRepository.findById(uploadedByUserId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String storedFileName = storageService.generateStoredFileName(file.getOriginalFilename());

        String savedPath;
        try {
            savedPath = storageService.store(file, cicloAcademico, facultad, escuela, storedFileName);
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el archivo en el servidor: " + e.getMessage());
        }

        Document document = Document.builder()
                .originalFileName(file.getOriginalFilename())
                .storedFileName(storedFileName)
                .filePath(savedPath)
                .fileSize(file.getSize())
                .tipo(tipo)
                .cicloAcademico(cicloAcademico)
                .facultad(facultad)
                .escuela(escuela)
                .campus(campus)
                .description(description)
                .uploadedBy(uploader)
                .uploadedAt(LocalDateTime.now())
                .build();

        return DocumentResponse.fromEntity(documentRepository.save(document));
    }

    @Transactional(readOnly = true)
    public DocumentResponse getById(Long id) {
        return DocumentResponse.fromEntity(findOrThrow(id));
    }

    @Transactional(readOnly = true)
    public List<DocumentResponse> getAll() {
        return documentRepository.findAll()
                .stream()
                .map(DocumentResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public InputStream getFileStream(Long id) {
        Document document = findOrThrow(id);
        try {
            return storageService.readFile(document.getFilePath());
        } catch (IOException e) {
            throw new RuntimeException("No se pudo leer el archivo: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public Document getEntityById(Long id) {
        return findOrThrow(id);
    }

    @Transactional
    public void delete(Long id) {
        Document document = findOrThrow(id);
        storageService.deleteFile(document.getFilePath());
        documentRepository.deleteById(id);
    }

    private Document findOrThrow(Long id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Documento no encontrado con id: " + id));
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Debe seleccionar un archivo");
        }

        String contentType = file.getContentType();
        String fileName = file.getOriginalFilename();

        boolean isPdfContentType = contentType != null && contentType.equals("application/pdf");
        boolean isPdfExtension = fileName != null && fileName.toLowerCase().endsWith(".pdf");

        if (!isPdfContentType || !isPdfExtension) {
            throw new RuntimeException("Solo se permiten archivos en formato PDF");
        }

        if (file.getSize() > MAX_FILE_SIZE_BYTES) {
            throw new RuntimeException("El archivo supera el tamaño máximo permitido (20 MB)");
        }
    }
}