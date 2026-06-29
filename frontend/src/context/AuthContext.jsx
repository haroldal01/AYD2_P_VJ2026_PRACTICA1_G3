import { createContext, useContext, useState, useCallback } from 'react';
import api from '../api/client';

const AuthContext = createContext(null);

// Provee el estado de autenticacion (usuario + token) a toda la app.
export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    const stored = localStorage.getItem('user');
    return stored ? JSON.parse(stored) : null;
  });

  // Guarda la sesion devuelta por register/login.
  const persistSession = useCallback((data) => {
    localStorage.setItem('token', data.token);
    localStorage.setItem('user', JSON.stringify(data));
    setUser(data);
  }, []);

  const login = useCallback(async (email, password) => {
    let response;
    try {
      response = await api.post('/auth/login', { email, password });
    } catch (studentError) {
      response = await api.post('/auth/admin/login', { email, password });
    }
    const { data } = response;
    persistSession(data);
    return data;
  }, [persistSession]);

  const register = useCallback(async (form) => {
    const { data } = await api.post('/auth/register', form);
    persistSession(data);
    return data;
  }, [persistSession]);

  const logout = useCallback(() => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    setUser(null);
  }, []);

  const value = {
    user,
    isAuthenticated: !!user,
    isAdmin: user?.role === 'ADMIN_CONTENIDO',
    login,
    register,
    logout,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

// Hook de conveniencia para consumir el contexto de autenticacion.
export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) {
    throw new Error('useAuth debe usarse dentro de <AuthProvider>');
  }
  return ctx;
}
