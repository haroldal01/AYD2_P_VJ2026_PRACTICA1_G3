import { useCallback, useEffect, useMemo, useState } from 'react';
import api, { extractError } from '../api/client';

const EMPTY_FILTERS = {
  title: '',
  contentTypeId: '',
  categoryId: '',
  difficultyLevelId: '',
  year: '',
};

function maxValue(items, key) {
  return Math.max(1, ...items.map((item) => Number(item[key] || 0)));
}

function BarList({ items, labelKey, valueKey, emptyText }) {
  const max = useMemo(() => maxValue(items, valueKey), [items, valueKey]);

  if (items.length === 0) {
    return <p className="muted">{emptyText}</p>;
  }

  return (
    <div className="bar-list">
      {items.map((item) => {
        const value = Number(item[valueKey] || 0);
        return (
          <div key={item[labelKey]} className="bar-row">
            <div className="bar-row__head">
              <strong>{item[labelKey]}</strong>
              <span className="muted">{value.toLocaleString()} vistas</span>
            </div>
            <div className="bar-row__track">
              <span style={{ width: `${Math.max(8, (value / max) * 100)}%` }} />
            </div>
          </div>
        );
      })}
    </div>
  );
}

export default function AdminDashboard() {
  const [stats, setStats] = useState(null);
  const [courses, setCourses] = useState([]);
  const [catalogs, setCatalogs] = useState({ types: [], categories: [], levels: [] });
  const [filters, setFilters] = useState(EMPTY_FILTERS);
  const [loading, setLoading] = useState(true);
  const [coursesLoading, setCoursesLoading] = useState(false);
  const [error, setError] = useState('');

  const loadStats = useCallback(async () => {
    const { data } = await api.get('/admin/dashboard/stats');
    setStats(data);
  }, []);

  const loadCatalogs = useCallback(async () => {
    const [types, categories, levels] = await Promise.all([
      api.get('/content-types'),
      api.get('/categories'),
      api.get('/difficulty-levels'),
    ]);
    setCatalogs({ types: types.data, categories: categories.data, levels: levels.data });
  }, []);

  const loadCourses = useCallback(async (currentFilters = filters) => {
    setCoursesLoading(true);
    setError('');
    try {
      const params = Object.fromEntries(
        Object.entries(currentFilters).filter(([, value]) => value !== '')
      );
      const { data } = await api.get('/admin/dashboard/courses', { params });
      setCourses(data);
    } catch (err) {
      setError(extractError(err));
    } finally {
      setCoursesLoading(false);
    }
  }, [filters]);

  useEffect(() => {
    setLoading(true);
    setError('');
    Promise.all([loadStats(), loadCatalogs(), loadCourses(EMPTY_FILTERS)])
      .catch((err) => setError(extractError(err)))
      .finally(() => setLoading(false));
  }, [loadStats, loadCatalogs, loadCourses]);

  const handleChange = (event) => {
    const { name, value } = event.target;
    setFilters((current) => ({ ...current, [name]: value }));
  };

  const handleSearch = (event) => {
    event.preventDefault();
    loadCourses(filters);
  };

  const clearFilters = () => {
    setFilters(EMPTY_FILTERS);
    loadCourses(EMPTY_FILTERS);
  };

  if (loading) return <p className="muted">Cargando dashboard...</p>;

  return (
    <div className="page">
      <header className="page__head">
        <h1>Dashboard administrativo</h1>
        <p className="muted">Estadisticas de consumo, cursos y suscripciones.</p>
      </header>

      {error && <div className="alert alert--error">{error}</div>}

      <section className="dashboard-grid">
        <article className="card">
          <h3>Categorias mas reproducidas</h3>
          <BarList
            items={stats?.topCategories || []}
            labelKey="categoryName"
            valueKey="viewCount"
            emptyText="Aun no hay reproducciones por categoria."
          />
        </article>

        <article className="card">
          <h3>Niveles mas cursados</h3>
          <BarList
            items={stats?.topDifficultyLevels || []}
            labelKey="levelName"
            valueKey="viewCount"
            emptyText="Aun no hay reproducciones por nivel."
          />
        </article>
      </section>

      <section className="dashboard-grid">
        <article className="card">
          <h3>Distribucion de suscripciones</h3>
          <div className="metric-list">
            {(stats?.subscriptionDistribution || []).length === 0 ? (
              <p className="muted">Aun no hay suscripciones activas.</p>
            ) : (
              stats.subscriptionDistribution.map((item) => (
                <div key={item.type} className="metric-item">
                  <span>{item.type}</span>
                  <strong>{Number(item.count || 0).toLocaleString()}</strong>
                </div>
              ))
            )}
          </div>
        </article>

        <article className="card">
          <h3>Top 10 cursos</h3>
          {(stats?.topCourses || []).length === 0 ? (
            <p className="muted">Aun no hay cursos visualizados.</p>
          ) : (
            <ol className="ranking">
              {stats.topCourses.map((course, index) => (
                <li key={course.courseId} className="ranking__item">
                  <span className="ranking__pos">{index + 1}</span>
                  <div className="ranking__info">
                    <strong>{course.title}</strong>
                    <span className="muted">Curso #{course.courseId}</span>
                  </div>
                  <span className="ranking__views">{Number(course.viewCount || 0).toLocaleString()} vistas</span>
                </li>
              ))}
            </ol>
          )}
        </article>
      </section>

      <section className="card">
        <div className="card__head">
          <div>
            <h3>Busqueda de cursos</h3>
            <p className="muted">Filtra por titulo, catalogos y anio de produccion.</p>
          </div>
        </div>

        <form className="dashboard-filters" onSubmit={handleSearch}>
          <label className="field">
            <span>Titulo</span>
            <input name="title" value={filters.title} onChange={handleChange} placeholder="Buscar..." />
          </label>
          <label className="field">
            <span>Tipo</span>
            <select name="contentTypeId" value={filters.contentTypeId} onChange={handleChange}>
              <option value="">Todos</option>
              {catalogs.types.map((item) => (
                <option key={item.id} value={item.id}>{item.name}</option>
              ))}
            </select>
          </label>
          <label className="field">
            <span>Categoria</span>
            <select name="categoryId" value={filters.categoryId} onChange={handleChange}>
              <option value="">Todas</option>
              {catalogs.categories.map((item) => (
                <option key={item.id} value={item.id}>{item.name}</option>
              ))}
            </select>
          </label>
          <label className="field">
            <span>Nivel</span>
            <select name="difficultyLevelId" value={filters.difficultyLevelId} onChange={handleChange}>
              <option value="">Todos</option>
              {catalogs.levels.map((item) => (
                <option key={item.id} value={item.id}>{item.name}</option>
              ))}
            </select>
          </label>
          <label className="field">
            <span>Anio</span>
            <input name="year" type="number" min="1900" max="2100" value={filters.year} onChange={handleChange} />
          </label>
          <div className="dashboard-filters__actions">
            <button className="btn btn--primary" type="submit">Buscar</button>
            <button className="btn btn--ghost" type="button" onClick={clearFilters}>Limpiar</button>
          </div>
        </form>

        {coursesLoading ? (
          <p className="muted">Cargando cursos...</p>
        ) : (
          <table className="table">
            <thead>
              <tr>
                <th>Titulo</th>
                <th>Instructor</th>
                <th>Anio</th>
                <th>Tipo</th>
                <th>Categoria</th>
                <th>Nivel</th>
                <th>Estado</th>
              </tr>
            </thead>
            <tbody>
              {courses.length === 0 && (
                <tr><td colSpan={7} className="muted">Sin resultados.</td></tr>
              )}
              {courses.map((course) => (
                <tr key={course.id}>
                  <td>{course.title}</td>
                  <td className="muted">{course.instructor}</td>
                  <td>{course.productionYear}</td>
                  <td>{course.contentType?.name || '-'}</td>
                  <td>{course.category?.name || '-'}</td>
                  <td>{course.difficultyLevel?.name || '-'}</td>
                  <td>
                    <span className={`badge ${course.active ? 'badge--green' : 'badge--gray'}`}>
                      {course.active ? 'Activo' : 'Inactivo'}
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </section>
    </div>
  );
}
