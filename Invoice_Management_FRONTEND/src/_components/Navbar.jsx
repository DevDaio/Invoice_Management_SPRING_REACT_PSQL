import { useAuth } from '../_context/AuthContext'
import { useModal } from '../_context/ModalContext'

export default function Navbar() {

  const { isLoggedIn, user, logout } = useAuth()
  const { openSettings } = useModal()

  return (
    <nav className="navbar navbar-expand navbar-dark bg-dark px-3">
      <a className="navbar-brand" href="#">Invoice Management</a>

      {isLoggedIn && (
        <ul className="navbar-nav ms-auto">
          {user?.role === 'ADMIN' && (
            <li className="nav-item"><button className="btn btn-link nav-link" onClick={openSettings}>Account</button></li>
          )}
          <li className="nav-item"><button className="btn btn-outline-light btn-sm ms-2" onClick={logout}>Logout</button></li>
        </ul>
      )}

    </nav>
  )
}