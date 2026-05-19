package com.proyecto.config;

import com.proyecto.entity.Role;
import com.proyecto.entity.User;
import com.proyecto.repository.RoleRepository;
import com.proyecto.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(RoleRepository roleRepository,
                      UserRepository userRepository,
                      PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        seedRoles();
        seedUsers();
    }

    private void seedRoles() {
        for (Role.RoleName roleName : Role.RoleName.values()) {
            if (roleRepository.findByName(roleName).isEmpty()) {
                Role role = new Role();
                role.setName(roleName);
                roleRepository.save(role);
                log.info("Rol creado: {}", roleName);
            }
        }
    }

    private void seedUsers() {
        createUserIfNotExists("Admin Principal",
                "admin@proyecto.com", "Admin1234!", Role.RoleName.ROLE_ADMIN);
        createUserIfNotExists("Coordinador Demo",
                "coordinador@proyecto.com", "Coord1234!", Role.RoleName.ROLE_COORDINADOR);
        createUserIfNotExists("Estudiante Demo",
                "estudiante@proyecto.com", "Estud1234!", Role.RoleName.ROLE_ESTUDIANTE);
    }

    private void createUserIfNotExists(String fullName, String email,
                                       String rawPassword, Role.RoleName roleName) {
        if (userRepository.existsByEmail(email)) {
            log.info("Usuario ya existe, se omite: {}", email);
            return;
        }
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + roleName));

        User user = User.builder()
                .fullName(fullName)
                .email(email)
                .password(passwordEncoder.encode(rawPassword))
                .active(true)
                .roles(Set.of(role))
                .build();

        userRepository.save(user);
        log.info("Usuario creado: {} [{}]", email, roleName);
    }
}