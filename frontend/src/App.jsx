import { Routes, Route, Navigate } from 'react-router-dom';
import { useAuth } from './context/AuthContext';
import ProtectedRoute from './components/ProtectedRoute';
import Layout from './components/Layout';
import Login from './pages/Login';
import Register from './pages/Register';
import Home from './pages/Home';
import Profile from './pages/Profile';
import Subscriptions from './pages/Subscriptions';
import ContentAdmin from './pages/ContentAdmin';
import AdminDashboard from './pages/AdminDashboard';

// Define todas las rutas de la aplicacion.
export default function App() {
  const { isAuthenticated } = useAuth();

  return (
    <Routes>
      {/* Publicas */}
      <Route
        path="/login"
        element={isAuthenticated ? <Navigate to="/inicio" replace /> : <Login />}
      />
      <Route
        path="/registro"
        element={isAuthenticated ? <Navigate to="/inicio" replace /> : <Register />}
      />

      {/* Privadas: comparten el Layout con navbar */}
      <Route
        element={
          <ProtectedRoute>
            <Layout />
          </ProtectedRoute>
        }
      >
        <Route path="/inicio" element={<Home />} />
        <Route path="/perfil" element={<Profile />} />
        <Route path="/suscripciones" element={<Subscriptions />} />
        <Route
          path="/admin/dashboard"
          element={
            <ProtectedRoute adminOnly>
              <AdminDashboard />
            </ProtectedRoute>
          }
        />
        <Route
          path="/admin/contenido"
          element={
            <ProtectedRoute adminOnly>
              <ContentAdmin />
            </ProtectedRoute>
          }
        />
      </Route>

      {/* Por defecto */}
      <Route path="/" element={<Navigate to="/inicio" replace />} />
      <Route path="*" element={<Navigate to="/inicio" replace />} />
    </Routes>
  );
}
