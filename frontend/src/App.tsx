import { BrowserRouter, Routes, Route, Outlet } from 'react-router-dom'
import { Home } from './pages/Home'
import { BuscaViagens } from './pages/BuscaViagens'
import { DashboardReservas } from './pages/dashboard/DashboardReservas'
import { Perfil } from './pages/dashboard/Perfil'
import { Login } from './pages/Login'
import { Register } from './pages/Register'
import { ForgotPassword } from './pages/ForgotPassword'
import { Checkout } from './pages/Checkout'
import { AuthProvider } from './contexts/AuthContext'
import { Navbar } from './components/Navbar'

function DashboardLayout() {
  return (
    <div className="min-h-screen bg-surface-50">
      <Navbar />
      <main className="p-6">
        <Outlet />
      </main>
    </div>
  )
}

function MainAppRoutes() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/busca" element={<BuscaViagens />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/forgot-password" element={<ForgotPassword />} />
        <Route path="/checkout" element={<Checkout />} />
        
        {/* Dashboard Routes */}
        <Route path="/dashboard" element={<DashboardLayout />}>
          <Route path="reservas" element={<DashboardReservas />} />
          <Route path="perfil" element={<Perfil />} />
        </Route>
      </Routes>
    </BrowserRouter>
  )
}

function App() {
  return (
    <AuthProvider>
      <MainAppRoutes />
    </AuthProvider>
  )
}

export default App
