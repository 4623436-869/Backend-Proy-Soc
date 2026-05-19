package com.proyecto.controller;

import com.proyecto.dto.beneficiary.*;
import com.proyecto.entity.Beneficiary;
import com.proyecto.service.BeneficiaryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/beneficiaries")
public class BeneficiaryController {

    private final BeneficiaryService beneficiaryService;

    public BeneficiaryController(BeneficiaryService beneficiaryService) {
        this.beneficiaryService = beneficiaryService;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_COORDINADOR')")
    public ResponseEntity<BeneficiaryResponse> create(
            @Valid @RequestBody BeneficiaryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(beneficiaryService.create(request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BeneficiaryResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(beneficiaryService.getById(id));
    }

    @GetMapping("/project/{projectId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<BeneficiaryResponse>> getByProject(
            @PathVariable Long projectId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Beneficiary.BeneficiaryStatus status) {
        return ResponseEntity.ok(
                beneficiaryService.getByProjectAndFilters(projectId, name, status));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_COORDINADOR')")
    public ResponseEntity<BeneficiaryResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody BeneficiaryRequest request) {
        return ResponseEntity.ok(beneficiaryService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        beneficiaryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}