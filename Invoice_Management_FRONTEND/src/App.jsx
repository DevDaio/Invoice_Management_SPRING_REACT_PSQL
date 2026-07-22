import { useState } from 'react'
import Navbar from './Components/Navbar'
import LandingPage from './Components/LandingPage'
import MainPage from './Components/MainPage'

let nextId = 1

function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(true)
  const [activeModal, setActiveModal] = useState(null)
  const [invoices, setInvoices] = useState([])

  const handleAddInvoice = (invoiceData) => {
    setInvoices(prev => [...prev, { id: nextId++, ...invoiceData }])
    setActiveModal(null)
  }

  const handleUpdateInvoice = (id, invoiceData) => {
    setInvoices(prev => prev.map(inv => inv.id === id ? { ...inv, ...invoiceData } : inv))
    setActiveModal(null)
  }

  const handleDeleteInvoice = (id) => {
    setInvoices(prev => prev.filter(inv => inv.id !== id))
    setActiveModal(null)
  }

  return (
    <>
      <Navbar isLoggedIn={isLoggedIn} onLogout={() => setIsLoggedIn(false)}
              onOpenModal={setActiveModal} />
      {isLoggedIn ? (
        <MainPage invoices={invoices}
                  onAddInvoice={handleAddInvoice}
                  onUpdateInvoice={handleUpdateInvoice}
                  onDeleteInvoice={handleDeleteInvoice}
                  activeModal={activeModal} onCloseModal={() => setActiveModal(null)}
                  onOpenModal={setActiveModal} />
      ) : (
        <LandingPage onLogin={() => setIsLoggedIn(true)} />
      )}
    </>
  )
}

export default App
