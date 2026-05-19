package com.proyecto;

import com.proyecto.dto.auth.LoginRequest;
import com.proyecto.entity.Role;
import com.proyecto.repository.*;
import com.proyecto.service.AuthService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FinalQaSuiteTest {

    @Autowired private TestRestTemplate restTemplate;
    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private AuthService authService;

    private static String estudianteToken;
    private static String coordinadorToken;

    @Test
    @Order(1)
    @DisplayName("QA-01 Login Admin → token válido")
    void qa01_loginAdmin() {
        var response = authService.login(
                new LoginRequest("admin@proyecto.com", "Admin1234!"));
        assertNotNull(response.getToken());
        assertEquals("ROLE_ADMIN", response.getRole());
    }

    @Test
    @Order(2)
    @DisplayName("QA-02 Login Coordinador → token válido")
    void qa02_loginCoordinador() {
        var response = authService.login(
                new LoginRequest("coordinador@proyecto.com", "Coord1234!"));
        assertNotNull(response.getToken());
        coordinadorToken = response.getToken();
    }

    @Test
    @Order(3)
    @DisplayName("QA-03 Login Estudiante → token válido")
    void qa03_loginEstudiante() {
        var response = authService.login(
                new LoginRequest("estudiante@proyecto.com", "Estud1234!"));
        assertNotNull(response.getToken());
        estudianteToken = response.getToken();
    }

    @Test
    @Order(4)
    @DisplayName("QA-04 Login con credenciales incorrectas → excepción")
    void qa04_loginWrongCredentials() {
        assertThrows(Exception.class, () ->
                authService.login(new LoginRequest("admin@proyecto.com", "wrong")));
    }

    @Test
    @Order(5)
    @DisplayName("QA-05 Sin token → 401")
    void qa05_noToken_returns401() {
        ResponseEntity<String> response = restTemplate
                .getForEntity("/api/projects", String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    @Order(6)
    @DisplayName("QA-06 Estudiante → 403 al crear proyecto")
    void qa06_estudiante_cannotCreateProject() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (estudianteToken != null) headers.setBearerAuth(estudianteToken);

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/projects", HttpMethod.POST,
                new HttpEntity<>("{}", headers), String.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    @Order(7)
    @DisplayName("QA-07 Roles seeded existen en DB")
    void qa07_seededRoles_exist() {
        assertTrue(roleRepository.findByName(Role.RoleName.ROLE_ADMIN).isPresent());
        assertTrue(roleRepository.findByName(Role.RoleName.ROLE_COORDINADOR).isPresent());
        assertTrue(roleRepository.findByName(Role.RoleName.ROLE_ESTUDIANTE).isPresent());
    }

    @Test
    @Order(8)
    @DisplayName("QA-08 Usuarios seeded están activos")
    void qa08_seededUsers_areActive() {
        var emails = java.util.List.of(
            "admin@proyecto.com",
            "coordinador@proyecto.com",
            "estudiante@proyecto.com"
        );
        emails.forEach(email -> {
            var user = userRepository.findByEmail(email);
            assertTrue(user.isPresent(), "No encontrado: " + email);
            assertTrue(user.get().getActive(), "Inactivo: " + email);
        });
    }
}