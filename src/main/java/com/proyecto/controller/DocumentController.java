package com.proyecto.controller;

import com.proyecto.dto.document.DocumentResponse;
import com.proyecto.entity.Document;
import com.proyecto.entity.Project;
import com.proyecto.security.UserDetailsServiceImpl;
import com.proyecto.service.DocumentService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;
    private final com.proyecto.repository.UserRepository userRepository;

    public DocumentController(DocumentService documentService,
                               com.proyecto.repository.UserRepository userRepository) {
        this.documentService = documentService;
        this.userRepository = userRepository;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_COORDINADOR')")
    public ResponseEntity<DocumentResponse> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("cicloAcademico") String cicloAcademico,
            @RequestParam("facultad") String facultad,
            @RequestParam("escuela") String escuela,
            @RequestParam("tipo") Document.DocumentType tipo,
            @RequestParam(value = "campus", required = false) Project.Campus campus,
            @RequestParam(value = "description", required = false) String description,
            Authentication authentication) {

        Long uploadedByUserId = resolveUserId(authentication);

        return ResponseEntity.ok(documentService.upload(
                file, cicloAcademico, facultad, escuela, tipo, campus, description, uploadedByUserId));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<DocumentResponse>> getAll() {
        return ResponseEntity.ok(documentService.getAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<DocumentResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(documentService.getById(id));
    }

    @GetMapping("/{id}/file")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<InputStreamResource> getFile(@PathVariable Long id) {
        var document = documentService.getEntityById(id);
        InputStreamResource resource = new InputStreamResource(documentService.getFileStream(id));

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + document.getOriginalFileName() + "\"")
                .body(resource);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        documentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private Long resolveUserId(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario autenticado no encontrado"))
                .getId();
    }
}