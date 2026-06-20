package com.proyecto.controller;

import com.proyecto.dto.attendance.StudentHoursSummaryResponse;
import com.proyecto.entity.Project;
import com.proyecto.repository.ProjectRepository;
import com.proyecto.service.AttendanceService;
import com.proyecto.service.ExcelReportService;
import com.proyecto.service.PdfReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ExcelReportService excelReportService;
    private final PdfReportService pdfReportService;
    private final AttendanceService attendanceService;
    private final ProjectRepository projectRepository;

    public ReportController(ExcelReportService excelReportService,
                             PdfReportService pdfReportService,
                             AttendanceService attendanceService,
                             ProjectRepository projectRepository) {
        this.excelReportService = excelReportService;
        this.pdfReportService = pdfReportService;
        this.attendanceService = attendanceService;
        this.projectRepository = projectRepository;
    }

    @GetMapping("/student/{userId}/excel")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_COORDINADOR')")
    public ResponseEntity<byte[]> studentExcel(@PathVariable Long userId) throws Exception {
        StudentHoursSummaryResponse resumen = attendanceService.getStudentHoursSummary(userId);
        byte[] file = excelReportService.generateStudentReport(resumen);
        return buildFileResponse(file, "reporte-estudiante-" + userId + ".xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    @GetMapping("/student/{userId}/pdf")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_COORDINADOR')")
    public ResponseEntity<byte[]> studentPdf(@PathVariable Long userId) throws Exception {
        StudentHoursSummaryResponse resumen = attendanceService.getStudentHoursSummary(userId);
        byte[] file = pdfReportService.generateStudentReport(resumen);
        return buildFileResponse(file, "reporte-estudiante-" + userId + ".pdf",
                MediaType.APPLICATION_PDF_VALUE);
    }

    @GetMapping("/project/{projectId}/excel")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_COORDINADOR')")
    public ResponseEntity<byte[]> projectExcel(@PathVariable Long projectId) throws Exception {
        Project project = findProjectOrThrow(projectId);
        byte[] file = excelReportService.generateProjectReport(project);
        return buildFileResponse(file, "reporte-proyecto-" + projectId + ".xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    @GetMapping("/project/{projectId}/pdf")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_COORDINADOR')")
    public ResponseEntity<byte[]> projectPdf(@PathVariable Long projectId) throws Exception {
        Project project = findProjectOrThrow(projectId);
        byte[] file = pdfReportService.generateProjectReport(project);
        return buildFileResponse(file, "reporte-proyecto-" + projectId + ".pdf",
                MediaType.APPLICATION_PDF_VALUE);
    }

    private Project findProjectOrThrow(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado con id: " + id));
    }

    private ResponseEntity<byte[]> buildFileResponse(byte[] file, String fileName, String contentType) {
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(file);
    }
}