package com.proyecto.repository;

import com.proyecto.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByStatus(Project.ProjectStatus status);

    List<Project> findByCoordinatorId(Long coordinatorId);

    @Query("SELECT p FROM Project p WHERE " +
           "(:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:status IS NULL OR p.status = :status)")
    List<Project> findByFilters(
            @Param("name") String name,
            @Param("status") Project.ProjectStatus status
    );

    Long countByStatus(Project.ProjectStatus status);

    @Query("SELECT p.campus, COUNT(p) FROM Project p " +
           "WHERE p.campus IS NOT NULL GROUP BY p.campus")
    List<Object[]> countByCampus();
}