package com.proyecto.entity;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 100)
    private String fullName;

    @Column(nullable = false)
    private Boolean active = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    public User() {}

    public User(Long id, String email, String password, String fullName,
                Boolean active, Set<Role> roles) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.active = active;
        this.roles = roles;
    }

    // Getters
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getFullName() { return fullName; }
    public Boolean getActive() { return active; }
    public Set<Role> getRoles() { return roles; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setActive(Boolean active) { this.active = active; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }

    // Builder
    public static UserBuilder builder() { return new UserBuilder(); }

    public static class UserBuilder {
        private Long id;
        private String email;
        private String password;
        private String fullName;
        private Boolean active = true;
        private Set<Role> roles;

        public UserBuilder id(Long id) { this.id = id; return this; }
        public UserBuilder email(String email) { this.email = email; return this; }
        public UserBuilder password(String password) { this.password = password; return this; }
        public UserBuilder fullName(String fullName) { this.fullName = fullName; return this; }
        public UserBuilder active(Boolean active) { this.active = active; return this; }
        public UserBuilder roles(Set<Role> roles) { this.roles = roles; return this; }

        public User build() {
            User u = new User();
            u.id = this.id;
            u.email = this.email;
            u.password = this.password;
            u.fullName = this.fullName;
            u.active = this.active;
            u.roles = this.roles;
            return u;
        }
    }
}