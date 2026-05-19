package com.proyecto.service;

import com.proyecto.dto.beneficiary.*;
import com.proyecto.entity.*;
import com.proyecto.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BeneficiaryService {

    private final BeneficiaryRepository beneficiaryRepository;
    private final ProjectRepository projectRepository;

    public BeneficiaryService(BeneficiaryRepository beneficiaryRepository,
                               ProjectRepository projectRepository) {
        this.beneficiaryRepository = beneficiaryRepository;
        this.projectRepository = projectRepository;
    }

    @Transactional
    public BeneficiaryResponse create(BeneficiaryRequest request) {
        if (request.getDocumentNumber() != null &&
            beneficiaryRepository.existsByDocumentNumber(request.getDocumentNumber())) {
            throw new RuntimeException(
                "Ya existe un beneficiario con ese número de documento");
        }

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

        Beneficiary beneficiary = Beneficiary.builder()
                .fullName(request.getFullName())
                .documentNumber(request.getDocumentNumber())
                .phone(request.getPhone())
                .address(request.getAddress())
                .birthDate(request.getBirthDate())
                .status(request.getStatus())
                .project(project)
                .build();

        return BeneficiaryResponse.fromEntity(beneficiaryRepository.save(beneficiary));
    }

    @Transactional(readOnly = true)
    public BeneficiaryResponse getById(Long id) {
        return BeneficiaryResponse.fromEntity(findOrThrow(id));
    }

    @Transactional(readOnly = true)
    public List<BeneficiaryResponse> getByProjectAndFilters(
            Long projectId, String name, Beneficiary.BeneficiaryStatus status) {
        return beneficiaryRepository.findByProjectIdAndFilters(projectId, name, status)
                .stream()
                .map(BeneficiaryResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public BeneficiaryResponse update(Long id, BeneficiaryRequest request) {
        Beneficiary beneficiary = findOrThrow(id);

        if (request.getDocumentNumber() != null &&
            !request.getDocumentNumber().equals(beneficiary.getDocumentNumber()) &&
            beneficiaryRepository.existsByDocumentNumber(request.getDocumentNumber())) {
            throw new RuntimeException(
                "Ya existe un beneficiario con ese número de documento");
        }

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

        beneficiary.setFullName(request.getFullName());
        beneficiary.setDocumentNumber(request.getDocumentNumber());
        beneficiary.setPhone(request.getPhone());
        beneficiary.setAddress(request.getAddress());
        beneficiary.setBirthDate(request.getBirthDate());
        beneficiary.setStatus(request.getStatus());
        beneficiary.setProject(project);

        return BeneficiaryResponse.fromEntity(beneficiaryRepository.save(beneficiary));
    }

    @Transactional
    public void delete(Long id) {
        if (!beneficiaryRepository.existsById(id)) {
            throw new RuntimeException("Beneficiario no encontrado con id: " + id);
        }
        beneficiaryRepository.deleteById(id);
    }

    private Beneficiary findOrThrow(Long id) {
        return beneficiaryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                    "Beneficiario no encontrado con id: " + id));
    }
}