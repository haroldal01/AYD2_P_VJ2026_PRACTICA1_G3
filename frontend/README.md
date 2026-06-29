# Frontend — LearnFlow

Interfaz web de estudiante y administrador construida con **React + Vite**.
Se conecta al backend de Spring Boot documentado en
[`../API_STUDENTS.md`](../API_STUDENTS.md) y [`../API_CONTENT.md`](../API_CONTENT.md).

## Requisitos

- Node.js 18+ (probado con Node 24)
- Backend corriendo en `http://localhost:8080`

## Configuracion

La URL del backend se define en `.env`:

```
VITE_API_URL=http://localhost:8080/api
```

## Comandos

```bash
npm install      # instalar dependencias
npm run dev      # servidor de desarrollo (http://localhost:5173)
npm run build    # build de produccion en dist/
npm run preview  # previsualizar el build
```

## Pantallas

| Ruta | Pantalla | Backend |
| ---- | -------- | ------- |
| `/login` | Inicio de sesion | Real (`/auth/login`) |
| `/registro` | Registro de estudiante | Real (`/auth/register`) |
| `/perfil` | Perfil / actualizacion de datos | Real (`/students/me`) |
| `/inicio` | Inicio: recomendaciones + top 10 | **Mock** (pendiente backend de reproduccion) |
| `/suscripciones` | Contratar / renovar / cancelar membresia | **Mock** (pendiente backend de suscripciones) |
| `/admin/contenido` | CRUD de cursos, tipos, categorias y niveles | Real (`/courses`, `/content-types`, `/categories`, `/difficulty-levels`) |

> Las pantallas marcadas como **Mock** usan datos simulados en
> `src/mocks/index.js` porque sus endpoints aun no existen en el backend
> (tareas de suscripciones y de reproduccion/bitacora). Cuando esos endpoints
> esten listos, basta con reemplazar las llamadas de `src/mocks` por peticiones
> reales con el cliente `src/api/client.js`.

## Autenticacion

El token JWT se guarda en `localStorage` y se adjunta automaticamente a cada
peticion mediante un interceptor de Axios (`src/api/client.js`). Si el backend
responde `401`/`403`, la sesion se limpia y se redirige a `/login`.

## Estructura

```
src/
  api/client.js          Cliente Axios + interceptores
  context/AuthContext.jsx Estado global de sesion
  components/            Navbar, Layout, ProtectedRoute
  pages/                 Una pantalla por archivo
  mocks/index.js         Datos simulados temporales
```
