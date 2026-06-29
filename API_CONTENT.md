# API de Gestion de Contenido - LearnFlow

## Descripcion

API REST para administrar el catalogo educativo de LearnFlow. Cubre los mantenimientos solicitados en la practica para tipos de contenido, categorias, niveles de dificultad y cursos.

Los endpoints requieren JWT porque la configuracion actual protege todas las rutas fuera de `/api/auth/**`.

---

## Tipos de Contenido

Base path:

```txt
/api/content-types
```

Ejemplo de request:

```json
{
  "name": "Clase grabada",
  "description": "Contenido asincronico disponible bajo demanda",
  "active": true
}
```

Endpoints:

| Metodo | Endpoint | Descripcion |
| ------ | -------- | ----------- |
| GET | `/api/content-types` | Lista tipos de contenido |
| GET | `/api/content-types/{id}` | Obtiene un tipo de contenido |
| POST | `/api/content-types` | Crea un tipo de contenido |
| PUT | `/api/content-types/{id}` | Actualiza un tipo de contenido |
| DELETE | `/api/content-types/{id}` | Elimina un tipo de contenido |

---

## Categorias

Base path:

```txt
/api/categories
```

Ejemplo de request:

```json
{
  "name": "Programacion",
  "description": "Cursos relacionados con desarrollo de software",
  "active": true
}
```

Endpoints:

| Metodo | Endpoint | Descripcion |
| ------ | -------- | ----------- |
| GET | `/api/categories` | Lista categorias |
| GET | `/api/categories/{id}` | Obtiene una categoria |
| POST | `/api/categories` | Crea una categoria |
| PUT | `/api/categories/{id}` | Actualiza una categoria |
| DELETE | `/api/categories/{id}` | Elimina una categoria |

---

## Niveles de Dificultad

Base path:

```txt
/api/difficulty-levels
```

Ejemplo de request:

```json
{
  "name": "Principiante",
  "description": "Contenido introductorio para estudiantes nuevos",
  "active": true
}
```

Endpoints:

| Metodo | Endpoint | Descripcion |
| ------ | -------- | ----------- |
| GET | `/api/difficulty-levels` | Lista niveles de dificultad |
| GET | `/api/difficulty-levels/{id}` | Obtiene un nivel |
| POST | `/api/difficulty-levels` | Crea un nivel |
| PUT | `/api/difficulty-levels/{id}` | Actualiza un nivel |
| DELETE | `/api/difficulty-levels/{id}` | Elimina un nivel |

---

## Cursos / Contenido Educativo

Base path:

```txt
/api/courses
```

Ejemplo de request:

```json
{
  "title": "Introduccion a Spring Boot",
  "productionYear": 2026,
  "instructor": "Ana Morales",
  "shortSummary": "Conceptos basicos para crear APIs REST con Spring Boot.",
  "description": "Curso introductorio sobre controladores, servicios, repositorios y persistencia con JPA.",
  "mediaUrl": "https://learnflow.local/media/spring-boot.mp4",
  "contentTypeId": 1,
  "categoryId": 1,
  "difficultyLevelId": 1,
  "active": true
}
```

Endpoints:

| Metodo | Endpoint | Descripcion |
| ------ | -------- | ----------- |
| GET | `/api/courses` | Lista cursos |
| GET | `/api/courses?title=spring` | Busca cursos por titulo |
| GET | `/api/courses/{id}` | Obtiene un curso |
| POST | `/api/courses` | Crea un curso |
| PUT | `/api/courses/{id}` | Actualiza un curso |
| DELETE | `/api/courses/{id}` | Elimina un curso |

Campos principales del curso:

| Campo | Descripcion |
| ----- | ----------- |
| `title` | Titulo del contenido |
| `productionYear` | Anio de produccion |
| `instructor` | Instructor responsable |
| `shortSummary` | Resumen breve |
| `description` | Descripcion completa |
| `mediaUrl` | URL del recurso multimedia |
| `contentTypeId` | Tipo: clase grabada, taller, conferencia, etc. |
| `categoryId` | Categoria tematica |
| `difficultyLevelId` | Nivel de dificultad |
| `active` | Estado del contenido |

---

## Datos Iniciales

Al iniciar el backend se crean automaticamente estos datos si no existen:

Tipos:

- Clase grabada
- Taller en vivo
- Conferencia

Categorias:

- Programacion
- Diseno
- Negocios

Niveles:

- Principiante
- Intermedio
- Avanzado
