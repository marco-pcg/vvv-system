import { useState, useEffect } from 'react'
import { useNavigate, useSearchParams } from 'react-router-dom'
import { useAuth } from '../contexts/AuthContext'
import { Button } from '../components/ui/Button'
import { Input } from '../components/ui/Input'
import { User, Lock, Mail, CreditCard, Phone, MapPin, Calendar, AlertCircle } from 'lucide-react'

export function Register() {
  const navigate = useNavigate()
  const [searchParams] = useSearchParams()
  const { register, isAuthenticated, loading: authLoading } = useAuth()

  const redirect = searchParams.get('redirect') || '/'

  const [formData, setFormData] = useState({
    username: '',
    password: '',
    nome: '',
    cpf: '',
    email: '',
    telefone: '',
    cep: '',
    dataNascimento: '',
  })

  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    if (isAuthenticated) {
      navigate(redirect)
    }
  }, [isAuthenticated, navigate, redirect])

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData(prev => ({
      ...prev,
      [e.target.name]: e.target.value,
    }))
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setError(null)

    // CPF validation (11 digits)
    const cleanCpf = formData.cpf.replace(/\D/g, '')
    if (cleanCpf.length !== 11) {
      setError('O CPF deve conter exatamente 11 dígitos numéricos.')
      return
    }

    // Phone validation (10 or 11 digits)
    const cleanPhone = formData.telefone.replace(/\D/g, '')
    if (cleanPhone && (cleanPhone.length < 10 || cleanPhone.length > 11)) {
      setError('O telefone deve conter 10 ou 11 dígitos numéricos.')
      return
    }

    // CEP validation (8 digits)
    const cleanCep = formData.cep.replace(/\D/g, '')
    if (cleanCep && cleanCep.length !== 8) {
      setError('O CEP deve conter exatamente 8 dígitos numéricos.')
      return
    }

    setLoading(true)
    try {
      await register({
        ...formData,
        cpf: cleanCpf,
        telefone: cleanPhone || undefined,
        cep: cleanCep || undefined,
        dataNascimento: formData.dataNascimento || undefined,
      })
      navigate(redirect)
    } catch (err: any) {
      setError(err.message || 'Ocorreu um erro no registro. Verifique os dados inseridos.')
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
      <div className="w-full max-w-2xl bg-white p-8 rounded-2xl border border-gray-100 shadow-xl space-y-6">
        <div className="text-center space-y-2">
          <h1 className="text-3xl font-bold text-text-primary">Criar Conta no VVV</h1>
          <p className="text-text-secondary text-sm">
            Cadastre-se para reservar viagens e emitir seus tickets online
          </p>
        </div>

        {error && (
          <div className="bg-red-50 text-danger border border-red-200 p-4 rounded-xl flex items-start gap-3 text-sm">
            <AlertCircle size={18} className="shrink-0 mt-0.5" />
            <span>{error}</span>
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-6">
          <div className="border-b border-gray-100 pb-4">
            <h3 className="font-bold text-text-primary mb-3 text-sm uppercase tracking-wide">Credenciais de Acesso</h3>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <Input
                label="Nome de Usuário *"
                name="username"
                placeholder="Ex: joaosilva"
                icon={<User size={18} />}
                value={formData.username}
                onChange={handleChange}
                required
                disabled={loading}
              />
              <Input
                label="Senha *"
                name="password"
                type="password"
                placeholder="Senha (mín. 4 caracteres)"
                icon={<Lock size={18} />}
                value={formData.password}
                onChange={handleChange}
                required
                disabled={loading}
              />
            </div>
          </div>

          <div>
            <h3 className="font-bold text-text-primary mb-3 text-sm uppercase tracking-wide">Dados Cadastrais</h3>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div className="md:col-span-2">
                <Input
                  label="Nome Completo *"
                  name="nome"
                  placeholder="Digite seu nome completo"
                  icon={<User size={18} />}
                  value={formData.nome}
                  onChange={handleChange}
                  required
                  disabled={loading}
                />
              </div>

              <Input
                label="CPF (apenas números) *"
                name="cpf"
                placeholder="Digite seu CPF"
                icon={<CreditCard size={18} />}
                value={formData.cpf}
                onChange={handleChange}
                required
                disabled={loading}
              />

              <Input
                label="E-mail *"
                name="email"
                type="email"
                placeholder="Ex: seuemail@dominio.com"
                icon={<Mail size={18} />}
                value={formData.email}
                onChange={handleChange}
                required
                disabled={loading}
              />

              <Input
                label="Telefone (apenas números)"
                name="telefone"
                placeholder="Ex: 21999999999"
                icon={<Phone size={18} />}
                value={formData.telefone}
                onChange={handleChange}
                disabled={loading}
              />

              <Input
                label="CEP (apenas números)"
                name="cep"
                placeholder="Ex: 20000000"
                icon={<MapPin size={18} />}
                value={formData.cep}
                onChange={handleChange}
                disabled={loading}
              />

              <Input
                label="Data de Nascimento *"
                name="dataNascimento"
                type="date"
                icon={<Calendar size={18} />}
                value={formData.dataNascimento}
                onChange={handleChange}
                required
                disabled={loading}
              />
            </div>
          </div>

          <Button type="submit" className="w-full" size="lg" disabled={loading}>
            {loading ? 'Criando Conta...' : 'Concluir Cadastro'}
          </Button>
        </form>

        <div className="text-center text-sm text-text-secondary">
          Já possui uma conta?{' '}
          <button
            onClick={() => navigate(`/login?redirect=${encodeURIComponent(redirect)}`)}
            className="font-bold text-primary-500 hover:text-primary-600 transition-colors"
          >
            Fazer login
          </button>
        </div>
      </div>
    </div>
  )
}
