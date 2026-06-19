package com.proyecto.repository;

import com.proyecto.entity.Participation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

import java.time.LocalDateTime;
import java.util.List;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {

    List<Participation> findByUserId(Long userId);

    List<Participation> findByProjectId(Long projectId);

    // Total de horas acumuladas por usuario en un proyecto
    @Query("SELECT COALESCE(SUM(p.hoursLogged), 0) FROM Participation p " +
           "WHERE p.user.id = :userId AND p.project.id = :projectId")
    Double sumHoursByUserAndProject(
            @Param("userId") Long userId,
            @Param("projectId") Long projectId
    );

    // Suma total de horas de un usuario en todos sus proyectos
    @Query("SELECT COALESCE(SUM(p.hoursLogged), 0) FROM Participation p " +
           "WHERE p.user.id = :userId")
    Double sumAllHoursByUser(@Param("userId") Long userId);

    // Horas por método de registro (MANUAL vs QR)
    @Query("SELECT p.registrationMethod, COALESCE(SUM(p.hoursLogged), 0) " +
           "FROM Participation p " +
           "WHERE p.user.id = :userId AND p.project.id = :projectId " +
           "GROUP BY p.registrationMethod")
    List<Object[]> sumHoursByMethodAndUserAndProject(
            @Param("userId") Long userId,
            @Param("projectId") Long projectId
    );

    // Verificar solapamiento de horarios
    @Query("SELECT COUNT(p) > 0 FROM Participation p " +
           "WHERE p.user.id = :userId " +
           "AND p.checkIn < :checkOut " +
           "AND p.checkOut > :checkIn")
    Boolean existsOverlap(
            @Param("userId") Long userId,
            @Param("checkIn") LocalDateTime checkIn,
            @Param("checkOut") LocalDateTime checkOut
    );

    // Buscar asistencia abierta (sin checkOut) de un usuario en un proyecto
    @Query("SELECT p FROM Participation p " +
           "WHERE p.user.id = :userId " +
           "AND p.project.id = :projectId " +
           "AND p.checkOut IS NULL " +
           "ORDER BY p.checkIn DESC")
    Optional<Participation> findOpenByUserAndProject(
            @Param("userId") Long userId,
            @Param("projectId") Long projectId
    );

    // Filtrado avanzado de registros
    @Query("SELECT p FROM Participation p " +
           "WHERE (:userId IS NULL OR p.user.id = :userId) " +
           "AND (:projectId IS NULL OR p.project.id = :projectId) " +
           "AND (:from IS NULL OR p.checkIn >= :from) " +
           "AND (:to IS NULL OR p.checkOut <= :to) " +
           "AND (:method IS NULL OR p.registrationMethod = :method) " +
           "ORDER BY p.checkIn DESC")
    List<Participation> findByFilters(
            @Param("userId") Long userId,
            @Param("projectId") Long projectId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            @Param("method") Participation.RegistrationMethod method
    );

    // Resumen de horas por usuario y proyecto
    @Query("SELECT p.user.id, p.user.fullName, p.project.id, p.project.name, " +
           "SUM(p.hoursLogged), COUNT(p) " +
           "FROM Participation p " +
           "WHERE (:projectId IS NULL OR p.project.id = :projectId) " +
           "GROUP BY p.user.id, p.user.fullName, p.project.id, p.project.name")
    List<Object[]> findSummaryByProject(@Param("projectId") Long projectId);

    // Validar usuarios activos — cuenta registros de usuarios inactivos
    @Query("SELECT COUNT(p) FROM Participation p " +
           "WHERE p.user.id = :userId AND p.user.active = false")
    Long countByInactiveUser(@Param("userId") Long userId);

// Horas de un usuario agrupadas por proyecto y ciclo académico
    @Query("SELECT p.project.id, p.project.name, p.project.cicloAcademico, " +
           "COALESCE(SUM(p.hoursLogged), 0) " +
           "FROM Participation p " +
           "WHERE p.user.id = :userId " +
           "GROUP BY p.project.id, p.project.name, p.project.cicloAcademico " +
           "ORDER BY p.project.cicloAcademico DESC")
    List<Object[]> findHoursByUserGroupedByProjectAndCiclo(@Param("userId") Long userId);

    // PR42/PR43 — Verificar si ya existe asistencia del usuario
    // en el mismo proyecto en el mismo día
    @Query("SELECT COUNT(p) > 0 FROM Participation p " +
           "WHERE p.user.id = :userId " +
           "AND p.project.id = :projectId " +
           "AND p.checkIn >= :inicioDia " +
           "AND p.checkIn <= :finDia")
    Boolean existsByUserAndProjectAndDate(
            @Param("userId") Long userId,
            @Param("projectId") Long projectId,
            @Param("inicioDia") LocalDateTime inicioDia,
            @Param("finDia") LocalDateTime finDia
    );
    // Total de horas registradas en todo el sistema
    @Query("SELECT COALESCE(SUM(p.hoursLogged), 0) FROM Participation p")
    Double sumAllHoursSystem();

    // Total de horas agrupadas por campus del proyecto
    @Query("SELECT p.project.campus, COALESCE(SUM(p.hoursLogged), 0) " +
           "FROM Participation p WHERE p.project.campus IS NOT NULL " +
           "GROUP BY p.project.campus")
    List<Object[]> sumHoursByCampus();
}