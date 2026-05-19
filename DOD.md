# ✅ Definition of Done — Sprint 1

## Autenticación y Seguridad
- [x] Login con JWT funcional
- [x] Registro de usuarios con rol asignable
- [x] Filtro JWT en cada request
- [x] Roles ADMIN, COORDINADOR, ESTUDIANTE operativos
- [x] Respuestas 401 y 403 en JSON
- [x] Data Seeding con 3 usuarios y 3 roles al iniciar

## Proyectos
- [x] CRUD completo de proyectos
- [x] Validación de rango de fechas
- [x] Filtrado por nombre y estado
- [x] Asignación de coordinador

## Beneficiarios
- [x] CRUD completo de beneficiarios
- [x] Validación de documento duplicado
- [x] Filtrado por proyecto, nombre y estado

## Asistencia Manual
- [x] Registro con checkIn y checkOut
- [x] Cálculo automático de horas
- [x] Validación de usuario activo
- [x] Validación de proyecto activo
- [x] Validación de solapamiento de horarios

## Módulo QR
- [x] Generación de QR con imagen Base64
- [x] Token único por UUID
- [x] Expiración configurable
- [x] Validación: activo + no usado + no expirado
- [x] Registro automático de asistencia al escanear
- [x] Cierre de sesión con checkOut
- [x] Cron job que desactiva QRs expirados
- [x] Lock pesimista contra race condition

## Horas
- [x] Suma automática de horas por usuario/proyecto
- [x] Total global de horas por usuario
- [x] Verificación de mínimo de horas requeridas
- [x] Resumen agrupado por proyecto

## QA
- [x] Prueba de matriz de permisos (17 casos)
- [x] Prueba de concurrencia QR (10 hilos simultáneos)
- [x] Pruebas unitarias de cálculo de horas
- [x] Pruebas de integración de edición y acumulado
- [x] Todos los tests pasan sin errores