import { Routes, Route, Navigate } from 'react-router-dom'
import { StoreProvider } from './store'
import Layout from './components/Layout/Layout'
import Login from './pages/Login'
import Dashboard from './pages/Dashboard'
import SupplierPool from './pages/SupplierPool'
import Category from './pages/Category'
import Assessment from './pages/Assessment'
import DepartmentScore from './pages/DepartmentScore'
import MeetingNote from './pages/MeetingNote'

function PrivateRoute({ children }) {
  const token = localStorage.getItem('token')
  if (!token) {
    return <Navigate to="/login" />
  }
  return children
}

function PublicRoute({ children }) {
  const token = localStorage.getItem('token')
  if (token) {
    return <Navigate to="/dashboard" />
  }
  return children
}

function App() {
  return (
    <StoreProvider>
      <Routes>
        <Route path="/login" element={<PublicRoute><Login /></PublicRoute>} />
        <Route path="/dashboard" element={<PrivateRoute><Layout><Dashboard /></Layout></PrivateRoute>} />
        <Route path="/supplier/pool" element={<PrivateRoute><Layout><SupplierPool /></Layout></PrivateRoute>} />
        <Route path="/category" element={<PrivateRoute><Layout><Category /></Layout></PrivateRoute>} />
        <Route path="/assessment" element={<PrivateRoute><Layout><Assessment /></Layout></PrivateRoute>} />
        <Route path="/department-score" element={<PrivateRoute><Layout><DepartmentScore /></Layout></PrivateRoute>} />
        <Route path="/meeting-note" element={<PrivateRoute><Layout><MeetingNote /></Layout></PrivateRoute>} />
        <Route path="/*" element={<PrivateRoute><Layout><Dashboard /></Layout></PrivateRoute>} />
      </Routes>
    </StoreProvider>
  )
}

export default App