import { Link, NavLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function Navbar() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const initials = user?.name
    ? user.name.split(' ').map(n => n[0]).join('').toUpperCase().slice(0, 2)
    : '?';

  return (
    <nav className="navbar">
      <Link to="/" className="navbar-brand">
        <span className="brand-icon">⚡</span>
        HabitTrigger
      </Link>

      <div className="navbar-links">
        <NavLink
          to="/"
          end
          className={({ isActive }) => `nav-link${isActive ? ' active' : ''}`}
        >
          Dashboard
        </NavLink>
        <NavLink
          to="/habits"
          className={({ isActive }) => `nav-link${isActive ? ' active' : ''}`}
        >
          My Habits
        </NavLink>
      </div>

      <div className="navbar-user">
        <span style={{ fontSize: '0.875rem', color: 'var(--text-muted)' }}>
          {user?.name}
        </span>
        <div className="user-avatar">{initials}</div>
        <button className="btn btn-ghost btn-sm" onClick={handleLogout} id="btn-logout">
          Logout
        </button>
      </div>
    </nav>
  );
}
