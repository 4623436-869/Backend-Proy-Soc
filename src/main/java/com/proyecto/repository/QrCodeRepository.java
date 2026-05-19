package com.proyecto.repository;

import com.proyecto.entity.QrCode;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface QrCodeRepository extends JpaRepository<QrCode, Long> {

    Optional<QrCode> findByToken(String token);

    List<QrCode> findByUserId(Long userId);

    List<QrCode> findByProjectId(Long projectId);

    // PESSIMISTIC_WRITE evita que dos transacciones lean el mismo QR al mismo tiempo
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT q FROM QrCode q " +
           "WHERE q.token = :token " +
           "AND q.active = true " +
           "AND q.used = false " +
           "AND q.expiresAt > :now")
    Optional<QrCode> findValidToken(
            @Param("token") String token,
            @Param("now") LocalDateTime now
    );

    @Query("SELECT q FROM QrCode q " +
           "WHERE q.active = true " +
           "AND q.expiresAt < :now")
    List<QrCode> findExpiredActive(@Param("now") LocalDateTime now);
}