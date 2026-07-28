import { createContext, useContext, useEffect, useState } from 'react'

import { api, setToken, getToken, clearToken } from '../_helpers/api'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {

  const [isLoggedIn, setIsLoggedIn] = useState(false)
  const [user, setUser] = useState(null)

  useEffect(() => {
    const savedToken = getToken()
    if (savedToken) {
      try {
        const payload = JSON.parse(atob(savedToken.split('.')[1]))
        setUser({ email: payload.sub })
      } catch {
        setUser({ email: 'unknown' })
      }
      setIsLoggedIn(true)
    }
  }, [])

  const login = async (email, password) => {
    const res = await api('/login', {
      method: 'POST',
      body: { mail: email, password },
    })
    setToken(res.token)
    setUser({ email })
    setIsLoggedIn(true)
  }

  const logout = () => {
    clearToken()
    setUser(null)
    setIsLoggedIn(false)
  }

  return (
    <AuthContext.Provider value={{ isLoggedIn, user, login, logout }}/>
  )
}

export function useAuth() {

  const ctx = useContext(AuthContext)

	if (!ctx)
		throw new Error('useAuth must be used inside AuthContext')
	return ctx
}