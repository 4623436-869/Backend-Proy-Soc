package com.proyecto.repository;

import com.proyecto.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    List<Document> findByCicloAcademico(String cicloAcademico);

    List<Document> findByFacultad(String facultad);

    @Query("SELECT d FROM Document d WHERE " +
           "(:cicloAcademico IS NULL OR d.cicloAcademico = :cicloAcademico) AND " +
           "(:facultad IS NULL OR LOWER(d.facultad) LIKE LOWER(CONCAT('%', :facultad, '%'))) AND " +
           "(:escuela IS NULL OR LOWER(d.escuela) LIKE LOWER(CONCAT('%', :escuela, '%'))) AND " +
           "(:nombre IS NULL OR LOWER(d.originalFileName) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND " +
           "(:tipo IS NULL OR d.tipo = :tipo) " +
           "ORDER BY d.uploadedAt DESC")
    List<Document> findByFilters(
            @Param("cicloAcademico") String cicloAcademico,
            @Param("facultad") String facultad,
            @Param("escuela") String escuela,
            @Param("nombre") String nombre,
            @Param("tipo") Document.DocumentType tipo
    );
}