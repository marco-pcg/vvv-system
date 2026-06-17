import { useEffect, useState } from "react"
import { useAuth } from "../../contexts/AuthContext"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "../../components/ui/Table"
import { Button } from "../../components/ui/Button"
import { CheckCircle2, XCircle, AlertCircle } from "lucide-react"

interface Reserva {
  id: number
  codigo: string
  idViagem: number
  idCliente: number
  idPassageiro: number
  dataCriacao: string
  status: 'PENDENTE' | 'CONFIRMADA' | 'AGUARDANDO_APROVACAO' | 'CANCELADA' | 'EXPIRADA'
  valorTotal: number
}

export function DashboardAprovacoes() {
  const { token } = useAuth()
  const [reservas, setReservas] = useState<Reserva[]>([])
  const [loading, setLoading] = useState(true)
  const [actioningId, setActioningId] = useState<number | null>(null)
  const [error, setError] = useState<string | null>(null)
  const [successMsg, setSuccessMsg] = useState<string | null>(null)

  const fetchReservas = async () => {
    try {
      setLoading(true)
      setError(null)
      const res = await fetch("/api/reservas", {
        headers: {
          "Authorization": `Bearer ${token}`
        }
      })
      if (!res.ok) {
        throw new Error("Erro ao carregar reservas.")
      }
      const data: Reserva[] = await res.json()
      // Filtra apenas as reservas que aguardam aprovação
      const pendentes = data.filter(r => r.status === "AGUARDANDO_APROVACAO")
      setReservas(pendentes)
    } catch (err: any) {
      console.error(err)
      setError("Não foi possível carregar as reservas para aprovação.")
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    if (token) {
      fetchReservas()
    }
  }, [token])

  const handleAprovar = async (reservaId: number) => {
    setActioningId(reservaId)
    setError(null)
    setSuccessMsg(null)
    try {
      const res = await fetch(`/api/gerenciamento-vendas/${reservaId}/aprovar`, {
        method: "PUT",
        headers: {
          "Authorization": `Bearer ${token}`
        }
      })

      const responseData = await res.json()
      if (!res.ok) {
        throw new Error(responseData.message || "Erro ao aprovar a reserva.")
      }

      setSuccessMsg(`Reserva #${reservaId} aprovada com sucesso! Ticket emitido.`)
      // Re-carrega a lista
      await fetchReservas()
    } catch (err: any) {
      setError(err.message || "Erro ao aprovar a reserva.")
    } finally {
      setActioningId(null)
    }
  }

  const handleRejeitar = async (reservaId: number) => {
    setActioningId(reservaId)
    setError(null)
    setSuccessMsg(null)
    try {
      const res = await fetch(`/api/gerenciamento-vendas/${reservaId}/rejeitar`, {
        method: "PUT",
        headers: {
          "Authorization": `Bearer ${token}`
        }
      })

      const responseData = await res.json()
      if (!res.ok) {
        throw new Error(responseData.message || "Erro ao rejeitar a reserva.")
      }

      setSuccessMsg(`Reserva #${reservaId} rejeitada com sucesso (cancelamento aplicado).`)
      // Re-carrega a lista
      await fetchReservas()
    } catch (err: any) {
      setError(err.message || "Erro ao rejeitar a reserva.")
    } finally {
      setActioningId(null)
    }
  }

  return (
    <div className="max-w-6xl mx-auto space-y-6">
      {/* Header */}
      <div className="border-b border-gray-200 pb-4">
        <h1 className="text-2xl font-bold text-text-primary">Painel de Aprovações</h1>
        <p className="text-text-secondary">Gerencie e valide as reservas virtuais solicitadas pelos clientes.</p>
      </div>

      {/* Feedbacks */}
      {error && (
        <div className="bg-red-50 text-danger border border-red-200 p-4 rounded-xl flex items-start gap-3 text-sm animate-in fade-in">
          <AlertCircle size={18} className="shrink-0 mt-0.5" />
          <span>{error}</span>
        </div>
      )}

      {successMsg && (
        <div className="bg-emerald-50 text-success border border-emerald-200 p-4 rounded-xl flex items-start gap-3 text-sm animate-in fade-in">
          <CheckCircle2 size={18} className="shrink-0 mt-0.5" />
          <span>{successMsg}</span>
        </div>
      )}

      {/* Table / Content */}
      {loading ? (
        <div className="text-center py-12 text-gray-400">Buscando solicitações...</div>
      ) : reservas.length > 0 ? (
        <div className="bg-surface rounded-2xl border border-gray-200 shadow-sm overflow-hidden">
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Código</TableHead>
                <TableHead>Cliente ID</TableHead>
                <TableHead>Passageiro ID</TableHead>
                <TableHead>Viagem ID</TableHead>
                <TableHead>Data Criação</TableHead>
                <TableHead>Valor</TableHead>
                <TableHead className="text-center">Ações</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {reservas.map(res => (
                <TableRow key={res.id}>
                  <TableCell className="font-mono font-bold text-primary-600">{res.codigo}</TableCell>
                  <TableCell className="text-xs text-text-secondary">#{res.idCliente}</TableCell>
                  <TableCell className="text-xs text-text-secondary">#{res.idPassageiro}</TableCell>
                  <TableCell className="text-xs text-text-secondary">#{res.idViagem}</TableCell>
                  <TableCell className="text-sm">
                    {new Date(res.dataCriacao).toLocaleDateString('pt-BR', { hour: '2-digit', minute: '2-digit' })}
                  </TableCell>
                  <TableCell className="font-bold text-accent-500">
                    {new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(res.valorTotal)}
                  </TableCell>
                  <TableCell>
                    <div className="flex gap-2 justify-center">
                      <Button
                        size="sm"
                        variant="primary"
                        onClick={() => handleAprovar(res.id)}
                        disabled={actioningId !== null}
                        className="bg-success hover:bg-green-700 rounded-xl"
                      >
                        <CheckCircle2 size={14} className="mr-1" />
                        {actioningId === res.id ? 'Aprovando...' : 'Aprovar'}
                      </Button>
                      <Button
                        size="sm"
                        variant="outline"
                        onClick={() => handleRejeitar(res.id)}
                        disabled={actioningId !== null}
                        className="text-danger border-danger hover:bg-red-50 hover:text-danger rounded-xl"
                      >
                        <XCircle size={14} className="mr-1" />
                        {actioningId === res.id ? 'Recusando...' : 'Rejeitar'}
                      </Button>
                    </div>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </div>
      ) : (
        <div className="bg-white p-16 rounded-2xl border border-gray-200 text-center space-y-4 max-w-xl mx-auto mt-6 shadow-sm">
          <div className="mx-auto w-12 h-12 rounded-full bg-emerald-50 text-success flex items-center justify-center">
            <CheckCircle2 size={28} />
          </div>
          <h3 className="text-xl font-bold text-text-primary">Tudo em dia!</h3>
          <p className="text-text-secondary text-sm">
            Não há nenhuma reserva aguardando aprovação no momento. Bom trabalho!
          </p>
        </div>
      )}
    </div>
  )
}
