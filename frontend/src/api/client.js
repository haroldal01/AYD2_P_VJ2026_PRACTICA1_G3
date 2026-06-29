import axios from 'axios';

// Cliente HTTP central. La URL base sale de VITE_API_URL (.env).
const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080/api',
});

// Interceptor de request: agrega el JWT guardado en localStorage a cada peticion.
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Interceptor de response: si el token expiro (401/403), limpia sesion y manda a login.
api.interceptors.response.use(
  (response) => response,
  (error) => {
    const status = error.response?.status;
    if (status === 401 || status === 403) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      if (!window.location.pathname.startsWith('/login')) {
        window.location.href = '/login';
      }
    }
    return Promise.reject(error);
  }
);

// Helper: extrae un mensaje de error legible de la respuesta del backend.
export function extractError(error) {
  const data = error.response?.data;
  if (data?.errors?.length) {
    return data.errors.join('\n');
  }
  return data?.message || error.message || 'Ocurrio un error inesperado';
}

export default api;
