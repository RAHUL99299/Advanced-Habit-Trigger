import {
  BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer,
  Cell, Legend
} from 'recharts';

const TYPE_COLORS = {
  MOOD: '#8b5cf6',
  WEATHER: '#3b82f6',
  LOCATION: '#10b981',
  TIME: '#f59e0b',
};

const TYPE_EMOJI = {
  MOOD: '😶',
  WEATHER: '🌤️',
  LOCATION: '📍',
  TIME: '⏰',
};

const CustomTooltip = ({ active, payload }) => {
  if (active && payload && payload.length) {
    const d = payload[0].payload;
    return (
      <div style={{
        background: '#1a1a2e',
        border: '1px solid rgba(255,255,255,0.1)',
        borderRadius: 10,
        padding: '0.75rem 1rem',
        boxShadow: '0 8px 24px rgba(0,0,0,0.4)',
      }}>
        <div style={{ fontWeight: 700, color: 'var(--text-primary)', marginBottom: 4 }}>
          {TYPE_EMOJI[d.triggerType]} {d.triggerValue}
        </div>
        <div style={{ fontSize: '0.875rem', color: '#818cf8' }}>
          {Math.round(d.impactScore * 100)}% completion
        </div>
        <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)', marginTop: 2 }}>
          {d.sampleSize} log{d.sampleSize !== 1 ? 's' : ''}
        </div>
        <div style={{ fontSize: '0.75rem', color: 'var(--text-secondary)', marginTop: 4, maxWidth: 200 }}>
          {d.insightText}
        </div>
      </div>
    );
  }
  return null;
};

export default function InsightChart({ impacts }) {
  if (!impacts || impacts.length === 0) {
    return (
      <div className="empty-state">
        <div className="empty-icon">📊</div>
        <h3>No chart data</h3>
        <p>Log more entries to generate insight charts.</p>
      </div>
    );
  }

  const chartData = impacts.map(impact => ({
    ...impact,
    label: `${impact.triggerValue}`,
    score: Math.round(impact.impactScore * 100),
  }));

  // Group by type for legend
  const types = [...new Set(impacts.map(i => i.triggerType))];

  return (
    <div>
      <ResponsiveContainer width="100%" height={320}>
        <BarChart
          data={chartData}
          margin={{ top: 10, right: 20, left: -10, bottom: 60 }}
          barSize={32}
        >
          <CartesianGrid
            strokeDasharray="3 3"
            stroke="rgba(255,255,255,0.05)"
            vertical={false}
          />
          <XAxis
            dataKey="label"
            tick={{ fill: 'var(--text-muted)', fontSize: 12 }}
            axisLine={false}
            tickLine={false}
            angle={-35}
            textAnchor="end"
            interval={0}
          />
          <YAxis
            tickFormatter={v => `${v}%`}
            tick={{ fill: 'var(--text-muted)', fontSize: 11 }}
            axisLine={false}
            tickLine={false}
            domain={[0, 100]}
          />
          <Tooltip content={<CustomTooltip />} cursor={{ fill: 'rgba(255,255,255,0.04)' }} />
          <Bar dataKey="score" radius={[6, 6, 0, 0]}>
            {chartData.map((entry, index) => (
              <Cell
                key={`cell-${index}`}
                fill={TYPE_COLORS[entry.triggerType] || '#6366f1'}
                fillOpacity={0.9}
              />
            ))}
          </Bar>
        </BarChart>
      </ResponsiveContainer>

      {/* Color legend */}
      <div className="flex gap-4 justify-center" style={{ flexWrap: 'wrap', marginTop: '0.5rem' }}>
        {types.map(type => (
          <div key={type} className="flex items-center gap-2">
            <div style={{
              width: 10, height: 10, borderRadius: '50%',
              background: TYPE_COLORS[type],
            }} />
            <span style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>
              {TYPE_EMOJI[type]} {type}
            </span>
          </div>
        ))}
      </div>
    </div>
  );
}
