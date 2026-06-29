import { useEffect, useState } from 'react';
import api, { extractError } from '../api/client';

const EMPTY = {
  fullName: '',
  dateOfBirth: '',
  email: '',
  password: '',
  nit: '',
  cardNumber: '',
  cardExpiry: '',
  photoUrl: '',
};

// Pantalla de perfil: carga datos del estudiante (GET /students/me)
// y permite actualizarlos (PUT /students/me).
export default function Profile() {
  const [form, setForm] = useState(EMPTY);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  useEffect(() => {
    let active = true;
    async function load() {
      try {
        const { data } = await api.get('/students/me');
        if (active) {
          // La contrasena nunca llega del backend: se deja vacia (opcional al guardar).
          setForm({ ...EMPTY, ...data, password: '' });
        }
      } catch (err) {
        if (active) setError(extractError(err));
      } finally {
        if (active) setLoading(false);
      }
    }
    load();
    return () => {
      active = false;
    };
  }, []);

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    setSaving(true);
    try {
      // Si la contrasena va vacia, no se envia (el backend conserva la anterior).
      const payload = { ...form };
      if (!payload.password) delete payload.password;
      const { data } = await api.put('/students/me', payload);
      setForm({ ...EMPTY, ...data, password: '' });
      setSuccess('Datos actualizados correctamente.');
    } catch (err) {
      setError(extractError(err));
    } finally {
      setSaving(false);
    }
  };

  if (loading) return <p className="muted">Cargando perfil...</p>;

  return (
    <div className="page">
      <header className="page__head">
        <h1>Mi perfil</h1>
        <p className="muted">Actualiza tus datos personales y de pago.</p>
      </header>

      <div className="profile-layout">
        <aside className="profile-photo">
          {form.photoUrl ? (
            <img src={form.photoUrl} alt="Fotografia del estudiante" />
          ) : (
            <div className="profile-photo__placeholder">Sin foto</div>
          )}
        </aside>

        <section className="card">
          {error && <div className="alert alert--error">{error}</div>}
          {success && <div className="alert alert--success">{success}</div>}

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
              <span>Nueva contrasena (opcional)</span>
              <input type="password" name="password" value={form.password} onChange={handleChange} minLength={6} placeholder="Dejar vacio para no cambiar" />
            </label>

            <label className="field">
              <span>NIT</span>
              <input name="nit" value={form.nit} onChange={handleChange} maxLength={20} required />
            </label>

            <label className="field">
              <span>Numero de tarjeta</span>
              <input name="cardNumber" value={form.cardNumber} onChange={handleChange} minLength={13} maxLength={19} required />
            </label>

            <label className="field">
              <span>Vencimiento de tarjeta</span>
              <input type="date" name="cardExpiry" value={form.cardExpiry} onChange={handleChange} required />
            </label>

            <label className="field field--full">
              <span>URL de fotografia</span>
              <input name="photoUrl" value={form.photoUrl} onChange={handleChange} maxLength={500} required />
            </label>

            <div className="field--full">
              <button type="submit" className="btn btn--primary" disabled={saving}>
                {saving ? 'Guardando...' : 'Guardar cambios'}
              </button>
            </div>
          </form>
        </section>
      </div>
    </div>
  );
}
