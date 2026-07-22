import { useState } from 'react'
import RegisterModal from '../modals/RegisterModal'

export default function LandingPage({ onLogin }) {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [showRegister, setShowRegister] = useState(false)
  
  return (
    <div style={{
      height: '100vh',
      background: "url('/BG1.png') center/cover no-repeat",
      display: 'flex',
      justifyContent: 'center',
      alignItems: 'center'
    }}>
      <div style={{
        background: 'rgba(44,44,44,0.95)',
        padding: '2rem',
        borderRadius: '8px',
        width: '320px',
        color: '#D3D3D3'
      }}>
        <input className="form-control mb-2" type="email" placeholder="Email"
               value={email} onChange={e => setEmail(e.target.value)}
               style={{ background: '#3A3A3A', color: '#D3D3D3', border: 'none' }} />
        <input className="form-control mb-2" type="password" placeholder="Passwort"
               value={password} onChange={e => setPassword(e.target.value)}
               style={{ background: '#3A3A3A', color: '#D3D3D3', border: 'none' }} />
        <button className="btn w-100"
                disabled={!email || !password}
                onClick={() => onLogin({ email, password })}
                style={{
                  background: !email || !password ? undefined : '#E8590C',
                  border: 'none',
                  color: '#fff'
                }}>
          Login
        </button>
        <button className="btn w-100 mt-2"
                style={{
                  background: 'transparent',
                  border: '1px solid #E8590C',
                  color: '#E8590C'
                }}
                onClick={() => setShowRegister(true)}>
          Account erstellen
        </button>
        <RegisterModal show={showRegister} onClose={() => setShowRegister(false)} />
      </div>
    </div>
  )
}
