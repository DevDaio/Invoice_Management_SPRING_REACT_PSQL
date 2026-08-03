import { useState } from 'react'
import { useAuth } from '../_context/AuthContext'

export default function LandingPage() {

  const { login } = useAuth()
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')

  return (
    <div className="container mt-5">
      <div className="row justify-content-center">
        <div className="col-md-4 col-lg-3">
          <div className="card">
            <div className="card-body">
              <h4 className="card-title text-center mb-4">Login</h4>
              <form onSubmit={e => { e.preventDefault(); login(email, password) }}>
                <div className="mb-3">
                  <input className="form-control" type="email" placeholder="Email"
                         value={email} onChange={e => setEmail(e.target.value)} />
                </div>
                <div className="mb-3">
                  <input className="form-control" type="password" placeholder="Password"
                         value={password} onChange={e => setPassword(e.target.value)} />
                </div>
                <button className="btn btn-primary w-100" disabled={!email || !password}
                        type="submit">
                  Login
                </button>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}