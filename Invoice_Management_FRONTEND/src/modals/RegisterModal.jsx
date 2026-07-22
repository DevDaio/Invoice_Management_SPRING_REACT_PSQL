import { useState } from 'react'

export default function RegisterModal({ show, onClose }) {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [confirm, setConfirm] = useState('')
 
  if (!show) return null

  return (
    <div style={{
      position: 'fixed', inset: 0, zIndex: 1050,
      display: 'flex', alignItems: 'center', justifyContent: 'center',
      background: 'rgba(0,0,0,0.6)'
    }} onClick={onClose}>
      <div style={{
        background: '#2C2C2C', padding: '2rem', borderRadius: '8px',
        width: '340px', color: '#D3D3D3'
      }} onClick={e => e.stopPropagation()}>
        <h4 style={{ color: '#E8590C', marginBottom: '1rem' }}>Account erstellen</h4>
        <input className="form-control mb-2" type="email" placeholder="Email"
               value={email} onChange={e => setEmail(e.target.value)}
               style={{ background: '#3A3A3A', color: '#D3D3D3', border: 'none' }} />
        <input className="form-control mb-2" type="password" placeholder="Passwort"
               value={password} onChange={e => setPassword(e.target.value)}
               style={{ background: '#3A3A3A', color: '#D3D3D3', border: 'none' }} />
        <input className="form-control mb-3" type="password" placeholder="Passwort bestätigen"
               value={confirm} onChange={e => setConfirm(e.target.value)}
               style={{ background: '#3A3A3A', color: '#D3D3D3', border: 'none' }} />
        <button className="btn w-100" disabled={!email || !password || !confirm || password !== confirm}
                style={{
                  background: !email || !password || !confirm || password !== confirm ? undefined : '#E8590C',
                  border: 'none', color: '#fff'
                }}>
          Registrieren
        </button>
        <button className="btn w-100 mt-2"
                style={{ background: 'transparent', border: '1px solid #E8590C', color: '#E8590C' }}
                onClick={onClose}>
          Abbrechen
        </button>
      </div>
    </div>
  )
}
