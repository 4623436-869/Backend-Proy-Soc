package com.proyecto.controller;

import com.proyecto.dto.attendance.AttendanceSummaryResponse;
import com.proyecto.service.HoursCalculatorService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hours")
public class HoursController {

    private final HoursCalculatorService hoursCalculatorService;

    public HoursController(HoursCalculatorService hoursCalculatorService) {
        this.hoursCalculatorService = hoursCalculatorService;
    }

    @GetMapping("/user/{userId}/project/{projectId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Double> getHoursByUserAndProject(
            @PathVariable Long userId,
            @PathVariable Long projectId) {
        return ResponseEntity.ok(
                hoursCalculatorService.getTotalHours(userId, projectId));
    }

    @GetMapping("/user/{userId}/total")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Double> getTotalHoursByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(
                hoursCalculatorService.getTotalHoursByUser(userId));
    }

    @GetMapping("/project/{projectId}/summary")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_COORDINADOR')")
    public ResponseEntity<List<AttendanceSummaryResponse>> getProjectSummary(
            @PathVariable Long projectId) {
        return ResponseEntity.ok(
                hoursCalculatorService.getProjectSummary(projectId));
    }

    @GetMapping("/user/{userId}/project/{projectId}/check")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Boolean> checkMinimumHours(
            @PathVariable Long userId,
            @PathVariable Long projectId,
            @RequestParam(defaultValue = "120") double minimumHours) {
        return ResponseEntity.ok(
                hoursCalculatorService.hasReachedMinimumHours(
                        userId, projectId, minimumHours));
    }
}