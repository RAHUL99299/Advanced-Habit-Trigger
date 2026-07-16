import { useEffect, useState } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import Navbar from '../components/Navbar';
import StreakBadge from '../components/StreakBadge';
import axiosClient from '../api/axiosClient';

const MOOD_EMOJI = { happy: '😊', calm: '😌', stressed: '😰', tired: '😴', motivated: '💪' };
const WEATHER_EMOJI = { sunny: '☀️', cloudy: '☁️', rainy: '🌧️', snowy: '❄️', windy: '💨' };
const LOCATION_EMOJI = { home: '🏠', office: '🏢', gym: '🏋️', outdoors: '🌳', other: '📍' };
const TIME_EMOJI = { morning: '🌅', afternoon: '☀️', evening: '🌆', night: '🌙' };

export default function HabitDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [habit, setHabit] = useState(null);
  const [logs, setLogs] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Promise.all([
      axiosClient.get(`/api/habits/${id}`),
      axiosClient.get(`/api/habits/${id}/logs`),
    ]).then(([habitRes, logsRes]) => {
      setHabit(habitRes.data);
      setLogs(logsRes.data);
    }).catch(() => navigate('/habits'))
      .finally(() => setLoading(false));
  }, [id, navigate]);

  const handleDelete = async () => {
    if (!window.confirm(`Delete "${habit.name}" and all its data permanently?`)) return;
    await axiosClient.delete(`/api/habits/${id}`);
    navigate('/habits');
  };

  if (loading) return (
    <div className="page-layout">
      <Navbar />
      <div className="loading-page"><div className="spinner" style={{ width: 36, height: 36, borderWidth: 3 }} /></div>
    </div>
  );

  if (!habit) return null;

  const rate = habit.completionRate || 0;

  return (
    <div className="page-layout">
      <Navbar />
      <main className="page-content">
        {/* Back */}
        <Link to="/habits" className="btn btn-ghost btn-sm" style={{ marginBottom: '1.5rem', display: 'inline-flex' }} id="btn-back-habits">
          ← Back to Habits
        </Link>

        {/* Header Card */}
        <div className="card card-accent fade-in-up" style={{ marginBottom: '1.5rem' }}>
          <div className="flex items-center justify-between" style={{ flexWrap: 'wrap', gap: '1rem' }}>
            <div>
              <h1 style={{ fontSize: '1.75rem', marginBottom: '0.5rem' }}>{habit.name}</h1>
              <div className="flex gap-2 items-center" style={{ flexWrap: 'wrap' }}>
                {habit.category && <span className="badge badge-accent">📂 {habit.category}</span>}
                {habit.targetFrequency && <span className="badge badge-accent">📅 {habit.targetFrequency}</span>}
                <StreakBadge streak={habit.currentStreak} />
              </div>
            </div>
            <div className="flex gap-2">
              <Link to={`/habits/${id}/log`} className="btn btn-primary" id="btn-log-today">
                ✏️ Log Today
              </Link>
              <Link to={`/habits/${id}/insights`} className="btn btn-ghost" id="btn-view-insights" style={{ color: '#818cf8' }}>
                💡 Insights
              </Link>
              <button className="btn btn-danger btn-sm" onClick={handleDelete} id="btn-delete-habit">🗑️</button>
            </div>
          </div>

          {/* Stats Row */}
          <div className="divider" />
          <div className="grid-4" style={{ gap: '1rem' }}>
            <div>
              <div style={{ fontSize: '1.5rem', fontWeight: 800, color: '#818cf8' }}>{rate.toFixed(0)}%</div>
              <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)', textTransform: 'uppercase', letterSpacing: '0.05em' }}>Completion Rate</div>
            </div>
            <div>
              <div style={{ fontSize: '1.5rem', fontWeight: 800, color: 'var(--success)' }}>{habit.currentStreak}</div>
              <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)', textTransform: 'uppercase', letterSpacing: '0.05em' }}>Current Streak</div>
            </div>
            <div>
              <div style={{ fontSize: '1.5rem', fontWeight: 800, color: 'var(--warning)' }}>{habit.longestStreak}</div>
              <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)', textTransform: 'uppercase', letterSpacing: '0.05em' }}>Best Streak</div>
            </div>
            <div>
              <div style={{ fontSize: '1.5rem', fontWeight: 800, color: 'var(--text-primary)' }}>{habit.totalLogs}</div>
              <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)', textTransform: 'uppercase', letterSpacing: '0.05em' }}>Total Logs</div>
            </div>
          </div>
        </div>

        {/* Log History */}
        <div className="card">
          <h3 style={{ marginBottom: '1.25rem' }}>Log History</h3>

          {logs.length === 0 ? (
            <div className="empty-state">
              <div className="empty-icon">📝</div>
              <h3>No logs yet</h3>
              <p>Start logging to see your history here.</p>
              <Link to={`/habits/${id}/log`} className="btn btn-primary" id="btn-start-logging">Log Now</Link>
            </div>
          ) : (
            <div className="table-wrapper">
              <table>
                <thead>
                  <tr>
                    <th>Date</th>
                    <th>Status</th>
                    <th>Mood</th>
                    <th>Weather</th>
                    <th>Location</th>
                    <th>Time</th>
                    <th>Notes</th>
                  </tr>
                </thead>
                <tbody>
                  {logs.map(log => (
                    <tr key={log.id}>
                      <td style={{ fontWeight: 600, color: 'var(--text-primary)' }}>
                        {new Date(log.logDate).toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' })}
                      </td>
                      <td>
                        {log.completed
                          ? <span className="badge badge-success">✓ Done</span>
                          : <span className="badge badge-danger">✗ Skipped</span>
                        }
                      </td>
                      <td>{log.mood ? <span className="chip chip-mood">{MOOD_EMOJI[log.mood] || '😶'} {log.mood}</span> : '–'}</td>
                      <td>{log.weather ? <span className="chip chip-weather">{WEATHER_EMOJI[log.weather] || '🌡️'} {log.weather}</span> : '–'}</td>
                      <td>{log.location ? <span className="chip chip-location">{LOCATION_EMOJI[log.location] || '📍'} {log.location}</span> : '–'}</td>
                      <td>{log.timeOfDay ? <span className="chip chip-time">{TIME_EMOJI[log.timeOfDay] || '⏰'} {log.timeOfDay}</span> : '–'}</td>
                      <td style={{ maxWidth: 200, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>
                        {log.notes || <span style={{ color: 'var(--text-muted)' }}>–</span>}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>
      </main>
    </div>
  );
}
