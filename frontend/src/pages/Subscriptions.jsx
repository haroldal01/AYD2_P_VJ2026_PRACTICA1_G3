import { useEffect, useMemo, useState } from 'react';
import api, { extractError } from '../api/client';
import { useAuth } from '../context/AuthContext';

const SUBSCRIPTION_PLANS = [
  {
    id: 'MENSUAL',
    name: 'Mensual',
    price: 75,
    days: 30,
    description: 'Acceso completo a LearnFlow por 30 dias.',
  },
  {
    id: 'TRIMESTRAL',
    name: 'Trimestral',
    price: 200,
    days: 90,
    description: 'Acceso por 90 dias con mejor precio que el plan mensual.',
  },
  {
    id: 'ANUAL',
    name: 'Anual',
    price: 700,
    days: 365,
    description: 'Acceso completo por un anio con el mayor ahorro.',
  },
];

const PLAN_LABELS = {
  MENSUAL: 'Mensual',
  TRIMESTRAL: 'Trimestral',
  ANUAL: 'Anual',
};

const STATUS_LABELS = {
  ACTIVA: 'Activa',
  CANCELADA: 'Cancelada',
  EXPIRADA: 'Expirada',
};

function formatDate(value) {
  if (!value) return '-';
  return new Date(`${value}T00:00:00`).toLocaleDateString();
}

function formatPrice(value) {
  const number = Number(value || 0);
  return `Q${number.toFixed(2)}`;
}

export default function Subscriptions() {
  const { user } = useAuth();
  const studentId = user?.studentId;
  const [history, setHistory] = useState([]);
  const [hasActive, setHasActive] = useState(false);
  const [loading, setLoading] = useState(true);
  const [busyPlan, setBusyPlan] = useState('');
  const [busyCancel, setBusyCancel] = useState(false);
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');

  const activeMembership = useMemo(
    () => history.find((item) => item.status === 'ACTIVA') || null,
    [history]
  );

  const refresh = async () => {
    if (!studentId) {
      setLoading(false);
      return;
    }

    setLoading(true);
    setError('');
    try {
      const [historyRes, activeRes] = await Promise.all([
        api.get(`/subscriptions/${studentId}`),
        api.get(`/subscriptions/${studentId}/active`),
      ]);
      setHistory(historyRes.data);
      setHasActive(activeRes.data);
    } catch (err) {
      setError(extractError(err));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    refresh();
  }, [studentId]);

  const runPlanAction = async (plan) => {
    setBusyPlan(plan.id);
    setMessage('');
    setError('');
    try {
      const endpoint = hasActive ? 'renew' : 'contract';
      await api.post(`/subscriptions/${studentId}/${endpoint}`, { planType: plan.id });
      await refresh();
      setMessage(
        hasActive
          ? `Membresia renovada con el plan ${plan.name}.`
          : `Te suscribiste al plan ${plan.name}.`
      );
    } catch (err) {
      setError(extractError(err));
    } finally {
      setBusyPlan('');
    }
  };

  const cancelMembership = async () => {
    setBusyCancel(true);
    setMessage('');
    setError('');
    try {
      await api.post(`/subscriptions/${studentId}/cancel`);
      await refresh();
      setMessage('Membresia cancelada.');
    } catch (err) {
      setError(extractError(err));
    } finally {
      setBusyCancel(false);
    }
  };

  if (!studentId) {
    return (
      <div className="page">
        <header className="page__head">
          <h1>Suscripciones</h1>
          <p className="muted">Esta vista esta disponible para estudiantes.</p>
        </header>
      </div>
    );
  }

  if (loading) return <p className="muted">Cargando suscripcion...</p>;

  return (
    <div className="page">
      <header className="page__head">
        <h1>Suscripciones</h1>
        <p className="muted">Contrata, renueva o cancela tu membresia.</p>
      </header>

      {error && <div className="alert alert--error">{error}</div>}
      {message && <div className="alert alert--success">{message}</div>}

      <section className={`card membership ${hasActive ? 'membership--active' : 'membership--inactive'}`}>
        {activeMembership ? (
          <div>
            <h3>Membresia {PLAN_LABELS[activeMembership.planType] || activeMembership.planType}</h3>
            <p className="muted">
              Vigencia: {formatDate(activeMembership.startDate)} a {formatDate(activeMembership.endDate)}
            </p>
            <p className="muted">Precio: {formatPrice(activeMembership.price)}</p>
            <span className="badge badge--green">Activa</span>
          </div>
        ) : (
          <div>
            <h3>Sin membresia activa</h3>
            <p className="muted">Elige un plan para habilitar la reproduccion de cursos.</p>
            <span className="badge badge--gray">Inactiva</span>
          </div>
        )}

        {activeMembership && (
          <div className="membership__actions">
            <button
              className="btn btn--danger"
              disabled={busyCancel || !!busyPlan}
              onClick={cancelMembership}
            >
              Cancelar
            </button>
          </div>
        )}
      </section>

      <h2 className="section-title">Planes disponibles</h2>
      <div className="plans">
        {SUBSCRIPTION_PLANS.map((plan) => (
          <div key={plan.id} className="plan-card">
            <h3>{plan.name}</h3>
            <p className="plan-card__price">
              Q{plan.price.toFixed(2)}
              <span> / {plan.days} dias</span>
            </p>
            <p className="muted">{plan.description}</p>
            <button
              className="btn btn--primary btn--block"
              disabled={busyCancel || !!busyPlan}
              onClick={() => runPlanAction(plan)}
            >
              {busyPlan === plan.id ? 'Procesando...' : hasActive ? 'Renovar con este plan' : 'Contratar'}
            </button>
          </div>
        ))}
      </div>

      <section>
        <h2 className="section-title">Historial de suscripciones</h2>
        {history.length === 0 ? (
          <p className="muted">Aun no tienes suscripciones registradas.</p>
        ) : (
          <div className="history-list">
            {history.map((item) => (
              <article key={item.id} className="history-item">
                <div className="history-item__info">
                  <strong>{PLAN_LABELS[item.planType] || item.planType}</strong>
                  <span className="muted">
                    {formatDate(item.startDate)} a {formatDate(item.endDate)} - {formatPrice(item.price)}
                  </span>
                </div>
                <span className={`badge ${item.status === 'ACTIVA' ? 'badge--green' : 'badge--gray'}`}>
                  {STATUS_LABELS[item.status] || item.status}
                </span>
              </article>
            ))}
          </div>
        )}
      </section>
    </div>
  );
}
