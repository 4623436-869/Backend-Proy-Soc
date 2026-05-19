package com.proyecto.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 50)
    private RoleName name;

    public enum RoleName {
        ROLE_ADMIN,
        ROLE_COORDINADOR,
        ROLE_ESTUDIANTE
    }

    public Role() {}

    public Long getId() { return id; }
    public RoleName getName() { return name; }
    public void setId(Long id) { this.id = id; }
    public void setName(RoleName name) { this.name = name; }
}