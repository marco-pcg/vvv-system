import { Search, MapPin, Calendar } from "lucide-react"
import { Button } from "./components/ui/Button"
import { Input } from "./components/ui/Input"
import { Badge } from "./components/ui/Badge"
import { TripCard } from "./components/ui/TripCard"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "./components/ui/Table"

function App() {
  return (
    <div className="min-h-screen p-8 max-w-5xl mx-auto space-y-12">
      <header className="space-y-2">
        <h1 className="text-4xl font-bold text-primary-500 tracking-tight">VVV Design System</h1>
        <p className="text-text-secondary">Biblioteca de componentes base para a plataforma Vai & Volta Viagens.</p>
      </header>

      <section className="space-y-6">
        <h2 className="text-2xl font-bold text-text-primary border-b border-gray-200 pb-2">Botões</h2>
        <div className="flex flex-wrap gap-4 items-end">
          <Button variant="primary">Primary Button</Button>
          <Button variant="accent">Comprar Agora</Button>
          <Button variant="outline">Ver Detalhes</Button>
          <Button variant="ghost">Cancelar</Button>
          <Button variant="primary" disabled>Desabilitado</Button>
        </div>
      </section>

      <section className="space-y-6">
        <h2 className="text-2xl font-bold text-text-primary border-b border-gray-200 pb-2">Inputs & Formulários</h2>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 bg-surface p-6 rounded-2xl shadow-sm border border-gray-100">
          <Input label="Origem" placeholder="De onde você sai?" icon={<MapPin size={18} />} />
          <Input label="Destino" placeholder="Para onde vai?" icon={<MapPin size={18} />} />
          <Input label="Data da Ida" type="date" icon={<Calendar size={18} />} />
          <div className="md:col-span-3 flex justify-end">
            <Button variant="accent" className="w-full md:w-auto"><Search size={18} className="mr-2" /> Buscar Passagens</Button>
          </div>
        </div>
      </section>

      <section className="space-y-6">
        <h2 className="text-2xl font-bold text-text-primary border-b border-gray-200 pb-2">Status Badges</h2>
        <div className="flex gap-4 bg-surface p-6 rounded-2xl shadow-sm border border-gray-100">
          <Badge variant="success">Confirmada</Badge>
          <Badge variant="warning">Pendente</Badge>
          <Badge variant="danger">Cancelada</Badge>
          <Badge variant="neutral">Rascunho</Badge>
        </div>
      </section>

      <section className="space-y-6">
        <h2 className="text-2xl font-bold text-text-primary border-b border-gray-200 pb-2">Cards de Viagem (B2C)</h2>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <TripCard
            origem="São Paulo, SP"
            destino="Rio de Janeiro, RJ"
            partida="08:00"
            chegada="14:30"
            preco={149.90}
            modalTipo="ONIBUS"
            transportadora="Viação Cometa"
          />
          <TripCard
            origem="Belo Horizonte, MG"
            destino="Vitória, ES"
            partida="10:00"
            chegada="11:15"
            preco={350.00}
            modalTipo="AVIAO"
            transportadora="Azul Linhas Aéreas"
            status="CONFIRMADA"
          />
        </div>
      </section>

      <section className="space-y-6">
        <h2 className="text-2xl font-bold text-text-primary border-b border-gray-200 pb-2">Tabelas de Gestão (Admin/Gerência)</h2>
        <div className="bg-surface p-6 rounded-2xl shadow-sm border border-gray-100">
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Passageiro</TableHead>
                <TableHead>Viagem</TableHead>
                <TableHead>Data</TableHead>
                <TableHead>Status</TableHead>
                <TableHead className="text-right">Valor</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              <TableRow>
                <TableCell className="font-medium">João Silva</TableCell>
                <TableCell>SP {'->'} RJ (Ônibus)</TableCell>
                <TableCell>20/06/2026</TableCell>
                <TableCell><Badge variant="success">Confirmada</Badge></TableCell>
                <TableCell className="text-right text-accent-500 font-bold">R$ 149,90</TableCell>
              </TableRow>
              <TableRow>
                <TableCell className="font-medium">Maria Clara</TableCell>
                <TableCell>BH {'->'} VIX (Avião)</TableCell>
                <TableCell>21/06/2026</TableCell>
                <TableCell><Badge variant="warning">Pendente</Badge></TableCell>
                <TableCell className="text-right text-accent-500 font-bold">R$ 350,00</TableCell>
              </TableRow>
            </TableBody>
          </Table>
        </div>
      </section>
    </div>
  )
}

export default App
