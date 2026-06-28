# API de Estudiantes — LearnFlow

## Descripción

API REST para la autenticación y gestión de estudiantes en la plataforma **LearnFlow**. Implementa registro, inicio de sesión, consulta y actualización de perfil usando **Spring Boot + JWT + BCrypt**.

---

##  Autenticación

| Método | Endpoint | Auth | Descripción |
|--------|----------|------|-------------|
| `POST` | `/api/auth/register` |  No | Registro de nuevo estudiante |
| `POST` | `/api/auth/login` |  No | Inicio de sesión |
| `GET` | `/api/students/me` |  JWT | Obtener perfil propio |
| `PUT` | `/api/students/me` |  JWT | Actualizar perfil propio |
| `GET` | `/api/auth/health` |  No | Health check del API |

---

## 1. Registro de Estudiante

Registra un nuevo estudiante en el sistema. Crea el usuario con contraseña encriptada (BCrypt) y su perfil de estudiante. Retorna un **JWT** para uso inmediato.

### Request

```
POST /api/auth/register
Content-Type: application/json
```

```json
{
    "fullName": "Juan Pérez López",
    "dateOfBirth": "2000-05-15",
    "email": "juan.perez@ejemplo.com",
    "password": "MiClave123",
    "nit": "12345678-9",
    "cardNumber": "1234567890123456",
    "cardExpiry": "2028-12-31",
    "photoUrl": "https://ejemplo.com/fotos/juan.jpg"
}
```

### Validaciones del Request

| Campo | Reglas |
|-------|--------|
| `fullName` | Requerido, máximo 150 caracteres |
| `dateOfBirth` | Requerido, debe ser una fecha pasada |
| `email` | Requerido, formato email válido, máximo 100 caracteres, **debe ser único** |
| `password` | Requerido, entre 6 y 100 caracteres |
| `nit` | Requerido, máximo 20 caracteres |
| `cardNumber` | Requerido, solo dígitos, entre 13 y 19 caracteres |
| `cardExpiry` | Requerido, debe ser una fecha futura |
| `photoUrl` | Requerido, máximo 500 caracteres |

### Response Exitoso — `201 Created`

```json
{
    "token": "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJqdWFuLnBlcmV6QGVqZW1wbG8uY29tIiwidXNlcklkIjoxLCJyb2xlIjoiRVNUVURJQU5URSIsImlhdCI6MTc1OTAwMDAwMCwiZXhwIjoxNzU5MDg2NDAwfQ.abc123...",
    "userId": 1,
    "studentId": 1,
    "email": "juan.perez@ejemplo.com",
    "role": "ESTUDIANTE",
    "fullName": "Juan Pérez López"
}
```

### Errores Comunes

**Email duplicado** — `400 Bad Request`
```json
{
    "timestamp": "2026-06-28T12:00:00",
    "status": 400,
    "message": "El correo ya esta registrado",
    "errors": []
}
```

**Datos inválidos** — `400 Bad Request`
```json
{
    "timestamp": "2026-06-28T12:00:00",
    "status": 400,
    "message": "Datos invalidos",
    "errors": [
        "email: El correo no tiene un formato valido",
        "password: La contrasena debe tener entre 6 y 100 caracteres",
        "cardNumber: La tarjeta debe contener entre 13 y 19 digitos"
    ]
}
```

---

## 2. Inicio de Sesión

Autentica al estudiante con su correo y contraseña. Retorna un **JWT** que debe usarse en los endpoints protegidos.

### Request

```
POST /api/auth/login
Content-Type: application/json
```

```json
{
    "email": "juan.perez@ejemplo.com",
    "password": "MiClave123"
}
```

### Validaciones del Request

| Campo | Reglas |
|-------|--------|
| `email` | Requerido, formato email válido |
| `password` | Requerido |

### Response Exitoso — `200 OK`

```json
{
    "token": "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJqdWFuLnBlcmV6QGVqZW1wbG8uY29tIiwidXNlcklkIjoxLCJyb2xlIjoiRVNUVURJQU5URSIsImlhdCI6MTc1OTAwMDAwMCwiZXhwIjoxNzU5MDg2NDAwfQ.abc123...",
    "userId": 1,
    "studentId": 1,
    "email": "juan.perez@ejemplo.com",
    "role": "ESTUDIANTE",
    "fullName": "Juan Pérez López"
}
```

### Error Común

**Credenciales inválidas** — `400 Bad Request`
```json
{
    "timestamp": "2026-06-28T12:00:00",
    "status": 400,
    "message": "Credenciales invalidas",
    "errors": []
}
```

---

## 3. Obtener Perfil

Obtiene los datos del estudiante autenticado. Requiere JWT en el header `Authorization`.

### Request

```
GET /api/students/me
Authorization: Bearer eyJhbGciOiJIUzM4NCJ9...
```

### Response Exitoso — `200 OK`

```json
{
    "studentId": 1,
    "userId": 1,
    "fullName": "Juan Pérez López",
    "dateOfBirth": "2000-05-15",
    "email": "juan.perez@ejemplo.com",
    "nit": "12345678-9",
    "cardNumber": "1234567890123456",
    "cardExpiry": "2028-12-31",
    "photoUrl": "https://ejemplo.com/fotos/juan.jpg"
}
```

---

## 4. Actualizar Perfil

Actualiza los datos del estudiante autenticado. La contraseña es **opcional** en actualización (si no se envía, se conserva la anterior). Requiere JWT.

### Request

```
PUT /api/students/me
Authorization: Bearer eyJhbGciOiJIUzM4NCJ9...
Content-Type: application/json
```

```json
{
    "fullName": "Juan Pérez López Actualizado",
    "dateOfBirth": "2000-05-15",
    "email": "juan.nuevo@ejemplo.com",
    "password": "NuevaClave456",
    "nit": "87654321-9",
    "cardNumber": "6543210987654321",
    "cardExpiry": "2029-06-30",
    "photoUrl": "https://ejemplo.com/fotos/juan-nuevo.jpg"
}
```

> **Nota**: Si no deseas cambiar la contraseña, omite el campo `password` o envíalo vacío. El sistema conservará la contraseña anterior.

### Validaciones del Request

| Campo | Reglas |
|-------|--------|
| `fullName` | Requerido, máximo 150 caracteres |
| `dateOfBirth` | Requerido, debe ser fecha pasada |
| `email` | Requerido, formato válido, **único** (puede ser el mismo del usuario actual) |
| `password` | **Opcional**. Si se envía, mínimo 6 caracteres |
| `nit` | Requerido, máximo 20 caracteres |
| `cardNumber` | Requerido, solo dígitos (13-19) |
| `cardExpiry` | Requerido, fecha futura |
| `photoUrl` | Requerido, máximo 500 caracteres |

### Response Exitoso — `200 OK`

```json
{
    "studentId": 1,
    "userId": 1,
    "fullName": "Juan Pérez López Actualizado",
    "dateOfBirth": "2000-05-15",
    "email": "juan.nuevo@ejemplo.com",
    "nit": "87654321-9",
    "cardNumber": "6543210987654321",
    "cardExpiry": "2029-06-30",
    "photoUrl": "https://ejemplo.com/fotos/juan-nuevo.jpg"
}
```

---

## 5. Health Check

Endpoint simple para verificar que el API está corriendo.

```
GET /api/auth/health
```

### Response — `200 OK`

```
LearnFlow API is running
```

---

##  Guía de Conexión (Frontend)

### Flujo típico

```
┌──────────┐          ┌──────────┐          ┌──────────┐
│  Login   │ ──JWT──► │  Obtener │ ──JWT──► │ Actualizar│
│  /login  │          │  Perfil  │          │  Perfil   │
└──────────┘          └──────────┘          └──────────┘
     │
     │ (si no tiene cuenta)
     ▼
┌──────────┐
│ Register │
│/register │
└──────────┘
```

### 1. Registro / Login → Guardar JWT

```javascript
// Ejemplo con fetch (JavaScript)
async function registerStudent(data) {
    const response = await fetch('http://localhost:8080/api/auth/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    });
    const result = await response.json();
    
    if (response.ok) {
        // Guardar token (localStorage, sessionStorage, contexto global)
        localStorage.setItem('token', result.token);
        localStorage.setItem('user', JSON.stringify(result));
        return result;
    }
    throw new Error(result.message);
}

async function login(email, password) {
    const response = await fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password })
    });
    const result = await response.json();
    
    if (response.ok) {
        localStorage.setItem('token', result.token);
        localStorage.setItem('user', JSON.stringify(result));
        return result;
    }
    throw new Error(result.message);
}
```

### 2. Usar JWT en endpoints protegidos

```javascript
async function getProfile() {
    const token = localStorage.getItem('token');
    const response = await fetch('http://localhost:8080/api/students/me', {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    });
    
    if (response.ok) {
        return await response.json();
    }
    
    // Si 401 → token expirado, redirigir a login
    if (response.status === 401 || response.status === 403) {
        localStorage.removeItem('token');
        window.location.href = '/login';
    }
    throw new Error('Error al obtener perfil');
}

async function updateProfile(data) {
    const token = localStorage.getItem('token');
    const response = await fetch('http://localhost:8080/api/students/me', {
        method: 'PUT',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    });
    
    if (response.ok) {
        return await response.json();
    }
    throw new Error('Error al actualizar perfil');
}
```

### 3. Ejemplo con Axios

```javascript
import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8080/api'
});

// Interceptor: agrega el token automáticamente a todas las peticiones
api.interceptors.request.use(config => {
    const token = localStorage.getItem('token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

// Interceptor: redirige al login si el token expiró
api.interceptors.response.use(
    response => response,
    error => {
        if (error.response?.status === 401 || error.response?.status === 403) {
            localStorage.removeItem('token');
            window.location.href = '/login';
        }
        return Promise.reject(error);
    }
);

// ─── Uso ───

async function register(formData) {
    const { data } = await api.post('/auth/register', formData);
    localStorage.setItem('token', data.token);
    return data;
}

async function login(email, password) {
    const { data } = await api.post('/auth/login', { email, password });
    localStorage.setItem('token', data.token);
    return data;
}

async function getProfile() {
    const { data } = await api.get('/students/me');
    return data;
}

async function updateProfile(formData) {
    const { data } = await api.put('/students/me', formData);
    return data;
}
```

### 4. Manejo de errores de validación

```javascript
try {
    const result = await register(formData);
    // Éxito
} catch (error) {
    if (error.response?.status === 400) {
        const { message, errors } = error.response.data;
        
        if (errors.length > 0) {
            // Errores de validación por campo
            errors.forEach(err => {
                console.log('Error de campo:', err);
                // err tiene formato: "email: El correo no tiene un formato valido"
            });
        } else {
            // Error de negocio (email duplicado, credenciales inválidas)
            alert(message);
        }
    }
}
```

---

##  Configuración del Backend

### Requisitos

- **Java 17** o superior
- **MySQL** 8.0+
- **Maven** 3.9+

### Base de datos

Editar `backend/src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/learnflow?useSSL=false&serverTimezone=America/Guatemala&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=admin
```

> Las tablas (`roles`, `users`, `students`) se crean automáticamente con `ddl-auto=update`.

### Ejecutar

```bash
cd backend
./mvnw spring-boot:run
```

El servidor inicia en `http://localhost:8080`.

### Seed de datos

Al iniciar, el sistema crea automáticamente dos roles si no existen:
- `ESTUDIANTE` — para estudiantes
- `ADMIN_CONTENIDO` — para administradores de contenido

---

##  Pruebas rápidas con cURL

### Registrar
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "Juan Pérez",
    "dateOfBirth": "2000-05-15",
    "email": "juan@ejemplo.com",
    "password": "MiClave123",
    "nit": "12345678-9",
    "cardNumber": "1234567890123456",
    "cardExpiry": "2028-12-31",
    "photoUrl": "https://ejemplo.com/foto.jpg"
  }'
```

### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "juan@ejemplo.com", "password": "MiClave123"}'
```

### Obtener perfil (reemplaza TOKEN con el JWT recibido)
```bash
curl -X GET http://localhost:8080/api/students/me \
  -H "Authorization: Bearer TOKEN"
```

### Actualizar perfil
```bash
curl -X PUT http://localhost:8080/api/students/me \
  -H "Authorization: Bearer TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "Juan Pérez Actualizado",
    "dateOfBirth": "2000-05-15",
    "email": "juan@ejemplo.com",
    "nit": "87654321-9",
    "cardNumber": "6543210987654321",
    "cardExpiry": "2029-06-30",
    "photoUrl": "https://ejemplo.com/foto-nueva.jpg"
  }'
```

---

##  Resumen de Estados HTTP

| Código | Significado |
|--------|-------------|
| `200 OK` | Login, get profile, update profile exitosos |
| `201 Created` | Registro exitoso |
| `400 Bad Request` | Datos inválidos o email duplicado |
| `401 Unauthorized` | Token faltante, inválido o expirado |
| `403 Forbidden` | No tiene el rol requerido (ESTUDIANTE) |
| `409 Conflict` | Error interno del servidor (ej: rol no encontrado) |
