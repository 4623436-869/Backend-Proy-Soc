package com.proyecto.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sessions")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 512)
    private String token;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private Boolean active = true;

    public Session() {}

    // Getters
    public Long getId() { return id; }
    public User getUser() { return user; }
    public String getToken() { return token; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public Boolean getActive() { return active; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setUser(User user) { this.user = user; }
    public void setToken(String token) { this.token = token; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
    public void setActive(Boolean active) { this.active = active; }

    public static SessionBuilder builder() { return new SessionBuilder(); }

    public static class SessionBuilder {
        private User user;
        private String token;
        private LocalDateTime createdAt;
        private LocalDateTime expiresAt;
        private Boolean active = true;

        public SessionBuilder user(User user) { this.user = user; return this; }
        public SessionBuilder token(String token) { this.token = token; return this; }
        public SessionBuilder createdAt(LocalDateTime t) { this.createdAt = t; return this; }
        public SessionBuilder expiresAt(LocalDateTime t) { this.expiresAt = t; return this; }
        public SessionBuilder active(Boolean active) { this.active = active; return this; }

        public Session build() {
            Session s = new Session();
            s.user = this.user; s.token = this.token;
            s.createdAt = this.createdAt; s.expiresAt = this.expiresAt;
            s.active = this.active;
            return s;
        }
    }
}