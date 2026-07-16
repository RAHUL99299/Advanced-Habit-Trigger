import { useEffect, useState } from 'react';
import Navbar from '../components/Navbar';
import HabitCard from '../components/HabitCard';
import axiosClient from '../api/axiosClient';

const CATEGORIES = ['Health', 'Fitness', 'Learning', 'Mindfulness', 'Nutrition', 'Sleep', 'Productivity', 'Social', 'Finance', 'Creativity', 'Other'];
const FREQUENCIES = ['daily', 'weekly'];

function AddHabitModal({ onClose, onSave }) {
  const [form, setForm] = useState({ name: '', category: 'Health', targetFrequency: 'daily' });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    try {
      const res = await axiosClient.post('/api/habits', form);
      onSave(res.data);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to create habit.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="modal-overlay" onClick={e => e.target === e.currentTarget && onClose()}>
      <div className="modal">
        <div className="modal-header">
          <h3>Create New Habit</h3>
          <button className="btn btn-ghost btn-icon" onClick={onClose} id="btn-close-modal">✕</button>
        </div>

        {error && <div className="alert alert-error">{error}</div>}

        <form onSubmit={handleSubmit} id="add-habit-form">
          <div className="form-group">
            <label className="form-label">Habit Name *</label>
            <input
              id="habit-name-input"
              type="text"
              className="form-control"
              placeholder="e.g. Morning Run"
              value={form.name}
              onChange={e => setForm(f => ({ ...f, name: e.target.value }))}
              required
              autoFocus
            />
          </div>

          <div className="grid-2">
            <div className="form-group">
              <label className="form-label">Category</label>
              <select
                id="habit-category-select"
                className="form-control"
                value={form.category}
                onChange={e => setForm(f => ({ ...f, category: e.target.value }))}
              >
                {CATEGORIES.map(c => <option key={c}>{c}</option>)}
              </select>
            </div>
            <div className="form-group">
              <label className="form-label">Frequency</label>
              <select
                id="habit-frequency-select"
                className="form-control"
                value={form.targetFrequency}
                onChange={e => setForm(f => ({ ...f, targetFrequency: e.target.value }))}
              >
                {FREQUENCIES.map(f => <option key={f}>{f}</option>)}
              </select>
            </div>
          </div>

          <div className="flex gap-3 justify-between" style={{ marginTop: '1.5rem' }}>
            <button type="button" className="btn btn-ghost" onClick={onClose}>Cancel</button>
            <button id="btn-save-habit" type="submit" className="btn btn-primary" disabled={loading}>
              {loading ? 'Creating…' : 'Create Habit'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default function Habits() {
  const [habits, setHabits] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);

  useEffect(() => {
    axiosClient.get('/api/habits')
      .then(res => setHabits(res.data))
      .catch(console.error)
      .finally(() => setLoading(false));
  }, []);

  const handleSave = (newHabit) => {
    setHabits(prev => [newHabit, ...prev]);
    setShowModal(false);
  };

  const handleDelete = async (habitId) => {
    if (!window.confirm('Delete this habit and all its data?')) return;
    await axiosClient.delete(`/api/habits/${habitId}`);
    setHabits(prev => prev.filter(h => h.id !== habitId));
  };

  return (
    <div className="page-layout">
      <Navbar />
      <main className="page-content">
        <div className="page-header">
          <div>
            <h1 className="page-title">My Habits</h1>
            <p className="page-subtitle">{habits.length} habit{habits.length !== 1 ? 's' : ''} tracked</p>
          </div>
          <button
            id="btn-add-habit"
            className="btn btn-primary"
            onClick={() => setShowModal(true)}
          >
            + Add Habit
          </button>
        </div>

        {loading ? (
          <div className="loading-page"><div className="spinner" style={{ width: 36, height: 36, borderWidth: 3 }} /></div>
        ) : habits.length === 0 ? (
          <div className="empty-state card">
            <div className="empty-icon">🌱</div>
            <h3>No habits yet</h3>
            <p>Start by creating your first habit to track.</p>
            <button className="btn btn-primary" onClick={() => setShowModal(true)} id="btn-add-first-habit">
              + Create My First Habit
            </button>
          </div>
        ) : (
          <div className="grid-auto stagger">
            {habits.map(habit => (
              <HabitCard key={habit.id} habit={habit} onDelete={handleDelete} />
            ))}
          </div>
        )}
      </main>

      {showModal && (
        <AddHabitModal onClose={() => setShowModal(false)} onSave={handleSave} />
      )}
    </div>
  );
}
