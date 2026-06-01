package com.proyecto;

import com.proyecto.entity.*;
import com.proyecto.repository.*;
import com.proyecto.service.QrCodeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QrCodeServiceTest {

    @Mock private QrCodeRepository qrCodeRepository;
    @Mock private UserRepository userRepository;
    @Mock private ProjectRepository projectRepository;
    @Mock private ParticipationRepository participationRepository;

    @InjectMocks
    private QrCodeService qrCodeService;

    private User activeUser;
    private Project activeProject;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(qrCodeService, "expirationMinutes", 30);
        ReflectionTestUtils.setField(qrCodeService, "qrSize", 300);

        activeUser = User.builder()
                .id(1L).fullName("Usuario Test")
                .email("test@proyecto.com").password("pass")
                .active(true).roles(Set.of()).build();

        activeProject = Project.builder()
                .id(1L).name("Proyecto Test")
                .status(Project.ProjectStatus.ACTIVO)
                .startDate(LocalDateTime.now()).build();
    }

    @Test
    @DisplayName("Generar QR con usuario y proyecto activos → éxito")
    void generateQr_activeUserAndProject_shouldSucceed() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(activeUser));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(activeProject));
        when(qrCodeRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var response = qrCodeService.generateQr(1L, 1L);

        assertNotNull(response.getToken());
        assertNotNull(response.getQrBase64());
        assertNotNull(response.getExpiresAt());
        assertTrue(response.getExpiresAt().isAfter(LocalDateTime.now()));
    }

    @Test
    @DisplayName("Generar QR con usuario inactivo → lanza excepción")
    void generateQr_inactiveUser_shouldThrow() {
        activeUser.setActive(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(activeUser));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> qrCodeService.generateQr(1L, 1L));
        assertTrue(ex.getMessage().contains("inactivo"));
    }

    @Test
    @DisplayName("Generar QR con proyecto inactivo → lanza excepción")
    void generateQr_inactiveProject_shouldThrow() {
        activeProject.setStatus(Project.ProjectStatus.INACTIVO);
        when(userRepository.findById(1L)).thenReturn(Optional.of(activeUser));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(activeProject));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> qrCodeService.generateQr(1L, 1L));
        assertTrue(ex.getMessage().contains("activo"));
    }

    @Test
    @DisplayName("Validar token inexistente → lanza excepción")
    void validateToken_invalid_shouldThrow() {
        when(qrCodeRepository.findValidToken(any(), any()))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> qrCodeService.validateAndRegister("token-invalido"));
        assertTrue(ex.getMessage().contains("inválido") ||
                   ex.getMessage().contains("expirado"));
    }

    @Test
    @DisplayName("Validar token válido → marca como usado y registra asistencia")
    void validateToken_valid_shouldMarkUsedAndRegister() {
        QrCode qrCode = QrCode.builder()
                .id(1L).token("token-valido")
                .user(activeUser).project(activeProject)
                .generatedAt(LocalDateTime.now().minusMinutes(5))
                .expiresAt(LocalDateTime.now().plusMinutes(25))
                .used(false).active(true).build();

        when(qrCodeRepository.findValidToken(any(), any()))
                .thenReturn(Optional.of(qrCode));
        when(participationRepository.existsByUserAndProjectAndDate(
                any(), any(), any(), any()))
                .thenReturn(false);
        when(participationRepository.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));
        when(qrCodeRepository.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));

        var response = qrCodeService.validateAndRegister("token-valido");

        assertTrue(response.getValid());
        assertTrue(qrCode.getUsed());
        assertFalse(qrCode.getActive());
        assertEquals("Usuario Test", response.getUserFullName());
        verify(participationRepository, times(1)).save(any());
    }
}