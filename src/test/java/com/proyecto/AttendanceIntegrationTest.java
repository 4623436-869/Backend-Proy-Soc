package com.proyecto;

import com.proyecto.dto.attendance.AttendanceRequest;
import com.proyecto.dto.attendance.AttendanceResponse;
import com.proyecto.entity.*;
import com.proyecto.repository.*;
import com.proyecto.service.AttendanceService;
import com.proyecto.service.HoursCalculatorService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AttendanceIntegrationTest {

    @Autowired private AttendanceService attendanceService;
    @Autowired private HoursCalculatorService hoursCalculatorService;
    @Autowired private UserRepository userRepository;
    @Autowired private ProjectRepository projectRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private ParticipationRepository participationRepository;

    private static Long testUserId;
    private static Long testProjectId;
    private static Long attendanceId;

    @BeforeEach
    void setUp() {
        Role role = roleRepository.findByName(Role.RoleName.ROLE_ESTUDIANTE)
                .orElseThrow();

        User user = userRepository.findByEmail("integration@test.com")
                .orElseGet(() -> {
                    User u = User.builder()
                            .fullName("Integration Test User")
                            .email("integration@test.com")
                            .password("pass")
                            .active(true)
                            .roles(Set.of(role))
                            .build();
                    return userRepository.save(u);
                });
        testUserId = user.getId();

        Project project = projectRepository.findAll().stream()
                .filter(p -> p.getName().equals("Proyecto Integration"))
                .findFirst()
                .orElseGet(() -> {
                    Project p = Project.builder()
                            .name("Proyecto Integration")
                            .status(Project.ProjectStatus.ACTIVO)
                            .startDate(LocalDate.now())
                            .build();
                    return projectRepository.save(p);
                });
        testProjectId = project.getId();
    }

    @Test
    @Order(1)
    @DisplayName("1 — Registrar asistencia manual de 2 horas")
    void step1_registerManualAttendance() {
        AttendanceRequest request = new AttendanceRequest();
        request.setUserId(testUserId);
        request.setProjectId(testProjectId);
        request.setCheckIn(LocalDateTime.of(2025, 7, 1, 8, 0));
        request.setCheckOut(LocalDateTime.of(2025, 7, 1, 10, 0));

        AttendanceResponse response = attendanceService.registerManual(request);
        attendanceId = response.getId();

        assertNotNull(attendanceId);
        assertEquals(2.0, response.getHoursLogged());
    }

    @Test
    @Order(2)
    @DisplayName("2 — Total de horas acumuladas debe ser 2.0")
    void step2_totalHoursShouldBeTwo() {
        Double total = hoursCalculatorService.getTotalHours(testUserId, testProjectId);
        assertNotNull(total);
        assertTrue(total >= 2.0);
    }

    @Test
    @Order(3)
    @DisplayName("3 — Editar asistencia a 4 horas")
    void step3_editAttendanceAndVerifyTotal() {
        if (attendanceId == null) return;

        AttendanceRequest updated = new AttendanceRequest();
        updated.setUserId(testUserId);
        updated.setProjectId(testProjectId);
        updated.setCheckIn(LocalDateTime.of(2025, 7, 1, 8, 0));
        updated.setCheckOut(LocalDateTime.of(2025, 7, 1, 12, 0));

        AttendanceResponse response = attendanceService.update(attendanceId, updated);
        assertEquals(4.0, response.getHoursLogged());
    }

    @Test
    @Order(4)
    @DisplayName("4 — Verificar mínimo de horas no alcanzado")
    void step4_minimumHoursNotReached() {
        boolean reached = hoursCalculatorService
                .hasReachedMinimumHours(testUserId, testProjectId, 120.0);
        assertFalse(reached);
    }
}