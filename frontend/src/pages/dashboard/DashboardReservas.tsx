import { useEffect, useState } from "react"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "../../components/ui/Table"
import { Badge } from "../../components/ui/Badge"
import { Button } from "../../components/ui/Button"
import { useNavigate } from "react-router-dom"
import { useAuth } from "../../contexts/AuthContext"
import { Ticket as TicketIcon, QrCode, Printer, X, MapPin, User, AlertCircle, Compass } from "lucide-react"

interface TicketData {
  id: number
  numero: string
  assento: string
  idReserva: number
  codigoReserva: string
  passageiroNome: string
  passageiroCpf: string
  origemCidade: string
  destinoCidade: string
  dataPartida: string
  dataChegada: string
}

export function DashboardReservas() {
  const navigate = useNavigate()
  const { token } = useAuth()
  const [reservas, setReservas] = useState<any[]>([])
  const [loading, setLoading] = useState(true)

  // Ticket modal state
  const [selectedTicket, setSelectedTicket] = useState<TicketData | null>(null)
  const [loadingTicket, setLoadingTicket] = useState(false)
  const [errorTicket, setErrorTicket] = useState<string | null>(null)

  const fetchReservas = () => {
    if (!token) return
    setLoading(true)
    fetch("/api/reservas/minhas", {
      headers: {
        "Authorization": `Bearer ${token}`
      }
    })
      .then(res => {
        if (!res.ok) throw new Error("Não autorizado")
        return res.json()
      })
      .then(data => setReservas(data))
      .catch(err => {
        console.error(err)
        navigate("/login")
      })
      .finally(() => setLoading(false))
  }

  useEffect(() => {
    fetchReservas()
  }, [token, navigate])

  const openTicketModal = async (reservaId: number) => {
    setLoadingTicket(true)
    setErrorTicket(null)
    try {
      const res = await fetch(`/api/tickets/reserva/${reservaId}`, {
        headers: {
          "Authorization": `Bearer ${token}`
        }
      })
      if (!res.ok) {
        throw new Error("Não foi possível carregar o ticket da reserva.")
      }
      const data: TicketData = await res.json()
      setSelectedTicket(data)
    } catch (err: any) {
      console.error(err)
      setErrorTicket(err.message || "Erro ao consultar o ticket.")
    } finally {
      setLoadingTicket(false)
    }
  }

  const handlePrint = () => {
    window.print()
  }

  return (
    <div className="max-w-5xl mx-auto space-y-6">
      {/* Estilos CSS embutidos para impressão do ticket */}
      <style dangerouslySetInnerHTML={{__html: `
        @media print {
          body * {
            visibility: hidden !important;
          }
          #printable-ticket, #printable-ticket * {
            visibility: visible !important;
          }
          #printable-ticket {
            position: absolute !important;
            left: 50% !important;
            top: 50% !important;
            transform: translate(-50%, -50%) !important;
            width: 100% !important;
            max-width: 600px !important;
            background: white !important;
            box-shadow: none !important;
            border: 2px dashed #e2e8f0 !important;
            padding: 20px !important;
            border-radius: 16px !important;
            color: black !important;
          }
          .no-print {
            display: none !important;
          }
        }
      `}} />

      <div className="flex justify-between items-center border-b border-gray-200 pb-4">
        <div>
          <h1 className="text-2xl font-bold text-text-primary">Minhas Reservas</h1>
          <p className="text-text-secondary">Gerencie suas passagens e viagens futuras.</p>
        </div>
        <Button variant="outline" onClick={() => navigate("/")}>Nova Viagem</Button>
      </div>

      {errorTicket && (
        <div className="bg-red-50 text-danger border border-red-200 p-4 rounded-xl flex items-start gap-3 text-sm animate-in fade-in">
          <AlertCircle size={18} className="shrink-0 mt-0.5" />
          <span>{errorTicket}</span>
        </div>
      )}

      {loading ? (
        <div className="text-center py-10 text-gray-400">Carregando reservas...</div>
      ) : reservas.length > 0 ? (
        <div className="bg-surface rounded-2xl border border-gray-200 shadow-sm overflow-hidden">
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Código</TableHead>
                <TableHead>Viagem</TableHead>
                <TableHead>Data Criação</TableHead>
                <TableHead>Status</TableHead>
                <TableHead className="text-right">Valor Total</TableHead>
                <TableHead className="text-center">Ações</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {reservas.map(res => (
                <TableRow key={res.id}>
                  <TableCell className="font-mono font-bold text-primary-600">{res.codigo}</TableCell>
                  <TableCell>Viagem #{res.idViagem}</TableCell>
                  <TableCell>{new Date(res.dataCriacao).toLocaleDateString('pt-BR')}</TableCell>
                  <TableCell>
                    <Badge variant={res.status === 'CONFIRMADA' ? 'success' : res.status === 'CANCELADA' ? 'danger' : 'warning'}>
                      {res.status === 'AGUARDANDO_APROVACAO' ? 'AGUARDANDO APROVAÇÃO' : res.status}
                    </Badge>
                  </TableCell>
                  <TableCell className="text-right text-accent-500 font-bold">
                    {new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(res.valorTotal)}
                  </TableCell>
                  <TableCell>
                    <div className="flex justify-center">
                      {res.status === 'CONFIRMADA' ? (
                        <Button
                          size="sm"
                          variant="primary"
                          onClick={() => openTicketModal(res.id)}
                          disabled={loadingTicket}
                          className="bg-primary-500 text-white rounded-xl shadow-xs"
                        >
                          <TicketIcon size={14} className="mr-1" /> Ver Bilhete
                        </Button>
                      ) : (
                        <span className="text-text-secondary text-xs">-</span>
                      )}
                    </div>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </div>
      ) : (
        <div className="bg-white p-10 rounded-2xl border border-gray-200 text-center space-y-4">
          <h3 className="text-xl font-bold text-text-primary">Você não possui nenhuma viagem</h3>
          <p className="text-text-secondary">Que tal explorar nossos destinos e planejar sua próxima aventura?</p>
          <Button variant="primary" onClick={() => navigate("/")}>Explorar Destinos</Button>
        </div>
      )}

      {/* Ticket Modal Overlay */}
      {selectedTicket && (
        <div className="fixed inset-0 z-50 bg-black/50 backdrop-blur-sm flex items-center justify-center p-4 animate-in fade-in">
          <div className="bg-white w-full max-w-xl rounded-3xl shadow-2xl border border-gray-100 overflow-hidden flex flex-col max-h-[90vh]">
            {/* Modal Header (no-print) */}
            <div className="p-6 border-b border-gray-100 flex justify-between items-center bg-gray-50 no-print">
              <span className="font-extrabold text-lg tracking-wider text-primary-600 flex items-center gap-1.5">
                <Compass className="h-5 w-5 animate-spin-slow text-primary-500" /> Bilhete de Embarque VVV
              </span>
              <button 
                onClick={() => setSelectedTicket(null)}
                className="text-text-secondary hover:text-text-primary bg-white p-1.5 rounded-xl border border-gray-200 hover:bg-gray-50 transition-colors cursor-pointer"
              >
                <X size={18} />
              </button>
            </div>

            {/* Scrollable container for modal scroll on small screens */}
            <div className="flex-1 overflow-y-auto p-6 flex items-center justify-center">
              {/* Boarding Pass Design */}
              <div 
                id="printable-ticket" 
                className="w-full bg-gradient-to-b from-white to-slate-50/50 rounded-2xl border border-gray-200 shadow-sm relative overflow-hidden"
              >
                {/* Visual cutout circles for coupon design */}
                <div className="absolute top-1/2 -left-3 w-6 h-6 bg-slate-900 md:bg-black/50 rounded-full border-r border-gray-200 transform -translate-y-1/2 no-print"></div>
                <div className="absolute top-1/2 -right-3 w-6 h-6 bg-slate-900 md:bg-black/50 rounded-full border-l border-gray-200 transform -translate-y-1/2 no-print"></div>

                {/* Ticket Top Part: Trip info */}
                <div className="p-6 pb-4 border-b border-dashed border-gray-300">
                  <div className="flex justify-between items-start mb-6">
                    <div>
                      <span className="text-xs text-text-secondary uppercase tracking-widest font-mono">Código da Reserva</span>
                      <span className="block text-lg font-bold text-primary-600 font-mono mt-0.5">{selectedTicket.codigoReserva}</span>
                    </div>
                    <div className="text-right">
                      <span className="text-xs text-text-secondary uppercase tracking-widest font-mono">Número do Ticket</span>
                      <span className="block text-lg font-bold text-text-primary font-mono mt-0.5">{selectedTicket.numero}</span>
                    </div>
                  </div>

                  {/* Route details */}
                  <div className="flex items-center justify-between gap-4 py-2">
                    <div className="flex-1">
                      <span className="text-xs text-text-secondary uppercase tracking-widest font-mono flex items-center gap-1"><MapPin size={12} className="text-primary-500" /> Origem</span>
                      <strong className="block text-xl text-text-primary tracking-tight mt-1">{selectedTicket.origemCidade}</strong>
                      <span className="text-xs text-text-secondary block mt-1 font-mono">
                        {new Date(selectedTicket.dataPartida).toLocaleString('pt-BR', { dateStyle: 'short', timeStyle: 'short' })}
                      </span>
                    </div>
                    <div className="flex flex-col items-center justify-center px-4">
                      <div className="w-12 h-0.5 bg-gray-300 relative">
                        <div className="absolute top-1/2 left-1/2 w-2 h-2 rounded-full bg-primary-500 transform -translate-x-1/2 -translate-y-1/2"></div>
                      </div>
                    </div>
                    <div className="flex-1 text-right">
                      <span className="text-xs text-text-secondary uppercase tracking-widest font-mono flex items-center gap-1 justify-end"><MapPin size={12} className="text-accent-500" /> Destino</span>
                      <strong className="block text-xl text-text-primary tracking-tight mt-1">{selectedTicket.destinoCidade}</strong>
                      <span className="text-xs text-text-secondary block mt-1 font-mono">
                        {new Date(selectedTicket.dataChegada).toLocaleString('pt-BR', { dateStyle: 'short', timeStyle: 'short' })}
                      </span>
                    </div>
                  </div>
                </div>

                {/* Ticket Bottom Part: Passenger & seat details */}
                <div className="p-6 pt-5 grid grid-cols-2 md:grid-cols-3 gap-6 items-end">
                  <div className="col-span-2">
                    <span className="text-xs text-text-secondary uppercase tracking-widest font-mono flex items-center gap-1"><User size={12} /> Passageiro</span>
                    <strong className="block text-text-primary truncate mt-1">{selectedTicket.passageiroNome}</strong>
                    <span className="text-xs text-text-secondary font-mono mt-1 block">CPF: {selectedTicket.passageiroCpf}</span>
                  </div>
                  <div>
                    <span className="text-xs text-text-secondary uppercase tracking-widest font-mono flex items-center gap-1"><QrCode size={12} /> Assento</span>
                    <strong className="block text-2xl text-accent-500 font-extrabold tracking-wide mt-1">{selectedTicket.assento}</strong>
                  </div>
                </div>
              </div>
            </div>

            {/* Modal Footer (no-print) */}
            <div className="p-6 border-t border-gray-100 bg-gray-50 flex gap-3 justify-end no-print">
              <Button 
                variant="outline" 
                onClick={() => setSelectedTicket(null)}
                className="rounded-xl font-semibold"
              >
                Fechar
              </Button>
              <Button 
                variant="accent" 
                onClick={handlePrint}
                className="rounded-xl font-semibold flex items-center gap-2 shadow-md shadow-accent-500/10"
              >
                <Printer size={16} /> Exportar PDF / Imprimir
              </Button>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}
