# Modulo de Reproduccion, Bitacora y Recomendaciones

**Responsable:** Kenneth Isai Aquino Ortiz - 202100678
**Rama:** `feature/playback-history`

---

## Descripcion

Este modulo implementa la reproduccion de contenido educativo, el registro de bitacora de visualizacion,
la generacion de recomendaciones personalizadas y el ranking de los cursos mas vistos en LearnFlow.

---

## Endpoints

| Metodo | Endpoint | Auth | Descripcion |
|---|---|---|---|
| `POST` | `/api/playback/play/{courseId}` | JWT | Reproducir contenido |
| `GET` | `/api/playback/history` | JWT | Historial del estudiante |
| `GET` | `/api/playback/recommendations` | JWT | Recomendaciones personalizadas |
| `GET` | `/api/playback/top10` | JWT | Top 10 cursos mas vistos |

---

## Flujo de prueba completo

### 1. Iniciar backend

```powershell
cd backend
./mvnw.cmd spring-boot:run
```

### 2. Registrar estudiante

```powershell
$body = '{
    "fullName": "Kenneth Aquino",
    "dateOfBirth": "2000-01-15",
    "email": "kenneth@correo.com",
    "password": "MiClave123",
    "nit": "12345678-9",
    "cardNumber": "1234567890123456",
    "cardExpiry": "2028-12-31",
    "photoUrl": "https://ejemplo.com/foto.jpg"
}'

$r = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/register" `
    -Method Post -Body $body -ContentType "application/json"

$token = $r.token
echo $token
```

### 3. Contratar membresia 

```powershell
$headers = @{ Authorization = "Bearer $token" }
$planBody = '{"planType": "MENSUAL"}'

Invoke-RestMethod -Uri "http://localhost:8080/api/subscriptions/contract" `
    -Method Post -Body $planBody -ContentType "application/json" -Headers $headers
```

### 4. Crear contenido de prueba

```powershell
# Tipo de contenido
Invoke-RestMethod -Uri "http://localhost:8080/api/content-types" -Method Post `
    -Body '{"name":"Clase grabada"}' -ContentType "application/json"

# Categoria
Invoke-RestMethod -Uri "http://localhost:8080/api/categories" -Method Post `
    -Body '{"name":"Programacion"}' -ContentType "application/json"

# Dificultad
Invoke-RestMethod -Uri "http://localhost:8080/api/difficulty-levels" -Method Post `
    -Body '{"name":"Principiante"}' -ContentType "application/json"

# Curso
$curso = Invoke-RestMethod -Uri "http://localhost:8080/api/courses" -Method Post -Body '{
    "title": "Spring Boot desde Cero",
    "productionYear": 2026,
    "instructor": "Juan Perez",
    "shortSummary": "Curso introductorio",
    "description": "Aprende Spring Boot",
    "mediaUrl": "https://ejemplo.com/video.mp4",
    "contentTypeId": 1,
    "categoryId": 1,
    "difficultyLevelId": 1
}' -ContentType "application/json"

$courseId = $curso.id
echo "Curso creado con ID: $courseId"
```

### 5. Probar reproduccion

```powershell
$play = Invoke-RestMethod -Uri "http://localhost:8080/api/playback/play/$courseId" `
    -Method Post -Headers $headers
echo $play
```
→ `Reproduccion registrada exitosamente`

### 6. Ver historial

```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/playback/history" `
    -Method Get -Headers $headers
```

### 7. Ver recomendaciones

```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/playback/recommendations" `
    -Method Get -Headers $headers
```

### 8. Ver top 10

```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/playback/top10" `
    -Method Get -Headers $headers
```

---

## Casos de error esperados

| Escenario | HTTP | Mensaje |
|---|---|---|
| Sin token | `401` | Unauthorized |
| Curso inexistente | `400` | Contenido no encontrado |
| Sin membresia activa | `409` | No tienes una membresia activa |
| Sin historial | `200` | `[]` |
