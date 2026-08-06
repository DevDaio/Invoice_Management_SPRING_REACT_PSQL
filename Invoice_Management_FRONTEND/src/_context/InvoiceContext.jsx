import { createContext, useContext, useEffect, useState, useCallback } from 'react'

import { api } from '../_helpers/api'
import { useAuth } from './AuthContext'
const InvoiceContext = createContext(null)

const TAX_RATES = { Tax0: 0, Tax7: 0.07, Tax19: 0.19 }

function calcArticleGross(article) {
  const rate = TAX_RATES[article.taxType] ?? 0
  return article.priceNet * (1 + rate)
}

function calcArticleTax(article) {
  const rate = TAX_RATES[article.taxType] ?? 0
  return article.priceNet * rate
}

function mapBackendInvoice(inv) {
  const articles = (inv.articles || []).map(a => ({
    articleNumber: a.articleNumber,
    name: a.name,
    priceNet: a.priceNet,
    taxType: a.tax,
    unitType: a.unit,
    quantity: a.quantity,
    gross: calcArticleGross(a),
    tax: calcArticleTax(a),
  }))
  const totalNet = articles.reduce((sum, a) => sum + (a.priceNet * a.quantity), 0)
  const totalGross = articles.reduce((sum, a) => sum + (calcArticleGross(a) * a.quantity), 0)
  const totalTax = articles.reduce((sum, a) => sum + (calcArticleTax(a) * a.quantity), 0)
  return {
    id: inv.id,
    number: inv.number,
    date: inv.date,
    supplier: inv.supplier?.name || inv.supplier,
    articles,
    totalNet,
    totalGross,
    totalTax,
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

  const fetchInvoices = useCallback(() => {
    api('/invoices')
      .then(data => setInvoices(data.map(mapBackendInvoice)))
      .catch(console.error)
  }, [])

  const { isLoggedIn } = useAuth()

  useEffect(() => {
    if (!isLoggedIn) {
      setInvoices([])
      return
    }
    fetchInvoices()
    const interval = setInterval(fetchInvoices, 10000)

    const onVisible = () => { document.visibilityState === 'visible' && fetchInvoices() }
    document.addEventListener('visibilitychange', onVisible)

    return () => {
      clearInterval(interval)
      document.removeEventListener('visibilitychange', onVisible)
    }
  }, [isLoggedIn, fetchInvoices])

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
      payed: data.payed,
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