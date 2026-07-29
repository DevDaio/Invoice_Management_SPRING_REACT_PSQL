import { createContext, useContext, useEffect, useState } from 'react'

import { api, getToken } from '../_helpers/api'
const InvoiceContext = createContext(null)

function mapBackendInvoice(inv) {
  const articles = (inv.articles || []).map(a => ({
    articleNumber: a.articleNumber,
    name: a.name,
    priceNet: a.priceNet,
    taxType: a.tax,
    unitType: a.unit,
    quantity: a.quantity,
  }))
  const totalNet = articles.reduce((sum, a) => sum + (a.priceNet * a.quantity), 0)
  return {
    id: inv.id,
    number: inv.number,
    date: inv.date,
    supplier: inv.supplier?.name || inv.supplier,
    articles,
    totalNet,
    payed: inv.payed,
  }
}

function mapToBackendInvoice(data) {
  return {
    invoiceNumber: data.number,
    date: data.date,
    supplierName: data.supplier,
    articles: data.articles.map(a => ({
      articleNumber: a.articleNumber,
      name: a.name,
      priceNet: parseFloat(a.priceNet),
      tax: a.taxType,
      unit: a.unitType,
      quantity: parseInt(a.quantity),
    })),
  }
}

export function InvoiceProvider({ children }) {

  const [invoices, setInvoices] = useState([])

  useEffect(() => {
    if (getToken()) {
      api('/invoices')
        .then(data => setInvoices(data.map(mapBackendInvoice)))
        .catch(console.error)
    }
  }, [])

  const addInvoice = async (data) => {
    const body = mapToBackendInvoice(data)
    const created = await api('/invoice', { method: 'POST', body })
    setInvoices(prev => [...prev, mapBackendInvoice(created)])
  }

  const updateInvoice = async (id, data) => {
    const body = {
      number: data.number,
      date: data.date,
      supplierName: data.supplier,
      articles: data.articles.map(a => ({
        articleNumber: a.articleNumber,
        name: a.name,
        priceNet: parseFloat(a.priceNet),
        tax: a.taxType,
        unit: a.unitType,
        quantity: parseInt(a.quantity),
      })),
    }
    const updated = await api('/update/invoice/' + id, { method: 'PUT', body })
    setInvoices(prev => prev.map(inv => inv.id === id ? mapBackendInvoice(updated) : inv))
  }

  const deleteInvoice = async (id) => {
    await api('/invoice/' + id, { method: 'DELETE' })
    setInvoices(prev => prev.filter(inv => inv.id !== id))
  }

  return (
    <InvoiceContext.Provider value={{ invoices, addInvoice, updateInvoice, deleteInvoice }}>
      {children}
    </InvoiceContext.Provider>
  )
}

export function useInvoices() {
  const ctx = useContext(InvoiceContext)
	if (!ctx)
		throw new Error('useInvoices must be used inside InvoiceProvider')
	return ctx
}