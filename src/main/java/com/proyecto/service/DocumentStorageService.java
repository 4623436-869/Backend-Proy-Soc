package com.proyecto.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.UUID;

@Service
public class DocumentStorageService {

    @Value("${app.upload.dir:uploads/documentos}")
    private String baseUploadDir;

    /**
     * Guarda el PDF en disco siguiendo la estructura jerárquica:
     * uploads/documentos/{ciclo}/{facultad}/{escuela}/{uuid}_{nombreOriginal}
     * Retorna la ruta relativa guardada (para almacenar en BD).
     */
    public String store(MultipartFile file, String cicloAcademico,
                         String facultad, String escuela, String storedFileName) throws IOException {

        String safeCiclo = sanitize(cicloAcademico);
        String safeFacultad = sanitize(facultad);
        String safeEscuela = sanitize(escuela);

        Path dir = Paths.get(baseUploadDir, safeCiclo, safeFacultad, safeEscuela);
        Files.createDirectories(dir);

        Path destino = dir.resolve(storedFileName);
        Files.copy(file.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);

        return destino.toString();
    }

    public InputStream readFile(String filePath) throws IOException {
        return Files.newInputStream(Paths.get(filePath));
    }

    public void deleteFile(String filePath) {
        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException ignored) {
            // Si falla el borrado físico no detenemos el flujo, solo se borra el registro en BD
        }
    }

    public String generateStoredFileName(String originalFileName) {
        return UUID.randomUUID() + "_" + originalFileName.replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    private String sanitize(String value) {
        if (value == null) return "general";
        return value.trim().replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}