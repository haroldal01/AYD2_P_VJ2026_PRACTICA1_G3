import { useEffect, useState } from 'react';
import {
  SUBSCRIPTION_PLANS,
  getMembership,
  subscribe,
  renewMembership,
  cancelMembership,
} from '../mocks';

// Pantalla de suscripciones.
// NOTA: usa datos MOCK porque el backend de membresias (tarea de Madeline,
// feature/subscriptions-factory) aun no existe. Reemplazar las llamadas de
// ../mocks por endpoints reales cuando esten disponibles.
export default function Subscriptions() {
  const [membership, setMembership] = useState(null);
  const [loading, setLoading] = useState(true);
  const [busy, setBusy] = useState(false);
  const [message, setMessage] = useState('');

  const refresh = async () => {
    const data = await getMembership();
    setMembership(data);
  };

  useEffect(() => {
    refresh().finally(() => setLoading(false));
  }, []);

  const run = async (action, okMessage) => {
    setBusy(true);
    setMessage('');
    try {
      await action();
      await refresh();
      setMessage(okMessage);
    } catch (err) {
      setMessage(err.message);
    } finally {
      setBusy(false);
    }
  };

  const hasActive = membership?.active;

  if (loading) return <p className="muted">Cargando suscripcion...</p>;

  return (
    <div className="page">
      <header className="page__head">
        <h1>Suscripciones</h1>
        <p className="muted">Contrata, renueva o cancela tu membresia.</p>
      </header>

      <div className="alert alert--info">
        Datos de demostracion (mock): el backend de membresias aun no esta implementado.
      </div>

      {message && <div className="alert alert--success">{message}</div>}

      {membership && (
        <section className={`card membership ${hasActive ? 'membership--active' : 'membership--inactive'}`}>
          <div>
            <h3>Membresia {membership.planName}</h3>
            <p className="muted">
              Vigencia: {membership.startDate} → {membership.endDate}
            </p>
            <span className={`badge ${hasActive ? 'badge--green' : 'badge--gray'}`}>
              {hasActive ? 'Activa' : 'Cancelada'}
            </span>
          </div>
          <div className="membership__actions">
            <button className="btn btn--secondary" disabled={busy} onClick={() => run(renewMembership, 'Membresia renovada.')}>
              Renovar
            </button>
            {hasActive && (
              <button className="btn btn--danger" disabled={busy} onClick={() => run(cancelMembership, 'Membresia cancelada.')}>
                Cancelar
              </button>
            )}
          </div>
        </section>
      )}

      <h2 className="section-title">Planes disponibles</h2>
      <div className="plans">
        {SUBSCRIPTION_PLANS.map((plan) => (
          <div key={plan.id} className="plan-card">
            <h3>{plan.name}</h3>
            <p className="plan-card__price">
              Q{plan.price}
              <span> / {plan.months} mes(es)</span>
            </p>
            <p className="muted">{plan.description}</p>
            <button
              className="btn btn--primary btn--block"
              disabled={busy}
              onClick={() => run(() => subscribe(plan.id), `Te suscribiste al plan ${plan.name}.`)}
            >
              {hasActive ? 'Cambiar a este plan' : 'Contratar'}
            </button>
          </div>
        ))}
      </div>
    </div>
  );
}
