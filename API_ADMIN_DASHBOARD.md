# API Dashboard de Administrador — LearnFlow

## Descripcion

API REST para el dashboard del administrador de contenido. Cubre autenticacion de administrador, busqueda y filtrado de cursos, estadisticas para graficas, y los catalogos de apoyo (tipos de contenido, categorias, niveles de dificultad).

Todos los endpoints de administrador requieren JWT con rol `ADMIN_CONTENIDO`. El token se obtiene con el endpoint `POST /api/auth/admin/login`.

---

## Resumen de Endpoints

| Metodo | Endpoint | Auth | Descripcion |
|--------|----------|------|-------------|
| `POST` | `/api/auth/admin/login` | No | Login del administrador |
| `GET` | `/api/admin/dashboard/courses` | JWT Admin | Buscar/filtrar cursos |
| `GET` | `/api/admin/dashboard/stats` | JWT Admin | Estadisticas para graficas |
| `GET` | `/api/categories` | JWT Admin | Listar categorias |
| `POST` | `/api/categories` | JWT Admin | Crear categoria |
| `PUT` | `/api/categories/{id}` | JWT Admin | Actualizar categoria |
| `DELETE` | `/api/categories/{id}` | JWT Admin | Eliminar categoria |
| `GET` | `/api/content-types` | JWT Admin | Listar tipos de contenido |
| `POST` | `/api/content-types` | JWT Admin | Crear tipo de contenido |
| `PUT` | `/api/content-types/{id}` | JWT Admin | Actualizar tipo de contenido |
| `DELETE` | `/api/content-types/{id}` | JWT Admin | Eliminar tipo de contenido |
| `GET` | `/api/difficulty-levels` | JWT Admin | Listar niveles de dificultad |
| `POST` | `/api/difficulty-levels` | JWT Admin | Crear nivel de dificultad |
| `PUT` | `/api/difficulty-levels/{id}` | JWT Admin | Actualizar nivel de dificultad |
| `DELETE` | `/api/difficulty-levels/{id}` | JWT Admin | Eliminar nivel de dificultad |

---

## 1. Login del Administrador

Autentica al administrador de contenido. Solo funciona para usuarios con rol `ADMIN_CONTENIDO`. Retorna un JWT que debe incluirse en todos los endpoints del dashboard.

### Request

```
POST /api/auth/admin/login
Content-Type: application/json
```

```json
{
    "email": "admin@learnflow.com",
    "password": "admin123"
}
```

### Response Exitoso — `200 OK`

```json
{
    "token": "eyJhbGciOiJIUzM4NCJ9...",
    "userId": 1,
    "studentId": null,
    "email": "admin@learnflow.com",
    "role": "ADMIN_CONTENIDO",
    "fullName": "Administrador"
}
```

### Errores

| Codigo | Causa |
|--------|-------|
| `401 Unauthorized` | Credenciales incorrectas o cuenta deshabilitada |
| `403 Forbidden` | El usuario existe pero no tiene rol `ADMIN_CONTENIDO` |

---

## 2. Busqueda y Filtrado de Cursos

Devuelve cursos activos con filtros opcionales combinables. Sin parametros retorna todos los cursos activos.

### Request

```
GET /api/admin/dashboard/courses
Authorization: Bearer {token}
```

Parametros de query (todos opcionales):

| Parametro | Tipo | Descripcion |
|-----------|------|-------------|
| `title` | `string` | Busqueda parcial por titulo (ignora mayusculas) |
| `contentTypeId` | `long` | ID del tipo de contenido |
| `categoryId` | `long` | ID de la categoria |
| `difficultyLevelId` | `long` | ID del nivel de dificultad |
| `year` | `int` | Ano de produccion |

### Ejemplos

```
GET /api/admin/dashboard/courses
GET /api/admin/dashboard/courses?title=React
GET /api/admin/dashboard/courses?categoryId=1
GET /api/admin/dashboard/courses?difficultyLevelId=2&year=2025
GET /api/admin/dashboard/courses?title=Python&categoryId=1&difficultyLevelId=1
```

### Response Exitoso — `200 OK`

```json
[
    {
        "id": 1,
        "title": "Introduccion a React",
        "productionYear": 2025,
        "instructor": "Carlos Mendez",
        "shortSummary": "Fundamentos de React para principiantes",
        "description": "Aprende los conceptos basicos de React incluyendo componentes, props y estado.",
        "mediaUrl": "https://learnflow.local/media/react-intro.mp4",
        "contentType": "Clase grabada",
        "category": "Programacion",
        "difficultyLevel": "Principiante",
        "active": true
    }
]
```

---

## 3. Estadisticas para Graficas

Retorna en un solo objeto los datos para las cuatro graficas del dashboard. Los conteos se basan en los registros de visualizacion (`viewing_logs`) y suscripciones activas.

### Request

```
GET /api/admin/dashboard/stats
Authorization: Bearer {token}
```

### Response Exitoso — `200 OK`

```json
{
    "topCategories": [
        { "categoryName": "Programacion", "viewCount": 150 },
        { "categoryName": "Diseno", "viewCount": 98 },
        { "categoryName": "Negocios", "viewCount": 43 }
    ],
    "topDifficultyLevels": [
        { "levelName": "Principiante", "viewCount": 210 },
        { "levelName": "Intermedio", "viewCount": 134 },
        { "levelName": "Avanzado", "viewCount": 47 }
    ],
    "topCourses": [
        { "courseId": 3, "title": "Python para Data Science", "viewCount": 89 },
        { "courseId": 1, "title": "Introduccion a React", "viewCount": 76 },
        { "courseId": 7, "title": "Diseno UI con Figma", "viewCount": 54 }
    ],
    "subscriptionDistribution": [
        { "type": "MENSUAL", "count": 120 },
        { "type": "TRIMESTRAL", "count": 85 },
        { "type": "ANUAL", "count": 40 }
    ]
}
```

### Descripcion de cada grafica

| Campo | Grafica | Descripcion |
|-------|---------|-------------|
| `topCategories` | Barras / Pie | Top 3 categorias con mas reproducciones |
| `topDifficultyLevels` | Barras / Pie | Top 3 niveles de dificultad mas vistos |
| `topCourses` | Ranking / Barras horizontales | Top 10 cursos mas visualizados |
| `subscriptionDistribution` | Pie / Donut | Estudiantes activos por tipo de suscripcion |

> **Nota:** Si no hay registros de visualizacion o suscripciones, los arrays retornan vacios `[]`. Esto es esperado en una base de datos recien iniciada.

---

## 4. Catalogos de Apoyo

Los catalogos comparten la misma estructura de request y response. Se usan sus IDs al crear o editar cursos.

### Estructura compartida

**Request (crear / actualizar):**

```json
{
    "name": "Nombre del elemento",
    "description": "Descripcion del elemento",
    "active": true
}
```

**Response:**

```json
{
    "id": 1,
    "name": "Nombre del elemento",
    "description": "Descripcion del elemento",
    "active": true
}
```

---

### 4.1 Categorias

Base path: `/api/categories`

| Metodo | Endpoint | Descripcion |
|--------|----------|-------------|
| `GET` | `/api/categories` | Lista todas las categorias |
| `GET` | `/api/categories/{id}` | Obtiene una categoria por ID |
| `POST` | `/api/categories` | Crea una categoria |
| `PUT` | `/api/categories/{id}` | Actualiza una categoria |
| `DELETE` | `/api/categories/{id}` | Elimina una categoria |

Categorias sembradas al iniciar:

| ID | Nombre | Descripcion |
|----|--------|-------------|
| 1 | Programacion | Cursos relacionados con desarrollo de software |
| 2 | Diseno | Cursos relacionados con diseno visual y experiencia de usuario |
| 3 | Negocios | Cursos relacionados con gestion, ventas y emprendimiento |

---

### 4.2 Tipos de Contenido

Base path: `/api/content-types`

| Metodo | Endpoint | Descripcion |
|--------|----------|-------------|
| `GET` | `/api/content-types` | Lista todos los tipos de contenido |
| `GET` | `/api/content-types/{id}` | Obtiene un tipo por ID |
| `POST` | `/api/content-types` | Crea un tipo de contenido |
| `PUT` | `/api/content-types/{id}` | Actualiza un tipo de contenido |
| `DELETE` | `/api/content-types/{id}` | Elimina un tipo de contenido |

Tipos sembrados al iniciar:

| ID | Nombre | Descripcion |
|----|--------|-------------|
| 1 | Clase grabada | Contenido asincronico disponible bajo demanda |
| 2 | Taller en vivo | Sesion interactiva transmitida en tiempo real |
| 3 | Conferencia | Presentacion educativa dirigida por un instructor |

---

### 4.3 Niveles de Dificultad

Base path: `/api/difficulty-levels`

| Metodo | Endpoint | Descripcion |
|--------|----------|-------------|
| `GET` | `/api/difficulty-levels` | Lista todos los niveles |
| `GET` | `/api/difficulty-levels/{id}` | Obtiene un nivel por ID |
| `POST` | `/api/difficulty-levels` | Crea un nivel |
| `PUT` | `/api/difficulty-levels/{id}` | Actualiza un nivel |
| `DELETE` | `/api/difficulty-levels/{id}` | Elimina un nivel |

Niveles sembrados al iniciar:

| ID | Nombre | Descripcion |
|----|--------|-------------|
| 1 | Principiante | Contenido introductorio para estudiantes nuevos |
| 2 | Intermedio | Contenido para estudiantes con conocimientos base |
| 3 | Avanzado | Contenido especializado de mayor complejidad |

---

## Guia de Conexion (Frontend)

### Flujo tipico del dashboard

```
┌─────────────────┐
│  Login Admin    │  POST /api/auth/admin/login
│  → obtiene JWT  │
└────────┬────────┘
         │ JWT (ADMIN_CONTENIDO)
         ▼
┌─────────────────────────────────────────────┐
│              Dashboard Admin                 │
│                                             │
│  ┌──────────────┐   ┌───────────────────┐   │
│  │ Buscar Cursos│   │  Ver Estadisticas │   │
│  │ con filtros  │   │  (4 graficas)     │   │
│  └──────────────┘   └───────────────────┘   │
│                                             │
│  ┌──────────────────────────────────────┐   │
│  │  Gestionar Catalogos                 │   │
│  │  Categorias / Tipos / Niveles        │   │
│  └──────────────────────────────────────┘   │
└─────────────────────────────────────────────┘
```

### 1. Login y guardar JWT

```javascript
async function loginAdmin(email, password) {
    const response = await fetch('http://localhost:8080/api/auth/admin/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password })
    });
    const result = await response.json();

    if (response.ok) {
        localStorage.setItem('adminToken', result.token);
        return result;
    }
    throw new Error(result.message || 'Credenciales invalidas');
}
```

### 2. Buscar cursos con filtros

```javascript
async function searchCourses({ title, contentTypeId, categoryId, difficultyLevelId, year } = {}) {
    const token = localStorage.getItem('adminToken');
    const params = new URLSearchParams();

    if (title)             params.append('title', title);
    if (contentTypeId)     params.append('contentTypeId', contentTypeId);
    if (categoryId)        params.append('categoryId', categoryId);
    if (difficultyLevelId) params.append('difficultyLevelId', difficultyLevelId);
    if (year)              params.append('year', year);

    const url = `http://localhost:8080/api/admin/dashboard/courses?${params}`;
    const response = await fetch(url, {
        headers: { 'Authorization': `Bearer ${token}` }
    });
    return response.json();
}
```

### 3. Obtener estadisticas para las graficas

```javascript
async function getDashboardStats() {
    const token = localStorage.getItem('adminToken');
    const response = await fetch('http://localhost:8080/api/admin/dashboard/stats', {
        headers: { 'Authorization': `Bearer ${token}` }
    });
    const stats = await response.json();

    // stats.topCategories          → grafica de categorias
    // stats.topDifficultyLevels    → grafica de niveles
    // stats.topCourses             → ranking de cursos
    // stats.subscriptionDistribution → grafica de suscripciones

    return stats;
}
```

### 4. Ejemplo con Axios

```javascript
import axios from 'axios';

const adminApi = axios.create({
    baseURL: 'http://localhost:8080/api'
});

// Agrega el token automaticamente
adminApi.interceptors.request.use(config => {
    const token = localStorage.getItem('adminToken');
    if (token) config.headers.Authorization = `Bearer ${token}`;
    return config;
});

// Login
const loginAdmin = (email, password) =>
    adminApi.post('/auth/admin/login', { email, password }).then(r => r.data);

// Dashboard
const searchCourses = (filters) =>
    adminApi.get('/admin/dashboard/courses', { params: filters }).then(r => r.data);

const getStats = () =>
    adminApi.get('/admin/dashboard/stats').then(r => r.data);

// Catalogos
const getCategories    = () => adminApi.get('/categories').then(r => r.data);
const createCategory   = (body) => adminApi.post('/categories', body).then(r => r.data);
const updateCategory   = (id, body) => adminApi.put(`/categories/${id}`, body).then(r => r.data);
const deleteCategory   = (id) => adminApi.delete(`/categories/${id}`);
```

---

## Entidades y Modelos

### Subscription (Suscripcion)

Representa la suscripcion de un estudiante. Usada en `subscriptionDistribution`.

| Campo | Tipo | Descripcion |
|-------|------|-------------|
| `id` | `Long` | Identificador |
| `student` | `Student` | Estudiante propietario (uno a uno) |
| `type` | `Enum` | `MENSUAL`, `TRIMESTRAL` o `ANUAL` |
| `status` | `Enum` | `ACTIVA`, `CANCELADA` o `EXPIRADA` |
| `startDate` | `LocalDate` | Fecha de inicio |
| `endDate` | `LocalDate` | Fecha de vencimiento |

### ViewingLog (Registro de Visualizacion)

Registra cada vez que un estudiante ve un curso. Usada para calcular `topCategories`, `topDifficultyLevels` y `topCourses`.

| Campo | Tipo | Descripcion |
|-------|------|-------------|
| `id` | `Long` | Identificador |
| `student` | `Student` | Estudiante que vio el contenido |
| `course` | `Course` | Curso visualizado |
| `viewedAt` | `LocalDateTime` | Fecha y hora de la visualizacion |

---

## Configuracion del Backend

### Requisitos

- **Java 17**
- **MySQL 8.0**
- **Maven 3.9+**
- **Docker** (para el contenedor de MySQL)

### Iniciar la base de datos

```bash
# Desde la raiz del proyecto (donde esta docker-compose.yml)
docker compose up -d
```

### Iniciar el backend

```bash
cd backend
./mvnw spring-boot:run
```

El servidor inicia en `http://localhost:8080`.

### Datos sembrados automaticamente

Al primer inicio el sistema crea:

- Roles: `ESTUDIANTE`, `ADMIN_CONTENIDO`
- Usuario admin: `admin@learnflow.com` / `admin123`
- 3 tipos de contenido, 3 categorias, 3 niveles de dificultad

---

## Pruebas rapidas con cURL

### Login Admin

```bash
curl -X POST http://localhost:8080/api/auth/admin/login \
  -H "Content-Type: application/json" \
  -d '{"email": "admin@learnflow.com", "password": "admin123"}'
```

### Buscar todos los cursos

```bash
curl http://localhost:8080/api/admin/dashboard/courses \
  -H "Authorization: Bearer TOKEN"
```

### Buscar cursos con filtros

```bash
curl "http://localhost:8080/api/admin/dashboard/courses?title=React&categoryId=1" \
  -H "Authorization: Bearer TOKEN"
```

### Obtener estadisticas

```bash
curl http://localhost:8080/api/admin/dashboard/stats \
  -H "Authorization: Bearer TOKEN"
```

### Listar categorias

```bash
curl http://localhost:8080/api/categories \
  -H "Authorization: Bearer TOKEN"
```

### Crear categoria

```bash
curl -X POST http://localhost:8080/api/categories \
  -H "Authorization: Bearer TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"name": "Marketing Digital", "description": "Estrategias de marketing en linea", "active": true}'
```

---

## Resumen de Estados HTTP

| Codigo | Significado |
|--------|-------------|
| `200 OK` | Operacion exitosa |
| `400 Bad Request` | Datos invalidos o nombre duplicado en catalogo |
| `401 Unauthorized` | Token faltante, invalido, expirado o credenciales incorrectas |
| `403 Forbidden` | El usuario no tiene rol `ADMIN_CONTENIDO` |
| `404 Not Found` | Recurso no encontrado (ID inexistente) |
| `409 Conflict` | El nombre del elemento ya existe en el catalogo |
