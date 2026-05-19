package com.proyecto.controller;

import com.proyecto.dto.attendance.AttendanceResponse;
import com.proyecto.dto.qr.*;
import com.proyecto.service.AttendanceService;
import com.proyecto.service.QrCodeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/qr")
public class QrCodeController {

    private final QrCodeService qrCodeService;
    private final AttendanceService attendanceService;

    public QrCodeController(QrCodeService qrCodeService,
                             AttendanceService attendanceService) {
        this.qrCodeService = qrCodeService;
        this.attendanceService = attendanceService;
    }

    @PostMapping("/generate")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_COORDINADOR')")
    public ResponseEntity<QrGenerateResponse> generate(
            @RequestParam Long userId,
            @RequestParam Long projectId) {
        return ResponseEntity.ok(qrCodeService.generateQr(userId, projectId));
    }

    @PostMapping("/scan")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<QrValidateResponse> scan(
            @Valid @RequestBody QrScanRequest request) {
        return ResponseEntity.ok(
                qrCodeService.validateAndRegister(request.getToken()));
    }

    @PostMapping("/checkout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AttendanceResponse> checkOut(
            @Valid @RequestBody QrCheckOutRequest request) {
        return ResponseEntity.ok(
                attendanceService.closeOpenAttendance(
                        request.getUserId(), request.getProjectId()));
    }
}