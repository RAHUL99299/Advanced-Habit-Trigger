import { useState } from 'react';
import { Link, NavLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

import logo from '../assets/logo.png';

export default function Navbar() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [showLogoutModal, setShowLogoutModal] = useState(false);

  const handleLogoutClick = () => {
    setShowLogoutModal(true);
  };

  const initials = user?.name
    ? user.name.split(' ').map(n => n[0]).join('').toUpperCase().slice(0, 2)
    : '?';

  return (
    <nav className="navbar">
      <Link to="/" className="navbar-brand" style={{ padding: 0 }}>
        <img src={logo} alt="HabitTrigger" className="brand-logo-img" />
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
        <Link to="/profile" className="flex items-center gap-2 nav-profile-link" style={{ textDecoration: 'none', cursor: 'pointer' }}>
          <span className="navbar-user-name" style={{ fontSize: '0.875rem', color: 'var(--text-secondary)' }}>
            {user?.name}
          </span>
          <div className="user-avatar">{initials}</div>
        </Link>
        <button className="btn btn-ghost btn-sm" onClick={handleLogoutClick} id="btn-logout">
          Logout
        </button>
      </div>

      {showLogoutModal && (
        <div className="modal-overlay" onClick={() => setShowLogoutModal(false)}>
          <div className="modal" onClick={(e) => e.stopPropagation()}>
            <h3 style={{ fontSize: '1.25rem', marginBottom: '0.5rem' }}>Confirm Logout</h3>
            <p className="text-secondary" style={{ fontSize: '0.9rem', marginBottom: '1.5rem' }}>
              Are you sure you want to log out of your HabitTrigger account?
            </p>
            <div style={{ display: 'flex', gap: '0.75rem', justifyContent: 'flex-end' }}>
              <button className="btn btn-ghost btn-sm" onClick={() => setShowLogoutModal(false)}>
                Cancel
              </button>
              <button 
                className="btn btn-danger btn-sm" 
                onClick={() => {
                  logout();
                  navigate('/login');
                }}
                id="confirm-logout"
              >
                Log Out
              </button>
            </div>
          </div>
        </div>
      )}
    </nav>
  );
}
