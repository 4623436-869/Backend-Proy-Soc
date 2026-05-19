package com.proyecto.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "qr_codes")
public class QrCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime generatedAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private Boolean used = false;
    private Boolean active = true;

    public QrCode() {}

    // Getters
    public Long getId() { return id; }
    public String getToken() { return token; }
    public Project getProject() { return project; }
    public User getUser() { return user; }
    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public Boolean getUsed() { return used; }
    public Boolean getActive() { return active; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setToken(String token) { this.token = token; }
    public void setProject(Project project) { this.project = project; }
    public void setUser(User user) { this.user = user; }
    public void setGeneratedAt(LocalDateTime t) { this.generatedAt = t; }
    public void setExpiresAt(LocalDateTime t) { this.expiresAt = t; }
    public void setUsed(Boolean used) { this.used = used; }
    public void setActive(Boolean active) { this.active = active; }

    public static QrCodeBuilder builder() { return new QrCodeBuilder(); }

    public static class QrCodeBuilder {
        private Long id;
        private String token;
        private Project project;
        private User user;
        private LocalDateTime generatedAt;
        private LocalDateTime expiresAt;
        private Boolean used = false;
        private Boolean active = true;

        public QrCodeBuilder id(Long id) { this.id = id; return this; }
        public QrCodeBuilder token(String t) { this.token = t; return this; }
        public QrCodeBuilder project(Project p) { this.project = p; return this; }
        public QrCodeBuilder user(User u) { this.user = u; return this; }
        public QrCodeBuilder generatedAt(LocalDateTime t) { this.generatedAt = t; return this; }
        public QrCodeBuilder expiresAt(LocalDateTime t) { this.expiresAt = t; return this; }
        public QrCodeBuilder used(Boolean used) { this.used = used; return this; }
        public QrCodeBuilder active(Boolean active) { this.active = active; return this; }

        public QrCode build() {
            QrCode q = new QrCode();
            q.id = this.id; q.token = this.token;
            q.project = this.project; q.user = this.user;
            q.generatedAt = this.generatedAt; q.expiresAt = this.expiresAt;
            q.used = this.used; q.active = this.active;
            return q;
        }
    }
}