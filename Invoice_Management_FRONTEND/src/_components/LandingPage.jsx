import { useState } from 'react'
import { useAuth } from '../_context/AuthContext'

export default function LandingPage() {

  const { login } = useAuth()
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')

  return (
    <div>
      <div>
        <input type="email" placeholder="Email"
               value={email} onChange={e => setEmail(e.target.value)} />

        <input type="password" placeholder="Password"
               value={password} onChange={e => setPassword(e.target.value)} />

        <button disabled={!email || !password}
                onClick={() => login(email, password)}>
          Login
        </button>
      </div>
    </div>
  )
}