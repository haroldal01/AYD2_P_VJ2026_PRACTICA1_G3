import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { extractError } from '../api/client';

// Pantalla de inicio de sesion (correo + contrasena).
export default function Login() {
  const { login } = useAuth();
  const navigate = useNavigate();
  const [form, setForm] = useState({ email: '', password: '' });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      await login(form.email, form.password);
      navigate('/inicio');
    } catch (err) {
      setError(extractError(err));
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-page">
      <div className="auth-card">
        <h1 className="auth-card__brand">LearnFlow</h1>
        <h2>Iniciar sesion</h2>

        {error && <div className="alert alert--error">{error}</div>}

        <form onSubmit={handleSubmit} className="form">
          <label className="field">
            <span>Correo</span>
            <input
              type="email"
              name="email"
              value={form.email}
              onChange={handleChange}
              placeholder="correo@ejemplo.com"
              required
            />
          </label>

          <label className="field">
            <span>Contrasena</span>
            <input
              type="password"
              name="password"
              value={form.password}
              onChange={handleChange}
              placeholder="********"
              required
            />
          </label>

          <button type="submit" className="btn btn--primary btn--block" disabled={loading}>
            {loading ? 'Ingresando...' : 'Ingresar'}
          </button>
        </form>

        <p className="auth-card__footer">
          No tienes cuenta? <Link to="/registro">Registrate</Link>
        </p>
      </div>
    </div>
  );
}
