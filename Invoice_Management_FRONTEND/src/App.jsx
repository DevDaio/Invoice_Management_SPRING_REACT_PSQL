import { AuthProvider, useAuth } from './_context/AuthContext'
import { InvoiceProvider } from './_context/InvoiceContext'
import { ModalProvider } from './_context/ModalContext'
import Navbar from './_components/Navbar'
import LandingPage from './_components/LandingPage'
import MainPage from './_components/MainPage'

function AppInner() {
  const { isLoggedIn } = useAuth()
  return (
    <>
      <Navbar />
      {isLoggedIn ? <MainPage /> : <LandingPage />}
    </>
  )
}

function App() {
  return (
    <AuthProvider>
      <InvoiceProvider>
        <ModalProvider>
          <AppInner />
        </ModalProvider>
      </InvoiceProvider>
    </AuthProvider>
  )
}

export default App