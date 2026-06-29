import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { extractError } from '../api/client';

// Estado inicial del formulario de registro. Los campos coinciden con
// StudentRegisterRequest del backend (ver API_STUDENTS.md).
const INITIAL = {
  fullName: '',
  dateOfBirth: '',
  email: '',
  password: '',
  nit: '',
  cardNumber: '',
  cardExpiry: '',
  photoUrl: '',
};

// Pantalla de registro de estudiante.
export default function Register() {
  const { register } = useAuth();
  const navigate = useNavigate();
  const [form, setForm] = useState(INITIAL);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      await register(form);
      navigate('/inicio');
    } catch (err) {
      setError(extractError(err));
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-page">
      <div className="auth-card auth-card--wide">
        <h1 className="auth-card__brand">▶ LearnFlow</h1>
        <h2>Crear cuenta de estudiante</h2>

        {error && <div className="alert alert--error">{error}</div>}

        <form onSubmit={handleSubmit} className="form form--grid">
          <label className="field field--full">
            <span>Nombre completo</span>
            <input name="fullName" value={form.fullName} onChange={handleChange} maxLength={150} required />
          </label>

          <label className="field">
            <span>Fecha de nacimiento</span>
            <input type="date" name="dateOfBirth" value={form.dateOfBirth} onChange={handleChange} required />
          </label>

          <label className="field">
            <span>Correo</span>
            <input type="email" name="email" value={form.email} onChange={handleChange} maxLength={100} required />
          </label>

          <label className="field">
            <span>Contrasena</span>
            <input type="password" name="password" value={form.password} onChange={handleChange} minLength={6} maxLength={100} required />
          </label>

          <label className="field">
            <span>NIT</span>
            <input name="nit" value={form.nit} onChange={handleChange} maxLength={20} required />
          </label>

          <label className="field">
            <span>Numero de tarjeta</span>
            <input name="cardNumber" value={form.cardNumber} onChange={handleChange} minLength={13} maxLength={19} placeholder="Solo digitos" required />
          </label>

          <label className="field">
            <span>Vencimiento de tarjeta</span>
            <input type="date" name="cardExpiry" value={form.cardExpiry} onChange={handleChange} required />
          </label>

          <label className="field field--full">
            <span>URL de fotografia</span>
            <input name="photoUrl" value={form.photoUrl} onChange={handleChange} maxLength={500} placeholder="https://..." required />
          </label>

          <div className="field--full">
            <button type="submit" className="btn btn--primary btn--block" disabled={loading}>
              {loading ? 'Creando cuenta...' : 'Registrarme'}
            </button>
          </div>
        </form>

        <p className="auth-card__footer">
          ¿Ya tienes cuenta? <Link to="/login">Inicia sesion</Link>
        </p>
      </div>
    </div>
  );
}
