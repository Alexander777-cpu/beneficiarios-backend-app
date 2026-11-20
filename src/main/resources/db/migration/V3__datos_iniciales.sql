-- =====================================================================
-- V3: Datos Semilla (Privilegios, Roles, Usuarios, Facultades, Proyectos, Talleres, Beneficiarios y Asistencias)
-- =====================================================================

-- 1. PRIVILEGIOS
INSERT INTO privilegios (id, nombre, descripcion) VALUES (1, 'FACULTAD_LEER', 'Consultar listado y detalle de facultades');
INSERT INTO privilegios (id, nombre, descripcion) VALUES (2, 'FACULTAD_GESTIONAR', 'Crear, editar y eliminar facultades');
INSERT INTO privilegios (id, nombre, descripcion) VALUES (3, 'PROYECTO_LEER', 'Consultar proyectos sociales');
INSERT INTO privilegios (id, nombre, descripcion) VALUES (4, 'PROYECTO_GESTIONAR', 'Crear, editar y eliminar proyectos');
INSERT INTO privilegios (id, nombre, descripcion) VALUES (5, 'TALLER_LEER', 'Consultar talleres asignados');
INSERT INTO privilegios (id, nombre, descripcion) VALUES (6, 'TALLER_GESTIONAR', 'Crear, editar y eliminar talleres');
INSERT INTO privilegios (id, nombre, descripcion) VALUES (7, 'BENEFICIARIO_LEER', 'Consultar padron de beneficiarios');
INSERT INTO privilegios (id, nombre, descripcion) VALUES (8, 'BENEFICIARIO_CREAR', 'Registrar nuevos beneficiarios');
INSERT INTO privilegios (id, nombre, descripcion) VALUES (9, 'BENEFICIARIO_EDITAR', 'Actualizar datos de beneficiarios');
INSERT INTO privilegios (id, nombre, descripcion) VALUES (10, 'BENEFICIARIO_ELIMINAR', 'Eliminar beneficiarios sin asistencias');
INSERT INTO privilegios (id, nombre, descripcion) VALUES (11, 'ASISTENCIA_LEER', 'Consultar planillas y reportes de asistencia');
INSERT INTO privilegios (id, nombre, descripcion) VALUES (12, 'ASISTENCIA_REGISTRAR', 'Guardar y actualizar asistencias diarias');
INSERT INTO privilegios (id, nombre, descripcion) VALUES (13, 'CERTIFICADO_EMITIR', 'Validar y emitir certificados por asistencia');
INSERT INTO privilegios (id, nombre, descripcion) VALUES (14, 'USUARIO_GESTIONAR', 'Administrar cuentas y roles de usuario');
INSERT INTO privilegios (id, nombre, descripcion) VALUES (15, 'AUDITORIA_LEER', 'Consultar bitacora inmutable de movimientos');

-- 2. ROLES
INSERT INTO roles (id, nombre, descripcion) VALUES (1, 'SUPER_ADMIN', 'Administrador general con acceso total');
INSERT INTO roles (id, nombre, descripcion) VALUES (2, 'PROGRAMADOR', 'Soporte tecnico TI y gestion operativa');
INSERT INTO roles (id, nombre, descripcion) VALUES (3, 'LIDER_DOCENTE', 'Coordinador de facultad');
INSERT INTO roles (id, nombre, descripcion) VALUES (4, 'LIDER_ESTUDIANTE', 'Gestor operativo de proyecto');

-- 3. ROL_PRIVILEGIOS
INSERT INTO rol_privilegios (rol_id, privilegio_id) VALUES (1, 1);
INSERT INTO rol_privilegios (rol_id, privilegio_id) VALUES (1, 2);
INSERT INTO rol_privilegios (rol_id, privilegio_id) VALUES (1, 3);
INSERT INTO rol_privilegios (rol_id, privilegio_id) VALUES (1, 4);
INSERT INTO rol_privilegios (rol_id, privilegio_id) VALUES (1, 5);
INSERT INTO rol_privilegios (rol_id, privilegio_id) VALUES (1, 6);
INSERT INTO rol_privilegios (rol_id, privilegio_id) VALUES (1, 7);
INSERT INTO rol_privilegios (rol_id, privilegio_id) VALUES (1, 8);
INSERT INTO rol_privilegios (rol_id, privilegio_id) VALUES (1, 9);
INSERT INTO rol_privilegios (rol_id, privilegio_id) VALUES (1, 10);
INSERT INTO rol_privilegios (rol_id, privilegio_id) VALUES (1, 11);
INSERT INTO rol_privilegios (rol_id, privilegio_id) VALUES (1, 12);
INSERT INTO rol_privilegios (rol_id, privilegio_id) VALUES (1, 13);
INSERT INTO rol_privilegios (rol_id, privilegio_id) VALUES (1, 14);
INSERT INTO rol_privilegios (rol_id, privilegio_id) VALUES (1, 15);

INSERT INTO rol_privilegios (rol_id, privilegio_id) VALUES (2, 1);
INSERT INTO rol_privilegios (rol_id, privilegio_id) VALUES (2, 2);
INSERT INTO rol_privilegios (rol_id, privilegio_id) VALUES (2, 3);
INSERT INTO rol_privilegios (rol_id, privilegio_id) VALUES (2, 4);
INSERT INTO rol_privilegios (rol_id, privilegio_id) VALUES (2, 5);
INSERT INTO rol_privilegios (rol_id, privilegio_id) VALUES (2, 6);
INSERT INTO rol_privilegios (rol_id, privilegio_id) VALUES (2, 7);
INSERT INTO rol_privilegios (rol_id, privilegio_id) VALUES (2, 8);
INSERT INTO rol_privilegios (rol_id, privilegio_id) VALUES (2, 9);
INSERT INTO rol_privilegios (rol_id, privilegio_id) VALUES (2, 10);
INSERT INTO rol_privilegios (rol_id, privilegio_id) VALUES (2, 11);
INSERT INTO rol_privilegios (rol_id, privilegio_id) VALUES (2, 12);
INSERT INTO rol_privilegios (rol_id, privilegio_id) VALUES (2, 14);
INSERT INTO rol_privilegios (rol_id, privilegio_id) VALUES (2, 15);

INSERT INTO rol_privilegios (rol_id, privilegio_id) VALUES (3, 1);
INSERT INTO rol_privilegios (rol_id, privilegio_id) VALUES (3, 3);
INSERT INTO rol_privilegios (rol_id, privilegio_id) VALUES (3, 4);
INSERT INTO rol_privilegios (rol_id, privilegio_id) VALUES (3, 5);
INSERT INTO rol_privilegios (rol_id, privilegio_id) VALUES (3, 6);
INSERT INTO rol_privilegios (rol_id, privilegio_id) VALUES (3, 7);
INSERT INTO rol_privilegios (rol_id, privilegio_id) VALUES (3, 8);
INSERT INTO rol_privilegios (rol_id, privilegio_id) VALUES (3, 9);
INSERT INTO rol_privilegios (rol_id, privilegio_id) VALUES (3, 10);
INSERT INTO rol_privilegios (rol_id, privilegio_id) VALUES (3, 11);
INSERT INTO rol_privilegios (rol_id, privilegio_id) VALUES (3, 12);

INSERT INTO rol_privilegios (rol_id, privilegio_id) VALUES (4, 3);
INSERT INTO rol_privilegios (rol_id, privilegio_id) VALUES (4, 5);
INSERT INTO rol_privilegios (rol_id, privilegio_id) VALUES (4, 7);
INSERT INTO rol_privilegios (rol_id, privilegio_id) VALUES (4, 8);
INSERT INTO rol_privilegios (rol_id, privilegio_id) VALUES (4, 9);
INSERT INTO rol_privilegios (rol_id, privilegio_id) VALUES (4, 11);
INSERT INTO rol_privilegios (rol_id, privilegio_id) VALUES (4, 12);

-- 4. FACULTADES Y PROYECTOS INICIALES
INSERT INTO facultades (id, nombre) VALUES (1, 'FIA - Facultad de Ingenieria y Arquitectura');
INSERT INTO facultades (id, nombre) VALUES (2, 'FCS - Facultad de Ciencias de la Salud');
INSERT INTO facultades (id, nombre) VALUES (3, 'FCE - Facultad de Ciencias Empresariales');

INSERT INTO proyectos (id, nombre, facultad_id) VALUES (1, 'Alfabetizacion Digital Comunitaria', 1);
INSERT INTO proyectos (id, nombre, facultad_id) VALUES (2, 'Robotica Educativa Escolar', 1);
INSERT INTO proyectos (id, nombre, facultad_id) VALUES (3, 'Prevencion y Nutricion Infantil', 2);

INSERT INTO talleres (id, nombre) VALUES (1, 'Ofimatica Basica y Ciudadania Digital');
INSERT INTO talleres (id, nombre) VALUES (2, 'Introduccion a la Programacion por Bloques');
INSERT INTO talleres (id, nombre) VALUES (3, 'Alimentacion Saludable en Familia');

INSERT INTO proyecto_talleres (id, proyecto_id, taller_id) VALUES (1, 1, 1);
INSERT INTO proyecto_talleres (id, proyecto_id, taller_id) VALUES (2, 2, 2);
INSERT INTO proyecto_talleres (id, proyecto_id, taller_id) VALUES (3, 3, 3);

-- 5. USUARIOS DE PRUEBA (Clave para todos: Agro2026*)
INSERT INTO usuarios (id, username, password, nombre_completo, estado, facultad_id, proyecto_id, fecha_creacion)
VALUES (1, 'admin', '$2a$10$Bq7C4bL5xMvZJszx4y0.xu7OPXaVV1InPtlPKgDy9cA53uwsqdYVu', 'Giezi Alexander Vivas - Super Admin', 1, NULL, NULL, CURRENT_TIMESTAMP);

INSERT INTO usuarios (id, username, password, nombre_completo, estado, facultad_id, proyecto_id, fecha_creacion)
VALUES (2, 'programador1', '$2a$10$Bq7C4bL5xMvZJszx4y0.xu7OPXaVV1InPtlPKgDy9cA53uwsqdYVu', 'Soporte TI UPeU', 1, NULL, NULL, CURRENT_TIMESTAMP);

INSERT INTO usuarios (id, username, password, nombre_completo, estado, facultad_id, proyecto_id, fecha_creacion)
VALUES (3, 'docente_fia', '$2a$10$Bq7C4bL5xMvZJszx4y0.xu7OPXaVV1InPtlPKgDy9cA53uwsqdYVu', 'Ing. Carlos Mendoza (Lider FIA)', 1, 1, NULL, CURRENT_TIMESTAMP);

INSERT INTO usuarios (id, username, password, nombre_completo, estado, facultad_id, proyecto_id, fecha_creacion)
VALUES (4, 'estudiante1', '$2a$10$Bq7C4bL5xMvZJszx4y0.xu7OPXaVV1InPtlPKgDy9cA53uwsqdYVu', 'Ana Torres (Lider Proyecto 1)', 1, 1, 1, CURRENT_TIMESTAMP);

INSERT INTO usuario_roles (usuario_id, rol_id) VALUES (1, 1);
INSERT INTO usuario_roles (usuario_id, rol_id) VALUES (2, 2);
INSERT INTO usuario_roles (usuario_id, rol_id) VALUES (3, 3);
INSERT INTO usuario_roles (usuario_id, rol_id) VALUES (4, 4);

-- 6. BENEFICIARIOS DE PRUEBA
INSERT INTO beneficiarios (id, tipo_beneficiario, nombres, apellido_paterno, apellido_materno, telefono, edad, grado_estudios, estado_civil, tutor_nombre, talla_polo, talla_pantalon, talla_calzado, proyecto_taller_id)
VALUES (1, 'NINO', 'Mateo Gabriel', 'Quispe', 'Huaman', '987654321', 10, '5to Primaria', 'SOLTERO(A)', 'Maria Huaman', '12', '12', 34, 1);

INSERT INTO beneficiarios (id, tipo_beneficiario, nombres, apellido_paterno, apellido_materno, telefono, edad, grado_estudios, estado_civil, tutor_nombre, talla_polo, talla_pantalon, talla_calzado, proyecto_taller_id)
VALUES (2, 'ADOLESCENTE', 'Valeria Sofia', 'Rojas', 'Paredes', '912345678', 15, '4to Secundaria', 'SOLTERO(A)', 'Jorge Rojas', 'S', '28', 37, 2);

INSERT INTO beneficiarios (id, tipo_beneficiario, nombres, apellido_paterno, apellido_materno, telefono, edad, grado_estudios, estado_civil, tutor_nombre, talla_polo, talla_pantalon, talla_calzado, proyecto_taller_id)
VALUES (3, 'ADULTO', 'Rosa Elena', 'Mamani', 'Condori', '955443322', 34, 'Secundaria Completa', 'CASADO(A)', NULL, 'M', '32', 38, 3);

-- 7. ASISTENCIAS INICIALES (Mateo Gabriel tiene 5 asistencias -> APTO CERTIFICADO)
INSERT INTO asistencias (fecha, asistio, beneficiario_id, proyecto_taller_id) VALUES ('2026-10-05', 1, 1, 1);
INSERT INTO asistencias (fecha, asistio, beneficiario_id, proyecto_taller_id) VALUES ('2026-10-12', 1, 1, 1);
INSERT INTO asistencias (fecha, asistio, beneficiario_id, proyecto_taller_id) VALUES ('2026-10-19', 1, 1, 1);
INSERT INTO asistencias (fecha, asistio, beneficiario_id, proyecto_taller_id) VALUES ('2026-10-26', 1, 1, 1);
INSERT INTO asistencias (fecha, asistio, beneficiario_id, proyecto_taller_id) VALUES ('2026-11-02', 1, 1, 1);

INSERT INTO asistencias (fecha, asistio, beneficiario_id, proyecto_taller_id) VALUES ('2026-10-10', 1, 2, 2);
INSERT INTO asistencias (fecha, asistio, beneficiario_id, proyecto_taller_id) VALUES ('2026-10-17', 1, 2, 2);

-- 8. AUDITORIA INICIAL
INSERT INTO auditorias (usuario_accion, accion, modulo, descripcion, fecha)
VALUES ('SISTEMA', 'INICIALIZACION', 'SISTEMA', 'Carga inicial de catalogo institucional y usuarios semilla', CURRENT_TIMESTAMP);

COMMIT;
