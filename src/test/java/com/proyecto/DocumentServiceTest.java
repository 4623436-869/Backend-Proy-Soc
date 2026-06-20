package com.proyecto;

import com.proyecto.dto.document.DocumentResponse;
import com.proyecto.entity.Document;
import com.proyecto.entity.Project;
import com.proyecto.entity.User;
import com.proyecto.repository.DocumentRepository;
import com.proyecto.repository.UserRepository;
import com.proyecto.service.DocumentService;
import com.proyecto.service.DocumentStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {

    @Mock private DocumentRepository documentRepository;
    @Mock private UserRepository userRepository;
    @Mock private DocumentStorageService storageService;

    @InjectMocks
    private DocumentService documentService;

    private User admin;

    @BeforeEach
    void setUp() {
        admin = User.builder()
                .id(1L).fullName("Admin Test")
                .email("admin@proyecto.com").password("pass")
                .active(true).roles(Set.of()).build();
    }

    @Test
    @DisplayName("Subir PDF válido → éxito")
    void upload_validPdf_shouldSucceed() throws IOException {
        MultipartFile pdfFile = new MockMultipartFile(
                "file", "plan.pdf", "application/pdf", "contenido-pdf".getBytes());

        when(userRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(storageService.generateStoredFileName(anyString())).thenReturn("uuid_plan.pdf");
        when(storageService.store(any(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn("uploads/documentos/2025-1/ing/sistemas/uuid_plan.pdf");
        when(documentRepository.save(any())).thenAnswer(inv -> {
            Document d = inv.getArgument(0);
            d.setId(1L);
            return d;
        });

        DocumentResponse result = documentService.upload(
                pdfFile, "2025-1", "Facultad de Ingeniería", "Escuela de Sistemas",
                Document.DocumentType.PLAN, Project.Campus.LIMA, "Plan inicial", 1L);

        assertNotNull(result);
        assertEquals("plan.pdf", result.getOriginalFileName());
        assertEquals("PLAN", result.getTipo());
    }

    @Test
    @DisplayName("Subir archivo que no es PDF (extensión .docx) → lanza excepción")
    void upload_nonPdfExtension_shouldThrow() {
        MultipartFile wordFile = new MockMultipartFile(
                "file", "documento.docx",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                "contenido".getBytes());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> documentService.upload(wordFile, "2025-1", "Ingeniería", "Sistemas",
                        Document.DocumentType.PLAN, Project.Campus.LIMA, null, 1L));
        assertTrue(ex.getMessage().contains("PDF"));
    }

    @Test
    @DisplayName("Subir archivo con content-type falsificado pero extensión .pdf → lanza excepción")
    void upload_fakedContentType_shouldThrow() {
        MultipartFile fakeFile = new MockMultipartFile(
                "file", "falso.pdf", "image/png", "no-es-un-pdf-real".getBytes());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> documentService.upload(fakeFile, "2025-1", "Ingeniería", "Sistemas",
                        Document.DocumentType.PLAN, Project.Campus.LIMA, null, 1L));
        assertTrue(ex.getMessage().contains("PDF"));
    }

    @Test
    @DisplayName("Subir archivo vacío → lanza excepción")
    void upload_emptyFile_shouldThrow() {
        MultipartFile emptyFile = new MockMultipartFile(
                "file", "vacio.pdf", "application/pdf", new byte[0]);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> documentService.upload(emptyFile, "2025-1", "Ingeniería", "Sistemas",
                        Document.DocumentType.PLAN, Project.Campus.LIMA, null, 1L));
        assertTrue(ex.getMessage().contains("seleccionar"));
    }

    @Test
    @DisplayName("Subir archivo que supera el tamaño máximo (20MB) → lanza excepción")
    void upload_fileTooLarge_shouldThrow() {
        byte[] bigContent = new byte[21 * 1024 * 1024]; // 21MB
        MultipartFile bigFile = new MockMultipartFile(
                "file", "grande.pdf", "application/pdf", bigContent);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> documentService.upload(bigFile, "2025-1", "Ingeniería", "Sistemas",
                        Document.DocumentType.PLAN, Project.Campus.LIMA, null, 1L));
        assertTrue(ex.getMessage().contains("tamaño máximo"));
    }

    @Test
    @DisplayName("Subir documento con usuario inexistente → lanza excepción")
    void upload_userNotFound_shouldThrow() {
        MultipartFile pdfFile = new MockMultipartFile(
                "file", "plan.pdf", "application/pdf", "contenido".getBytes());

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> documentService.upload(pdfFile, "2025-1", "Ingeniería", "Sistemas",
                        Document.DocumentType.PLAN, Project.Campus.LIMA, null, 99L));
    }

    @Test
    @DisplayName("Eliminar documento existente → borra archivo físico y registro en BD")
    void delete_existingDocument_shouldDeleteFileAndRecord() {
        Document doc = Document.builder()
                .filePath("uploads/documentos/2025-1/ing/sistemas/archivo.pdf")
                .build();
        doc.setId(1L);

        when(documentRepository.findById(1L)).thenReturn(Optional.of(doc));

        documentService.delete(1L);

        verify(storageService, times(1)).deleteFile(doc.getFilePath());
        verify(documentRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Eliminar documento inexistente → lanza excepción")
    void delete_nonExistentDocument_shouldThrow() {
        when(documentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> documentService.delete(99L));
    }
}