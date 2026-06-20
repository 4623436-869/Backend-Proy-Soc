package com.proyecto.service;

import com.proyecto.dto.attendance.StudentHoursSummaryResponse;
import com.proyecto.entity.Participation;
import com.proyecto.entity.Project;
import com.proyecto.repository.ParticipationRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExcelReportService {

    private final ParticipationRepository participationRepository;

    public ExcelReportService(ParticipationRepository participationRepository) {
        this.participationRepository = participationRepository;
    }

    public byte[] generateStudentReport(StudentHoursSummaryResponse resumen) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Reporte de Estudiante");

            CellStyle titleStyle = createTitleStyle(workbook);
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle normalStyle = createNormalStyle(workbook);

            int rowIdx = 0;

            Row title = sheet.createRow(rowIdx++);
            Cell titleCell = title.createCell(0);
            titleCell.setCellValue("Universidad - Reporte de Participación Estudiantil (SUNEDU/AAA)");
            titleCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 3));

            rowIdx++;

            Row infoRow1 = sheet.createRow(rowIdx++);
            infoRow1.createCell(0).setCellValue("Estudiante:");
            infoRow1.createCell(1).setCellValue(resumen.getFullName());

            Row infoRow2 = sheet.createRow(rowIdx++);
            infoRow2.createCell(0).setCellValue("Código:");
            infoRow2.createCell(1).setCellValue(
                    resumen.getCodigoEstudiante() != null ? resumen.getCodigoEstudiante() : "Sin asignar");

            Row infoRow3 = sheet.createRow(rowIdx++);
            infoRow3.createCell(0).setCellValue("Total de horas acumuladas:");
            infoRow3.createCell(1).setCellValue(resumen.getTotalHoursAllCycles());

            rowIdx++;

            Row headerRow = sheet.createRow(rowIdx++);
            String[] headers = {"Proyecto", "Ciclo Académico", "Horas"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            for (StudentHoursSummaryResponse.ProjectCycleHours d : resumen.getDetails()) {
                Row row = sheet.createRow(rowIdx++);
                createCell(row, 0, d.getProjectName(), normalStyle);
                createCell(row, 1, d.getCicloAcademico(), normalStyle);
                createCell(row, 2, d.getHours(), normalStyle);
            }

            for (int i = 0; i < 4; i++) {
                sheet.autoSizeColumn(i);
            }

            return toByteArray(workbook);
        }
    }

    public byte[] generateProjectReport(Project project) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Reporte de Proyecto");

            CellStyle titleStyle = createTitleStyle(workbook);
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle normalStyle = createNormalStyle(workbook);

            List<Object[]> rows = participationRepository.findSummaryByProject(project.getId())
                    .stream()
                    .filter(r -> ((Number) r[2]).longValue() == project.getId())
                    .collect(Collectors.toList());

            int rowIdx = 0;

            Row title = sheet.createRow(rowIdx++);
            Cell titleCell = title.createCell(0);
            titleCell.setCellValue("Universidad - Reporte de Participación por Proyecto (SUNEDU/AAA)");
            titleCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 3));

            rowIdx++;

            Row infoRow1 = sheet.createRow(rowIdx++);
            infoRow1.createCell(0).setCellValue("Proyecto:");
            infoRow1.createCell(1).setCellValue(project.getName());

            Row infoRow2 = sheet.createRow(rowIdx++);
            infoRow2.createCell(0).setCellValue("Ciclo Académico:");
            infoRow2.createCell(1).setCellValue(
                    project.getCicloAcademico() != null ? project.getCicloAcademico() : "—");

            Row infoRow3 = sheet.createRow(rowIdx++);
            infoRow3.createCell(0).setCellValue("Campus:");
            infoRow3.createCell(1).setCellValue(
                    project.getCampus() != null ? project.getCampus().name() : "—");

            double totalHoras = rows.stream()
                    .mapToDouble(r -> r[4] != null ? ((Number) r[4]).doubleValue() : 0.0)
                    .sum();

            Row infoRow4 = sheet.createRow(rowIdx++);
            infoRow4.createCell(0).setCellValue("Total de horas del proyecto:");
            infoRow4.createCell(1).setCellValue(totalHoras);

            rowIdx++;

            Row headerRow = sheet.createRow(rowIdx++);
            String[] headers = {"Participante", "Horas Acumuladas", "Sesiones"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            for (Object[] row : rows) {
                Row r = sheet.createRow(rowIdx++);
                createCell(r, 0, (String) row[1], normalStyle);
                createCell(r, 1, row[4] != null ? ((Number) row[4]).doubleValue() : 0.0, normalStyle);
                createCell(r, 2, ((Number) row[5]).longValue(), normalStyle);
            }

            for (int i = 0; i < 4; i++) {
                sheet.autoSizeColumn(i);
            }

            return toByteArray(workbook);
        }
    }

    private void createCell(Row row, int col, String value, CellStyle style) {
        Cell cell = row.createCell(col);
        cell.setCellValue(value != null ? value : "—");
        cell.setCellStyle(style);
    }

    private void createCell(Row row, int col, double value, CellStyle style) {
        Cell cell = row.createCell(col);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    private void createCell(Row row, int col, long value, CellStyle style) {
        Cell cell = row.createCell(col);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    private CellStyle createTitleStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 14);
        style.setFont(font);
        return style;
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.INDIGO.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    private CellStyle createNormalStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        return style;
    }

    private byte[] toByteArray(Workbook workbook) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            workbook.write(out);
            return out.toByteArray();
        }
    }
}