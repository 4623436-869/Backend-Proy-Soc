package com.proyecto.repository;

import com.proyecto.entity.Beneficiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {

    List<Beneficiary> findByProjectId(Long projectId);

    Boolean existsByDocumentNumber(String documentNumber);

    @Query("SELECT b FROM Beneficiary b WHERE " +
           "b.project.id = :projectId AND " +
           "(:name IS NULL OR LOWER(b.fullName) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:status IS NULL OR b.status = :status)")
    List<Beneficiary> findByProjectIdAndFilters(
            @Param("projectId") Long projectId,
            @Param("name") String name,
            @Param("status") Beneficiary.BeneficiaryStatus status
    );

    @Query("SELECT b.project.campus, COUNT(b) FROM Beneficiary b " +
           "WHERE b.project.campus IS NOT NULL GROUP BY b.project.campus")
    List<Object[]> countByCampus();
}