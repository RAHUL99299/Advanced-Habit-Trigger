import { useState, useEffect } from 'react';
import Navbar from '../components/Navbar';
import { useAuth } from '../context/AuthContext';
import axiosClient from '../api/axiosClient';

const AVAILABLE_THEMES = [
  { name: 'Nebula Glow (Purple & Bronze)', from: '#a855f7', to: '#f97316', gradient: 'linear-gradient(135deg, #a855f7 0%, #ec4899 50%, #f97316 100%)' },
  { name: 'Aurora Borealis (Teal & Emerald)', from: '#0d9488', to: '#10b981', gradient: 'linear-gradient(135deg, #0d9488 0%, #14b8a6 50%, #10b981 100%)' },
  { name: 'Cyberpunk Neon (Cyan & Pink)', from: '#06b6d4', to: '#ec4899', gradient: 'linear-gradient(135deg, #06b6d4 0%, #3b82f6 50%, #ec4899 100%)' },
  { name: 'Solar Flare (Orange & Red)', from: '#f97316', to: '#ef4444', gradient: 'linear-gradient(135deg, #f97316 0%, #f43f5e 50%, #ef4444 100%)' },
];

export default function Profile() {
  const { user } = useAuth();
  const [name, setName] = useState(user?.name || '');
  const [email] = useState(user?.email || '');
  const [successMsg, setSuccessMsg] = useState('');
  const [selectedTheme, setSelectedTheme] = useState(() => {
    return localStorage.getItem('theme_name') || AVAILABLE_THEMES[0].name;
  });
  const [summary, setSummary] = useState(null);
  const [loadingSummary, setLoadingSummary] = useState(true);

  useEffect(() => {
    axiosClient.get('/api/dashboard/summary')
      .then(res => {
        setSummary(res.data);
      })
      .catch(console.error)
      .finally(() => setLoadingSummary(false));
  }, []);

  const getConsistencyTier = () => {
    if (loadingSummary || !summary) return { label: 'AI CATALYST (PREMIUM)', icon: '⭐', class: 'badge-accent', desc: 'Consistent to their habits' };
    const rate = summary.completionRate ?? 0;
    if (rate >= 80) {
      return { label: 'CONSISTENCY TITAN (PLATINUM)', icon: '🏆', class: 'badge-success', desc: 'Extremely consistent to their habits' };
    } else if (rate >= 50) {
      return { label: 'AI CATALYST (GOLD)', icon: '⭐', class: 'badge-accent', desc: 'Consistent to their habits' };
    } else if (rate >= 20) {
      return { label: 'ACTIVE BUILDER (SILVER)', icon: '⚡', class: 'badge-warning', desc: 'Developing consistent habits' };
    } else {
      return { label: 'HABIT EXPLORER (BRONZE)', icon: '🌱', class: 'badge-danger', desc: 'Starting habit consistency' };
    }
  };

  const tier = getConsistencyTier();

  const initials = name
    ? name.split(' ').map(n => n[0]).join('').toUpperCase().slice(0, 2)
    : '?';

  // Apply theme accent color
  const applyTheme = (theme) => {
    document.documentElement.style.setProperty('--accent-from', theme.from);
    document.documentElement.style.setProperty('--accent-to', theme.to);
    document.documentElement.style.setProperty('--accent-gradient', theme.gradient);
    document.documentElement.style.setProperty('--border-accent', `${theme.from}59`);
    localStorage.setItem('theme_name', theme.name);
    localStorage.setItem('theme_from', theme.from);
    localStorage.setItem('theme_to', theme.to);
    localStorage.setItem('theme_gradient', theme.gradient);
  };

  const handleThemeChange = (themeName) => {
    setSelectedTheme(themeName);
    const themeObj = AVAILABLE_THEMES.find(t => t.name === themeName);
    if (themeObj) {
      applyTheme(themeObj);
    }
  };

  const handleSaveProfile = (e) => {
    e.preventDefault();
    if (!name.trim()) return;

    // Update name in local storage
    const storedUser = localStorage.getItem('habit_user');
    if (storedUser) {
      const parsed = JSON.parse(storedUser);
      parsed.name = name;
      localStorage.setItem('habit_user', JSON.stringify(parsed));
      // Force page reload to sync state or let context handle it
      setSuccessMsg('Profile updated successfully!');
      setTimeout(() => {
        setSuccessMsg('');
        window.location.reload();
      }, 1500);
    }
  };

  return (
    <div className="page-layout">
      <Navbar />
      <main className="page-content fade-in">
        <div className="page-header" style={{ marginBottom: '2.5rem' }}>
          <div>
            <h1 className="page-title">User Profile</h1>
            <p className="page-subtitle">Manage your personal settings, theme accents, and configurations.</p>
          </div>
        </div>

        {successMsg && (
          <div className="alert alert-success" style={{ marginBottom: '1.5rem' }}>
            ✨ {successMsg}
          </div>
        )}

        <div className="grid-2" style={{ alignItems: 'start' }}>
          {/* Profile Details Card */}
          <div className="card">
            <div className="flex flex-col items-center text-center" style={{ padding: '1.5rem 0' }}>
              <div className="user-avatar" style={{
                width: '90px',
                height: '90px',
                fontSize: '2.25rem',
                marginBottom: '1.25rem',
                boxShadow: 'var(--accent-glow)'
              }}>
                {initials}
              </div>
              <h2 style={{ fontSize: '1.5rem', marginBottom: '0.25rem' }}>{name}</h2>
              <div className={`badge ${tier.class}`} style={{ marginBottom: '0.5rem' }}>
                {tier.icon} {tier.label}
              </div>
              <p className="text-xs text-muted" style={{ marginBottom: '1.25rem', fontStyle: 'italic', fontWeight: 500 }}>
                {tier.desc}
              </p>
              <div className="divider" style={{ width: '100%', marginTop: 0 }}></div>
              <div style={{ width: '100%', textAlign: 'left', display: 'flex', flexDirection: 'column', gap: '0.75rem' }}>
                <div className="flex justify-between text-sm">
                  <span style={{ color: 'var(--text-muted)' }}>Role</span>
                  <span className="font-semibold text-secondary">Early Beta Tester</span>
                </div>
                <div className="flex justify-between text-sm">
                  <span style={{ color: 'var(--text-muted)' }}>Joined</span>
                  <span className="font-semibold text-secondary">July 2026</span>
                </div>
                <div className="flex justify-between text-sm">
                  <span style={{ color: 'var(--text-muted)' }}>Triggers Monitored</span>
                  <span className="font-semibold text-secondary">Mood, Location, Weather, Time</span>
                </div>
                <div className="flex justify-between text-sm">
                  <span style={{ color: 'var(--text-muted)' }}>Consistency Score</span>
                  <span className="font-semibold" style={{ color: 'var(--accent-from)', fontWeight: 700 }}>
                    {loadingSummary ? 'Calculating...' : `${summary?.completionRate ?? 0}%`}
                  </span>
                </div>
              </div>
            </div>
          </div>

          {/* Edit Profile & Accent Customizer */}
          <div style={{ display: 'flex', flexDirection: 'column', gap: '1.5rem' }}>
            <div className="card">
              <h3 style={{ fontSize: '1.2rem', marginBottom: '1rem' }}>Edit Profile Information</h3>
              <form onSubmit={handleSaveProfile}>
                <div className="form-group">
                  <label className="form-label" htmlFor="profile-name">Full Name</label>
                  <input
                    id="profile-name"
                    type="text"
                    className="form-control"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    required
                  />
                </div>
                <div className="form-group">
                  <label className="form-label" htmlFor="profile-email">Email Address</label>
                  <input
                    id="profile-email"
                    type="email"
                    className="form-control"
                    value={email}
                    disabled
                    style={{ opacity: 0.6, cursor: 'not-allowed' }}
                  />
                </div>
                <button type="submit" className="btn btn-primary w-full mt-2" id="btn-save-profile">
                  Save Changes
                </button>
              </form>
            </div>

            {/* Accent Theme Selector */}
            <div className="card">
              <h3 style={{ fontSize: '1.2rem', marginBottom: '0.25rem' }}>Theme Customizer</h3>
              <p className="text-sm text-muted" style={{ marginBottom: '1rem' }}>
                Select an accent gradient that fits your personal workspace vibe.
              </p>
              <div style={{ display: 'flex', flexDirection: 'column', gap: '0.75rem' }}>
                {AVAILABLE_THEMES.map(theme => (
                  <button
                    key={theme.name}
                    onClick={() => handleThemeChange(theme.name)}
                    style={{
                      width: '100%',
                      padding: '0.875rem 1rem',
                      background: 'rgba(255,255,255,0.02)',
                      border: selectedTheme === theme.name ? '1px solid var(--accent-from)' : '1px solid var(--border)',
                      borderRadius: 'var(--radius)',
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'space-between',
                      cursor: 'pointer',
                      transition: 'var(--transition)'
                    }}
                    onMouseEnter={(e) => {
                      if (selectedTheme !== theme.name) {
                        e.currentTarget.style.borderColor = 'rgba(255,255,255,0.2)';
                        e.currentTarget.style.background = 'rgba(255,255,255,0.04)';
                      }
                    }}
                    onMouseLeave={(e) => {
                      if (selectedTheme !== theme.name) {
                        e.currentTarget.style.borderColor = 'var(--border)';
                        e.currentTarget.style.background = 'rgba(255,255,255,0.02)';
                      }
                    }}
                  >
                    <span style={{ fontSize: '0.9rem', fontWeight: 600, color: 'var(--text-primary)' }}>
                      {theme.name}
                    </span>
                    <div style={{
                      width: '40px',
                      height: '12px',
                      borderRadius: '4px',
                      background: theme.gradient
                    }} />
                  </button>
                ))}
              </div>
            </div>
          </div>
        </div>
      </main>
    </div>
  );
}
