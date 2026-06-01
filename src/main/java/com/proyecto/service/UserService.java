package com.proyecto.service;

import com.proyecto.entity.Role;
import com.proyecto.entity.User;
import com.proyecto.repository.RoleRepository;
import com.proyecto.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional(readOnly = true)
    public List<User> getAllActive() {
        return userRepository.findAll()
                .stream()
                .filter(User::getActive)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                    "Usuario no encontrado con id: " + id));
    }

    @Transactional
    public User toggleActive(Long id) {
        User user = getById(id);
        user.setActive(!user.getActive());
        return userRepository.save(user);
    }

    public void validateActive(Long userId) {
        User user = getById(userId);
        if (!user.getActive()) {
            throw new RuntimeException(
                "El usuario con id " + userId + " está inactivo");
        }
    }

    @Transactional
    public User changeRole(Long userId, Role.RoleName newRole) {
        log.info("Iniciando cambio de rol: userId={} newRole={}", userId, newRole);

        User user = getById(userId);
        log.info("Usuario encontrado: email={} rolesActuales={}", 
                user.getEmail(), user.getRoles());

        Role role = roleRepository.findByName(newRole)
                .orElseThrow(() -> new RuntimeException(
                    "Rol no encontrado: " + newRole));
        log.info("Rol encontrado en BD: id={} name={}", role.getId(), role.getName());

        try {
            user.getRoles().clear();
            user.getRoles().add(role);
            User saved = userRepository.save(user);
            log.info("Rol actualizado correctamente: userId={} nuevoRol={}", 
                    userId, newRole);
            return saved;
        } catch (Exception e) {
            log.error("Error al cambiar rol: {}", e.getMessage(), e);
            throw new RuntimeException("Error al actualizar el rol: " + e.getMessage());
        }
    }
}