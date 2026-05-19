package com.proyecto.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.proyecto.dto.qr.QrGenerateResponse;
import com.proyecto.dto.qr.QrValidateResponse;
import com.proyecto.entity.*;
import com.proyecto.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

@Service
public class QrCodeService {

    private static final Logger log = LoggerFactory.getLogger(QrCodeService.class);

    private final QrCodeRepository qrCodeRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final ParticipationRepository participationRepository;

    @Value("${app.qr.expiration-minutes:30}")
    private int expirationMinutes;

    @Value("${app.qr.size:300}")
    private int qrSize;

    public QrCodeService(QrCodeRepository qrCodeRepository,
                         UserRepository userRepository,
                         ProjectRepository projectRepository,
                         ParticipationRepository participationRepository) {
        this.qrCodeRepository = qrCodeRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.participationRepository = participationRepository;
    }

    @Transactional
    public QrGenerateResponse generateQr(Long userId, Long projectId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!user.getActive()) {
            throw new RuntimeException("El usuario está inactivo");
        }

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

        if (project.getStatus() != Project.ProjectStatus.ACTIVO) {
            throw new RuntimeException("El proyecto no está activo");
        }

        String token = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();

        QrCode qrCode = QrCode.builder()
                .token(token)
                .user(user)
                .project(project)
                .generatedAt(now)
                .expiresAt(now.plusMinutes(expirationMinutes))
                .used(false)
                .active(true)
                .build();

        qrCodeRepository.save(qrCode);

        String qrBase64 = generateQrImage(token);

        return QrGenerateResponse.builder()
                .token(token)
                .qrBase64(qrBase64)
                .userId(userId)
                .projectId(projectId)
                .generatedAt(now)
                .expiresAt(now.plusMinutes(expirationMinutes))
                .build();
    }

    @Transactional
    public QrValidateResponse validateAndRegister(String token) {
        QrCode qrCode = qrCodeRepository
                .findValidToken(token, LocalDateTime.now())
                .orElseThrow(() -> new RuntimeException(
                    "QR inválido, expirado o ya utilizado"));

        LocalDateTime checkIn = LocalDateTime.now();
        LocalDateTime checkOut = checkIn.plusMinutes(1);

        if (participationRepository.existsOverlap(
                qrCode.getUser().getId(), checkIn, checkOut)) {
            throw new RuntimeException(
                "El usuario ya tiene una asistencia activa en este momento");
        }

        Participation participation = Participation.builder()
                .user(qrCode.getUser())
                .project(qrCode.getProject())
                .checkIn(checkIn)
                .checkOut(null)
                .hoursLogged(0.0)
                .registrationMethod(Participation.RegistrationMethod.QR)
                .build();

        participationRepository.save(participation);

        qrCode.setUsed(true);
        qrCode.setActive(false);
        qrCodeRepository.save(qrCode);

        log.info("Asistencia QR registrada: user={} project={} checkIn={}",
                qrCode.getUser().getId(), qrCode.getProject().getId(), checkIn);

        return QrValidateResponse.builder()
                .valid(true)
                .userId(qrCode.getUser().getId())
                .userFullName(qrCode.getUser().getFullName())
                .projectId(qrCode.getProject().getId())
                .projectName(qrCode.getProject().getName())
                .scannedAt(checkIn)
                .message("Asistencia registrada correctamente")
                .build();
    }

    @Scheduled(fixedRate = 3600000)
    @Transactional
    public void deactivateExpiredQrCodes() {
        var expired = qrCodeRepository.findExpiredActive(LocalDateTime.now());
        expired.forEach(q -> q.setActive(false));
        qrCodeRepository.saveAll(expired);
        if (!expired.isEmpty()) {
            log.info("QRs expirados desactivados: {}", expired.size());
        }
    }

    private String generateQrImage(String content) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints.put(EncodeHintType.MARGIN, 1);

            BitMatrix matrix = writer.encode(
                    content, BarcodeFormat.QR_CODE, qrSize, qrSize, hints);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "PNG", out);
            return Base64.getEncoder().encodeToString(out.toByteArray());

        } catch (WriterException | IOException e) {
            throw new RuntimeException("Error al generar imagen QR: " + e.getMessage());
        }
    }
}