package com.proyecto.service;

import com.proyecto.dto.auth.*;
import com.proyecto.entity.*;
import com.proyecto.repository.*;
import com.proyecto.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SessionRepository sessionRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       SessionRepository sessionRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.sessionRepository = sessionRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        Role.RoleName roleName = Role.RoleName.ROLE_ESTUDIANTE;
        if (request.getRole() != null) {
            try {
                roleName = Role.RoleName.valueOf(request.getRole());
            } catch (IllegalArgumentException ignored) {}
        }

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .active(true)
                .roles(Set.of(role))
                .build();

        userRepository.save(user);
        return buildAuthResponse(user, role);
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!user.getActive()) {
            throw new RuntimeException("Usuario inactivo");
        }

        Role role = user.getRoles().iterator().next();
        AuthResponse response = buildAuthResponse(user, role);

        Session session = Session.builder()
                .user(user)
                .token(response.getToken())
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now()
                        .plusSeconds(jwtService.getExpirationMs() / 1000))
                .active(true)
                .build();

        sessionRepository.save(session);
        return response;
    }

    private AuthResponse buildAuthResponse(User user, Role role) {
        String token = jwtService.generateToken(
                user.getEmail(),
                Map.of("role", role.getName().name(),
                       "name", user.getFullName()));

        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(role.getName().name())
                .expiresIn(jwtService.getExpirationMs())
                .build();
    }
}