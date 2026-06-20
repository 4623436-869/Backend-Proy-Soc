package com.proyecto.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.proyecto.dto.attendance.StudentHoursSummaryResponse;
import com.proyecto.entity.Project;
import com.proyecto.repository.ParticipationRepository;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PdfReportService {

    private final ParticipationRepository participationRepository;

    private static final Font TITLE_FONT = new Font(Font.HELVETICA, 14, Font.BOLD);
    private static final Font SUBTITLE_FONT = new Font(Font.HELVETICA, 10, Font.NORMAL, Color.GRAY);
    private static final Font LABEL_FONT = new Font(Font.HELVETICA, 10, Font.BOLD);
    private static final Font VALUE_FONT = new Font(Font.HELVETICA, 10, Font.NORMAL);
    private static final Font HEADER_FONT = new Font(Font.HELVETICA, 9, Font.BOLD, Color.WHITE);
    private static final Font CELL_FONT = new Font(Font.HELVETICA, 9, Font.NORMAL);

    public PdfReportService(ParticipationRepository participationRepository) {
        this.participationRepository = participationRepository;
    }

    public byte[] generateStudentReport(StudentHoursSummaryResponse resumen) throws DocumentException {
        Document document = new Document(PageSize.A4, 40, 40, 60, 40);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);
        document.open();

        addHeader(document, "Reporte de Participación Estudiantil");

        document.add(new Paragraph("Estudiante: " + resumen.getFullName(), LABEL_FONT));
        document.add(new Paragraph("Código: " +
                (resumen.getCodigoEstudiante() != null ? resumen.getCodigoEstudiante() : "Sin asignar"), VALUE_FONT));
        document.add(new Paragraph("Total de horas acumuladas (todos los ciclos): "
                + resumen.getTotalHoursAllCycles(), LABEL_FONT));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        addTableHeader(table, "Proyecto", "Ciclo Académico", "Horas");

        for (StudentHoursSummaryResponse.ProjectCycleHours d : resumen.getDetails()) {
            addCell(table, d.getProjectName());
            addCell(table, d.getCicloAcademico());
            addCell(table, String.valueOf(d.getHours()));
        }

        document.add(table);
        addFooter(document);
        document.close();

        return out.toByteArray();
    }

    public byte[] generateProjectReport(Project project) throws DocumentException {
        Document document = new Document(PageSize.A4, 40, 40, 60, 40);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);
        document.open();

        addHeader(document, "Reporte de Participación por Proyecto");

        List<Object[]> rows = participationRepository.findSummaryByProject(project.getId())
                .stream()
                .filter(r -> ((Number) r[2]).longValue() == project.getId())
                .collect(Collectors.toList());

        double totalHoras = rows.stream()
                .mapToDouble(r -> r[4] != null ? ((Number) r[4]).doubleValue() : 0.0)
                .sum();

        document.add(new Paragraph("Proyecto: " + project.getName(), LABEL_FONT));
        document.add(new Paragraph("Ciclo Académico: " +
                (project.getCicloAcademico() != null ? project.getCicloAcademico() : "—"), VALUE_FONT));
        document.add(new Paragraph("Campus: " +
                (project.getCampus() != null ? project.getCampus().name() : "—"), VALUE_FONT));
        document.add(new Paragraph("Total de horas del proyecto: " + totalHoras, LABEL_FONT));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        addTableHeader(table, "Participante", "Horas Acumuladas", "Sesiones");

        for (Object[] row : rows) {
            addCell(table, (String) row[1]);
            addCell(table, String.valueOf(row[4] != null ? ((Number) row[4]).doubleValue() : 0.0));
            addCell(table, String.valueOf(((Number) row[5]).longValue()));
        }

        document.add(table);
        addFooter(document);
        document.close();

        return out.toByteArray();
    }

    private void addHeader(Document document, String reportTitle) throws DocumentException {
        // Espacio reservado para logo institucional (se agregará cuando esté disponible)
        Paragraph universidad = new Paragraph("UNIVERSIDAD", TITLE_FONT);
        universidad.setAlignment(Element.ALIGN_CENTER);
        document.add(universidad);

        Paragraph area = new Paragraph("Área de Vinculación con el Medio", SUBTITLE_FONT);
        area.setAlignment(Element.ALIGN_CENTER);
        document.add(area);

        Paragraph titulo = new Paragraph(reportTitle, LABEL_FONT);
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingBefore(10);
        titulo.setSpacingAfter(20);
        document.add(titulo);
    }

    private void addFooter(Document document) throws DocumentException {
        Paragraph footer = new Paragraph(
                "Documento generado automáticamente por SocialTrack. Para fines de reporte SUNEDU/AAA.",
                SUBTITLE_FONT);
        footer.setSpacingBefore(20);
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);
    }

    private void addTableHeader(PdfPTable table, String... headers) {
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, HEADER_FONT));
            cell.setBackgroundColor(new Color(79, 70, 229)); // indigo
            cell.setPadding(6);
            table.addCell(cell);
        }
    }

    private void addCell(PdfPTable table, String value) {
        PdfPCell cell = new PdfPCell(new Phrase(value != null ? value : "—", CELL_FONT));
        cell.setPadding(5);
        table.addCell(cell);
    }
}