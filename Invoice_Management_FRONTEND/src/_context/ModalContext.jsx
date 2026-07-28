import { createContext, useContext, useState } from 'react'

const ModalContext = createContext(null)

export function ModalProvider({ children }) {
  const [activeModal, setActiveModal] = useState(null)
  const [selectedInvoice, setSelectedInvoice] = useState(null)
  const openModal = (name, invoice = null) => {
    setActiveModal(name)
    setSelectedInvoice(invoice)
  }
  const closeModal = () => {
    setActiveModal(null)
    setSelectedInvoice(null)
  }

  return (
    <ModalContext.Provider value={{ activeModal, selectedInvoice, openModal, closeModal }}>
      {children}
    </ModalContext.Provider>
  )
}

export function useModal() {
  const ctx = useContext(ModalContext)
  if (!ctx) throw new Error('useModal must be used inside ModalProvider')
  return ctx
}