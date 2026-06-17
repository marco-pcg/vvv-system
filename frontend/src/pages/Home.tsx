import { useState } from "react"
import { useNavigate } from "react-router-dom"
import { Search, MapPin, Calendar, Percent, ShieldCheck, HelpCircle, ArrowRight, Star, Compass } from "lucide-react"
import { Button } from "../components/ui/Button"
import { Input } from "../components/ui/Input"
import { Navbar } from "../components/Navbar"

export function Home() {
  const navigate = useNavigate()
  const [origem, setOrigem] = useState("")
  const [destino, setDestino] = useState("")
  const [data, setData] = useState("")

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault()
    if (origem && destino && data) {
      navigate(`/busca?origemId=${origem}&destinoId=${destino}&data=${data}`)
    }
  }

  // Pre-fills the form and focuses/scrolls to it
  const handleQuickBook = (destId: string, origId: string = "1") => {
    setOrigem(origId)
    setDestino(destId)
    
    // Set travel date to 7 days from now
    const travelDate = new Date()
    travelDate.setDate(travelDate.getDate() + 7)
    const yyyy = travelDate.getFullYear()
    const mm = String(travelDate.getMonth() + 1).padStart(2, '0')
    const dd = String(travelDate.getDate()).padStart(2, '0')
    setData(`${yyyy}-${mm}-${dd}`)

    // Smooth scroll to search form
    document.getElementById("search-form-section")?.scrollIntoView({ behavior: 'smooth' })
  }

  const popularDestinations = [
    {
      id: "2", // São Paulo
      nome: "São Paulo, SP",
      img: "/sp.png",
      preco: "120",
      tag: "Mais Procurado",
      rating: 4.8,
    },
    {
      id: "1", // Rio de Janeiro
      nome: "Rio de Janeiro, RJ",
      img: "/rio.png",
      preco: "150",
      tag: "Popular",
      rating: 4.9,
    },
    {
      id: "3", // Salvador
      nome: "Salvador, BA",
      img: "/salvador.png",
      preco: "280",
      tag: "Melhor Clima",
      rating: 4.7,
    }
  ]

  return (
    <div className="min-h-screen bg-background flex flex-col font-sans">
      <Navbar />

      {/* Hero Banner Section */}
      <section className="relative bg-gradient-to-r from-primary-600 via-primary-700 to-indigo-800 pt-20 pb-36 px-6 overflow-hidden">
        <div className="absolute inset-0 opacity-10 bg-[radial-gradient(ellipse_at_center,_var(--tw-gradient-stops))] from-white via-transparent to-transparent"></div>
        <div className="max-w-5xl mx-auto text-center space-y-6 relative z-10">
          <span className="inline-flex items-center gap-1.5 px-3 py-1.5 rounded-full bg-white/10 text-white text-xs font-semibold uppercase tracking-wider backdrop-blur-md">
            <Star size={12} className="text-yellow-300 fill-yellow-300" /> Viaje com Conforto e Segurança
          </span>
          <h1 className="text-4xl md:text-6xl font-extrabold text-white tracking-tight leading-tight">
            Descubra o Brasil com a <span className="text-transparent bg-clip-text bg-gradient-to-r from-accent-500 to-amber-300">VVV System</span>
          </h1>
          <p className="text-lg md:text-xl text-blue-100 max-w-2xl mx-auto leading-relaxed">
            As melhores rotas terrestres, marítimas e aéreas integradas em uma única plataforma. Planeje, reserve e embarque na sua aventura.
          </p>
        </div>
      </section>

      {/* Search Box Card */}
      <section id="search-form-section" className="max-w-5xl mx-auto w-full px-6 -mt-20 relative z-20">
        <div className="bg-white p-6 md:p-8 rounded-3xl shadow-xl border border-gray-100/80 backdrop-blur-lg">
          <div className="mb-4">
            <h3 className="text-lg font-bold text-text-primary">Onde você quer ir?</h3>
            <p className="text-text-secondary text-sm">Insira as cidades de origem e destino para ver as viagens disponíveis.</p>
          </div>
          <form onSubmit={handleSearch} className="flex flex-col lg:flex-row gap-4 items-end">
            <div className="flex-1 w-full">
              <Input 
                label="Cidade de Origem (ID)" 
                placeholder="Ex: 1" 
                icon={<MapPin size={18} className="text-primary-500" />} 
                value={origem} 
                onChange={e => setOrigem(e.target.value)} 
                required 
              />
            </div>
            <div className="flex-1 w-full">
              <Input 
                label="Cidade de Destino (ID)" 
                placeholder="Ex: 2" 
                icon={<MapPin size={18} className="text-accent-500" />} 
                value={destino} 
                onChange={e => setDestino(e.target.value)} 
                required 
              />
            </div>
            <div className="flex-1 w-full">
              <Input 
                label="Data da Viagem" 
                type="date" 
                icon={<Calendar size={18} className="text-gray-400" />} 
                value={data} 
                onChange={e => setData(e.target.value)} 
                required 
              />
            </div>
            <Button type="submit" variant="accent" size="lg" className="w-full lg:w-auto px-8 py-3.5 shadow-md shadow-accent-500/10 hover:-translate-y-0.5 transition-all duration-200">
              <Search size={18} className="mr-2" /> Buscar Passagens
            </Button>
          </form>
        </div>
      </section>

      {/* Popular Destinations */}
      <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-20 w-full">
        <div className="flex flex-col md:flex-row justify-between items-start md:items-end mb-10">
          <div>
            <h2 className="text-3xl font-extrabold text-text-primary tracking-tight">Destinos Populares</h2>
            <p className="text-text-secondary mt-2 text-lg">Os destinos mais cobiçados pelos nossos viajantes com saídas diárias.</p>
          </div>
          <span className="text-sm font-semibold text-primary-500 hover:text-primary-600 transition-colors flex items-center gap-1 mt-3 md:mt-0 cursor-pointer">
            Ver todas as ofertas <ArrowRight size={16} />
          </span>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
          {popularDestinations.map(destination => (
            <div 
              key={destination.id} 
              className="group bg-white rounded-2xl overflow-hidden border border-gray-100 shadow-sm hover:shadow-md transition-all duration-300 flex flex-col"
            >
              {/* Destination Image & Tag */}
              <div className="relative h-56 overflow-hidden">
                <img 
                  src={destination.img} 
                  alt={destination.nome}
                  className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-500"
                />
                <div className="absolute top-4 left-4">
                  <span className="bg-white/90 backdrop-blur-md text-primary-600 px-3 py-1 rounded-full text-xs font-bold shadow-sm">
                    {destination.tag}
                  </span>
                </div>
                <div className="absolute bottom-4 right-4 bg-black/60 backdrop-blur-sm px-2.5 py-1 rounded-lg text-white text-xs font-semibold flex items-center gap-1">
                  <Star size={12} className="text-yellow-400 fill-yellow-400" /> {destination.rating}
                </div>
              </div>

              {/* Destination Details */}
              <div className="p-6 flex flex-col flex-1 justify-between space-y-4">
                <div>
                  <h3 className="text-xl font-bold text-text-primary group-hover:text-primary-500 transition-colors duration-200">
                    {destination.nome}
                  </h3>
                  <p className="text-text-secondary text-sm mt-1">Viagens diárias saindo do Terminal Integrado.</p>
                </div>
                <div className="flex items-center justify-between pt-2 border-t border-gray-50">
                  <div>
                    <span className="text-xs text-text-secondary block">A partir de</span>
                    <span className="text-lg font-extrabold text-text-primary">R$ {destination.preco}</span>
                  </div>
                  <Button 
                    variant="outline" 
                    size="sm" 
                    onClick={() => handleQuickBook(destination.id)}
                    className="font-semibold"
                  >
                    Viajar agora
                  </Button>
                </div>
              </div>
            </div>
          ))}
        </div>
      </section>

      {/* Why choose VVV & Business Rules (RN14) */}
      <section className="bg-gray-50 border-y border-gray-100 py-20 w-full">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center max-w-2xl mx-auto mb-16 space-y-3">
            <h2 className="text-3xl font-extrabold text-text-primary tracking-tight">Por que viajar com o VVV System?</h2>
            <p className="text-text-secondary">Simplificamos suas viagens integrando reservas, descontos automáticos e segurança em uma única conta.</p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            {/* Feature 1: Discount Rule */}
            <div className="bg-white p-8 rounded-2xl border border-gray-100 shadow-xs flex flex-col items-start gap-4">
              <div className="w-12 h-12 rounded-xl bg-orange-50 text-accent-500 flex items-center justify-center">
                <Percent size={24} />
              </div>
              <div>
                <h3 className="text-lg font-bold text-text-primary mb-2">Desconto Infantil de 40%</h3>
                <p className="text-text-secondary text-sm leading-relaxed">
                  Crianças de 2 a 10 anos acompanhadas por um responsável ganham 40% de desconto automático calculado no fechamento da reserva (Regra RN14).
                </p>
              </div>
            </div>

            {/* Feature 2: Secure Purchase */}
            <div className="bg-white p-8 rounded-2xl border border-gray-100 shadow-xs flex flex-col items-start gap-4">
              <div className="w-12 h-12 rounded-xl bg-blue-50 text-primary-500 flex items-center justify-center">
                <ShieldCheck size={24} />
              </div>
              <div>
                <h3 className="text-lg font-bold text-text-primary mb-2">Reserva & Pagamento Seguro</h3>
                <p className="text-text-secondary text-sm leading-relaxed">
                  Confirmação em tempo real com cartões de crédito e débito. Suas reservas são processadas em ambiente criptografado e seguro.
                </p>
              </div>
            </div>

            {/* Feature 3: Integrated System */}
            <div className="bg-white p-8 rounded-2xl border border-gray-100 shadow-xs flex flex-col items-start gap-4">
              <div className="w-12 h-12 rounded-xl bg-indigo-50 text-indigo-500 flex items-center justify-center">
                <Compass size={24} />
              </div>
              <div>
                <h3 className="text-lg font-bold text-text-primary mb-2">Multimodais Conectados</h3>
                <p className="text-text-secondary text-sm leading-relaxed">
                  Pesquise e combine viagens rodoviárias, aéreas ou aquáticas para chegar ao seu destino da maneira mais conveniente e barata.
                </p>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Elegant Footer */}
      <footer className="bg-slate-900 text-slate-400 py-12 mt-auto w-full">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 grid grid-cols-1 md:grid-cols-4 gap-8">
          <div className="space-y-4">
            <span className="font-extrabold text-xl tracking-wider text-white flex items-center gap-2">
              <Compass className="h-5 w-5 text-primary-500" /> VVV System
            </span>
            <p className="text-xs text-slate-400 leading-relaxed">
              O sistema inteligente de integração de passagens B2C que conecta você a qualquer destino no território nacional.
            </p>
          </div>
          <div>
            <h4 className="text-white font-semibold text-sm mb-4">Plataforma</h4>
            <ul className="space-y-2 text-xs">
              <li><a href="/" className="hover:text-white transition-colors">Buscar Passagens</a></li>
              <li><a href="/login" className="hover:text-white transition-colors">Área do Cliente</a></li>
              <li><a href="/register" className="hover:text-white transition-colors">Cadastre-se</a></li>
            </ul>
          </div>
          <div>
            <h4 className="text-white font-semibold text-sm mb-4">Informações</h4>
            <ul className="space-y-2 text-xs">
              <li><a href="#" className="hover:text-white transition-colors">Políticas de Privacidade</a></li>
              <li><a href="#" className="hover:text-white transition-colors">Termos de Uso</a></li>
              <li><a href="#" className="hover:text-white transition-colors">Regulamento Geral</a></li>
            </ul>
          </div>
          <div>
            <h4 className="text-white font-semibold text-sm mb-4">Ajuda & Contato</h4>
            <ul className="space-y-2 text-xs">
              <li><a href="#" className="hover:text-white transition-colors">Suporte ao Cliente</a></li>
              <li><a href="#" className="hover:text-white transition-colors">Dúvidas Frequentes</a></li>
              <li className="flex items-center gap-1"><HelpCircle size={12} /> contato@vvvsystem.com.br</li>
            </ul>
          </div>
        </div>
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 border-t border-slate-800 mt-8 pt-8 text-center text-xs text-slate-500">
          © {new Date().getFullYear()} VVV System Ltda. Todos os direitos reservados.
        </div>
      </footer>
    </div>
  )
}
