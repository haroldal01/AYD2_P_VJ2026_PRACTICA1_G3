# LearnFlow

**PONDERACIÓN:** 5
**Horas Aproximadas:** 30

Universidad San Carlos de Guatemala
Facultad de Ingeniería
Ingeniería en Ciencias y Sistemas
Análisis y Diseño de Sistemas 2

---

## Índice

- [Competencias](#competencias)
- [Objetivos](#objetivos)
  - [Generales](#generales)
  - [Específicos](#específicos)
- [Descripción / Enunciado](#descripción--enunciado)
- [Instrucciones](#instrucciones)
- [Documentación](#documentación)
- [Entregables](#entregables)
- [Consideraciones](#consideraciones)
- [Cronograma](#cronograma)
- [Valores](#valores)
- [Rúbrica de Calificación](#rúbrica-de-calificación)
- [Preguntas](#preguntas)

---

## Competencias

Identifica y describe conceptos de frameworks aplicando casos de uso y características comunes para seleccionar adecuadamente un framework de acuerdo al proyecto.

---

## Objetivos

### Generales

- Familiarizarse con los frameworks de desarrollo y los patrones de diseño
- Comprender la importancia y la organización de trabajo en equipo
- Establecer un flujo de trabajo estructurado y bien definido que facilite la colaboración entre equipos

### Específicos

- Utilizar un framework de desarrollo e implementar patrones de diseño
- Implementar la estrategia de branching git-flow para el manejo de control de versiones
- Identificar de los requerimientos funcionales y no funcionales de soluciones de software

---

## Descripción / Enunciado

LearnFlow S.A., una compañía especializada en el sector del e-learning, busca implementar una plataforma innovadora orientada a la distribución de cursos mediante video. El propósito fundamental es establecer un ecosistema integral que facilite la gestión de alumnos, el catálogo de cursos, los modelos de suscripción y el visor de contenido multimedia.

Este sistema debe estructurarse considerando diferentes perfiles de acceso, cada uno con un conjunto de permisos y herramientas dedicadas. La arquitectura de la aplicación debe habilitar el registro de usuarios interesados en adquirir un plan para consumir clases asíncronas, talleres y conferencias, a la par de proveer un entorno de administración centralizado para el control del negocio.

### Estudiante

El perfil de estudiante corresponde a los usuarios finales que consumen el material didáctico. El sistema debe capturar y almacenar información clave como:

- Nombre completo
- Fecha de nacimiento
- Correo electrónico (este debe ser único en el sistema)
- Contraseña
- NIT
- Número de tarjeta y su fecha de vencimiento
- Fotografía

#### Registro y Actualización

El módulo debe incluir un formulario estructurado para el alta inicial, así como un panel de configuración donde el alumno pueda modificar su información personal y actualizar sus métodos de pago.

#### Suscripciones

Para visualizar los cursos, es requisito indispensable que el alumno adquiera un plan de suscripción activo. El sistema soportará tres modalidades tarifarias: Mensual, Trimestral y Anual. Adicionalmente, el alumno tendrá total libertad para gestionar la renovación o aplicar la cancelación de su membresía en el momento que lo decida.

#### Bitácora y Página de inicio del estudiante

La plataforma debe registrar un historial detallado del consumo de cada usuario (bitácora de visualización) para alimentar un motor de sugerencias. Al iniciar sesión con una membresía activa, el panel principal presentará recomendaciones personalizadas basadas en la categoría temática de mayor interés del alumno, junto con un ranking destacando los 10 cursos con mayor tráfico en toda la red.

#### Reproducción de contenido

Se requiere integrar un reproductor multimedia nativo o embebido que haga posible la visualización fluida y controlada del material audiovisual educativo.

### Administrador de contenido

Este rol tiene la responsabilidad operativa de mantener el catálogo de la plataforma actualizado. Sus atribuciones de mantenimiento abarcan:

- Gestión de Tipos de contenido (Ej. Clase grabada, Taller en vivo, Conferencia).
- Gestión de Categorías temáticas (Ej. Programación, Diseño, Negocios).
- Gestión de Niveles de Dificultad (Principiante, Intermedio, Avanzado).
- Gestión del Contenido per se (Ingreso del título, año de producción, instructor, resumen breve y descripción completa del curso).

#### Dashboard de administrador de contenido

El administrador dispondrá de un panel de control avanzado como su pantalla de inicio. Este tablero de mando permitirá buscar cursos específicos por título y aplicar filtros cruzados por tipo, categoría, dificultad o año de lanzamiento. Además, deberá renderizar las siguientes gráficas analíticas:

- Top 3 de Categorías con mayor cantidad de reproducciones.
- Top 3 de Niveles de Dificultad más cursados.
- Top 10 de Cursos más visualizados globalmente.
- Distribución cuantitativa de estudiantes según su tipo de suscripción.

### Autenticación en la plataforma

El control de acceso a la plataforma para cualquier perfil (Estudiante o Administrador) estará protegido mediante el uso de credenciales estándar: correo electrónico y contraseña.

---

## Instrucciones

### 1. Selección del Patrón y Framework

a. Seleccionar un framework de programación
b. Elegir un patrón de diseño a implementar

### 2. Implementación del Demo

a. Desarrollar una aplicación web sencilla, que tenga frontend, backend y persistencia de información por medio de una base de datos. La aplicación debe hacer uso del patrón de diseño y el framework elegido, tomando en cuenta los requerimientos funcionales y no funcionales.

### 3. Estrategia de Branching

Se debe realizar la estrategia de branching git-flow, en donde se encuentran las ramas principales llamadas `main` y `develop`. Por otra parte, están las ramas de soporte que son `feature`, `hotfix`, etc. Cada commit debe llevar el formato **"#carnet: mensaje"** (eg., `12345678: agregar botón`). Para cada feature se hace una rama a partir de `develop`, y al terminar la feature, se hace merge a `develop`.

> **NOTA:** No debe eliminar las ramas creadas para las features.

Cuando se tengan varias features en develop, se hace merge a `main`.

---

## Documentación

Se debe elaborar un **Documento de Decisión de Arquitectura (DDA)** que fungirá como el Manual Técnico del proyecto. Este debe integrarse en formato Markdown dentro del repositorio e incluir:

1. **Core del negocio:** Descripción detallada del negocio tomando en cuenta sus Casos de Uso (CDU) expandidos.
2. **Drivers Funcionales:** Definición clara de las funcionalidades que el sistema debe ejecutar, desglosando las características puntuales.
3. **Drivers No Funcionales:** Especificación de los atributos de calidad (rendimiento, seguridad, escalabilidad) y cómo estos impactan las decisiones de algoritmos, tecnologías o infraestructura.
4. **Justificación Arquitectónica:** Exposición de los motivos por los cuales se eligió el framework y el patrón de diseño. Se deben utilizar diagramas UML para respaldar la elección.

---

## Entregables

- Link a repositorio privado en GitHub a través de UEDI. Utilizar el formato:
  `AYD2_<SECCION>_VJ2026_PRACTICA1_G<#>` (eg., `AYD2_P_VJ2026_PRACTICA1_G4`)
- No se aceptarán entregas fuera de fecha ni por otro medio que no sea UEDI.
- Documentación en formato markdown, dentro del repositorio, no se aceptará otro formato.
- Agregar al auxiliar:
  - `luisrene50`

---

## Consideraciones

Recordar que la participación de cada integrante del grupo es indispensable por lo que se solicita que todo se trabaje en un repositorio de GitHub, contando cada integrante con al menos un commit **VÁLIDO**. Se revisará el contenido de los commits.

---

## Cronograma

| Tarea | Fecha |
|---|---|
| Asignación de la práctica / Entrega del enunciado | 23 de junio 2026 |
| Fecha límite de entrega | 27 de junio 2026 |
| Fecha de calificación | 28 de junio 2026 |

---

## Valores

En el desarrollo de la práctica, se espera que cada estudiante demuestre honestidad académica y profesionalismo. Por lo tanto, se establecen los siguientes principios:

### 1. Originalidad del Trabajo

- Cada estudiante o equipo debe desarrollar su propio código y/o documentación, aplicando los conocimientos adquiridos en el curso.

### 2. Prohibición de Copias y Plagio

- Si se detecta la copia total o parcial del código, documentación o cualquier otro entregable, la calificación será de **0 puntos**.
- Esto incluye la reproducción de código entre compañeros, la reutilización de proyectos de semestres anteriores o el uso de código externo sin la debida referencia.

### 3. Uso Responsable de Recursos Externos

- El uso de bibliotecas, frameworks y ejemplos de código externos está permitido, siempre y cuando se referencien correctamente y se comprendan plenamente. (Consultar con el catedrático su política)

### 4. Revisión y Detección de Plagio

- Se podrán utilizar herramientas automatizadas y revisiones manuales para identificar similitudes en los proyectos.
- En caso de sospecha, el estudiante deberá justificar su código y demostrar su desarrollo individual o en equipo. Si este extremo no es comprobable la calificación será de **0 puntos**.

Al detectarse estos aspectos se informará al catedrático del curso quien realizará las acciones que considere oportunas.

---

## Rúbrica de Calificación

| Descripción de Ponderación | Valor | Observación | Punteo |
|---|---|---|---|
| **DOCUMENTACIÓN** | **35** | | |
| Elección y justificación de Framework | 10 | | |
| Elección y justificación de Patrón de diseño | 5 | | |
| Core del negocio (Hasta CDU expandidos) | 10 | | |
| Drivers funcionales y no funcionales | 5 | | |
| **Implementación** | **20** | | |
| Implementación correcta de framework | 10 | | |
| Implementación correcta de patrón de diseño | 10 | | |
| **Funcionalidad** | **35** | | |
| Registro del estudiante | 2 | | |
| Actualización de datos del estudiante | 2 | | |
| Suscripción y Renovación | 3 | | |
| Gestión tipo, clasificación y género de contenido | 6 | | |
| Gestión de contenido | 4 | | |
| Reproducción de contenido | 3 | | |
| Bitácora de reproducciones del estudiante | 4 | | |
| Página de inicio del estudiante | 3 | | |
| Dashboard - búsqueda de contenido | 5 | | |
| Dashboard - Gráficas de contenido | 5 | | |
| **Estrategia de Branching** | **4** | | |
| Creación de ramas principales | 1 | | |
| Realización correcta de la estrategia del branching | 2 | | |
| Utilización formato de commit solicitado | 1 | | |
| **Habilidades Blandas y Evaluación Integral** | **10** | | |
| Trabajo en equipo (evidencias de reuniones) | 5 | | |

> **Nota:** En el documento original, la sección de "Habilidades Blandas y Evaluación Integral" tiene un valor total de 10 puntos, de los cuales 5 corresponden a "Trabajo en equipo (evidencias de reuniones)". El desglose de los 5 puntos restantes de esta sección no se especifica en el documento fuente (posiblemente corresponde a la sección de "Preguntas" descrita abajo).

---

## Preguntas

**Total: 5**

| Pregunta | Valor |
|---|---|
| Pregunta 1 | 1 |
| Pregunta 2 | 1 |
| Pregunta 3 | 1 |
| Pregunta 4 | 1 |

> **Nota:** El documento original lista únicamente 4 preguntas individuales (cada una con valor de 1 punto, sumando 4), aunque el total indicado en la tabla es 5. Esta discrepancia proviene del documento fuente y no fue corregida para mantener fidelidad al original.
