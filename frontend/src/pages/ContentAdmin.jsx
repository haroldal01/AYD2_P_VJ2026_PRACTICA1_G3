import { useEffect, useState, useCallback } from 'react';
import api, { extractError } from '../api/client';

// Catalogos simples (tipos, categorias, niveles): comparten la misma forma
// { id, name, description, active } y los mismos endpoints CRUD.
const CATALOGS = {
  'content-types': { label: 'Tipos de contenido', path: '/content-types' },
  categories: { label: 'Categorias', path: '/categories' },
  'difficulty-levels': { label: 'Niveles', path: '/difficulty-levels' },
};

const EMPTY_CATALOG = { name: '', description: '', active: true };
const EMPTY_COURSE = {
  title: '',
  productionYear: new Date().getFullYear(),
  instructor: '',
  shortSummary: '',
  description: '',
  mediaUrl: '',
  contentTypeId: '',
  categoryId: '',
  difficultyLevelId: '',
  active: true,
};

// Dashboard de administracion de contenido educativo.
// Conecta de verdad con el backend (cursos + catalogos).
export default function ContentAdmin() {
  const [tab, setTab] = useState('courses');
  return (
    <div className="page">
      <header className="page__head">
        <h1>Administracion de contenido</h1>
        <p className="muted">Gestiona cursos, tipos, categorias y niveles de dificultad.</p>
      </header>

      <div className="tabs">
        <button className={tab === 'courses' ? 'tab tab--active' : 'tab'} onClick={() => setTab('courses')}>
          Cursos
        </button>
        {Object.entries(CATALOGS).map(([key, cfg]) => (
          <button key={key} className={tab === key ? 'tab tab--active' : 'tab'} onClick={() => setTab(key)}>
            {cfg.label}
          </button>
        ))}
      </div>

      {tab === 'courses' ? <CoursesPanel /> : <CatalogPanel catalogKey={tab} />}
    </div>
  );
}

// ---------------------------------------------------------------------
// Panel generico para los catalogos simples (tipos, categorias, niveles).
// ---------------------------------------------------------------------
function CatalogPanel({ catalogKey }) {
  const cfg = CATALOGS[catalogKey];
  const [items, setItems] = useState([]);
  const [form, setForm] = useState(EMPTY_CATALOG);
  const [editingId, setEditingId] = useState(null);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(true);

  const load = useCallback(async () => {
    setLoading(true);
    try {
      const { data } = await api.get(cfg.path);
      setItems(data);
    } catch (err) {
      setError(extractError(err));
    } finally {
      setLoading(false);
    }
  }, [cfg.path]);

  useEffect(() => {
    setForm(EMPTY_CATALOG);
    setEditingId(null);
    load();
  }, [load]);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setForm({ ...form, [name]: type === 'checkbox' ? checked : value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      if (editingId) {
        await api.put(`${cfg.path}/${editingId}`, form);
      } else {
        await api.post(cfg.path, form);
      }
      setForm(EMPTY_CATALOG);
      setEditingId(null);
      load();
    } catch (err) {
      setError(extractError(err));
    }
  };

  const startEdit = (item) => {
    setEditingId(item.id);
    setForm({ name: item.name, description: item.description || '', active: item.active });
  };

  const remove = async (id) => {
    if (!window.confirm('¿Eliminar este registro?')) return;
    setError('');
    try {
      await api.delete(`${cfg.path}/${id}`);
      load();
    } catch (err) {
      setError(extractError(err));
    }
  };

  return (
    <div className="admin-layout">
      <section className="card">
        <h3>{editingId ? 'Editar' : 'Nuevo'} — {cfg.label}</h3>
        {error && <div className="alert alert--error">{error}</div>}
        <form onSubmit={handleSubmit} className="form">
          <label className="field">
            <span>Nombre</span>
            <input name="name" value={form.name} onChange={handleChange} required />
          </label>
          <label className="field">
            <span>Descripcion</span>
            <textarea name="description" value={form.description} onChange={handleChange} rows={3} />
          </label>
          <label className="field field--check">
            <input type="checkbox" name="active" checked={form.active} onChange={handleChange} />
            <span>Activo</span>
          </label>
          <div className="form__actions">
            <button type="submit" className="btn btn--primary">{editingId ? 'Actualizar' : 'Crear'}</button>
            {editingId && (
              <button type="button" className="btn btn--ghost" onClick={() => { setEditingId(null); setForm(EMPTY_CATALOG); }}>
                Cancelar
              </button>
            )}
          </div>
        </form>
      </section>

      <section className="card">
        <h3>Listado</h3>
        {loading ? (
          <p className="muted">Cargando...</p>
        ) : (
          <table className="table">
            <thead>
              <tr><th>Nombre</th><th>Descripcion</th><th>Estado</th><th></th></tr>
            </thead>
            <tbody>
              {items.length === 0 && <tr><td colSpan={4} className="muted">Sin registros.</td></tr>}
              {items.map((item) => (
                <tr key={item.id}>
                  <td>{item.name}</td>
                  <td className="muted">{item.description}</td>
                  <td>
                    <span className={`badge ${item.active ? 'badge--green' : 'badge--gray'}`}>
                      {item.active ? 'Activo' : 'Inactivo'}
                    </span>
                  </td>
                  <td className="table__actions">
                    <button className="btn btn--small" onClick={() => startEdit(item)}>Editar</button>
                    <button className="btn btn--small btn--danger" onClick={() => remove(item.id)}>Eliminar</button>
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

// ---------------------------------------------------------------------
// Panel de cursos: incluye busqueda por titulo y selects de catalogos.
// ---------------------------------------------------------------------
function CoursesPanel() {
  const [courses, setCourses] = useState([]);
  const [catalogs, setCatalogs] = useState({ types: [], categories: [], levels: [] });
  const [form, setForm] = useState(EMPTY_COURSE);
  const [editingId, setEditingId] = useState(null);
  const [search, setSearch] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(true);

  const loadCourses = useCallback(async (title = '') => {
    setLoading(true);
    try {
      const { data } = await api.get('/courses', { params: title ? { title } : {} });
      setCourses(data);
    } catch (err) {
      setError(extractError(err));
    } finally {
      setLoading(false);
    }
  }, []);

  // Carga los catalogos para poblar los selects del formulario.
  const loadCatalogs = useCallback(async () => {
    try {
      const [types, categories, levels] = await Promise.all([
        api.get('/content-types'),
        api.get('/categories'),
        api.get('/difficulty-levels'),
      ]);
      setCatalogs({ types: types.data, categories: categories.data, levels: levels.data });
    } catch (err) {
      setError(extractError(err));
    }
  }, []);

  useEffect(() => {
    loadCatalogs();
    loadCourses();
  }, [loadCatalogs, loadCourses]);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setForm({ ...form, [name]: type === 'checkbox' ? checked : value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    // Convierte los ids y el anio a numero antes de enviar.
    const payload = {
      ...form,
      productionYear: Number(form.productionYear),
      contentTypeId: Number(form.contentTypeId),
      categoryId: Number(form.categoryId),
      difficultyLevelId: Number(form.difficultyLevelId),
    };
    try {
      if (editingId) {
        await api.put(`/courses/${editingId}`, payload);
      } else {
        await api.post('/courses', payload);
      }
      setForm(EMPTY_COURSE);
      setEditingId(null);
      loadCourses(search);
    } catch (err) {
      setError(extractError(err));
    }
  };

  const startEdit = (course) => {
    setEditingId(course.id);
    setForm({
      title: course.title || '',
      productionYear: course.productionYear || new Date().getFullYear(),
      instructor: course.instructor || '',
      shortSummary: course.shortSummary || '',
      description: course.description || '',
      mediaUrl: course.mediaUrl || '',
      contentTypeId: course.contentTypeId ?? course.contentType?.id ?? '',
      categoryId: course.categoryId ?? course.category?.id ?? '',
      difficultyLevelId: course.difficultyLevelId ?? course.difficultyLevel?.id ?? '',
      active: course.active ?? true,
    });
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  const remove = async (id) => {
    if (!window.confirm('¿Eliminar este curso?')) return;
    setError('');
    try {
      await api.delete(`/courses/${id}`);
      loadCourses(search);
    } catch (err) {
      setError(extractError(err));
    }
  };

  // Resuelve el nombre de un catalogo a partir de su id (para la tabla).
  const nameOf = (list, id) => list.find((x) => x.id === id)?.name || '—';

  return (
    <div className="admin-layout admin-layout--wide">
      <section className="card">
        <h3>{editingId ? 'Editar curso' : 'Nuevo curso'}</h3>
        {error && <div className="alert alert--error">{error}</div>}
        <form onSubmit={handleSubmit} className="form form--grid">
          <label className="field field--full">
            <span>Titulo</span>
            <input name="title" value={form.title} onChange={handleChange} required />
          </label>
          <label className="field">
            <span>Anio de produccion</span>
            <input type="number" name="productionYear" value={form.productionYear} onChange={handleChange} min={1900} max={2100} required />
          </label>
          <label className="field">
            <span>Instructor</span>
            <input name="instructor" value={form.instructor} onChange={handleChange} required />
          </label>
          <label className="field">
            <span>Tipo</span>
            <select name="contentTypeId" value={form.contentTypeId} onChange={handleChange} required>
              <option value="">Selecciona...</option>
              {catalogs.types.map((t) => <option key={t.id} value={t.id}>{t.name}</option>)}
            </select>
          </label>
          <label className="field">
            <span>Categoria</span>
            <select name="categoryId" value={form.categoryId} onChange={handleChange} required>
              <option value="">Selecciona...</option>
              {catalogs.categories.map((c) => <option key={c.id} value={c.id}>{c.name}</option>)}
            </select>
          </label>
          <label className="field">
            <span>Dificultad</span>
            <select name="difficultyLevelId" value={form.difficultyLevelId} onChange={handleChange} required>
              <option value="">Selecciona...</option>
              {catalogs.levels.map((l) => <option key={l.id} value={l.id}>{l.name}</option>)}
            </select>
          </label>
          <label className="field">
            <span>URL multimedia</span>
            <input name="mediaUrl" value={form.mediaUrl} onChange={handleChange} placeholder="https://..." />
          </label>
          <label className="field field--full">
            <span>Resumen breve</span>
            <input name="shortSummary" value={form.shortSummary} onChange={handleChange} />
          </label>
          <label className="field field--full">
            <span>Descripcion</span>
            <textarea name="description" value={form.description} onChange={handleChange} rows={3} />
          </label>
          <label className="field field--check">
            <input type="checkbox" name="active" checked={form.active} onChange={handleChange} />
            <span>Activo</span>
          </label>
          <div className="field--full form__actions">
            <button type="submit" className="btn btn--primary">{editingId ? 'Actualizar' : 'Crear curso'}</button>
            {editingId && (
              <button type="button" className="btn btn--ghost" onClick={() => { setEditingId(null); setForm(EMPTY_COURSE); }}>
                Cancelar
              </button>
            )}
          </div>
        </form>
      </section>

      <section className="card">
        <div className="card__head">
          <h3>Cursos</h3>
          <form className="search" onSubmit={(e) => { e.preventDefault(); loadCourses(search); }}>
            <input placeholder="Buscar por titulo..." value={search} onChange={(e) => setSearch(e.target.value)} />
            <button className="btn btn--secondary btn--small" type="submit">Buscar</button>
          </form>
        </div>
        {loading ? (
          <p className="muted">Cargando...</p>
        ) : (
          <table className="table">
            <thead>
              <tr><th>Titulo</th><th>Instructor</th><th>Anio</th><th>Tipo</th><th>Categoria</th><th>Nivel</th><th></th></tr>
            </thead>
            <tbody>
              {courses.length === 0 && <tr><td colSpan={7} className="muted">Sin cursos.</td></tr>}
              {courses.map((course) => {
                const typeId = course.contentTypeId ?? course.contentType?.id;
                const catId = course.categoryId ?? course.category?.id;
                const lvlId = course.difficultyLevelId ?? course.difficultyLevel?.id;
                return (
                  <tr key={course.id}>
                    <td>{course.title}</td>
                    <td className="muted">{course.instructor}</td>
                    <td>{course.productionYear}</td>
                    <td>{nameOf(catalogs.types, typeId)}</td>
                    <td>{nameOf(catalogs.categories, catId)}</td>
                    <td>{nameOf(catalogs.levels, lvlId)}</td>
                    <td className="table__actions">
                      <button className="btn btn--small" onClick={() => startEdit(course)}>Editar</button>
                      <button className="btn btn--small btn--danger" onClick={() => remove(course.id)}>Eliminar</button>
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        )}
      </section>
    </div>
  );
}
