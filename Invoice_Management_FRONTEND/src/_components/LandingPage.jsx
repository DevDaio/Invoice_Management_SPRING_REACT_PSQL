import { useState } from 'react'
import { useAuth } from '../_context/AuthContext'

export default function LandingPage() {

  const { login } = useAuth()
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')

  return (
    <div className="login-page">

      <div className="login-branding">
        <div className="login-branding-inner">
          <div className="login-logo">
            <svg width="48" height="48" viewBox="0 0 48 48" fill="none">
              <rect width="48" height="48" rx="12" fill="#198754" />
              <path d="M14 16h20v4H14zm0 8h20v2H14zm0 6h12v2H14z" fill="#fff" />
            </svg>
          </div>
          <h1 className="login-app-name">INVOICE MANAGEMENT</h1>


        </div>
      </div>

      <div className="login-form-panel">
        <div className="login-card">
          <h2 className="login-heading">Anmelden</h2>
          <p className="login-subtext">Melde dich mit deiner E-Mail und deinem Passwort an.</p>
          <form onSubmit={e => { e.preventDefault(); login(email, password) }}>
            <div className="login-input-group">
              <svg className="login-input-icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <rect x="2" y="4" width="20" height="16" rx="2" />
                <path d="m22 7-8.97 5.7a1.94 1.94 0 0 1-2.06 0L2 7" />
              </svg>
              <input className="login-input" type="email" placeholder="E-Mail"
                     value={email} onChange={e => setEmail(e.target.value)} />
            </div>
            <div className="login-input-group">
              <svg className="login-input-icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <rect x="3" y="11" width="18" height="11" rx="2" />
                <path d="M7 11V7a5 5 0 0 1 10 0v4" />
              </svg>
              <input className="login-input" type="password" placeholder="Passwort"
                     value={password} onChange={e => setPassword(e.target.value)} />
            </div>
            <button className="login-btn" disabled={!email || !password} type="submit">
              Anmelden
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <path d="M5 12h14" />
                <path d="m12 5 7 7-7 7" />
              </svg>
            </button>
          </form>
        </div>
      </div>

    </div>
  )
}
