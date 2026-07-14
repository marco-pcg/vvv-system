import { useState } from 'react'
import { useNavigate, useSearchParams } from 'react-router-dom'
import { useAuth } from '../contexts/AuthContext'
import { Button } from '../components/ui/Button'
import { Input } from '../components/ui/Input'
import { Mail, Lock, Key, AlertCircle, CheckCircle2 } from 'lucide-react'

export function ForgotPassword() {
  const navigate = useNavigate()
  const [searchParams] = useSearchParams()
  const { forgotPassword, resetPassword } = useAuth()
  
  const redirect = searchParams.get('redirect') || '/'

  const [step, setStep] = useState<1 | 2 | 3>(1)
  const [email, setEmail] = useState('')
  const [code, setCode] = useState('')
  const [newPassword, setNewPassword] = useState('')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [successMsg, setSuccessMsg] = useState('')

  const handleRequest = async (e: React.FormEvent) => {
    e.preventDefault()
    setError(null)
    setLoading(true)
    try {
      const msg = await forgotPassword(email)
      setSuccessMsg(msg)
      setStep(2)
    } catch (err: any) {
      setError(err.message || 'E-mail não encontrado no sistema')
    } finally {
      setLoading(false)
    }
  }

  const handleReset = async (e: React.FormEvent) => {
    e.preventDefault()
    setError(null)
    setLoading(true)
    try {
      await resetPassword({ email, code, newPassword })
      setStep(3)
    } catch (err: any) {
      setError(err.message || 'Código de verificação incorreto ou erro ao alterar a senha')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="min-h-screen flex items-center justify-center bg-background p-6">
      <div className="w-full max-w-md bg-white p-8 rounded-2xl border border-gray-100 shadow-xl space-y-6">
        
        {step === 1 && (
          <>
            <div className="text-center space-y-2">
              <h1 className="text-3xl font-bold text-text-primary">Recuperar Senha</h1>
              <p className="text-text-secondary text-sm">
                Digite o e-mail associado à sua conta para receber um código de recuperação
              </p>
            </div>

            {error && (
              <div className="bg-red-50 text-danger border border-red-200 p-4 rounded-xl flex items-start gap-3 text-sm">
                <AlertCircle size={18} className="shrink-0 mt-0.5" />
                <span>{error}</span>
              </div>
            )}

            <form onSubmit={handleRequest} className="space-y-4">
              <Input
                label="Endereço de E-mail"
                type="email"
                placeholder="Ex: seuemail@dominio.com"
                icon={<Mail size={18} />}
                value={email}
                onChange={e => setEmail(e.target.value)}
                required
                disabled={loading}
              />

              <Button type="submit" className="w-full" size="lg" disabled={loading}>
                {loading ? 'Enviando...' : 'Solicitar Código'}
              </Button>
            </form>
          </>
        )}

        {step === 2 && (
          <>
            <div className="text-center space-y-2">
              <h1 className="text-3xl font-bold text-text-primary">Redefinir Senha</h1>
              <p className="text-text-secondary text-sm">
                Insira o código enviado e sua nova senha
              </p>
              {successMsg && (
                <div className="bg-blue-50 text-blue-800 p-3 rounded-xl text-xs text-left">
                  {successMsg}
                </div>
              )}
            </div>

            {error && (
              <div className="bg-red-50 text-danger border border-red-200 p-4 rounded-xl flex items-start gap-3 text-sm">
                <AlertCircle size={18} className="shrink-0 mt-0.5" />
                <span>{error}</span>
              </div>
            )}

            <form onSubmit={handleReset} className="space-y-4">
              <Input
                label="Código de Verificação"
                placeholder="Ex: 123456"
                icon={<Key size={18} />}
                value={code}
                onChange={e => setCode(e.target.value)}
                required
                disabled={loading}
              />

              <Input
                label="Nova Senha"
                type="password"
                placeholder="Mínimo 4 caracteres"
                icon={<Lock size={18} />}
                value={newPassword}
                onChange={e => setNewPassword(e.target.value)}
                required
                disabled={loading}
              />

              <Button type="submit" className="w-full" size="lg" disabled={loading}>
                {loading ? 'Redefinindo...' : 'Alterar Senha'}
              </Button>
            </form>
          </>
        )}

        {step === 3 && (
          <div className="text-center space-y-6 py-4">
            <div className="flex justify-center text-success">
              <CheckCircle2 size={64} />
            </div>
            <div className="space-y-2">
              <h2 className="text-2xl font-bold text-text-primary">Senha Alterada!</h2>
              <p className="text-text-secondary text-sm">
                Sua senha foi redefinida com sucesso. Você já pode fazer login com suas novas credenciais.
              </p>
            </div>
            <Button
              onClick={() => navigate(`/login?redirect=${encodeURIComponent(redirect)}`)}
              className="w-full"
              size="lg"
            >
              Ir para o Login
            </Button>
          </div>
        )}

        {step !== 3 && (
          <div className="text-center">
            <button
              onClick={() => navigate(`/login?redirect=${encodeURIComponent(redirect)}`)}
              className="text-sm font-medium text-text-secondary hover:text-primary-500 transition-colors"
            >
              Voltar para o Login
            </button>
          </div>
        )}
      </div>
    </div>
  )
}
