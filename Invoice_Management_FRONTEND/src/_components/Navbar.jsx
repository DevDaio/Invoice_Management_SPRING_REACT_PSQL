import { useAuth } from '../_context/AuthContext'

export default function Navbar() {

  const { isLoggedIn, logout } = useAuth()

  return (
    <nav className="navbar navbar-expand navbar-dark bg-dark px-3">
      <a className="navbar-brand" href="#">Invoice Management</a>

      {isLoggedIn && (
        <ul className="navbar-nav ms-auto">
          <li className="nav-item"><a className="nav-link" href="#">Account</a></li>
          <li className="nav-item"><button className="btn btn-outline-light btn-sm ms-2" onClick={logout}>Logout</button></li>
        </ul>
      )}

    </nav>
  )
}