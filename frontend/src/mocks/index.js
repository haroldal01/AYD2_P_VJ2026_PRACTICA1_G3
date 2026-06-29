// =====================================================================
// DATOS MOCK (temporales)
// ---------------------------------------------------------------------
// Estas funciones simulan endpoints que AUN NO existen en el backend:
//   - Suscripciones / membresias  -> tarea de Madeline (feature/subscriptions-factory)
//   - Reproduccion / recomendaciones / top 10 -> tarea de Kenneth (feature/playback-history)
//
// Cuando esos endpoints existan, reemplazar estas llamadas por peticiones
// reales con el cliente `api`. La forma de los datos intenta anticipar la
// respuesta real para minimizar cambios.
// =====================================================================

const delay = (ms) => new Promise((resolve) => setTimeout(resolve, ms));

// ---- Suscripciones ----
export const SUBSCRIPTION_PLANS = [
  { id: 'MENSUAL', name: 'Mensual', price: 49, months: 1, description: 'Acceso completo por 1 mes.' },
  { id: 'TRIMESTRAL', name: 'Trimestral', price: 129, months: 3, description: 'Ahorra contratando 3 meses.' },
  { id: 'ANUAL', name: 'Anual', price: 449, months: 12, description: 'El mejor precio: 12 meses.' },
];

// Estado de membresia simulado en localStorage para que persista en la demo.
function readMembership() {
  const raw = localStorage.getItem('mock_membership');
  return raw ? JSON.parse(raw) : null;
}
function writeMembership(value) {
  if (value) localStorage.setItem('mock_membership', JSON.stringify(value));
  else localStorage.removeItem('mock_membership');
}

export async function getMembership() {
  await delay(250);
  return readMembership();
}

export async function subscribe(planId) {
  await delay(400);
  const plan = SUBSCRIPTION_PLANS.find((p) => p.id === planId);
  const start = new Date();
  const end = new Date();
  end.setMonth(end.getMonth() + plan.months);
  const membership = {
    plan: plan.id,
    planName: plan.name,
    startDate: start.toISOString().slice(0, 10),
    endDate: end.toISOString().slice(0, 10),
    active: true,
  };
  writeMembership(membership);
  return membership;
}

export async function renewMembership() {
  await delay(400);
  const current = readMembership();
  if (!current) throw new Error('No hay membresia para renovar');
  const plan = SUBSCRIPTION_PLANS.find((p) => p.id === current.plan);
  const end = new Date(current.endDate);
  end.setMonth(end.getMonth() + plan.months);
  const updated = { ...current, endDate: end.toISOString().slice(0, 10), active: true };
  writeMembership(updated);
  return updated;
}

export async function cancelMembership() {
  await delay(400);
  const current = readMembership();
  if (!current) return null;
  const updated = { ...current, active: false };
  writeMembership(updated);
  return updated;
}

// ---- Recomendaciones y ranking (pantalla de inicio) ----
const MOCK_COURSES = [
  { id: 1, title: 'Introduccion a Spring Boot', instructor: 'Ana Morales', category: 'Programacion', views: 1240 },
  { id: 2, title: 'Diseno UI con Figma', instructor: 'Luis Castro', category: 'Diseno', views: 1110 },
  { id: 3, title: 'Fundamentos de React', instructor: 'Maria Lopez', category: 'Programacion', views: 980 },
  { id: 4, title: 'Marketing Digital', instructor: 'Jorge Ruiz', category: 'Negocios', views: 870 },
  { id: 5, title: 'Bases de Datos SQL', instructor: 'Ana Morales', category: 'Programacion', views: 845 },
  { id: 6, title: 'Branding para Startups', instructor: 'Sofia Diaz', category: 'Negocios', views: 760 },
  { id: 7, title: 'CSS Moderno', instructor: 'Maria Lopez', category: 'Diseno', views: 690 },
  { id: 8, title: 'Java Avanzado', instructor: 'Pedro Gomez', category: 'Programacion', views: 640 },
  { id: 9, title: 'Finanzas para Emprendedores', instructor: 'Jorge Ruiz', category: 'Negocios', views: 590 },
  { id: 10, title: 'Prototipado Rapido', instructor: 'Sofia Diaz', category: 'Diseno', views: 540 },
  { id: 11, title: 'APIs REST con Node', instructor: 'Pedro Gomez', category: 'Programacion', views: 500 },
];

export async function getTopCourses() {
  await delay(250);
  return [...MOCK_COURSES].sort((a, b) => b.views - a.views).slice(0, 10);
}

// Recomendaciones segun la categoria mas vista del estudiante (simulada).
export async function getRecommendations() {
  await delay(250);
  const favoriteCategory = 'Programacion';
  return {
    favoriteCategory,
    courses: MOCK_COURSES.filter((c) => c.category === favoriteCategory).slice(0, 4),
  };
}
