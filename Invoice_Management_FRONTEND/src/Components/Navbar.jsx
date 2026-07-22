export default function Navbar({ isLoggedIn, onLogout, onOpenModal }) {
  return (
    <nav className="navbar navbar-expand-lg" style={{ background: '#2C2C2C' }}>
      <div className="container-fluid">
        <a className="navbar-brand fw-bold" href="#"
           style={{ color: '#E8590C' }}>DevDaio INVOICE MANAGEMENT</a>
        <button className="navbar-toggler" type="button" data-bs-toggle="collapse"
                data-bs-target="#navbarNav" aria-controls="navbarNav"
                aria-expanded="false" aria-label="Toggle navigation"
                style={{ borderColor: '#E8590C' }}>
          <span className="navbar-toggler-icon"
                style={{ filter: 'brightness(0) saturate(100%) invert(36%) sepia(97%) saturate(1243%) hue-rotate(10deg) brightness(96%) contrast(96%)' }}></span>
        </button>
        <div className="collapse navbar-collapse" id="navbarNav">
          <ul className="navbar-nav ms-auto">
            {isLoggedIn && (
              <>
                <li className="nav-item dropdown">
                  <a className="nav-link dropdown-toggle" href="#" role="button"
                     data-bs-toggle="dropdown" aria-expanded="false"
                     style={{ color: '#D3D3D3' }}>
                    Invoice
                  </a>
                  <ul className="dropdown-menu" style={{ background: '#2C2C2C' }}>
                    <li><a className="dropdown-item" href="#"
                           style={{ color: '#D3D3D3' }}
                           onClick={() => onOpenModal('addInvoice')}>Add Invoice</a></li>
                  </ul>
                </li>
                <li className="nav-item">
                  <a className="nav-link" href="/Account-Settings"
                     style={{ color: '#D3D3D3' }}>Account-Settings</a>
                </li>
                <li className="nav-item">
                  <button className="btn ms-2" onClick={onLogout}
                          style={{
                            background: '#E8590C',
                            color: '#fff',
                            border: 'none'
                          }}>
                    Logout
                  </button>
                </li>
              </>
            )}
          </ul>
        </div>
      </div>
    </nav>
  )
}
