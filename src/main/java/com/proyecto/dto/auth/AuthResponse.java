package com.proyecto.dto.auth;

public class AuthResponse {

    private String token;
    private String email;
    private String fullName;
    private String role;
    private Long expiresIn;

    public AuthResponse() {}

    // Getters
    public String getToken() { return token; }
    public String getEmail() { return email; }
    public String getFullName() { return fullName; }
    public String getRole() { return role; }
    public Long getExpiresIn() { return expiresIn; }

    // Setters
    public void setToken(String token) { this.token = token; }
    public void setEmail(String email) { this.email = email; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setRole(String role) { this.role = role; }
    public void setExpiresIn(Long expiresIn) { this.expiresIn = expiresIn; }

    // Builder
    public static AuthResponseBuilder builder() { return new AuthResponseBuilder(); }

    public static class AuthResponseBuilder {
        private String token;
        private String email;
        private String fullName;
        private String role;
        private Long expiresIn;

        public AuthResponseBuilder token(String token) { this.token = token; return this; }
        public AuthResponseBuilder email(String email) { this.email = email; return this; }
        public AuthResponseBuilder fullName(String fullName) { this.fullName = fullName; return this; }
        public AuthResponseBuilder role(String role) { this.role = role; return this; }
        public AuthResponseBuilder expiresIn(Long expiresIn) { this.expiresIn = expiresIn; return this; }

        public AuthResponse build() {
            AuthResponse r = new AuthResponse();
            r.token = this.token; r.email = this.email;
            r.fullName = this.fullName; r.role = this.role;
            r.expiresIn = this.expiresIn;
            return r;
        }
    }
}