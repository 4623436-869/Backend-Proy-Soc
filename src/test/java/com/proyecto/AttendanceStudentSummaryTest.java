package com.proyecto;

import com.proyecto.entity.*;
import com.proyecto.repository.*;
import com.proyecto.service.AttendanceService;
import com.proyecto.dto.attendance.StudentHoursSummaryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AttendanceStudentSummaryTest {

    @Mock private ParticipationRepository participationRepository;
    @Mock private UserRepository userRepository;
    @Mock private ProjectRepository projectRepository;

    @InjectMocks
    private AttendanceService attendanceService;

    private User estudiante;

    @BeforeEach
    void setUp() {
        estudiante = User.builder()
                .id(1L).fullName("Juan Pérez")
                .email("juan@proyecto.com").password("pass")
                .active(true).codigoEstudiante("2021012345")
                .roles(Set.of()).build();
    }

    @Test
    @DisplayName("Resumen de horas con varios proyectos y ciclos → suma correctamente")
    void getStudentHoursSummary_multipleProjectsAndCycles_shouldSumCorrectly() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(estudiante));

        List<Object[]> rows = List.of(
                new Object[]{10L, "La Hora del Código", "2025-1", 12.5},
                new Object[]{11L, "Campaña de Salud", "2025-2", 8.0}
        );
        when(participationRepository.findHoursByUserGroupedByProjectAndCiclo(1L))
                .thenReturn(rows);

        StudentHoursSummaryResponse result = attendanceService.getStudentHoursSummary(1L);

        assertEquals("Juan Pérez", result.getFullName());
        assertEquals("2021012345", result.getCodigoEstudiante());
        assertEquals(20.5, result.getTotalHoursAllCycles());
        assertEquals(2, result.getDetails().size());
    }

    @Test
    @DisplayName("Resumen de horas de estudiante sin participaciones → total 0 y lista vacía")
    void getStudentHoursSummary_noParticipations_shouldReturnZero() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(estudiante));
        when(participationRepository.findHoursByUserGroupedByProjectAndCiclo(1L))
                .thenReturn(List.of());

        StudentHoursSummaryResponse result = attendanceService.getStudentHoursSummary(1L);

        assertEquals(0.0, result.getTotalHoursAllCycles());
        assertTrue(result.getDetails().isEmpty());
    }

    @Test
    @DisplayName("Resumen de horas de usuario inexistente → lanza excepción")
    void getStudentHoursSummary_userNotFound_shouldThrow() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> attendanceService.getStudentHoursSummary(99L));
    }

    @Test
    @DisplayName("Resumen de horas de estudiante sin código asignado → codigoEstudiante es null")
    void getStudentHoursSummary_studentWithoutCodigo_shouldReturnNullCodigo() {
        estudiante.setCodigoEstudiante(null);
        when(userRepository.findById(1L)).thenReturn(Optional.of(estudiante));
        when(participationRepository.findHoursByUserGroupedByProjectAndCiclo(1L))
                .thenReturn(List.of());

        StudentHoursSummaryResponse result = attendanceService.getStudentHoursSummary(1L);

        assertNull(result.getCodigoEstudiante());
    }
}