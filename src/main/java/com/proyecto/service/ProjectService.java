package com.proyecto.service;

import com.proyecto.dto.project.*;
import com.proyecto.entity.*;
import com.proyecto.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository,
                          UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ProjectResponse create(ProjectRequest request) {
        validateDates(request);

        Project project = Project.builder()
                .name(request.getName())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status(request.getStatus())
                .build();

        if (request.getCoordinatorId() != null) {
            User coordinator = userRepository.findById(request.getCoordinatorId())
                    .orElseThrow(() -> new RuntimeException("Coordinador no encontrado"));
            project.setCoordinator(coordinator);
        }

        return ProjectResponse.fromEntity(projectRepository.save(project));
    }

    @Transactional(readOnly = true)
    public ProjectResponse getById(Long id) {
        return ProjectResponse.fromEntity(findOrThrow(id));
    }

    @Transactional(readOnly = true)
    public List<ProjectResponse> getAll() {
        return projectRepository.findAll()
                .stream()
                .map(ProjectResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProjectResponse> getByFilters(String name, Project.ProjectStatus status) {
        return projectRepository.findByFilters(name, status)
                .stream()
                .map(ProjectResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProjectResponse update(Long id, ProjectRequest request) {
        validateDates(request);
        Project project = findOrThrow(id);
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setStartDate(request.getStartDate());
        project.setEndDate(request.getEndDate());
        project.setStatus(request.getStatus());

        if (request.getCoordinatorId() != null) {
            User coordinator = userRepository.findById(request.getCoordinatorId())
                    .orElseThrow(() -> new RuntimeException("Coordinador no encontrado"));
            project.setCoordinator(coordinator);
        }

        return ProjectResponse.fromEntity(projectRepository.save(project));
    }

    @Transactional
    public void delete(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new RuntimeException("Proyecto no encontrado con id: " + id);
        }
        projectRepository.deleteById(id);
    }

    private Project findOrThrow(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                    "Proyecto no encontrado con id: " + id));
    }

    private void validateDates(ProjectRequest request) {
        if (request.getEndDate() != null &&
            request.getEndDate().isBefore(request.getStartDate())) {
            throw new RuntimeException(
                "La fecha de fin no puede ser anterior a la fecha de inicio");
        }
    }
}