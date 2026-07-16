import { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import Navbar from '../components/Navbar';
import InsightChart from '../components/InsightChart';
import axiosClient from '../api/axiosClient';

const TYPE_COLORS = {
  MOOD: { bg: 'rgba(139,92,246,0.12)', border: 'rgba(139,92,246,0.25)', text: '#a78bfa', emoji: '😶' },
  WEATHER: { bg: 'rgba(59,130,246,0.12)', border: 'rgba(59,130,246,0.25)', text: '#60a5fa', emoji: '🌤️' },
  LOCATION: { bg: 'rgba(16,185,129,0.12)', border: 'rgba(16,185,129,0.25)', text: '#34d399', emoji: '📍' },
  TIME: { bg: 'rgba(245,158,11,0.12)', border: 'rgba(245,158,11,0.25)', text: '#fbbf24', emoji: '⏰' },
};

export default function Insights() {
  const { id } = useParams();
  const [habit, setHabit] = useState(null);
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    Promise.all([
      axiosClient.get(`/api/habits/${id}`),
      axiosClient.get(`/api/habits/${id}/insights`),
    ]).then(([habitRes, insightRes]) => {
      setHabit(habitRes.data);
      setResult(insightRes.data);
    }).catch(err => {
      setError(err.response?.data?.message || 'Failed to load insights.');
    }).finally(() => setLoading(false));
  }, [id]);

  const handleRefresh = () => {
    setLoading(true);
    axiosClient.get(`/api/habits/${id}/insights`)
      .then(res => setResult(res.data))
      .catch(err => setError(err.response?.data?.message || 'Failed to refresh.'))
      .finally(() => setLoading(false));
  };

  if (loading) return (
    <div className="page-layout">
      <Navbar />
      <div className="loading-page">
        <div className="spinner" style={{ width: 40, height: 40, borderWidth: 3 }} />
        <p style={{ color: 'var(--text-muted)' }}>Analyzing trigger patterns…</p>
      </div>
    </div>
  );

  // Group impacts by trigger type
  const grouped = {};
  (result?.impacts || []).forEach(impact => {
    if (!grouped[impact.triggerType]) grouped[impact.triggerType] = [];
    grouped[impact.triggerType].push(impact);
  });

  return (
    <div className="page-layout">
      <Navbar />
      <main className="page-content">
        {/* Header */}
        <div className="flex items-center justify-between" style={{ marginBottom: '1.5rem', flexWrap: 'wrap', gap: '1rem' }}>
          <div>
            <Link to={`/habits/${id}`} className="btn btn-ghost btn-sm" style={{ marginBottom: '0.5rem', display: 'inline-flex' }} id="btn-back-detail">
              ← Back
            </Link>
            <h1 className="page-title">💡 Insights</h1>
            {habit && <p className="page-subtitle">Trigger analysis for: <strong style={{ color: 'var(--text-primary)' }}>{habit.name}</strong></p>}
          </div>
          <button
            id="btn-refresh-insights"
            className="btn btn-ghost"
            onClick={handleRefresh}
            disabled={loading}
          >
            🔄 Re-analyze
          </button>
        </div>

        {error && <div className="alert alert-error">{error}</div>}

        {!result || result.impacts?.length === 0 ? (
          <div className="card empty-state">
            <div className="empty-icon">🔍</div>
            <h3>Not enough data yet</h3>
            <p>{result?.summary || 'Log this habit at least 5–10 times with context to generate insights.'}</p>
            <Link to={`/habits/${id}/log`} className="btn btn-primary" id="btn-go-log">
              ✏️ Log Now
            </Link>
          </div>
        ) : (
          <>
            {/* Top Insight Banner */}
            <div className="card card-accent fade-in-up" style={{ marginBottom: '1.5rem', background: 'rgba(99,102,241,0.08)' }}>
              <div className="flex items-center gap-3">
                <div style={{ fontSize: '2rem' }}>🏆</div>
                <div>
                  <div style={{ fontSize: '0.75rem', color: '#818cf8', fontWeight: 600, textTransform: 'uppercase', letterSpacing: '0.08em', marginBottom: '0.25rem' }}>
                    Top Insight
                  </div>
                  <div style={{ fontSize: '1.05rem', color: 'var(--text-primary)', fontWeight: 500 }}>
                    {result.topInsightText}
                  </div>
                  {result.summary && (
                    <div style={{ fontSize: '0.85rem', color: 'var(--text-muted)', marginTop: '0.5rem' }}>
                      {result.summary}
                    </div>
                  )}
                </div>
              </div>
            </div>

            {/* Chart */}
            <div className="card fade-in-up" style={{ marginBottom: '1.5rem' }}>
              <h3 style={{ marginBottom: '1.5rem' }}>Impact Score by Trigger</h3>
              <InsightChart impacts={result.impacts} />
            </div>

            {/* Grouped by Trigger Type */}
            <div className="grid-2 stagger" style={{ marginBottom: '1.5rem' }}>
              {Object.entries(grouped).map(([type, impacts]) => {
                const style = TYPE_COLORS[type] || TYPE_COLORS.MOOD;
                return (
                  <div key={type} className="card fade-in-up" style={{ borderColor: style.border }}>
                    <h3 style={{ fontSize: '0.9rem', marginBottom: '1rem', color: style.text }}>
                      {style.emoji} {type} Triggers
                    </h3>
                    <div style={{ display: 'flex', flexDirection: 'column', gap: '0.75rem' }}>
                      {impacts
                        .sort((a, b) => b.impactScore - a.impactScore)
                        .map(impact => (
                          <div key={impact.triggerValue}>
                            <div className="flex justify-between items-center" style={{ marginBottom: '0.35rem' }}>
                              <span style={{ fontSize: '0.875rem', color: 'var(--text-primary)', fontWeight: 500, textTransform: 'capitalize' }}>
                                {impact.triggerValue}
                              </span>
                              <div className="flex items-center gap-2">
                                <span style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>{impact.sampleSize} logs</span>
                                <span style={{ fontSize: '0.875rem', fontWeight: 700, color: style.text }}>
                                  {Math.round(impact.impactScore * 100)}%
                                </span>
                              </div>
                            </div>
                            <div className="progress-bar">
                              <div
                                style={{
                                  height: '100%',
                                  width: `${Math.round(impact.impactScore * 100)}%`,
                                  background: `linear-gradient(90deg, ${style.text}aa, ${style.text})`,
                                  borderRadius: 'var(--radius-full)',
                                  transition: 'width 0.8s cubic-bezier(0.4,0,0.2,1)',
                                }}
                              />
                            </div>
                          </div>
                        ))}
                    </div>
                  </div>
                );
              })}
            </div>

            {/* Insight Sentences */}
            <div className="card fade-in-up">
              <h3 style={{ marginBottom: '1.25rem' }}>📋 All Insights</h3>
              <div style={{ display: 'flex', flexDirection: 'column', gap: '0.6rem' }}>
                {result.impacts.map((impact, i) => {
                  const style = TYPE_COLORS[impact.triggerType] || {};
                  return (
                    <div
                      key={i}
                      className="flex items-center gap-3"
                      style={{
                        padding: '0.75rem',
                        borderRadius: 'var(--radius)',
                        background: style.bg,
                        border: `1px solid ${style.border}`,
                      }}
                    >
                      <span style={{ fontSize: '1.1rem', flexShrink: 0 }}>{style.emoji}</span>
                      <span style={{ fontSize: '0.875rem', color: 'var(--text-secondary)' }}>
                        {impact.insightText}
                      </span>
                      <span style={{ marginLeft: 'auto', fontWeight: 700, color: style.text, flexShrink: 0, fontSize: '0.875rem' }}>
                        {Math.round(impact.impactScore * 100)}%
                      </span>
                    </div>
                  );
                })}
              </div>
            </div>
          </>
        )}
      </main>
    </div>
  );
}
