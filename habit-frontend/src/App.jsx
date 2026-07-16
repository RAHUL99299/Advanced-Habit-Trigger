import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import ProtectedRoute from './components/ProtectedRoute';
import Login from './pages/Login';
import Register from './pages/Register';
import Dashboard from './pages/Dashboard';
import Habits from './pages/Habits';
import HabitDetail from './pages/HabitDetail';
import LogHabit from './pages/LogHabit';
import Insights from './pages/Insights';

export default function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/" element={<ProtectedRoute><Dashboard /></ProtectedRoute>} />
          <Route path="/habits" element={<ProtectedRoute><Habits /></ProtectedRoute>} />
          <Route path="/habits/:id" element={<ProtectedRoute><HabitDetail /></ProtectedRoute>} />
          <Route path="/habits/:id/log" element={<ProtectedRoute><LogHabit /></ProtectedRoute>} />
          <Route path="/habits/:id/insights" element={<ProtectedRoute><Insights /></ProtectedRoute>} />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}
