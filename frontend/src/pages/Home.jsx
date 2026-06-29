import { useEffect, useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { getRecommendations, getTopCourses } from '../mocks';

// Pagina de inicio del estudiante: recomendaciones + ranking top 10.
// NOTA: usa datos MOCK porque el backend de reproduccion/recomendaciones
// (tarea de Kenneth, feature/playback-history) aun no existe.
export default function Home() {
  const { user } = useAuth();
  const [recommendations, setRecommendations] = useState(null);
  const [top, setTop] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Promise.all([getRecommendations(), getTopCourses()])
      .then(([recs, topCourses]) => {
        setRecommendations(recs);
        setTop(topCourses);
      })
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <p className="muted">Cargando contenido...</p>;

  return (
    <div className="page">
      <header className="page__head">
        <h1>Hola, {user?.fullName?.split(' ')[0]} 👋</h1>
        <p className="muted">Esto es lo que te recomendamos hoy.</p>
      </header>

      <div className="alert alert--info">
        Recomendaciones y ranking de demostracion (mock): el backend de reproduccion aun no esta implementado.
      </div>

      <section>
        <h2 className="section-title">
          Recomendado para ti{' '}
          <span className="muted">· basado en {recommendations.favoriteCategory}</span>
        </h2>
        <div className="course-grid">
          {recommendations.courses.map((course) => (
            <article key={course.id} className="course-card">
              <div className="course-card__thumb">▶</div>
              <h3>{course.title}</h3>
              <p className="muted">{course.instructor}</p>
              <span className="badge badge--blue">{course.category}</span>
            </article>
          ))}
        </div>
      </section>

      <section>
        <h2 className="section-title">Top 10 cursos mas vistos</h2>
        <ol className="ranking">
          {top.map((course, index) => (
            <li key={course.id} className="ranking__item">
              <span className="ranking__pos">{index + 1}</span>
              <div className="ranking__info">
                <strong>{course.title}</strong>
                <span className="muted">{course.instructor} · {course.category}</span>
              </div>
              <span className="ranking__views">{course.views.toLocaleString()} vistas</span>
            </li>
          ))}
        </ol>
      </section>
    </div>
  );
}
