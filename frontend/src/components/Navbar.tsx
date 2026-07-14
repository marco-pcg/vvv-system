import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../contexts/AuthContext'
import { Menu, X, LogOut, User, Compass } from 'lucide-react'
import { Button } from './ui/Button'

export function Navbar() {
  const { user, isAuthenticated, logout } = useAuth()
  const navigate = useNavigate()
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false)
  const isGerente = user?.roles?.includes('ROLE_GERENTE') || user?.roles?.includes('ROLE_ADMIN')

  const handleLogout = () => {
    logout()
    setMobileMenuOpen(false)
    navigate('/')
  }

  // Get user initials for avatar
  const getInitials = (name: string) => {
    if (!name) return 'U'
    const parts = name.trim().split(' ')
    if (parts.length > 1) {
      return (parts[0][0] + parts[parts.length - 1][0]).toUpperCase()
    }
    return parts[0].slice(0, 2).toUpperCase()
  }

  return (
    <nav className="sticky top-0 z-50 bg-surface/90 backdrop-blur-md border-b border-gray-100 shadow-sm transition-all duration-300">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between h-16">
          {/* Logo Section */}
          <div className="flex items-center">
            <Link to="/" className="flex items-center gap-2 group">
              <div className="bg-primary-500 text-white p-2 rounded-xl group-hover:scale-105 transition-transform duration-200">
                <Compass className="h-5 w-5 animate-pulse" />
              </div>
              <span className="font-extrabold text-xl tracking-wider text-primary-600 group-hover:text-primary-500 transition-colors duration-200">
                VVV <span className="text-accent-500 text-xs font-semibold uppercase tracking-widest block -mt-1 font-mono">System</span>
              </span>
            </Link>
          </div>

          {/* Desktop Navigation Links */}
          <div className="hidden md:flex items-center space-x-6">
            <Link to="/" className="text-text-secondary hover:text-primary-500 font-medium transition-colors text-sm py-2">
              Buscar Viagens
            </Link>

            {isAuthenticated ? (
              <>
                {isGerente && (
                  <Link to="/dashboard/aprovacoes" className="text-accent-500 hover:text-accent-600 font-semibold text-sm py-2 px-3 bg-orange-50 border border-orange-100 rounded-xl transition-colors flex items-center gap-1">
                    Painel de Aprovações
                  </Link>
                )}
                <Link to="/dashboard/reservas" className="text-text-secondary hover:text-primary-500 font-medium transition-colors text-sm py-2">
                  Minhas Reservas
                </Link>
                <Link to="/dashboard/perfil" className="text-text-secondary hover:text-primary-500 font-medium transition-colors text-sm py-2">
                  Meu Perfil
                </Link>
                
                <span className="h-4 w-px bg-gray-200"></span>
                
                {/* User Profile Widget */}
                <div className="flex items-center gap-3">
                  <div className="flex items-center gap-2">
                    <div className="w-8 h-8 rounded-full bg-primary-500 text-white flex items-center justify-center font-bold text-xs shadow-inner">
                      {user ? getInitials(user.nome) : <User size={14} />}
                    </div>
                    {user && (
                      <span className="text-sm text-text-secondary">
                        Olá, <strong className="text-text-primary font-semibold">{user.nome.split(' ')[0]}</strong>
                      </span>
                    )}
                  </div>
                  
                  <Button 
                    onClick={handleLogout} 
                    variant="ghost" 
                    size="sm" 
                    className="text-danger hover:bg-red-50 hover:text-danger rounded-xl flex items-center gap-1.5 font-semibold text-sm cursor-pointer"
                  >
                    <LogOut size={16} /> Sair
                  </Button>
                </div>
              </>
            ) : (
              <>
                <span className="h-4 w-px bg-gray-200"></span>
                <Link to="/register">
                  <Button variant="ghost" size="sm" className="font-semibold text-sm">
                    Criar conta
                  </Button>
                </Link>
                <Link to="/login">
                  <Button variant="primary" size="sm" className="font-semibold text-sm shadow-md shadow-primary-500/10">
                    Entrar
                  </Button>
                </Link>
              </>
            )}
          </div>

          {/* Mobile Menu Toggle Button */}
          <div className="flex items-center md:hidden">
            <button
              onClick={() => setMobileMenuOpen(!mobileMenuOpen)}
              className="inline-flex items-center justify-center p-2 rounded-xl text-text-secondary hover:text-primary-500 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-inset focus:ring-primary-500 transition-all cursor-pointer"
              aria-expanded="false"
            >
              <span className="sr-only">Abrir menu</span>
              {mobileMenuOpen ? <X className="block h-6 w-6" aria-hidden="true" /> : <Menu className="block h-6 w-6" aria-hidden="true" />}
            </button>
          </div>
        </div>
      </div>

      {/* Mobile Drawer menu */}
      {mobileMenuOpen && (
        <div className="md:hidden border-t border-gray-100 bg-surface animate-in fade-in slide-in-from-top-4 duration-200">
          <div className="px-2 pt-2 pb-4 space-y-1 sm:px-3">
            <Link
              to="/"
              onClick={() => setMobileMenuOpen(false)}
              className="block px-3 py-2.5 rounded-xl text-base font-medium text-text-secondary hover:text-primary-500 hover:bg-gray-50 transition-colors"
            >
              Buscar Viagens
            </Link>

            {isAuthenticated ? (
              <>
                {isGerente && (
                  <Link
                    to="/dashboard/aprovacoes"
                    onClick={() => setMobileMenuOpen(false)}
                    className="block px-3 py-2.5 rounded-xl text-base font-bold text-accent-500 bg-orange-50 border border-orange-100 transition-colors"
                  >
                    Painel de Aprovações
                  </Link>
                )}
                <Link
                  to="/dashboard/reservas"
                  onClick={() => setMobileMenuOpen(false)}
                  className="block px-3 py-2.5 rounded-xl text-base font-medium text-text-secondary hover:text-primary-500 hover:bg-gray-50 transition-colors"
                >
                  Minhas Reservas
                </Link>
                <Link
                  to="/dashboard/perfil"
                  onClick={() => setMobileMenuOpen(false)}
                  className="block px-3 py-2.5 rounded-xl text-base font-medium text-text-secondary hover:text-primary-500 hover:bg-gray-50 transition-colors"
                >
                  Meu Perfil
                </Link>

                <div className="pt-4 pb-2 border-t border-gray-100 px-3 flex items-center justify-between">
                  <div className="flex items-center gap-3">
                    <div className="w-10 h-10 rounded-full bg-primary-500 text-white flex items-center justify-center font-bold text-sm">
                      {user ? getInitials(user.nome) : <User size={16} />}
                    </div>
                    <div>
                      <div className="text-sm font-semibold text-text-primary">{user?.nome}</div>
                      <div className="text-xs text-text-secondary">{user?.email}</div>
                    </div>
                  </div>
                  <button
                    onClick={handleLogout}
                    className="flex items-center gap-1.5 text-danger font-semibold text-sm px-3 py-2 rounded-xl hover:bg-red-50 transition-colors cursor-pointer"
                  >
                    <LogOut size={16} /> Sair
                  </button>
                </div>
              </>
            ) : (
              <div className="pt-4 border-t border-gray-100 flex flex-col gap-2 px-3">
                <Link to="/register" onClick={() => setMobileMenuOpen(false)} className="w-full">
                  <Button variant="outline" className="w-full font-semibold">
                    Criar conta
                  </Button>
                </Link>
                <Link to="/login" onClick={() => setMobileMenuOpen(false)} className="w-full">
                  <Button variant="primary" className="w-full font-semibold">
                    Entrar
                  </Button>
                </Link>
              </div>
            )}
          </div>
        </div>
      )}
    </nav>
  )
}
