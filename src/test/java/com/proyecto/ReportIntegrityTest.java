package com.proyecto;

import com.proyecto.dto.attendance.StudentHoursSummaryResponse;
import com.proyecto.entity.Project;
import com.proyecto.repository.ParticipationRepository;
import com.proyecto.service.ExcelReportService;
import com.proyecto.service.PdfReportService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportIntegrityTest {

    @Mock private ParticipationRepository participationRepository;

    @InjectMocks
    private ExcelReportService excelReportService;

    @InjectMocks
    private PdfReportService pdfReportService;

    private StudentHoursSummaryResponse resumenEstudiante;
    private Project proyecto;

    @BeforeEach
    void setUp() {
        resumenEstudiante = StudentHoursSummaryResponse.builder()
                .userId(1L)
                .fullName("Juan Pérez")
                .codigoEstudiante("2021012345")
                .totalHoursAllCycles(20.5)
                .details(List.of(
                        new StudentHoursSummaryResponse.ProjectCycleHours(
                                10L, "La Hora del Código", "2025-1", 12.5),
                        new StudentHoursSummaryResponse.ProjectCycleHours(
                                11L, "Campaña de Salud", "2025-2", 8.0)
                ))
                .build();

        proyecto = Project.builder()
                .id(1L).name("La Hora del Código")
                .status(Project.ProjectStatus.ACTIVO)
                .cicloAcademico("2025-1")
                .campus(Project.Campus.LIMA)
                .startDate(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Reporte Excel de estudiante → genera archivo no vacío y con datos correctos")
    void generateStudentExcel_shouldContainCorrectData() throws Exception {
        byte[] result = excelReportService.generateStudentReport(resumenEstudiante);

        assertNotNull(result);
        assertTrue(result.length > 0);

        try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(result))) {
            Sheet sheet = workbook.getSheetAt(0);
            assertNotNull(sheet);

            // Verifica que el nombre del estudiante aparece en alguna celda
            boolean nombreEncontrado = false;
            for (Row row : sheet) {
                for (var cell : row) {
                    if (cell.getCellType().toString().equals("STRING")
                            && cell.getStringCellValue().contains("Juan Pérez")) {
                        nombreEncontrado = true;
                    }
                }
            }
            assertTrue(nombreEncontrado, "El nombre del estudiante debe aparecer en el Excel generado");
        }
    }

    @Test
    @DisplayName("Reporte Excel de estudiante sin horas registradas → no lanza excepción, archivo válido")
    void generateStudentExcel_noHours_shouldNotThrow() throws Exception {
        StudentHoursSummaryResponse vacio = StudentHoursSummaryResponse.builder()
                .userId(2L).fullName("Estudiante Sin Horas")
                .codigoEstudiante(null)
                .totalHoursAllCycles(0.0)
                .details(List.of())
                .build();

        byte[] result = excelReportService.generateStudentReport(vacio);

        assertNotNull(result);
        assertTrue(result.length > 0);
    }

    @Test
    @DisplayName("Reporte PDF de estudiante → genera archivo no vacío con cabecera PDF válida")
    void generateStudentPdf_shouldBeValidPdf() throws Exception {
        byte[] result = pdfReportService.generateStudentReport(resumenEstudiante);

        assertNotNull(result);
        assertTrue(result.length > 0);

        // Todo PDF válido inicia con la firma %PDF-
        String header = new String(result, 0, 5);
        assertEquals("%PDF-", header);
    }

    @Test
    @DisplayName("Reporte Excel de proyecto → genera archivo no vacío")
    void generateProjectExcel_shouldNotBeEmpty() throws Exception {
when(participationRepository.findSummaryByProject(1L)).thenReturn(
                java.util.Collections.singletonList(
                        new Object[]{1L, "Juan Pérez", 1L, "La Hora del Código", 12.5, 3L}
                ));

        byte[] result = excelReportService.generateProjectReport(proyecto);

        assertNotNull(result);
        assertTrue(result.length > 0);
    }

    @Test
    @DisplayName("Reporte PDF de proyecto sin participantes → no lanza excepción")
    void generateProjectPdf_noParticipants_shouldNotThrow() throws Exception {
        when(participationRepository.findSummaryByProject(1L)).thenReturn(List.of());

        byte[] result = pdfReportService.generateProjectReport(proyecto);

        assertNotNull(result);
        assertTrue(result.length > 0);
    }
}