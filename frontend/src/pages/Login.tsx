import { useState, useEffect } from 'react'
import { useNavigate, useSearchParams } from 'react-router-dom'
import { useAuth } from '../contexts/AuthContext'
import { Button } from '../components/ui/Button'
import { Input } from '../components/ui/Input'
import { User, Lock, AlertCircle } from 'lucide-react'

export function Login() {
  const navigate = useNavigate()
  const [searchParams] = useSearchParams()
  const { login, isAuthenticated, loading: authLoading } = useAuth()
  
  const redirect = searchParams.get('redirect') || '/'

  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    if (isAuthenticated) {
      navigate(redirect)
    }
  }, [isAuthenticated, navigate, redirect])

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setError(null)
    setLoading(true)
    try {
      await login(username, password)
      navigate(redirect)
    } catch (err: any) {
      setError(err.message || 'Falha na autenticação. Verifique seu usuário e senha.')
    } finally {
      setLoading(false)
    }
  }

  if (authLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-background">
        <div className="text-gray-400">Carregando...</div>
      </div>
    )
  }

  return (
    <div className="min-h-screen flex items-center justify-center bg-background p-6">
      <div className="w-full max-w-md bg-white p-8 rounded-2xl border border-gray-100 shadow-xl space-y-6">
        <div className="text-center space-y-2">
          <h1 className="text-3xl font-bold text-text-primary">Entrar no VVV</h1>
          <p className="text-text-secondary text-sm">
            Para continuar, insira seu usuário e senha
          </p>
        </div>

        {error && (
          <div className="bg-red-50 text-danger border border-red-200 p-4 rounded-xl flex items-start gap-3 text-sm">
            <AlertCircle size={18} className="shrink-0 mt-0.5" />
            <span>{error}</span>
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-4">
          <Input
            label="Usuário"
            placeholder="Digite seu usuário"
            icon={<User size={18} />}
            value={username}
            onChange={e => setUsername(e.target.value)}
            required
            disabled={loading}
          />

          <div className="space-y-1">
            <Input
              label="Senha"
              type="password"
              placeholder="Digite sua senha"
              icon={<Lock size={18} />}
              value={password}
              onChange={e => setPassword(e.target.value)}
              required
              disabled={loading}
            />
            <div className="text-right">
              <button
                type="button"
                onClick={() => navigate(`/forgot-password?redirect=${encodeURIComponent(redirect)}`)}
                className="text-sm font-medium text-primary-500 hover:text-primary-600 transition-colors"
              >
                Esqueceu sua senha?
              </button>
            </div>
          </div>

          <Button type="submit" className="w-full" size="lg" disabled={loading}>
            {loading ? 'Entrando...' : 'Entrar'}
          </Button>
        </form>

        <div className="text-center text-sm text-text-secondary pt-2">
          Não tem uma conta?{' '}
          <button
            onClick={() => navigate(`/register?redirect=${encodeURIComponent(redirect)}`)}
            className="font-bold text-primary-500 hover:text-primary-600 transition-colors"
          >
            Criar conta
          </button>
        </div>
      </div>
    </div>
  )
}
