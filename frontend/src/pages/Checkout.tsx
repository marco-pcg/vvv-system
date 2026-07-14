import { useState, useEffect } from 'react'
import { useNavigate, useSearchParams } from 'react-router-dom'
import { useAuth } from '../contexts/AuthContext'
import { Button } from '../components/ui/Button'
import { Input } from '../components/ui/Input'
import { Navbar } from '../components/Navbar'
import {
  User, Mail, Phone, MapPin, Calendar, CheckCircle,
  ArrowRight, ShieldCheck, CreditCard as CardIcon, ChevronRight, AlertCircle
} from 'lucide-react'

interface Viagem {
  id: number
  cidadeOrigem: { nome: string; uf: string }
  cidadeDestino: { nome: string; uf: string }
  partida: string
  chegada: string
  preco: number
  status: string
  modais: Array<{
    tipo: 'ONIBUS' | 'AVIAO' | 'TREM' | 'NAVIO'
    transportadora: { nome: string }
  }>
}

export function Checkout() {
  const navigate = useNavigate()
  const [searchParams] = useSearchParams()
  const { user, isAuthenticated, token, loading: authLoading } = useAuth()

  const viagemId = searchParams.get('viagemId')

  const [viagem, setViagem] = useState<Viagem | null>(null)
  const [loadingViagem, setLoadingViagem] = useState(true)
  const [errorViagem, setErrorViagem] = useState<string | null>(null)

  // Stepper state
  const [step, setStep] = useState<1 | 2 | 3>(1)

  // Step 1: Passenger Form
  const [passengerData, setPassengerData] = useState({
    nome: '',
    cpf: '',
    email: '',
    telefone: '',
    cep: '',
    dataNascimento: '',
    possuiAcompanhante: false,
  })
  const [savingPassenger, setSavingPassenger] = useState(false)
  const [passengerId, setPassengerId] = useState<number | null>(null)

  // Step 2: Payment Form
  const [paymentData, setPaymentData] = useState({
    tipoPagamento: 'CREDITO' as 'CREDITO' | 'DEBITO',
    numeroCartao: '',
    parcelas: 1,
  })
  const [submittingBooking, setSubmittingBooking] = useState(false)
  const [errorBooking, setErrorBooking] = useState<string | null>(null)

  // Step 3: Success details
  const [bookingResult, setBookingResult] = useState<{
    reservaId: number
    codigo: string
    status: string
    mensagem: string
  } | null>(null)

  // 1. Guard check for Authentication
  useEffect(() => {
    if (!authLoading && !isAuthenticated) {
      navigate(`/login?redirect=${encodeURIComponent(`/checkout?viagemId=${viagemId}`)}`)
    }
  }, [isAuthenticated, authLoading, navigate, viagemId])

  // 2. Fetch selected Trip details
  useEffect(() => {
    if (viagemId && isAuthenticated) {
      setLoadingViagem(true)
      fetch(`/api/viagens/${viagemId}`, {
        headers: { 'Authorization': `Bearer ${token}` }
      })
        .then(res => {
          if (!res.ok) throw new Error('Erro ao obter dados da viagem')
          return res.json()
        })
        .then(data => {
          setViagem(data)
        })
        .catch(err => {
          console.error(err)
          setErrorViagem('Não foi possível carregar as informações da viagem selecionada.')
        })
        .finally(() => setLoadingViagem(false))
    }
  }, [viagemId, isAuthenticated, token])

  // Helper to copy logged-in User profile to Passenger Form
  const handleUseMyData = () => {
    if (user) {
      setPassengerData({
        nome: user.nome || '',
        cpf: user.cpf || '',
        email: user.email || '',
        telefone: user.telefone || '',
        cep: user.cep || '',
        dataNascimento: user.dataNascimento || '',
        possuiAcompanhante: false,
      })
    }
  }

  // Handle passenger save
  const handlePassengerSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setErrorBooking(null)

    const cleanCpf = passengerData.cpf.replace(/\D/g, '')
    if (cleanCpf.length !== 11) {
      setErrorBooking('O CPF do passageiro deve conter exatamente 11 dígitos numéricos.')
      return
    }

    const cleanPhone = passengerData.telefone.replace(/\D/g, '')
    if (cleanPhone && (cleanPhone.length < 10 || cleanPhone.length > 11)) {
      setErrorBooking('O telefone deve conter 10 ou 11 dígitos numéricos.')
      return
    }

    const cleanCep = passengerData.cep.replace(/\D/g, '')
    if (cleanCep && cleanCep.length !== 8) {
      setErrorBooking('O CEP deve conter exatamente 8 dígitos numéricos.')
      return
    }

    setSavingPassenger(true)
    try {
      const res = await fetch('/passageiros', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({
          ...passengerData,
          cpf: cleanCpf,
          telefone: cleanPhone || undefined,
          cep: cleanCep || undefined,
          dataNascimento: passengerData.dataNascimento,
        })
      })

      const responseData = await res.json()
      if (!res.ok) {
        throw new Error(responseData.message || 'Erro ao registrar informações do passageiro.')
      }

      setPassengerId(responseData.data.id)
      setStep(2) // Move to payment
    } catch (err: any) {
      setErrorBooking(err.message || 'Erro de rede ao salvar passageiro.')
    } finally {
      setSavingPassenger(false)
    }
  }

  // Handle payment and reservation submission
  const handlePaymentSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setErrorBooking(null)

    const cleanCard = paymentData.numeroCartao.replace(/\D/g, '')
    if (cleanCard.length < 13 || cleanCard.length > 19) {
      setErrorBooking('Número de cartão inválido. Insira entre 13 e 19 dígitos.')
      return
    }

    if (!viagem || !user || !passengerId) return

    setSubmittingBooking(true)
    try {
      const res = await fetch('/api/vendas-online/solicitar', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({
          reserva: {
            idViagem: viagem.id,
            idCliente: user.id,
            idPassageiro: passengerId,
          },
          tipoPagamento: paymentData.tipoPagamento,
          numeroCartao: cleanCard,
          parcelas: paymentData.tipoPagamento === 'CREDITO' ? Number(paymentData.parcelas) : 1
        })
      })

      if (!res.ok) {
        const responseData = await res.json()
        throw new Error(responseData.message || 'Erro ao processar sua venda.')
      }

      const responseData = await res.json()
      setBookingResult(responseData)
      setStep(3) // Success Screen
    } catch (err: any) {
      setErrorBooking(err.message || 'Erro ao processar pagamento ou registrar a reserva.')
    } finally {
      setSubmittingBooking(false)
    }
  }

  const formatDateTime = (dateString: string) => {
    const d = new Date(dateString)
    return d.toLocaleString('pt-BR', { dateStyle: 'short', timeStyle: 'short' })
  }

  const getPrecoFinal = () => {
    if (!viagem) return 0
    if (!passengerData.dataNascimento || !passengerData.possuiAcompanhante) {
      return viagem.preco
    }

    // Calcular idade do passageiro na data da partida da viagem
    const birthDate = new Date(passengerData.dataNascimento)
    const travelDate = new Date(viagem.partida)

    let age = travelDate.getFullYear() - birthDate.getFullYear()
    const monthDiff = travelDate.getMonth() - birthDate.getMonth()
    if (monthDiff < 0 || (monthDiff === 0 && travelDate.getDate() < birthDate.getDate())) {
      age--
    }

    // RN14: Crianças de 2 a 10 anos acompanhadas ganham 40% de desconto
    if (age >= 2 && age <= 10) {
      return viagem.preco * 0.60
    }

    return viagem.preco
  }

  if (authLoading || loadingViagem) {
    return (
      <div className="min-h-screen bg-background flex flex-col">
        <Navbar />
        <div className="flex-1 flex items-center justify-center">
          <div className="text-gray-400">Verificando dados da viagem...</div>
        </div>
      </div>
    )
  }

  if (errorViagem || !viagem) {
    return (
      <div className="min-h-screen bg-background flex flex-col">
        <Navbar />
        <div className="flex-1 flex items-center justify-center p-6">
          <div className="max-w-md w-full text-center space-y-4">
            <div className="text-danger flex justify-center"><AlertCircle size={48} /></div>
            <h2 className="text-xl font-bold text-text-primary">Ops! Algo deu errado</h2>
            <p className="text-text-secondary">{errorViagem || 'Viagem inválida'}</p>
            <Button onClick={() => navigate('/')} className="w-full">Voltar para a Home</Button>
          </div>
        </div>
      </div>
    )
  }

  return (
    <div className="min-h-screen bg-background flex flex-col">
      <Navbar />
      <div className="max-w-6xl mx-auto w-full p-6 grid grid-cols-1 lg:grid-cols-3 gap-8">

        {/* Left Column: Checkout steps */}
        <div className="lg:col-span-2 space-y-6">
          {/* Stepper Header */}
          <div className="bg-white p-4 rounded-2xl border border-gray-100 shadow-sm flex items-center gap-2 text-sm font-semibold text-text-secondary">
            <span className={step === 1 ? 'text-primary-600 font-bold' : step > 1 ? 'text-success' : ''}>1. Passageiro</span>
            <ChevronRight size={16} />
            <span className={step === 2 ? 'text-primary-600 font-bold' : step > 2 ? 'text-success' : ''}>2. Pagamento</span>
            <ChevronRight size={16} />
            <span className={step === 3 ? 'text-primary-600 font-bold' : ''}>3. Confirmação</span>
          </div>

          {errorBooking && (
            <div className="bg-red-50 text-danger border border-red-200 p-4 rounded-xl flex items-start gap-3 text-sm">
              <AlertCircle size={18} className="shrink-0 mt-0.5" />
              <span>{errorBooking}</span>
            </div>
          )}

          {/* Step 1 Form */}
          {step === 1 && (
            <div className="bg-white p-6 rounded-2xl border border-gray-100 shadow-md space-y-6">
              <div className="flex justify-between items-center border-b border-gray-100 pb-4">
                <div>
                  <h2 className="text-2xl font-bold text-text-primary">Identificação do Passageiro</h2>
                  <p className="text-text-secondary text-sm">Quem viajará nesta reserva?</p>
                </div>
                <Button variant="outline" size="sm" onClick={handleUseMyData}>
                  Usar Meus Dados
                </Button>
              </div>

              <form onSubmit={handlePassengerSubmit} className="space-y-4">
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div className="md:col-span-2">
                    <Input
                      label="Nome Completo *"
                      placeholder="Ex: Maria de Souza"
                      icon={<User size={18} />}
                      value={passengerData.nome}
                      onChange={e => setPassengerData(prev => ({ ...prev, nome: e.target.value }))}
                      required
                      disabled={savingPassenger}
                    />
                  </div>

                  <Input
                    label="CPF *"
                    placeholder="Digite o CPF"
                    icon={<CardIcon size={18} />}
                    value={passengerData.cpf}
                    onChange={e => setPassengerData(prev => ({ ...prev, cpf: e.target.value }))}
                    required
                    disabled={savingPassenger}
                  />

                  <Input
                    label="E-mail *"
                    type="email"
                    placeholder="Ex: email@dominio.com"
                    icon={<Mail size={18} />}
                    value={passengerData.email}
                    onChange={e => setPassengerData(prev => ({ ...prev, email: e.target.value }))}
                    required
                    disabled={savingPassenger}
                  />

                  <Input
                    label="Telefone"
                    placeholder="Ex: 21999999999"
                    icon={<Phone size={18} />}
                    value={passengerData.telefone}
                    onChange={e => setPassengerData(prev => ({ ...prev, telefone: e.target.value }))}
                    disabled={savingPassenger}
                  />

                  <Input
                    label="CEP"
                    placeholder="Ex: 20000000"
                    icon={<MapPin size={18} />}
                    value={passengerData.cep}
                    onChange={e => setPassengerData(prev => ({ ...prev, cep: e.target.value }))}
                    disabled={savingPassenger}
                  />

                  <Input
                    label="Data de Nascimento *"
                    type="date"
                    icon={<Calendar size={18} />}
                    value={passengerData.dataNascimento}
                    onChange={e => setPassengerData(prev => ({ ...prev, dataNascimento: e.target.value }))}
                    required
                    disabled={savingPassenger}
                  />

                  <div className="flex items-center gap-2 py-3 md:col-span-2">
                    <input
                      type="checkbox"
                      id="possuiAcompanhante"
                      checked={passengerData.possuiAcompanhante}
                      onChange={e => setPassengerData(prev => ({ ...prev, possuiAcompanhante: e.target.checked }))}
                      className="h-4 w-4 text-primary-600 focus:ring-primary-500 border-gray-300 rounded cursor-pointer"
                      disabled={savingPassenger}
                    />
                    <label htmlFor="possuiAcompanhante" className="text-sm font-medium text-text-primary cursor-pointer select-none">
                      Passageiro possui acompanhante que necessita de assistência especial
                    </label>
                  </div>
                </div>

                <div className="pt-4 flex justify-end">
                  <Button type="submit" size="lg" disabled={savingPassenger}>
                    {savingPassenger ? 'Registrando...' : 'Ir para o Pagamento'} <ArrowRight size={18} className="ml-2" />
                  </Button>
                </div>
              </form>
            </div>
          )}

          {/* Step 2 Form */}
          {step === 2 && (
            <div className="bg-white p-6 rounded-2xl border border-gray-100 shadow-md space-y-6">
              <div className="border-b border-gray-100 pb-4">
                <h2 className="text-2xl font-bold text-text-primary">Método de Pagamento</h2>
                <p className="text-text-secondary text-sm">Selecione e insira as informações do cartão</p>
              </div>

              <form onSubmit={handlePaymentSubmit} className="space-y-6">
                <div className="flex gap-4">
                  <button
                    type="button"
                    onClick={() => setPaymentData(prev => ({ ...prev, tipoPagamento: 'CREDITO' }))}
                    className={`flex-1 p-4 rounded-xl border-2 flex items-center justify-center gap-2 font-bold transition-all ${paymentData.tipoPagamento === 'CREDITO'
                        ? 'border-primary-500 bg-blue-50/50 text-primary-600'
                        : 'border-gray-200 text-text-secondary hover:border-gray-300'
                      }`}
                    disabled={submittingBooking}
                  >
                    <CardIcon size={20} /> Crédito
                  </button>
                  <button
                    type="button"
                    onClick={() => setPaymentData(prev => ({ ...prev, tipoPagamento: 'DEBITO' }))}
                    className={`flex-1 p-4 rounded-xl border-2 flex items-center justify-center gap-2 font-bold transition-all ${paymentData.tipoPagamento === 'DEBITO'
                        ? 'border-primary-500 bg-blue-50/50 text-primary-600'
                        : 'border-gray-200 text-text-secondary hover:border-gray-300'
                      }`}
                    disabled={submittingBooking}
                  >
                    <CardIcon size={20} /> Débito
                  </button>
                </div>

                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div className="md:col-span-2">
                    <Input
                      label="Número do Cartão *"
                      placeholder="XXXX XXXX XXXX XXXX"
                      icon={<CardIcon size={18} />}
                      value={paymentData.numeroCartao}
                      onChange={e => setPaymentData(prev => ({ ...prev, numeroCartao: e.target.value }))}
                      required
                      disabled={submittingBooking}
                    />
                  </div>

                  {paymentData.tipoPagamento === 'CREDITO' && (
                    <div className="w-full">
                      <label className="block text-sm font-medium text-text-primary mb-1.5">Parcelas *</label>
                      <select
                        value={paymentData.parcelas}
                        onChange={e => setPaymentData(prev => ({ ...prev, parcelas: Number(e.target.value) }))}
                        className="flex h-10 w-full rounded-xl border border-gray-300 bg-white px-3 py-2 text-sm text-text-primary focus:outline-none focus:ring-2 focus:ring-primary-500/20 focus:border-primary-500 disabled:opacity-50 transition-all cursor-pointer"
                        disabled={submittingBooking}
                        required
                      >
                        <option value={1}>1x de R$ {getPrecoFinal().toFixed(2)} (Sem juros)</option>
                        <option value={2}>2x de R$ {(getPrecoFinal() / 2).toFixed(2)} (Sem juros)</option>
                        <option value={3}>3x de R$ {(getPrecoFinal() / 3).toFixed(2)} (Sem juros)</option>
                      </select>
                    </div>
                  )}
                </div>

                <div className="bg-surface-50 p-4 rounded-xl border border-gray-200 flex items-start gap-3">
                  <ShieldCheck className="text-success shrink-0 mt-0.5" size={20} />
                  <p className="text-xs text-text-secondary leading-relaxed">
                    Pagamento 100% seguro. Seus dados de pagamento são criptografados de ponta a ponta e nunca armazenados em nossos servidores.
                  </p>
                </div>

                <div className="pt-4 flex justify-between items-center">
                  <Button type="button" variant="ghost" onClick={() => setStep(1)} disabled={submittingBooking}>
                    Voltar
                  </Button>
                  <Button type="submit" size="lg" disabled={submittingBooking}>
                    {submittingBooking ? 'Processando...' : 'Confirmar Compra'}
                  </Button>
                </div>
              </form>
            </div>
          )}

          {/* Step 3 Confirmation Success */}
          {step === 3 && bookingResult && (
            <div className="bg-white p-8 rounded-2xl border border-gray-100 shadow-md text-center space-y-6">
              <div className="flex justify-center text-success">
                <CheckCircle size={72} />
              </div>

              <div className="space-y-2">
                <h2 className="text-3xl font-bold text-text-primary">Reserva Solicitada!</h2>
                <p className="text-text-secondary text-sm max-w-md mx-auto">
                  {bookingResult.mensagem || 'Sua compra foi registrada com sucesso e está aguardando aprovação.'}
                </p>
              </div>

              <div className="bg-surface-50 p-6 rounded-2xl border border-gray-100 max-w-sm mx-auto space-y-3 text-left">
                <div className="flex justify-between border-b border-gray-200 pb-2">
                  <span className="text-text-secondary text-sm">Código da Reserva</span>
                  <span className="font-bold text-primary-600 text-sm">{bookingResult.codigo}</span>
                </div>
                <div className="flex justify-between pb-2">
                  <span className="text-text-secondary text-sm">Status</span>
                  <span className="font-semibold text-warning text-sm uppercase">{bookingResult.status}</span>
                </div>
                <div className="flex justify-between text-xs text-gray-400">
                  <span>Passageiro</span>
                  <span>{passengerData.nome}</span>
                </div>
              </div>

              <div className="pt-6 flex flex-col sm:flex-row gap-4 justify-center">
                <Button onClick={() => navigate('/dashboard/reservas')} variant="outline">
                  Ver Minhas Reservas
                </Button>
                <Button onClick={() => navigate('/')}>
                  Voltar para Home
                </Button>
              </div>
            </div>
          )}
        </div>

        {/* Right Column: Sticky Trip Summary */}
        <div className="space-y-6">
          <div className="bg-white p-6 rounded-2xl border border-gray-100 shadow-md space-y-4 sticky top-6">
            <h3 className="font-bold text-lg text-text-primary border-b border-gray-100 pb-3">
              Resumo da Viagem
            </h3>

            <div className="space-y-4">
              <div className="space-y-1">
                <span className="text-xs text-text-secondary font-semibold uppercase tracking-wider">Origem</span>
                <p className="font-bold text-text-primary">{viagem.cidadeOrigem.nome}, {viagem.cidadeOrigem.uf}</p>
                <p className="text-xs text-text-secondary">{formatDateTime(viagem.partida)}</p>
              </div>

              <div className="space-y-1">
                <span className="text-xs text-text-secondary font-semibold uppercase tracking-wider">Destino</span>
                <p className="font-bold text-text-primary">{viagem.cidadeDestino.nome}, {viagem.cidadeDestino.uf}</p>
                <p className="text-xs text-text-secondary">{formatDateTime(viagem.chegada)}</p>
              </div>

              <div className="border-t border-gray-100 pt-3 space-y-2">
                <div className="flex justify-between text-sm">
                  <span className="text-text-secondary">Transporte:</span>
                  <span className="font-semibold text-text-primary">{viagem.modais[0]?.tipo || 'Ônibus'}</span>
                </div>
                <div className="flex justify-between text-sm">
                  <span className="text-text-secondary">Operadora:</span>
                  <span className="font-semibold text-text-primary">{viagem.modais[0]?.transportadora.nome || 'Viação Exemplo'}</span>
                </div>
              </div>

              <div className="border-t border-gray-100 pt-4 space-y-2">
                {getPrecoFinal() < viagem.preco && (
                  <>
                    <div className="flex justify-between text-sm">
                      <span className="text-text-secondary">Preço Original:</span>
                      <span className="line-through text-gray-400">R$ {viagem.preco.toFixed(2)}</span>
                    </div>
                    <div className="flex justify-between text-sm text-success font-medium">
                      <span>Desconto Especial (40%):</span>
                      <span>- R$ {(viagem.preco * 0.4).toFixed(2)}</span>
                    </div>
                  </>
                )}
                <div className="flex justify-between items-end pt-2 border-t border-dashed border-gray-100">
                  <span className="font-bold text-text-primary">Total da Compra</span>
                  <span className="text-2xl font-black text-primary-500">
                    R$ {getPrecoFinal().toFixed(2)}
                  </span>
                </div>
              </div>
            </div>
          </div>
        </div>

      </div>
    </div>
  )
}
