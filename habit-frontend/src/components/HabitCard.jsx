import { Link } from 'react-router-dom';
import StreakBadge from './StreakBadge';

const CATEGORY_ICONS = {
  Health: '💪',
  Fitness: '🏃',
  Learning: '📚',
  Mindfulness: '🧘',
  Nutrition: '🥗',
  Sleep: '😴',
  Productivity: '⚡',
  Social: '🤝',
  Finance: '💰',
  Creativity: '🎨',
  Default: '✨',
};

export default function HabitCard({ habit, onDelete }) {
  const icon = CATEGORY_ICONS[habit.category] || CATEGORY_ICONS.Default;
  const rate = habit.completionRate || 0;
  const progressColor = rate >= 70 ? 'success' : rate >= 40 ? '' : 'danger';

  return (
    <div className="card fade-in-up" style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
      {/* Header */}
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-3">
          <div style={{
            width: 44, height: 44, borderRadius: 12,
            background: 'rgba(99,102,241,0.12)',
            border: '1px solid rgba(99,102,241,0.2)',
            display: 'flex', alignItems: 'center', justifyContent: 'center',
            fontSize: '1.3rem', flexShrink: 0,
          }}>
            {icon}
          </div>
          <div>
            <h3 style={{ fontSize: '1rem', fontWeight: 700, marginBottom: '0.2rem' }}>
              {habit.name}
            </h3>
            {habit.category && (
              <span className="text-xs text-muted">{habit.category}</span>
            )}
          </div>
        </div>
        <StreakBadge streak={habit.currentStreak} size="sm" />
      </div>

      {/* Stats Row */}
      <div className="flex gap-4" style={{ fontSize: '0.8rem' }}>
        <div>
          <div style={{ color: 'var(--text-muted)', marginBottom: 2 }}>Completion</div>
          <div style={{ fontWeight: 700, color: 'var(--text-primary)' }}>{rate.toFixed(0)}%</div>
        </div>
        <div>
          <div style={{ color: 'var(--text-muted)', marginBottom: 2 }}>Total Logs</div>
          <div style={{ fontWeight: 700, color: 'var(--text-primary)' }}>{habit.totalLogs}</div>
        </div>
        <div>
          <div style={{ color: 'var(--text-muted)', marginBottom: 2 }}>Best Streak</div>
          <div style={{ fontWeight: 700, color: 'var(--text-primary)' }}>{habit.longestStreak}d</div>
        </div>
      </div>

      {/* Progress Bar */}
      <div>
        <div className="progress-bar">
          <div
            className={`progress-fill ${progressColor}`}
            style={{ width: `${Math.min(rate, 100)}%` }}
          />
        </div>
      </div>

      {/* Actions */}
      <div className="flex gap-2 items-center justify-between" style={{ marginTop: 'auto' }}>
        <div className="flex gap-2">
          <Link to={`/habits/${habit.id}`} className="btn btn-ghost btn-sm" id={`btn-view-habit-${habit.id}`}>
            View
          </Link>
          <Link to={`/habits/${habit.id}/log`} className="btn btn-primary btn-sm" id={`btn-log-habit-${habit.id}`}>
            + Log
          </Link>
        </div>
        <Link
          to={`/habits/${habit.id}/insights`}
          className="btn btn-ghost btn-sm"
          id={`btn-insights-habit-${habit.id}`}
          style={{ color: '#818cf8' }}
        >
          Insights →
        </Link>
      </div>
    </div>
  );
}
