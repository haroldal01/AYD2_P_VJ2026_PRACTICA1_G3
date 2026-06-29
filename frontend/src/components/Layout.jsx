import { Outlet } from 'react-router-dom';
import Navbar from './Navbar';

// Layout comun para las paginas autenticadas: navbar + contenido.
export default function Layout() {
  return (
    <div className="app-shell">
      <Navbar />
      <main className="app-content">
        <Outlet />
      </main>
    </div>
  );
}
