export default function StreakBadge({ streak, size = 'md' }) {
  if (!streak || streak === 0) {
    return (
      <span className={`badge badge-accent`} style={{ fontSize: size === 'sm' ? '0.7rem' : '0.75rem' }}>
        🌱 No streak
      </span>
    );
  }

  const getStrengthClass = (s) => {
    if (s >= 30) return 'badge-danger';
    if (s >= 14) return 'badge-warning';
    if (s >= 7) return 'badge-success';
    return 'badge-accent';
  };

  const getFlame = (s) => {
    if (s >= 30) return '🔥🔥🔥';
    if (s >= 14) return '🔥🔥';
    return '🔥';
  };

  return (
    <span
      className={`badge ${getStrengthClass(streak)}`}
      style={{
        fontSize: size === 'sm' ? '0.7rem' : '0.75rem',
        animation: streak >= 7 ? 'pulse 2s infinite' : 'none',
      }}
    >
      {getFlame(streak)} {streak} day{streak !== 1 ? 's' : ''}
    </span>
  );
}
