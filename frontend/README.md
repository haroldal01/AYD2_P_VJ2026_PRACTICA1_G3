# Frontend - LearnFlow

Interfaz web de estudiante y administrador construida con React + Vite.
Se conecta al backend de Spring Boot documentado en:

- [API_STUDENTS.md](../API_STUDENTS.md)
- [API_CONTENT.md](../API_CONTENT.md)
- [API_ADMIN_DASHBOARD.md](../API_ADMIN_DASHBOARD.md)
- [API_SUBSCRIPTIONS.md](../API_SUBSCRIPTIONS.md)
- [backend/PLAYBACK_README.md](../backend/PLAYBACK_README.md)

## Requisitos

- Node.js 18+
- Backend corriendo en `http://localhost:8080`

## Configuracion

La URL del backend se define en `.env`:

```env
VITE_API_URL=http://localhost:8080/api
```

Si no existe `.env`, el cliente usa `http://localhost:8080/api` por defecto.

## Comandos

```bash
npm install
npm run dev
npm run build
npm run preview
```

## Pantallas

| Ruta | Pantalla | Backend |
| ---- | -------- | ------- |
| `/login` | Inicio de sesion | Real (`/auth/login`) |
| `/registro` | Registro de estudiante | Real (`/auth/register`) |
| `/perfil` | Perfil / actualizacion de datos | Real (`/students/me`) |
| `/inicio` | Recomendaciones, top 10, historial y reproductor | Real (`/playback/*`) |
| `/suscripciones` | Contratar, renovar, cancelar e historial de membresias | Real (`/subscriptions/*`) |
| `/admin/dashboard` | Estadisticas y busqueda administrativa | Real (`/admin/dashboard/*`) |
| `/admin/contenido` | CRUD de cursos, tipos, categorias y niveles | Real (`/courses`, `/content-types`, `/categories`, `/difficulty-levels`) |

## Autenticacion

El token JWT se guarda en `localStorage` y se adjunta automaticamente a cada
peticion mediante el interceptor de Axios en `src/api/client.js`. Si el backend
responde `401` o `403`, la sesion se limpia y se redirige a `/login`.

Los administradores son enviados a `/admin/dashboard`; los estudiantes son
enviados a `/inicio`.

## Estructura

```txt
src/
  api/client.js            Cliente Axios + interceptores
  context/AuthContext.jsx  Estado global de sesion
  components/              Navbar, Layout, ProtectedRoute
  pages/                   Pantallas principales
```
