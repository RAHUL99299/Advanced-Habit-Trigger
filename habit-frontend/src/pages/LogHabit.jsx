import { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import Navbar from '../components/Navbar';
import axiosClient from '../api/axiosClient';

const MOODS = ['happy', 'calm', 'stressed', 'tired', 'motivated'];
const WEATHERS = ['sunny', 'cloudy', 'rainy', 'snowy', 'windy'];
const LOCATIONS = ['home', 'office', 'gym', 'outdoors', 'other'];
const TIMES = ['morning', 'afternoon', 'evening', 'night'];

const MOOD_EMOJI = { happy: '😊', calm: '😌', stressed: '😰', tired: '😴', motivated: '💪' };
const WEATHER_EMOJI = { sunny: '☀️', cloudy: '☁️', rainy: '🌧️', snowy: '❄️', windy: '💨' };
const LOCATION_EMOJI = { home: '🏠', office: '🏢', gym: '🏋️', outdoors: '🌳', other: '📍' };
const TIME_EMOJI = { morning: '🌅', afternoon: '☀️', evening: '🌆', night: '🌙' };

function OptionButton({ value, selected, onSelect, emoji }) {
  return (
    <button
      type="button"
      onClick={() => onSelect(value)}
      id={`opt-${value}`}
      style={{
        padding: '0.5rem 1rem',
        borderRadius: 'var(--radius-full)',
        border: `1px solid ${selected ? 'var(--accent-from)' : 'var(--border)'}`,
        background: selected ? 'rgba(99,102,241,0.2)' : 'var(--bg-input)',
        color: selected ? '#818cf8' : 'var(--text-secondary)',
        fontSize: '0.85rem',
        fontWeight: selected ? 600 : 400,
        cursor: 'pointer',
        transition: 'var(--transition)',
        display: 'flex',
        alignItems: 'center',
        gap: '0.3rem',
        fontFamily: 'inherit',
      }}
    >
      <span>{emoji}</span>
      <span style={{ textTransform: 'capitalize' }}>{value}</span>
    </button>
  );
}

export default function LogHabit() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [habit, setHabit] = useState(null);
  const [form, setForm] = useState({
    logDate: new Date().toISOString().split('T')[0],
    completed: true,
    mood: '',
    weather: '',
    location: '',
    timeOfDay: '',
    notes: '',
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState(false);

  useEffect(() => {
    axiosClient.get(`/api/habits/${id}`)
      .then(res => setHabit(res.data))
      .catch(() => navigate('/habits'));
  }, [id, navigate]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      await axiosClient.post(`/api/habits/${id}/logs`, form);
      setSuccess(true);
      setTimeout(() => navigate(`/habits/${id}`), 1500);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to save log.');
    } finally {
      setLoading(false);
    }
  };

  if (!habit) return (
    <div className="page-layout">
      <Navbar />
      <div className="loading-page"><div className="spinner" style={{ width: 36, height: 36, borderWidth: 3 }} /></div>
    </div>
  );

  return (
    <div className="page-layout">
      <Navbar />
      <main className="page-content" style={{ maxWidth: 680 }}>
        <Link to={`/habits/${id}`} className="btn btn-ghost btn-sm" style={{ marginBottom: '1.5rem', display: 'inline-flex' }} id="btn-back-detail">
          ← Back
        </Link>

        <div className="card card-accent fade-in-up">
          <h1 style={{ fontSize: '1.4rem', marginBottom: '0.25rem' }}>Log Entry</h1>
          <p className="text-secondary text-sm" style={{ marginBottom: '2rem' }}>
            Logging for: <strong style={{ color: 'var(--text-primary)' }}>{habit.name}</strong>
          </p>

          {success && <div className="alert alert-success">✅ Log saved! Redirecting…</div>}
          {error && <div className="alert alert-error" id="log-error">⚠️ {error}</div>}

          <form onSubmit={handleSubmit} id="log-habit-form">
            {/* Date + Completed */}
            <div className="grid-2" style={{ marginBottom: '1.5rem' }}>
              <div className="form-group" style={{ marginBottom: 0 }}>
                <label className="form-label">Date *</label>
                <input
                  id="log-date"
                  type="date"
                  className="form-control"
                  value={form.logDate}
                  onChange={e => setForm(f => ({ ...f, logDate: e.target.value }))}
                  required
                />
              </div>

              <div className="form-group" style={{ marginBottom: 0 }}>
                <label className="form-label">Did you complete it?</label>
                <div className="flex gap-3" style={{ marginTop: '0.6rem' }}>
                  <button
                    type="button"
                    id="btn-completed-yes"
                    onClick={() => setForm(f => ({ ...f, completed: true }))}
                    className={`btn ${form.completed ? 'btn-success' : 'btn-ghost'}`}
                    style={{ flex: 1 }}
                  >
                    ✓ Yes
                  </button>
                  <button
                    type="button"
                    id="btn-completed-no"
                    onClick={() => setForm(f => ({ ...f, completed: false }))}
                    className={`btn ${!form.completed ? 'btn-danger' : 'btn-ghost'}`}
                    style={{ flex: 1 }}
                  >
                    ✗ No
                  </button>
                </div>
              </div>
            </div>

            {/* Mood */}
            <div style={{ marginBottom: '1.5rem' }}>
              <label className="form-label">😶 Mood</label>
              <div className="flex gap-2" style={{ flexWrap: 'wrap', marginTop: '0.5rem' }}>
                {MOODS.map(m => (
                  <OptionButton
                    key={m} value={m}
                    selected={form.mood === m}
                    onSelect={v => setForm(f => ({ ...f, mood: f.mood === v ? '' : v }))}
                    emoji={MOOD_EMOJI[m]}
                  />
                ))}
              </div>
            </div>

            {/* Weather */}
            <div style={{ marginBottom: '1.5rem' }}>
              <label className="form-label">🌤️ Weather</label>
              <div className="flex gap-2" style={{ flexWrap: 'wrap', marginTop: '0.5rem' }}>
                {WEATHERS.map(w => (
                  <OptionButton
                    key={w} value={w}
                    selected={form.weather === w}
                    onSelect={v => setForm(f => ({ ...f, weather: f.weather === v ? '' : v }))}
                    emoji={WEATHER_EMOJI[w]}
                  />
                ))}
              </div>
            </div>

            {/* Location */}
            <div style={{ marginBottom: '1.5rem' }}>
              <label className="form-label">📍 Location</label>
              <div className="flex gap-2" style={{ flexWrap: 'wrap', marginTop: '0.5rem' }}>
                {LOCATIONS.map(l => (
                  <OptionButton
                    key={l} value={l}
                    selected={form.location === l}
                    onSelect={v => setForm(f => ({ ...f, location: f.location === v ? '' : v }))}
                    emoji={LOCATION_EMOJI[l]}
                  />
                ))}
              </div>
            </div>

            {/* Time of Day */}
            <div style={{ marginBottom: '1.5rem' }}>
              <label className="form-label">⏰ Time of Day</label>
              <div className="flex gap-2" style={{ flexWrap: 'wrap', marginTop: '0.5rem' }}>
                {TIMES.map(t => (
                  <OptionButton
                    key={t} value={t}
                    selected={form.timeOfDay === t}
                    onSelect={v => setForm(f => ({ ...f, timeOfDay: f.timeOfDay === v ? '' : v }))}
                    emoji={TIME_EMOJI[t]}
                  />
                ))}
              </div>
            </div>

            {/* Notes */}
            <div className="form-group">
              <label className="form-label">📝 Notes (optional)</label>
              <textarea
                id="log-notes"
                className="form-control"
                placeholder="What happened? Any context worth noting…"
                rows={3}
                value={form.notes}
                onChange={e => setForm(f => ({ ...f, notes: e.target.value }))}
                style={{ resize: 'vertical' }}
              />
            </div>

            <div className="flex gap-3 justify-between" style={{ marginTop: '2rem' }}>
              <Link to={`/habits/${id}`} className="btn btn-ghost">Cancel</Link>
              <button id="btn-save-log" type="submit" className="btn btn-primary btn-lg" disabled={loading || success}>
                {loading ? <><span className="spinner" style={{ width: 16, height: 16 }} /> Saving…</> : '💾 Save Log'}
              </button>
            </div>
          </form>
        </div>
      </main>
    </div>
  );
}
