package com.proyecto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class PermissionMatrixTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("Sin token → 401 en endpoint protegido")
    void noToken_shouldReturn401() {
        ResponseEntity<String> response = restTemplate
                .getForEntity("/api/projects", String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    @DisplayName("Sin token → 401 al registrar asistencia")
    void noToken_attendance_shouldReturn401() {
        ResponseEntity<String> response = restTemplate
                .getForEntity("/api/attendance/manual", String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}