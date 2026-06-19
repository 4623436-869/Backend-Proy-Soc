package com.proyecto.service;

import com.proyecto.dto.dashboard.DashboardSummaryResponse;
import com.proyecto.entity.Project;
import com.proyecto.repository.BeneficiaryRepository;
import com.proyecto.repository.ParticipationRepository;
import com.proyecto.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final ProjectRepository projectRepository;
    private final BeneficiaryRepository beneficiaryRepository;
    private final ParticipationRepository participationRepository;

    public DashboardService(ProjectRepository projectRepository,
                             BeneficiaryRepository beneficiaryRepository,
                             ParticipationRepository participationRepository) {
        this.projectRepository = projectRepository;
        this.beneficiaryRepository = beneficiaryRepository;
        this.participationRepository = participationRepository;
    }

    @Transactional(readOnly = true)
    public DashboardSummaryResponse getSummary() {
        Long activeProjects = projectRepository.countByStatus(Project.ProjectStatus.ACTIVO);
        Double totalHours = participationRepository.sumAllHoursSystem();
        long totalBeneficiaries = beneficiaryRepository.count();

        Map<String, Long> projectsByCampus = projectRepository.countByCampus().stream()
                .collect(Collectors.toMap(
                        row -> ((Project.Campus) row[0]).name(),
                        row -> (Long) row[1]
                ));

        Map<String, Double> hoursByCampus = participationRepository.sumHoursByCampus().stream()
                .collect(Collectors.toMap(
                        row -> ((Project.Campus) row[0]).name(),
                        row -> (Double) row[1]
                ));

        Map<String, Long> beneficiariesByCampus = beneficiaryRepository.countByCampus().stream()
                .collect(Collectors.toMap(
                        row -> ((Project.Campus) row[0]).name(),
                        row -> (Long) row[1]
                ));

        List<DashboardSummaryResponse.CampusSummary> byCampus =
                java.util.Arrays.stream(Project.Campus.values())
                        .map(campus -> new DashboardSummaryResponse.CampusSummary(
                                campus.name(),
                                projectsByCampus.getOrDefault(campus.name(), 0L),
                                hoursByCampus.getOrDefault(campus.name(), 0.0),
                                beneficiariesByCampus.getOrDefault(campus.name(), 0L)
                        ))
                        .collect(Collectors.toList());

        return DashboardSummaryResponse.builder()
                .activeProjects(activeProjects)
                .totalHours(totalHours != null ? Math.round(totalHours * 100.0) / 100.0 : 0.0)
                .totalBeneficiaries(totalBeneficiaries)
                .byCampus(byCampus)
                .build();
    }
}