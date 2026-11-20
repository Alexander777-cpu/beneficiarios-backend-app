# Backend REST API - Sistema de Gestion de Beneficiarios (`beneficiarios-api`)

Servicio backend construido con **Spring Boot 3.3.5**, **Java 21**, **Oracle Database**, **Flyway**, **Spring Security + JWT** y **OpenAPI / Swagger UI**, siguiendo la arquitectura de referencia AgroAPI.

## 1. Requisitos previos
- **Java JDK 21** (`java -version`)
- **Maven 3.9+** (o wrapper integrado de IntelliJ IDEA)
- **Oracle Database 21c/23ai XE** (o Oracle Autonomous Database) con usuario/esquema `BENEFICIARIOS`

## 2. Configuracion de Base de Datos (Oracle)
Antes del primer arranque, crea el esquema en Oracle ejecutando como `SYS` / `SYSTEM`:
```sql
ALTER SESSION SET CONTAINER = XEPDB1;
CREATE USER BENEFICIARIOS IDENTIFIED BY "123";
GRANT CONNECT, RESOURCE, UNLIMITED TABLESPACE TO BENEFICIARIOS;
```
Al iniciar la aplicacion, **Flyway** creara automaticamente todas las tablas (`V1__esquema_beneficiarios.sql`), la seguridad RBAC (`V2__seguridad_rbac.sql`) y los datos semilla (`V3__datos_iniciales.sql`).

## 3. Arranque del servicio
```bash
mvn spring-boot:run
```
- **API Base:** `http://localhost:8080`
- **Health Check:** `http://localhost:8080/api/health`
- **Swagger UI:** `http://localhost:8080/swagger-ui.html`

## 4. Credenciales de prueba (Password para todos: `Agro2026*`)
| Usuario | Contrasena | Rol | Alcance / Privilegios |
|---|---|---|---|
| `admin` | `Agro2026*` | `SUPER_ADMIN` | Acceso global + emision de certificados |
| `programador1` | `Agro2026*` | `PROGRAMADOR` | Gestion global (sin emision de certificados) |
| `docente_fia` | `Agro2026*` | `LIDER_DOCENTE` | Coordinacion de proyectos/talleres en Facultad FIA (`facultadId=1`) |
| `estudiante1` | `Agro2026*` | `LIDER_ESTUDIANTE` | Registro de beneficiarios y asistencias en Proyecto 1 (`proyectoId=1`) |

## 5. Pruebas automatizadas y cobertura (JaCoCo / SonarQube)
```bash
mvn clean test jacoco:report
```
El reporte XML de cobertura se genera en `target/site/jacoco/jacoco.xml`.
