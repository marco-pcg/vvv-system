import * as React from "react"
import { Bus, Plane, Train, Ship, ArrowRight } from "lucide-react"
import { Button } from "./Button"
import { Badge } from "./Badge"

export interface TripCardProps {
  origem: string
  destino: string
  partida: string
  chegada: string
  preco: number
  modalTipo: 'ONIBUS' | 'AVIAO' | 'TREM' | 'NAVIO'
  transportadora: string
  onSelect?: () => void
  status?: string
}

export function TripCard({ origem, destino, partida, chegada, preco, modalTipo, transportadora, onSelect, status }: TripCardProps) {
  const Icon = modalTipo === 'ONIBUS' ? Bus : modalTipo === 'AVIAO' ? Plane : modalTipo === 'TREM' ? Train : Ship

  return (
    <div className="bg-surface rounded-2xl border border-gray-200 shadow-sm hover:shadow-md transition-shadow p-5 flex flex-col gap-4">
      <div className="flex justify-between items-center">
        <div className="flex items-center gap-3">
          <div className="bg-primary-500/10 p-2 rounded-lg text-primary-600">
            <Icon size={20} />
          </div>
          <span className="text-sm font-medium text-text-secondary">{transportadora}</span>
        </div>
        {status && (
          <Badge variant={status === 'CONFIRMADA' ? 'success' : status === 'CANCELADA' ? 'danger' : 'warning'}>
            {status}
          </Badge>
        )}
      </div>

      <div className="flex items-center justify-between py-2">
        <div className="flex flex-col">
          <span className="text-lg font-bold text-text-primary">{partida}</span>
          <span className="text-sm text-text-secondary">{origem}</span>
        </div>
        <div className="flex flex-col items-center px-4">
          <span className="text-xs text-gray-400 mb-1">Duração</span>
          <div className="flex items-center gap-2 text-gray-300">
            <div className="h-[1px] w-8 bg-gray-300"></div>
            <ArrowRight size={16} className="text-gray-400" />
            <div className="h-[1px] w-8 bg-gray-300"></div>
          </div>
        </div>
        <div className="flex flex-col text-right">
          <span className="text-lg font-bold text-text-primary">{chegada}</span>
          <span className="text-sm text-text-secondary">{destino}</span>
        </div>
      </div>

      <div className="pt-4 border-t border-gray-100 flex items-end justify-between mt-auto">
        <div className="flex flex-col">
          <span className="text-xs text-text-secondary">Preço por passageiro</span>
          <span className="text-2xl font-bold text-accent-500">
            {new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(preco)}
          </span>
        </div>
        <Button variant="accent" onClick={onSelect}>Selecionar</Button>
      </div>
    </div>
  )
}
