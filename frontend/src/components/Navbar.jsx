import { NavLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

// Barra de navegacion superior. Muestra enlaces segun el rol del usuario.
export default function Navbar() {
  const { user, isAdmin, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <nav className="navbar">
      <div className="navbar__brand">
        <span className="navbar__logo">LF</span> LearnFlow
      </div>

      <div className="navbar__links">
        {isAdmin ? (
          <>
            <NavLink to="/admin/dashboard">Dashboard</NavLink>
            <NavLink to="/admin/contenido">Contenido</NavLink>
          </>
        ) : (
          <>
            <NavLink to="/inicio">Inicio</NavLink>
            <NavLink to="/suscripciones">Suscripciones</NavLink>
            <NavLink to="/perfil">Perfil</NavLink>
          </>
        )}
      </div>

      <div className="navbar__user">
        <span className="navbar__name">{user?.fullName}</span>
        <button className="btn btn--ghost" onClick={handleLogout}>
          Salir
        </button>
      </div>
    </nav>
  );
}
