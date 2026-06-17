import { useEffect, useState } from "react"
import { Input } from "../../components/ui/Input"
import { Button } from "../../components/ui/Button"
import { useNavigate } from "react-router-dom"

export function Perfil() {
  const navigate = useNavigate()
  const [loading, setLoading] = useState(true)
  const [saving, setSaving] = useState(false)

  const [perfil, setPerfil] = useState({
    nome: "",
    cep: "",
    telefone: ""
  })

  useEffect(() => {
    const token = localStorage.getItem("token")
    if (!token) {
      navigate("/login")
      return
    }

    fetch("/api/clientes/me", {
      headers: { "Authorization": `Bearer ${token}` }
    })
      .then(res => res.json())
      .then(data => {
        setPerfil({
          nome: data.nome || "",
          cep: data.cep || "",
          telefone: data.telefone || ""
        })
      })
      .catch(err => console.error(err))
      .finally(() => setLoading(false))
  }, [navigate])

  const handleSave = (e: React.FormEvent) => {
    e.preventDefault()
    setSaving(true)
    const token = localStorage.getItem("token")
    fetch("/api/clientes/me", {
      method: "PUT",
      headers: { 
        "Authorization": `Bearer ${token}`,
        "Content-Type": "application/json"
      },
      body: JSON.stringify(perfil)
    })
      .then(res => {
        if (!res.ok) throw new Error("Falha ao salvar")
        alert("Perfil salvo com sucesso!")
      })
      .catch(() => alert("Erro ao salvar"))
      .finally(() => setSaving(false))
  }

  return (
    <div className="max-w-2xl mx-auto space-y-6">
      <div className="border-b border-gray-200 pb-4">
        <h1 className="text-2xl font-bold text-text-primary">Meu Perfil</h1>
        <p className="text-text-secondary">Atualize suas informações pessoais e de contato.</p>
      </div>

      {loading ? (
        <div className="text-center py-10">Carregando dados...</div>
      ) : (
        <form onSubmit={handleSave} className="bg-surface p-6 rounded-2xl shadow-sm border border-gray-100 space-y-6">
          <div className="space-y-4">
            <Input 
              label="Nome Completo" 
              value={perfil.nome} 
              onChange={e => setPerfil({...perfil, nome: e.target.value})} 
            />
            <div className="grid grid-cols-2 gap-4">
              <Input 
                label="CEP" 
                value={perfil.cep} 
                onChange={e => setPerfil({...perfil, cep: e.target.value})} 
              />
              <Input 
                label="Telefone" 
                value={perfil.telefone} 
                onChange={e => setPerfil({...perfil, telefone: e.target.value})} 
              />
            </div>
          </div>
          <div className="flex justify-end">
            <Button type="submit" variant="primary" disabled={saving}>
              {saving ? "Salvando..." : "Salvar Alterações"}
            </Button>
          </div>
        </form>
      )}
    </div>
  )
}
