# 📋 API Endpoints — Sprint 1

Base URL: `http://localhost:8080/api`

## 🔐 Autenticación
| Método | Ruta               | Body                              | Acceso  |
|--------|--------------------|-----------------------------------|---------|
| POST   | /auth/register     | fullName, email, password, role   | Público |
| POST   | /auth/login        | email, password                   | Público |

## 👤 Usuarios
| Método | Ruta                      | Acceso              |
|--------|---------------------------|---------------------|
| GET    | /users                    | ADMIN, COORDINADOR  |
| GET    | /users/{id}               | ADMIN, COORDINADOR  |
| PATCH  | /users/{id}/toggle-active | ADMIN               |
| PATCH  | /users/{id}/role          | ADMIN               |

## 📁 Proyectos
| Método | Ruta            | Acceso              |
|--------|-----------------|---------------------|
| POST   | /projects       | ADMIN, COORDINADOR  |
| GET    | /projects       | Autenticado         |
| GET    | /projects/{id}  | Autenticado         |
| PUT    | /projects/{id}  | ADMIN, COORDINADOR  |
| DELETE | /projects/{id}  | ADMIN               |

## 👥 Beneficiarios
| Método | Ruta                          | Acceso              |
|--------|-------------------------------|---------------------|
| POST   | /beneficiaries                | ADMIN, COORDINADOR  |
| GET    | /beneficiaries/{id}           | Autenticado         |
| GET    | /beneficiaries/project/{id}   | Autenticado         |
| PUT    | /beneficiaries/{id}           | ADMIN, COORDINADOR  |
| DELETE | /beneficiaries/{id}           | ADMIN               |

## 📅 Asistencia
| Método | Ruta                            | Acceso              |
|--------|---------------------------------|---------------------|
| POST   | /attendance/manual              | ADMIN, COORDINADOR  |
| GET    | /attendance/{id}                | Autenticado         |
| GET    | /attendance/user/{id}           | ADMIN, COORDINADOR  |
| GET    | /attendance/project/{id}        | ADMIN, COORDINADOR  |
| GET    | /attendance/filter              | ADMIN, COORDINADOR  |
| GET    | /attendance/hours               | Autenticado         |
| GET    | /attendance/summary/project/{id}| ADMIN, COORDINADOR  |
| PUT    | /attendance/{id}                | ADMIN, COORDINADOR  |
| DELETE | /attendance/{id}                | ADMIN               |

## ⏱️ Horas
| Método | Ruta                                      | Acceso              |
|--------|-------------------------------------------|---------------------|
| GET    | /hours/user/{id}/project/{id}             | Autenticado         |
| GET    | /hours/user/{id}/total                    | Autenticado         |
| GET    | /hours/project/{id}/summary               | ADMIN, COORDINADOR  |
| GET    | /hours/user/{id}/project/{id}/check       | Autenticado         |

## 📷 QR
| Método | Ruta           | Acceso              |
|--------|----------------|---------------------|
| POST   | /qr/generate   | ADMIN, COORDINADOR  |
| POST   | /qr/scan       | Autenticado         |
| POST   | /qr/checkout   | Autenticado         |