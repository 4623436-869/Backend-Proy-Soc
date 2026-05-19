package com.proyecto.service;

import com.proyecto.dto.attendance.AttendanceSummaryResponse;
import com.proyecto.repository.ParticipationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HoursCalculatorService {

    private final ParticipationRepository participationRepository;

    public HoursCalculatorService(ParticipationRepository participationRepository) {
        this.participationRepository = participationRepository;
    }

    @Transactional(readOnly = true)
    public Double getTotalHours(Long userId, Long projectId) {
        return participationRepository.sumHoursByUserAndProject(userId, projectId);
    }

    @Transactional(readOnly = true)
    public Double getTotalHoursByUser(Long userId) {
        return participationRepository.sumAllHoursByUser(userId);
    }

    @Transactional(readOnly = true)
    public List<AttendanceSummaryResponse> getProjectSummary(Long projectId) {
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

    @Transactional(readOnly = true)
    public boolean hasReachedMinimumHours(Long userId, Long projectId,
                                           double minimumHours) {
        Double total = getTotalHours(userId, projectId);
        return total != null && total >= minimumHours;
    }
}