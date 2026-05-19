package com.proyecto.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "participations")
public class Participation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(nullable = false)
    private LocalDateTime checkIn;

    private LocalDateTime checkOut;

    private Double hoursLogged;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RegistrationMethod registrationMethod;

    public enum RegistrationMethod { MANUAL, QR }

    public Participation() {}

    // Getters
    public Long getId() { return id; }
    public User getUser() { return user; }
    public Project getProject() { return project; }
    public LocalDateTime getCheckIn() { return checkIn; }
    public LocalDateTime getCheckOut() { return checkOut; }
    public Double getHoursLogged() { return hoursLogged; }
    public RegistrationMethod getRegistrationMethod() { return registrationMethod; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setUser(User user) { this.user = user; }
    public void setProject(Project project) { this.project = project; }
    public void setCheckIn(LocalDateTime checkIn) { this.checkIn = checkIn; }
    public void setCheckOut(LocalDateTime checkOut) { this.checkOut = checkOut; }
    public void setHoursLogged(Double hoursLogged) { this.hoursLogged = hoursLogged; }
    public void setRegistrationMethod(RegistrationMethod m) { this.registrationMethod = m; }

    public static ParticipationBuilder builder() { return new ParticipationBuilder(); }

    public static class ParticipationBuilder {
        private User user;
        private Project project;
        private LocalDateTime checkIn;
        private LocalDateTime checkOut;
        private Double hoursLogged;
        private RegistrationMethod registrationMethod;

        public ParticipationBuilder user(User u) { this.user = u; return this; }
        public ParticipationBuilder project(Project p) { this.project = p; return this; }
        public ParticipationBuilder checkIn(LocalDateTime t) { this.checkIn = t; return this; }
        public ParticipationBuilder checkOut(LocalDateTime t) { this.checkOut = t; return this; }
        public ParticipationBuilder hoursLogged(Double h) { this.hoursLogged = h; return this; }
        public ParticipationBuilder registrationMethod(RegistrationMethod m) { this.registrationMethod = m; return this; }

        public Participation build() {
            Participation p = new Participation();
            p.user = this.user; p.project = this.project;
            p.checkIn = this.checkIn; p.checkOut = this.checkOut;
            p.hoursLogged = this.hoursLogged;
            p.registrationMethod = this.registrationMethod;
            return p;
        }
    }
}