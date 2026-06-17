import React, { createContext, useContext, useState, useEffect } from 'react'

export interface Cliente {
  id: number
  nome: string
  cpf: string
  email: string
  telefone?: string
  cep?: string
  dataNascimento?: string
  roles?: string[]
}

interface AuthContextType {
  token: string | null
  user: Cliente | null
  loading: boolean
  isAuthenticated: boolean
  login: (username: string, password: string) => Promise<void>
  register: (data: any) => Promise<void>
  logout: () => void
  forgotPassword: (email: string) => Promise<string>
  resetPassword: (data: any) => Promise<void>
  refreshUser: () => Promise<void>
}

const AuthContext = createContext<AuthContextType | undefined>(undefined)

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [token, setToken] = useState<string | null>(localStorage.getItem('token'))
  const [user, setUser] = useState<Cliente | null>(() => {
    const cachedName = localStorage.getItem('userName')
    if (cachedName) {
      return { id: 0, nome: cachedName, cpf: '', email: '' }
    }
    return null
  })
  const [loading, setLoading] = useState(true)

  const fetchProfile = async (authToken: string, throwOnError = false) => {
    try {
      const authMeRes = await fetch('/auth/me', {
        headers: {
          'Authorization': `Bearer ${authToken}`,
        },
      })
      if (!authMeRes.ok) {
        logout()
        if (throwOnError) {
          throw new Error('Falha ao autenticar usuário.')
        }
        return
      }

      const authMeData = await authMeRes.json()
      const authUser = authMeData.data // { id: 1, username: 'gerente', roles: ['ROLE_GERENTE'] }
      const isCliente = authUser.roles.includes('ROLE_CLIENTE')

      if (isCliente) {
        const res = await fetch('/api/clientes/me', {
          headers: {
            'Authorization': `Bearer ${authToken}`,
          },
        })
        if (res.ok) {
          const data = await res.json()
          const clientData: Cliente = {
            ...data,
            roles: authUser.roles
          }
          setUser(clientData)
          localStorage.setItem('userName', clientData.nome)
        } else {
          logout()
          if (throwOnError) {
            throw new Error('Não foi possível carregar os dados de perfil do cliente.')
          }
        }
      } else {
        // Para administradores, gerentes ou outros funcionários sem tabela 'cliente'
        const mockUser: Cliente = {
          id: authUser.id,
          nome: authUser.username.charAt(0).toUpperCase() + authUser.username.slice(1),
          cpf: '',
          email: '',
          roles: authUser.roles
        }
        setUser(mockUser)
        localStorage.setItem('userName', mockUser.nome)
      }
    } catch (err) {
      console.error('Erro ao buscar perfil do usuário:', err)
      logout()
      if (throwOnError) {
        throw err
      }
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    if (token) {
      fetchProfile(token, false)
    } else {
      setLoading(false)
    }
  }, [token])

  const login = async (username: string, password: string) => {
    const res = await fetch('/auth/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username, password }),
    })
    
    const responseData = await res.json()
    if (!res.ok) {
      throw new Error(responseData.message || 'Usuário ou senha incorretos')
    }

    const jwtToken = responseData.data.token
    localStorage.setItem('token', jwtToken)
    setToken(jwtToken)
    await fetchProfile(jwtToken, true)
  }

  const register = async (data: any) => {
    const res = await fetch('/auth/register', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    })

    const responseData = await res.json()
    if (!res.ok) {
      throw new Error(responseData.message || 'Erro ao realizar o cadastro')
    }

    // Após registro com sucesso, fazer login automático
    await login(data.username, data.password)
  }

  const forgotPassword = async (email: string) => {
    const res = await fetch('/auth/forgot-password', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email }),
    })

    const responseData = await res.json()
    if (!res.ok) {
      throw new Error(responseData.message || 'Erro ao solicitar recuperação')
    }

    return responseData.message || 'E-mail de recuperação enviado com sucesso!'
  }

  const resetPassword = async (data: any) => {
    const res = await fetch('/auth/reset-password', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    })

    const responseData = await res.json()
    if (!res.ok) {
      throw new Error(responseData.message || 'Erro ao redefinir a senha')
    }
  }

  const logout = () => {
    localStorage.removeItem('token')
    localStorage.removeItem('userName')
    setToken(null)
    setUser(null)
    setLoading(false)
  }

  const refreshUser = async () => {
    if (token) {
      await fetchProfile(token, false)
    }
  }

  return (
    <AuthContext.Provider
      value={{
        token,
        user,
        loading,
        isAuthenticated: !!token,
        login,
        register,
        logout,
        forgotPassword,
        resetPassword,
        refreshUser,
      }}
    >
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  const context = useContext(AuthContext)
  if (!context) {
    throw new Error('useAuth deve ser usado dentro de um AuthProvider')
  }
  return context
}
