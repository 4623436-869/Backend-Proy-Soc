package com.proyecto.config;

import com.proyecto.entity.Role;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RoleNameConverter implements Converter<String, Role.RoleName> {

    @Override
    public Role.RoleName convert(String source) {
        try {
            return Role.RoleName.valueOf(source.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(
                "Rol inválido: '" + source + "'. " +
                "Valores permitidos: ROLE_ADMIN, ROLE_COORDINADOR, ROLE_ESTUDIANTE");
        }
    }
}