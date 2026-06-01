package com.proyecto;

import com.proyecto.entity.*;
import com.proyecto.repository.*;
import com.proyecto.service.QrCodeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class QrConcurrencyTest {

    @Autowired private QrCodeService qrCodeService;
    @Autowired private QrCodeRepository qrCodeRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ProjectRepository projectRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private ParticipationRepository participationRepository;

    private User testUser;
    private Project testProject;

    @BeforeEach
    void setUp() {
        participationRepository.deleteAll();
        qrCodeRepository.deleteAll();

        Role role = roleRepository.findByName(Role.RoleName.ROLE_ESTUDIANTE)
                .orElseThrow();

        testUser = userRepository.findByEmail("concurrency@test.com")
                .orElseGet(() -> {
                    User u = User.builder()
                            .fullName("Test Concurrencia")
                            .email("concurrency@test.com")
                            .password("pass").active(true)
                            .roles(Set.of(role)).build();
                    return userRepository.save(u);
                });

        testProject = projectRepository.findAll().stream()
                .filter(p -> p.getName().equals("Proyecto Concurrencia"))
                .findFirst()
                .orElseGet(() -> {
                    Project p = Project.builder()
                            .name("Proyecto Concurrencia")
                            .status(Project.ProjectStatus.ACTIVO)
                            .startDate(LocalDateTime.now()).build();
                    return projectRepository.save(p);
                });
    }

    @Test
    @DisplayName("Escaneo simultáneo del mismo QR — solo 1 debe registrar asistencia")
    void concurrentQrScan_onlyOneShouldSucceed() throws InterruptedException {
        var qrResponse = qrCodeService.generateQr(
                testUser.getId(), testProject.getId());
        String sharedToken = qrResponse.getToken();

        int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(1);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        List<Future<Boolean>> futures = new ArrayList<>();
        for (int i = 0; i < threadCount; i++) {
            futures.add(executor.submit(() -> {
                latch.await();
                try {
                    qrCodeService.validateAndRegister(sharedToken);
                    successCount.incrementAndGet();
                    return true;
                } catch (Exception e) {
                    failureCount.incrementAndGet();
                    return false;
                }
            }));
        }

        latch.countDown();
        executor.shutdown();
        executor.awaitTermination(15, TimeUnit.SECONDS);

        assertEquals(1, successCount.get(),
                "Solo un escaneo debe registrar asistencia");
        assertEquals(threadCount - 1, failureCount.get(),
                "Los demás deben fallar");
    }

    @Test
    @DisplayName("Tokens generados simultáneamente son únicos")
    void concurrentQrGeneration_allTokensShouldBeUnique()
            throws InterruptedException {
        int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(1);
        List<String> tokens = new CopyOnWriteArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    latch.await();
                    var response = qrCodeService.generateQr(
                            testUser.getId(), testProject.getId());
                    tokens.add(response.getToken());
                } catch (Exception ignored) {}
            });
        }

        latch.countDown();
        executor.shutdown();
        executor.awaitTermination(15, TimeUnit.SECONDS);

        long unique = tokens.stream().distinct().count();
        assertEquals(tokens.size(), unique,
                "Todos los tokens deben ser únicos");
    }
}