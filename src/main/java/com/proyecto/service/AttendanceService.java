package com.proyecto.service;

import com.proyecto.dto.attendance.*;
import com.proyecto.entity.*;
import com.proyecto.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    private static final Logger log = LoggerFactory.getLogger(AttendanceService.class);

    private final ParticipationRepository participationRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public AttendanceService(ParticipationRepository participationRepository,
                             UserRepository userRepository,
                             ProjectRepository projectRepository) {
        this.participationRepository = participationRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    @Transactional
    public AttendanceResponse registerManual(AttendanceRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!user.getActive()) {
            throw new RuntimeException(
                "No se puede registrar asistencia para un usuario inactivo");
        }

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

        if (project.getStatus() != Project.ProjectStatus.ACTIVO) {
            throw new RuntimeException(
                "No se puede registrar asistencia en un proyecto que no está activo");
        }

        if (participationRepository.existsOverlap(
                request.getUserId(), request.getCheckIn(), request.getCheckOut())) {
            throw new RuntimeException(
                "El usuario ya tiene una asistencia registrada en ese rango horario");
        }

        double hours = calculateHours(request.getCheckIn(), request.getCheckOut());

        Participation participation = Participation.builder()
                .user(user)
                .project(project)
                .checkIn(request.getCheckIn())
                .checkOut(request.getCheckOut())
                .hoursLogged(hours)
                .registrationMethod(Participation.RegistrationMethod.MANUAL)
                .build();

        return AttendanceResponse.fromEntity(participationRepository.save(participation));
    }

    @Transactional(readOnly = true)
    public AttendanceResponse getById(Long id) {
        return AttendanceResponse.fromEntity(findOrThrow(id));
    }

    @Transactional(readOnly = true)
    public List<AttendanceResponse> getByUser(Long userId) {
        return participationRepository.findByUserId(userId)
                .stream()
                .map(AttendanceResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AttendanceResponse> getByProject(Long projectId) {
        return participationRepository.findByProjectId(projectId)
                .stream()
                .map(AttendanceResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Double getTotalHours(Long userId, Long projectId) {
        return participationRepository.sumHoursByUserAndProject(userId, projectId);
    }

    @Transactional(readOnly = true)
    public List<AttendanceResponse> getByFilters(AttendanceFilterRequest filter) {
        return participationRepository.findByFilters(
                    filter.getUserId(),
                    filter.getProjectId(),
                    filter.getFrom(),
                    filter.getTo(),
                    filter.getMethod())
                .stream()
                .map(AttendanceResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AttendanceSummaryResponse> getSummaryByProject(Long projectId) {
        return participationRepository.findSummaryByProject(projectId)
                .stream()
                .map(row -> AttendanceSummaryResponse.builder()
                        .userId(((Number) row[0]).longValue())
                        .userFullName((String) row[1])
                        .projectId(((Number) row[2]).longValue())
                        .projectName((String) row[3])
                        .totalHours(row[4] != null
                                ? ((Number) row[4]).doubleValue() : 0.0)
                        .totalSessions(((Number) row[5]).longValue())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public AttendanceResponse closeOpenAttendance(Long userId, Long projectId) {
        Participation open = participationRepository
                .findOpenByUserAndProject(userId, projectId)
                .orElseThrow(() -> new RuntimeException(
                    "No hay una asistencia abierta para este usuario en este proyecto"));

        LocalDateTime checkOut = LocalDateTime.now();
        open.setCheckOut(checkOut);
        open.setHoursLogged(calculateHours(open.getCheckIn(), checkOut));

        return AttendanceResponse.fromEntity(participationRepository.save(open));
    }

    @Transactional
    public AttendanceResponse update(Long id, AttendanceRequest request) {
        Participation participation = findOrThrow(id);

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!user.getActive()) {
            throw new RuntimeException(
                "No se puede actualizar asistencia para un usuario inactivo");
        }

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

        if (request.getCheckOut().isBefore(request.getCheckIn())) {
            throw new RuntimeException(
                "La hora de salida no puede ser anterior a la hora de entrada");
        }

        double newHours = calculateHours(request.getCheckIn(), request.getCheckOut());

        log.info("Actualizando registro id={} | horas anteriores={} | horas nuevas={}",
                id, participation.getHoursLogged(), newHours);

        participation.setUser(user);
        participation.setProject(project);
        participation.setCheckIn(request.getCheckIn());
        participation.setCheckOut(request.getCheckOut());
        participation.setHoursLogged(newHours);

        return AttendanceResponse.fromEntity(participationRepository.save(participation));
    }

    @Transactional
    public void delete(Long id) {
        if (!participationRepository.existsById(id)) {
            throw new RuntimeException(
                "Registro de asistencia no encontrado con id: " + id);
        }
        participationRepository.deleteById(id);
    }

    // ─── helpers ─────────────────────────────────────────────────────────────

    private Participation findOrThrow(Long id) {
        return participationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                    "Registro no encontrado con id: " + id));
    }

    private double calculateHours(LocalDateTime checkIn, LocalDateTime checkOut) {
        long minutes = Duration.between(checkIn, checkOut).toMinutes();
        return Math.round((minutes / 60.0) * 100.0) / 100.0;
    }
}