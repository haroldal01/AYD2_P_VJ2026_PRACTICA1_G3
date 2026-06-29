import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// Configuracion de Vite. El servidor de desarrollo corre en el puerto 5173.
// El backend de Spring Boot corre en http://localhost:8080 (ver VITE_API_URL en .env).
export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    open: true,
  },
});
