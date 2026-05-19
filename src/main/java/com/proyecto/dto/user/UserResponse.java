package com.proyecto.dto.user;

import com.proyecto.entity.User;
import java.util.Set;
import java.util.stream.Collectors;

public class UserResponse {

    private Long id;
    private String fullName;
    private String email;
    private Boolean active;
    private Set<String> roles;

    public UserResponse() {}

    // Getters
    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public Boolean getActive() { return active; }
    public Set<String> getRoles() { return roles; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setEmail(String email) { this.email = email; }
    public void setActive(Boolean active) { this.active = active; }
    public void setRoles(Set<String> roles) { this.roles = roles; }

    public static UserResponse fromEntity(User user) {
        UserResponse r = new UserResponse();
        r.id = user.getId();
        r.fullName = user.getFullName();
        r.email = user.getEmail();
        r.active = user.getActive();
        r.roles = user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());
        return r;
    }
}