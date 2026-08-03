import { AuthProvider, useAuth } from './_context/AuthContext'
import { InvoiceProvider } from './_context/InvoiceContext'
import { ModalProvider } from './_context/ModalContext'
import { ThemeProvider } from './_context/ThemeContext'
import Sidebar from './_components/Sidebar'
import LandingPage from './_components/LandingPage'
import MainPage from './_components/MainPage'
import './App.css'

function AppInner() {
  const { isLoggedIn } = useAuth()
  return (
    <div className="app-layout">
      <Sidebar />
      <div className="app-content">
        {isLoggedIn ? <MainPage /> : <LandingPage />}
      </div>
    </div>
  )
}

function App() {
  return (
    <ThemeProvider>
      <AuthProvider>
        <InvoiceProvider>
          <ModalProvider>
            <AppInner />
          </ModalProvider>
        </InvoiceProvider>
      </AuthProvider>
    </ThemeProvider>
  )
}

export default App
