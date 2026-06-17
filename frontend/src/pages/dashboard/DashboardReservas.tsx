import { useEffect, useState } from "react"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "../../components/ui/Table"
import { Badge } from "../../components/ui/Badge"
import { Button } from "../../components/ui/Button"
import { useNavigate } from "react-router-dom"

export function DashboardReservas() {
  const navigate = useNavigate()
  const [reservas, setReservas] = useState<any[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    // Para MVP usando auth JWT, precisamos enviar o token. 
    // Assumimos que o token está no localStorage e adicionaremos nos headers.
    const token = localStorage.getItem("token")
    if (!token) {
      navigate("/login")
      return
    }

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
        // Redireciona para login em caso de erro 401
        navigate("/login")
      })
      .finally(() => setLoading(false))
  }, [navigate])

  return (
    <div className="max-w-5xl mx-auto space-y-6">
      <div className="flex justify-between items-center border-b border-gray-200 pb-4">
        <div>
          <h1 className="text-2xl font-bold text-text-primary">Minhas Reservas</h1>
          <p className="text-text-secondary">Gerencie suas passagens e viagens futuras.</p>
        </div>
        <Button variant="outline" onClick={() => navigate("/")}>Nova Viagem</Button>
      </div>

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
              </TableRow>
            </TableHeader>
            <TableBody>
              {reservas.map(res => (
                <TableRow key={res.id}>
                  <TableCell className="font-mono">{res.codigo}</TableCell>
                  <TableCell>Viagem #{res.viagemId}</TableCell>
                  <TableCell>{new Date(res.dataCriacao).toLocaleDateString('pt-BR')}</TableCell>
                  <TableCell>
                    <Badge variant={res.status === 'CONFIRMADA' ? 'success' : res.status === 'CANCELADA' ? 'danger' : 'warning'}>
                      {res.status}
                    </Badge>
                  </TableCell>
                  <TableCell className="text-right text-accent-500 font-bold">
                    {new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(res.valorTotal)}
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
    </div>
  )
}
