import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import Navbar from '../components/Navbar';
import { useAuth } from '../context/AuthContext';
import axiosClient from '../api/axiosClient';

export default function Dashboard() {
  const { user } = useAuth();
  const [summary, setSummary] = useState(null);
  const [habits, setHabits] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Promise.all([
      axiosClient.get('/api/dashboard/summary'),
      axiosClient.get('/api/habits'),
    ]).then(([summaryRes, habitsRes]) => {
      setSummary(summaryRes.data);
      setHabits(habitsRes.data.slice(0, 5));
    }).catch(console.error)
      .finally(() => setLoading(false));
  }, []);

  const hour = new Date().getHours();
  const greeting = hour < 12 ? 'Good morning' : hour < 17 ? 'Good afternoon' : 'Good evening';

  return (
    <div className="page-layout">
      <Navbar />
      <main className="page-content">
        {/* Greeting */}
        <div className="fade-in" style={{ marginBottom: '2.5rem' }}>
          <h1 style={{ marginBottom: '0.5rem' }}>
            {greeting}, <span className="text-gradient">{user?.name?.split(' ')[0]}</span> 👋
          </h1>
          <p className="text-secondary">Here's your habit overview for today.</p>
        </div>

        {loading ? (
          <div className="loading-page"><div className="spinner" style={{ width: 36, height: 36, borderWidth: 3 }} /></div>
        ) : (
          <>
            {/* Stats Grid */}
            <div className="grid-4 stagger" style={{ marginBottom: '2.5rem' }}>
              <div className="stat-card fade-in-up">
                <div className="stat-value">{summary?.habitCount ?? 0}</div>
                <div className="stat-label">Active Habits</div>
              </div>
              <div className="stat-card fade-in-up">
                <div className="stat-value">{summary?.completedLogs ?? 0}</div>
                <div className="stat-label">Total Completions</div>
              </div>
              <div className="stat-card fade-in-up">
                <div className="stat-value">{summary?.completionRate ?? 0}%</div>
                <div className="stat-label">Overall Rate</div>
              </div>
              <div className="stat-card fade-in-up">
                <div className="stat-value">{summary?.totalLogs ?? 0}</div>
                <div className="stat-label">Logs Recorded</div>
              </div>
            </div>

            {/* Two-column layout */}
            <div className="grid-2" style={{ alignItems: 'start' }}>
              {/* Recent Habits */}
              <div className="card">
                <div className="flex items-center justify-between mb-4">
                  <h3 style={{ fontSize: '1rem' }}>Recent Habits</h3>
                  <Link to="/habits" className="btn btn-ghost btn-sm" id="btn-view-all-habits">View All</Link>
                </div>

                {habits.length === 0 ? (
                  <div className="empty-state" style={{ padding: '2rem' }}>
                    <div className="empty-icon">🌱</div>
                    <p>No habits yet. Create your first one!</p>
                    <Link to="/habits" className="btn btn-primary btn-sm" id="btn-create-first-habit">
                      Create Habit
                    </Link>
                  </div>
                ) : (
                  <div style={{ display: 'flex', flexDirection: 'column', gap: '0.75rem' }}>
                    {habits.map(habit => (
                      <Link
                        key={habit.id}
                        to={`/habits/${habit.id}`}
                        id={`dashboard-habit-${habit.id}`}
                        style={{ textDecoration: 'none' }}
                      >
                        <div style={{
                          display: 'flex', alignItems: 'center', justifyContent: 'space-between',
                          padding: '0.75rem', borderRadius: 'var(--radius)',
                          background: 'rgba(255,255,255,0.03)', border: '1px solid var(--border)',
                          transition: 'var(--transition)',
                        }}
                          onMouseEnter={e => e.currentTarget.style.background = 'rgba(255,255,255,0.06)'}
                          onMouseLeave={e => e.currentTarget.style.background = 'rgba(255,255,255,0.03)'}
                        >
                          <div>
                            <div style={{ fontWeight: 600, fontSize: '0.9rem', color: 'var(--text-primary)' }}>{habit.name}</div>
                            <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>{habit.totalLogs} logs • {habit.completionRate?.toFixed(0)}% completion</div>
                          </div>
                          <div style={{ textAlign: 'right' }}>
                            <div style={{ fontSize: '0.75rem', color: 'var(--success)' }}>🔥 {habit.currentStreak}d streak</div>
                            <div style={{ marginTop: 4 }}>
                              <div className="progress-bar" style={{ width: 80 }}>
                                <div className="progress-fill" style={{ width: `${Math.min(habit.completionRate || 0, 100)}%` }} />
                              </div>
                            </div>
                          </div>
                        </div>
                      </Link>
                    ))}
                  </div>
                )}
              </div>

              {/* Quick Actions */}
              <div style={{ display: 'flex', flexDirection: 'column', gap: '1.25rem' }}>
                <div className="card card-accent">
                  <h3 style={{ fontSize: '1rem', marginBottom: '1rem' }}>🚀 Quick Actions</h3>
                  <div style={{ display: 'flex', flexDirection: 'column', gap: '0.75rem' }}>
                    <Link to="/habits" id="btn-quick-manage" className="btn btn-primary w-full">
                      📋 Manage My Habits
                    </Link>
                    <Link to="/habits" id="btn-quick-log" className="btn btn-ghost w-full">
                      ✏️ Log Today's Habits
                    </Link>
                  </div>
                </div>

                <div className="card">
                  <h3 style={{ fontSize: '1rem', marginBottom: '0.75rem' }}>💡 How it works</h3>
                  <div style={{ display: 'flex', flexDirection: 'column', gap: '0.75rem' }}>
                    {[
                      { icon: '📝', text: 'Create habits you want to track' },
                      { icon: '📊', text: 'Log each day with mood, weather & location' },
                      { icon: '🔍', text: 'AI analyzes your trigger patterns' },
                      { icon: '🎯', text: 'Get personalized insights to improve' },
                    ].map((item, i) => (
                      <div key={i} className="flex items-center gap-3" style={{ fontSize: '0.875rem' }}>
                        <span style={{ fontSize: '1.1rem' }}>{item.icon}</span>
                        <span style={{ color: 'var(--text-secondary)' }}>{item.text}</span>
                      </div>
                    ))}
                  </div>
                </div>
              </div>
            </div>
          </>
        )}
      </main>
    </div>
  );
}
