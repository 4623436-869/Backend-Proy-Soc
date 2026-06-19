package com.proyecto.controller;

import com.proyecto.dto.user.UserResponse;
import com.proyecto.entity.Role;
import com.proyecto.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_COORDINADOR')")
    public ResponseEntity<List<UserResponse>> getAllActive() {
        return ResponseEntity.ok(
            userService.getAllActive().stream()
                    .map(UserResponse::fromEntity)
                    .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_COORDINADOR')")
    public ResponseEntity<UserResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(UserResponse.fromEntity(userService.getById(id)));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_COORDINADOR')")
    public ResponseEntity<List<UserResponse>> searchStudents(
            @RequestParam String query) {
        return ResponseEntity.ok(
            userService.searchStudents(query).stream()
                    .map(UserResponse::fromEntity)
                    .collect(Collectors.toList()));
    }

    @PatchMapping("/{id}/toggle-active")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UserResponse> toggleActive(@PathVariable Long id) {
        return ResponseEntity.ok(
                UserResponse.fromEntity(userService.toggleActive(id)));
    }

    @PatchMapping("/{id}/role")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UserResponse> changeRole(
            @PathVariable Long id,
            @RequestParam Role.RoleName newRole) {
        return ResponseEntity.ok(
                UserResponse.fromEntity(userService.changeRole(id, newRole)));
    }

    @PatchMapping("/{id}/codigo")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UserResponse> setCodigoEstudiante(
            @PathVariable Long id,
            @RequestParam String codigo) {
        return ResponseEntity.ok(
                UserResponse.fromEntity(userService.setCodigoEstudiante(id, codigo)));
    }
}