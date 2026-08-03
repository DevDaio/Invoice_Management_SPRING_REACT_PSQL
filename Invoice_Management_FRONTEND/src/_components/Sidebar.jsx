import { useAuth } from '../_context/AuthContext'
import { useModal } from '../_context/ModalContext'

export default function Sidebar() {

  const { isLoggedIn, user, logout } = useAuth()
  const { openSettings } = useModal()

  return (
    <aside className="app-sidebar">
      <div className="brand">Invoice Management</div>

      <div className="spacer" />

      {isLoggedIn && (
        <>
          {user?.role === 'ADMIN' && (
            <button className="sidebar-btn" onClick={openSettings}>Account</button>
          )}
          <button className="sidebar-btn" onClick={logout}>Logout</button>
        </>
      )}
    </aside>
  )
}
