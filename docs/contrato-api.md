# Contrato del API REST (`/api/v1`)

## 1. Formato estandar de paginacion (`PaginaResponseDTO<T>`)
```json
{
  "contenido": [],
  "pagina": 0,
  "tamanio": 20,
  "totalElementos": 3,
  "totalPaginas": 1,
  "ultima": true
}
```

## 2. Formato unico de error (`ErrorResponseDTO`)
```json
{
  "timestamp": "2026-11-17T17:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Datos invalidos",
  "path": "/api/v1/beneficiarios",
  "validationErrors": {
    "nombres": "Ya existe un beneficiario registrado con los nombres: Mateo Gabriel"
  }
}
```

## 3. Endpoints principales
| Metodo | Ruta | Privilegio requerido | Codigos HTTP | Descripcion |
|---|---|---|---|---|
| `GET` | `/api/health` | Publico | `200` | Verifica disponibilidad del servicio |
| `POST` | `/api/v1/auth/login` | Publico | `200`, `400`, `401` | Autentica credenciales y devuelve JWT + roles + privilegios |
| `GET` | `/api/v1/auth/me` | Autenticado | `200`, `401` | Devuelve perfil, facultadId, proyectoId y privilegios |
| `GET` | `/api/v1/facultades` | `FACULTAD_LEER` | `200`, `401`, `403` | Lista facultades paginadas |
| `POST` | `/api/v1/facultades` | `FACULTAD_GESTIONAR` | `201`, `400`, `403` | Crea una facultad |
| `GET` | `/api/v1/proyectos` | `PROYECTO_LEER` | `200`, `401`, `403` | Lista proyectos paginados (`?facultadId=`) |
| `POST` | `/api/v1/proyectos` | `PROYECTO_GESTIONAR` | `201`, `400`, `403` | Crea un proyecto asociado a facultad |
| `GET` | `/api/v1/talleres` | `TALLER_LEER` | `200`, `401`, `403` | Lista asignaciones Proyecto-Taller (`?proyectoId=&facultadId=`) |
| `POST` | `/api/v1/talleres` | `TALLER_GESTIONAR` | `201`, `400`, `403` | Crea/asigna taller a proyecto |
| `GET` | `/api/v1/beneficiarios` | `BENEFICIARIO_LEER` | `200`, `401`, `403` | Lista beneficiarios paginados (`?proyectoTallerId=&proyectoId=&facultadId=&buscar=`) |
| `POST` | `/api/v1/beneficiarios` | `BENEFICIARIO_CREAR` | `201`, `400`, `403` | Registra beneficiario validando unicidad de nombres |
| `PUT` | `/api/v1/beneficiarios/{id}` | `BENEFICIARIO_EDITAR` | `200`, `400`, `403`, `404` | Actualiza datos del beneficiario |
| `DELETE` | `/api/v1/beneficiarios/{id}` | `BENEFICIARIO_ELIMINAR` | `204`, `403`, `404`, `409` | Elimina si no tiene asistencias registradas |
| `GET` | `/api/v1/asistencias/planilla/{proyectoTallerId}` | `ASISTENCIA_LEER` | `200`, `403`, `404` | Planilla diaria (`?fecha=YYYY-MM-DD`) |
| `POST` | `/api/v1/asistencias/guardar` | `ASISTENCIA_REGISTRAR` | `200`, `400`, `403` | Guarda o actualiza asistencias del dia en lote |
| `GET` | `/api/v1/certificados/beneficiarios` | `CERTIFICADO_EMITIR` | `200`, `403` | Listado con conteo historico y bandera `aptoCertificado` (>=5) |
| `POST` | `/api/v1/certificados/{id}/emitir` | `CERTIFICADO_EMITIR` | `200`, `403`, `409` | Valida >=5 asistencias y registra emision |
| `GET` | `/api/v1/auditorias` | `AUDITORIA_LEER` | `200`, `403` | Bitacora paginada de movimientos |
