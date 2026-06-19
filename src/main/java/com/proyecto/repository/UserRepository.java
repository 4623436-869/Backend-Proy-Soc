package com.proyecto.repository;

import com.proyecto.entity.Role;
import com.proyecto.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);

    Optional<User> findByCodigoEstudiante(String codigoEstudiante);
    Boolean existsByCodigoEstudiante(String codigoEstudiante);

    @Query("SELECT u FROM User u JOIN u.roles r " +
           "WHERE r.name = :roleName " +
           "AND (LOWER(u.codigoEstudiante) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<User> searchStudents(@Param("query") String query, @Param("roleName") Role.RoleName roleName);
}