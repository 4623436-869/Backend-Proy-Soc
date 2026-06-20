package com.proyecto;

import com.proyecto.dto.dashboard.DashboardSummaryResponse;
import com.proyecto.entity.Project;
import com.proyecto.repository.BeneficiaryRepository;
import com.proyecto.repository.ParticipationRepository;
import com.proyecto.repository.ProjectRepository;
import com.proyecto.service.DashboardService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock private ProjectRepository projectRepository;
    @Mock private BeneficiaryRepository beneficiaryRepository;
    @Mock private ParticipationRepository participationRepository;

    @InjectMocks
    private DashboardService dashboardService;

    @Test
    @DisplayName("Resumen del dashboard con datos en todos los campus → calcula correctamente")
    void getSummary_withDataInAllCampus_shouldCalculateCorrectly() {
        when(projectRepository.countByStatus(Project.ProjectStatus.ACTIVO)).thenReturn(5L);
        when(participationRepository.sumAllHoursSystem()).thenReturn(120.5);
        when(beneficiaryRepository.count()).thenReturn(30L);

        when(projectRepository.countByCampus()).thenReturn(List.of(
                new Object[]{Project.Campus.LIMA, 3L},
                new Object[]{Project.Campus.JULIACA, 2L}
        ));
        when(participationRepository.sumHoursByCampus()).thenReturn(List.of(
                new Object[]{Project.Campus.LIMA, 80.0},
                new Object[]{Project.Campus.JULIACA, 40.5}
        ));
        when(beneficiaryRepository.countByCampus()).thenReturn(List.of(
                new Object[]{Project.Campus.LIMA, 20L},
                new Object[]{Project.Campus.JULIACA, 10L}
        ));

        DashboardSummaryResponse result = dashboardService.getSummary();

        assertEquals(5L, result.getActiveProjects());
        assertEquals(120.5, result.getTotalHours());
        assertEquals(30L, result.getTotalBeneficiaries());
        assertEquals(3, result.getByCampus().size());

        var lima = result.getByCampus().stream()
                .filter(c -> c.getCampus().equals("LIMA"))
                .findFirst().orElseThrow();
        assertEquals(3L, lima.getProjectCount());
        assertEquals(80.0, lima.getTotalHours());
        assertEquals(20L, lima.getBeneficiaryCount());

        var tarapoto = result.getByCampus().stream()
                .filter(c -> c.getCampus().equals("TARAPOTO"))
                .findFirst().orElseThrow();
        assertEquals(0L, tarapoto.getProjectCount());
        assertEquals(0.0, tarapoto.getTotalHours());
    }

    @Test
    @DisplayName("Resumen del dashboard sin ningún dato → ceros en todo, sin lanzar excepción")
    void getSummary_noData_shouldReturnZeros() {
        when(projectRepository.countByStatus(Project.ProjectStatus.ACTIVO)).thenReturn(0L);
        when(participationRepository.sumAllHoursSystem()).thenReturn(null);
        when(beneficiaryRepository.count()).thenReturn(0L);
        when(projectRepository.countByCampus()).thenReturn(List.of());
        when(participationRepository.sumHoursByCampus()).thenReturn(List.of());
        when(beneficiaryRepository.countByCampus()).thenReturn(List.of());

        DashboardSummaryResponse result = dashboardService.getSummary();

        assertEquals(0L, result.getActiveProjects());
        assertEquals(0.0, result.getTotalHours());
        assertEquals(0L, result.getTotalBeneficiaries());
        assertEquals(3, result.getByCampus().size());
        result.getByCampus().forEach(c -> {
            assertEquals(0L, c.getProjectCount());
            assertEquals(0.0, c.getTotalHours());
            assertEquals(0L, c.getBeneficiaryCount());
        });
    }
}