import { useEffect, useState } from "react"
import { useSearchParams, useNavigate } from "react-router-dom"
import { TripCard } from "../components/ui/TripCard"
import { Button } from "../components/ui/Button"
import { ArrowLeft } from "lucide-react"
import { Navbar } from "../components/Navbar"

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

export function BuscaViagens() {
  const [searchParams] = useSearchParams()
  const navigate = useNavigate()
  
  const origemId = searchParams.get("origemId")
  const destinoId = searchParams.get("destinoId")
  const data = searchParams.get("data")

  const [viagens, setViagens] = useState<Viagem[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    if (origemId && destinoId && data) {
      setLoading(true)
      fetch(`/api/viagens/busca?origemId=${origemId}&destinoId=${destinoId}&data=${data}`)
        .then(res => {
          if (!res.ok) throw new Error("Erro na busca")
          return res.json()
        })
        .then(data => {
          setViagens(data)
        })
        .catch(err => {
          console.error(err)
        })
        .finally(() => setLoading(false))
    } else {
      setLoading(false)
    }
  }, [origemId, destinoId, data])

  const formatTime = (dateString: string) => {
    const d = new Date(dateString)
    return d.toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' })
  }

  return (
    <div className="min-h-screen bg-background flex flex-col">
      <Navbar />
      <div className="max-w-4xl mx-auto w-full p-6 space-y-6">
        <Button variant="ghost" onClick={() => navigate("/")} className="mb-4">
          <ArrowLeft size={18} className="mr-2" /> Voltar para Home
        </Button>

        <h1 className="text-3xl font-bold text-text-primary">
          Passagens encontradas
        </h1>
        <p className="text-text-secondary">
          Mostrando resultados para a data {data}
        </p>

        <div className="space-y-4">
          {loading ? (
            <div className="text-center py-10 text-gray-400">Buscando viagens...</div>
          ) : viagens.length > 0 ? (
            viagens.map(viagem => (
              <TripCard
                key={viagem.id}
                origem={`${viagem.cidadeOrigem.nome}, ${viagem.cidadeOrigem.uf}`}
                destino={`${viagem.cidadeDestino.nome}, ${viagem.cidadeDestino.uf}`}
                partida={formatTime(viagem.partida)}
                chegada={formatTime(viagem.chegada)}
                preco={viagem.preco}
                modalTipo={viagem.modais[0]?.tipo || 'ONIBUS'}
                transportadora={viagem.modais[0]?.transportadora.nome || 'Viação Padrão'}
                onSelect={() => navigate(`/checkout?viagemId=${viagem.id}`)}
              />
            ))
          ) : (
            <div className="bg-white p-10 rounded-2xl border border-gray-200 text-center space-y-4">
              <h3 className="text-xl font-bold text-text-primary">Puxa, não encontramos viagens!</h3>
              <p className="text-text-secondary">Tente buscar para o dia seguinte ou verificar outras rotas.</p>
            </div>
          )}
        </div>
      </div>
    </div>
  )
}
