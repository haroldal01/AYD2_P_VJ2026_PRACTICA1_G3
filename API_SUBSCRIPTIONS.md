# API de Suscripciones

Endpoints para contratar, renovar, cancelar y consultar membresias de estudiantes.

Base URL:

```txt
http://localhost:8080/api/subscriptions
```

Todas las rutas requieren JWT de estudiante.

## Planes disponibles

| Plan | Duracion | Precio |
| ---- | -------- | ------ |
| `MENSUAL` | 30 dias | Q75.00 |
| `TRIMESTRAL` | 90 dias | Q200.00 |
| `ANUAL` | 365 dias | Q700.00 |

Estos planes se crean mediante el patron Factory Method en el paquete:

```txt
backend/src/main/java/com/learnflow/factory
```

## Contratar membresia

```http
POST /api/subscriptions/{studentId}/contract
Authorization: Bearer <token>
Content-Type: application/json
```

Body:

```json
{
  "planType": "MENSUAL"
}
```

Respuesta:

```json
{
  "id": 1,
  "studentId": 1,
  "studentName": "Estudiante Demo",
  "planType": "MENSUAL",
  "status": "ACTIVA",
  "startDate": "2026-06-28",
  "endDate": "2026-07-28",
  "price": 75.00
}
```

## Renovar membresia

```http
POST /api/subscriptions/{studentId}/renew
Authorization: Bearer <token>
Content-Type: application/json
```

Body:

```json
{
  "planType": "ANUAL"
}
```

Renueva la membresia activa y actualiza fecha de vencimiento, plan y precio.

## Cancelar membresia

```http
POST /api/subscriptions/{studentId}/cancel
Authorization: Bearer <token>
```

Cambia la membresia activa a estado `CANCELADA`.

## Validar membresia activa

```http
GET /api/subscriptions/{studentId}/active
Authorization: Bearer <token>
```

Respuesta:

```json
true
```

## Historial de suscripciones

```http
GET /api/subscriptions/{studentId}
Authorization: Bearer <token>
```

Respuesta:

```json
[
  {
    "id": 1,
    "studentId": 1,
    "studentName": "Estudiante Demo",
    "planType": "MENSUAL",
    "status": "ACTIVA",
    "startDate": "2026-06-28",
    "endDate": "2026-07-28",
    "price": 75.00
  }
]
```

## Factory Method

La logica de suscripciones usa Factory Method para evitar instanciar planes
concretos directamente desde el servicio.

Piezas principales:

- `Membership`: producto base.
- `MonthlyMembership`, `QuarterlyMembership`, `AnnualMembership`: productos concretos.
- `MembershipFactory`: fabrica base.
- `MonthlyMembershipFactory`, `QuarterlyMembershipFactory`, `AnnualMembershipFactory`: fabricas concretas.
- `MembershipFactoryProvider`: selecciona la fabrica correcta segun `PlanType`.
- `SubscriptionService`: usa la fabrica para contratar o renovar.
