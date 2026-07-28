import { useAuth } from '../_context/AuthContext'

export default function Navbar() {

  const { isLoggedIn, logout } = useAuth()

  return (
    <nav>
      <a href="#">PLACEHOLDER DevDaio INVOICE MANAGEMENT</a>

      {isLoggedIn && (
        <ul>
          <li><a href="#">Account-Settings</a></li>
          <li><button onClick={logout}>Logout</button></li>
        </ul>
      )}

    </nav>
  )
}