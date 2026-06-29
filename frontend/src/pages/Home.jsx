import { useEffect, useState } from 'react';
import { useAuth } from '../context/AuthContext';
import api, { extractError } from '../api/client';

export default function Home() {
  const { user } = useAuth();
  const [recommendations, setRecommendations] = useState([]);
  const [top, setTop] = useState([]);
  const [history, setHistory] = useState([]);
  const [loading, setLoading] = useState(true);
  const [playError, setPlayError] = useState(null);

  const fetchData = () => {
    setLoading(true);
    Promise.all([
      api.get('/playback/recommendations'),
      api.get('/playback/top10'),
      api.get('/playback/history'),
    ])
      .then(([recsRes, topRes, historyRes]) => {
        setRecommendations(recsRes.data);
        setTop(topRes.data);
        setHistory(historyRes.data);
      })
      .catch(() => {})
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    fetchData();
  }, []);

  const handlePlay = async (courseId) => {
    setPlayError(null);
    try {
      await api.post(`/playback/play/${courseId}`);
      fetchData();
    } catch (err) {
      setPlayError(extractError(err));
    }
  };

  if (loading) return <p className="muted">Cargando contenido...</p>;

  const favoriteCategory = recommendations.length > 0
    ? recommendations[0].category
    : null;

  return (
    <div className="page">
      <header className="page__head">
        <h1>Hola, {user?.fullName?.split(' ')[0]}</h1>
        <p className="muted">Esto es lo que te recomendamos hoy.</p>
      </header>

      {playError && (
        <div className="alert alert--error">{playError}</div>
      )}

      <section>
        <h2 className="section-title">
          Recomendado para ti
          {favoriteCategory && (
            <span className="muted"> · basado en {favoriteCategory}</span>
          )}
        </h2>
        {recommendations.length === 0 ? (
          <p className="muted">Aun no hay recomendaciones. Empieza viendo contenido.</p>
        ) : (
          <div className="course-grid">
            {recommendations.map((course) => (
              <article key={course.courseId} className="course-card">
                <div className="course-card__thumb">▶</div>
                <h3>{course.title}</h3>
                <p className="muted">{course.instructor}</p>
                <span className="badge badge--blue">{course.category}</span>
                <p className="muted reason">{course.reason}</p>
                <button
                  className="btn btn--primary"
                  onClick={() => handlePlay(course.courseId)}
                >
                  Reproducir
                </button>
              </article>
            ))}
          </div>
        )}
      </section>

      <section>
        <h2 className="section-title">Top 10 cursos mas vistos</h2>
        {top.length === 0 ? (
          <p className="muted">Aun no hay visualizaciones registradas.</p>
        ) : (
          <ol className="ranking">
            {top.map((course, index) => (
              <li key={course.courseId} className="ranking__item">
                <span className="ranking__pos">{index + 1}</span>
                <div className="ranking__info">
                  <strong>{course.title}</strong>
                  <span className="muted">{course.instructor} · {course.category}</span>
                </div>
                <span className="ranking__views">{course.viewCount.toLocaleString()} vistas</span>
              </li>
            ))}
          </ol>
        )}
      </section>

      <section>
        <h2 className="section-title">Mi historial</h2>
        {history.length === 0 ? (
          <p className="muted">Aun no has visto ningun contenido.</p>
        ) : (
          <div className="history-list">
            {history.map((entry) => (
              <article key={entry.id} className="history-item">
                <div className="history-item__info">
                  <strong>{entry.courseTitle}</strong>
                  <span className="muted">{entry.instructor} · {entry.category}</span>
                </div>
                <span className="badge badge--gray">
                  {new Date(entry.watchedAt).toLocaleDateString()}
                </span>
              </article>
            ))}
          </div>
        )}
      </section>
    </div>
  );
}
