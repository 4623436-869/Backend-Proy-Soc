package com.proyecto;

import com.proyecto.dto.attendance.AttendanceRequest;
import com.proyecto.dto.attendance.AttendanceResponse;
import com.proyecto.entity.*;
import com.proyecto.repository.*;
import com.proyecto.service.AttendanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AttendanceHoursTest {

    @Mock private ParticipationRepository participationRepository;
    @Mock private UserRepository userRepository;
    @Mock private ProjectRepository projectRepository;

    @InjectMocks
    private AttendanceService attendanceService;

    private User activeUser;
    private User inactiveUser;
    private Project activeProject;
    private Project inactiveProject;

    @BeforeEach
    void setUp() {
        activeUser = User.builder()
                .id(1L).fullName("Usuario Activo")
                .email("activo@proyecto.com").password("pass")
                .active(true).roles(Set.of()).build();

        inactiveUser = User.builder()
                .id(2L).fullName("Usuario Inactivo")
                .email("inactivo@proyecto.com").password("pass")
                .active(false).roles(Set.of()).build();

        activeProject = Project.builder()
                .id(1L).name("Proyecto Activo")
                .status(Project.ProjectStatus.ACTIVO)
                .startDate(LocalDate.now()).build();

        inactiveProject = Project.builder()
                .id(2L).name("Proyecto Inactivo")
                .status(Project.ProjectStatus.INACTIVO)
                .startDate(LocalDate.now()).build();
    }

    @Test
    @DisplayName("Registro de 2 horas exactas → hoursLogged = 2.0")
    void register_twoHours_shouldLogCorrectly() {
        LocalDateTime checkIn  = LocalDateTime.of(2025, 6, 1, 8, 0);
        LocalDateTime checkOut = LocalDateTime.of(2025, 6, 1, 10, 0);
        AttendanceRequest request = buildRequest(1L, 1L, checkIn, checkOut);

        when(userRepository.findById(1L)).thenReturn(Optional.of(activeUser));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(activeProject));
        when(participationRepository.existsOverlap(any(), any(), any())).thenReturn(false);
        when(participationRepository.save(any())).thenAnswer(inv -> {
            Participation p = inv.getArgument(0);
            p.setId(1L);
            return p;
        });

        AttendanceResponse response = attendanceService.registerManual(request);
        assertEquals(2.0, response.getHoursLogged());
    }

    @Test
    @DisplayName("Registro de 1.5 horas → hoursLogged = 1.5")
    void register_oneAndHalfHours_shouldLogCorrectly() {
        LocalDateTime checkIn  = LocalDateTime.of(2025, 6, 1, 8, 0);
        LocalDateTime checkOut = LocalDateTime.of(2025, 6, 1, 9, 30);
        AttendanceRequest request = buildRequest(1L, 1L, checkIn, checkOut);

        when(userRepository.findById(1L)).thenReturn(Optional.of(activeUser));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(activeProject));
        when(participationRepository.existsOverlap(any(), any(), any())).thenReturn(false);
        when(participationRepository.save(any())).thenAnswer(inv -> {
            Participation p = inv.getArgument(0);
            p.setId(2L);
            return p;
        });

        AttendanceResponse response = attendanceService.registerManual(request);
        assertEquals(1.5, response.getHoursLogged());
    }

    @Test
    @DisplayName("Registro de 45 minutos → hoursLogged = 0.75")
    void register_45minutes_shouldLogCorrectly() {
        LocalDateTime checkIn  = LocalDateTime.of(2025, 6, 1, 8, 0);
        LocalDateTime checkOut = LocalDateTime.of(2025, 6, 1, 8, 45);
        AttendanceRequest request = buildRequest(1L, 1L, checkIn, checkOut);

        when(userRepository.findById(1L)).thenReturn(Optional.of(activeUser));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(activeProject));
        when(participationRepository.existsOverlap(any(), any(), any())).thenReturn(false);
        when(participationRepository.save(any())).thenAnswer(inv -> {
            Participation p = inv.getArgument(0);
            p.setId(3L);
            return p;
        });

        AttendanceResponse response = attendanceService.registerManual(request);
        assertEquals(0.75, response.getHoursLogged());
    }

    @Test
    @DisplayName("Usuario inactivo → lanza excepción")
    void register_inactiveUser_shouldThrow() {
        AttendanceRequest request = buildRequest(2L, 1L,
                LocalDateTime.of(2025, 6, 1, 8, 0),
                LocalDateTime.of(2025, 6, 1, 10, 0));

        when(userRepository.findById(2L)).thenReturn(Optional.of(inactiveUser));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> attendanceService.registerManual(request));
        assertTrue(ex.getMessage().contains("inactivo"));
    }

    @Test
    @DisplayName("Proyecto inactivo → lanza excepción")
    void register_inactiveProject_shouldThrow() {
        AttendanceRequest request = buildRequest(1L, 2L,
                LocalDateTime.of(2025, 6, 1, 8, 0),
                LocalDateTime.of(2025, 6, 1, 10, 0));

        when(userRepository.findById(1L)).thenReturn(Optional.of(activeUser));
        when(projectRepository.findById(2L)).thenReturn(Optional.of(inactiveProject));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> attendanceService.registerManual(request));
        assertTrue(ex.getMessage().contains("activo"));
    }

    @Test
    @DisplayName("Horario solapado → lanza excepción")
    void register_overlappingSchedule_shouldThrow() {
        AttendanceRequest request = buildRequest(1L, 1L,
                LocalDateTime.of(2025, 6, 1, 8, 0),
                LocalDateTime.of(2025, 6, 1, 10, 0));

        when(userRepository.findById(1L)).thenReturn(Optional.of(activeUser));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(activeProject));
        when(participationRepository.existsOverlap(any(), any(), any())).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> attendanceService.registerManual(request));
        assertTrue(ex.getMessage().contains("solapado") ||
                   ex.getMessage().contains("rango horario"));
    }

    private AttendanceRequest buildRequest(Long userId, Long projectId,
                                           LocalDateTime checkIn,
                                           LocalDateTime checkOut) {
        AttendanceRequest r = new AttendanceRequest();
        r.setUserId(userId);
        r.setProjectId(projectId);
        r.setCheckIn(checkIn);
        r.setCheckOut(checkOut);
        return r;
    }
}