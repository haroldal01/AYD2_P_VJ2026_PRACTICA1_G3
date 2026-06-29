import { Navigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

// Envuelve rutas que requieren sesion iniciada.
// Si adminOnly es true, ademas exige el rol ADMIN_CONTENIDO.
export default function ProtectedRoute({ children, adminOnly = false }) {
  const { isAuthenticated, isAdmin } = useAuth();

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }
  if (adminOnly && !isAdmin) {
    return <Navigate to="/inicio" replace />;
  }
  return children;
}
