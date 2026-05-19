package com.proyecto.controller;

import com.proyecto.dto.attendance.*;
import com.proyecto.entity.Participation;
import com.proyecto.service.AttendanceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping("/manual")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_COORDINADOR')")
    public ResponseEntity<AttendanceResponse> registerManual(
            @Valid @RequestBody AttendanceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(attendanceService.registerManual(request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AttendanceResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(attendanceService.getById(id));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_COORDINADOR')")
    public ResponseEntity<List<AttendanceResponse>> getByUser(
            @PathVariable Long userId) {
        return ResponseEntity.ok(attendanceService.getByUser(userId));
    }

    @GetMapping("/project/{projectId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_COORDINADOR')")
    public ResponseEntity<List<AttendanceResponse>> getByProject(
            @PathVariable Long projectId) {
        return ResponseEntity.ok(attendanceService.getByProject(projectId));
    }

    @GetMapping("/filter")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_COORDINADOR')")
    public ResponseEntity<List<AttendanceResponse>> getByFilters(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) LocalDateTime from,
            @RequestParam(required = false) LocalDateTime to,
            @RequestParam(required = false) Participation.RegistrationMethod method) {
        AttendanceFilterRequest filter =
                new AttendanceFilterRequest(userId, projectId, from, to, method);
        return ResponseEntity.ok(attendanceService.getByFilters(filter));
    }

    @GetMapping("/hours")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Double> getTotalHours(
            @RequestParam Long userId,
            @RequestParam Long projectId) {
        return ResponseEntity.ok(attendanceService.getTotalHours(userId, projectId));
    }

    @GetMapping("/summary/project/{projectId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_COORDINADOR')")
    public ResponseEntity<List<AttendanceSummaryResponse>> getSummaryByProject(
            @PathVariable Long projectId) {
        return ResponseEntity.ok(attendanceService.getSummaryByProject(projectId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_COORDINADOR')")
    public ResponseEntity<AttendanceResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody AttendanceRequest request) {
        return ResponseEntity.ok(attendanceService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        attendanceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}