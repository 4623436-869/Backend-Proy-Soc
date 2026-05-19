package com.proyecto.repository;


import com.proyecto.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {
    Optional<Session> findByTokenAndActiveTrue(String token);
    void deleteByUserId(Long userId);
}
