package com.proyecto.service;

import com.proyecto.entity.Role;
import com.proyecto.entity.User;
import com.proyecto.repository.RoleRepository;
import com.proyecto.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

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
        User user = getById(userId);
        Role role = roleRepository.findByName(newRole)
                .orElseThrow(() -> new RuntimeException(
                    "Rol no encontrado: " + newRole));
        user.setRoles(Set.of(role));
        return userRepository.save(user);
    }
}