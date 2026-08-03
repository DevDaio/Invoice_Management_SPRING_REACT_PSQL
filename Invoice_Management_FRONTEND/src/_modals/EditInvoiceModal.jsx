import { useState, useEffect } from 'react'
import { api } from '../_helpers/api'

const TAX_RATES = { Tax0: 0, Tax7: 0.07, Tax19: 0.19 }

const TAX_OPTIONS = [
  { value: 'Tax0', label: '0%' },
  { value: 'Tax7', label: '7%' },
  { value: 'Tax19', label: '19%' },
]

const UNIT_OPTIONS = [
  { value: 'Pieces', label: 'Pieces' },
  { value: 'Meter', label: 'Meter' },
  { value: 'Box', label: 'Box' },
  { value: 'Pallet', label: 'Pallet' },
  { value: 'Service', label: 'Service' },
  { value: 'Position', label: 'Position' },
]

export default function EditInvoiceModal({ show, onClose, onSave, invoice }) {

  const [number, setNumber] = useState(invoice?.number || '')
  const [date, setDate] = useState(invoice?.date || '')
  const [supplier, setSupplier] = useState(invoice?.supplier || '')
  const [payed, setPayed] = useState(invoice?.payed || false)
  const [articles, setArticles] = useState(
    invoice?.articles || [{ articleNumber: '', name: '', priceNet: '', taxType: 'Tax19', unitType: 'Pieces', quantity: '1' }]
  )
  const [suppliers, setSuppliers] = useState([])

  useEffect(() => {
    if (show) {
      api('/suppliers').then(data => setSuppliers(data.map(s => s.name))).catch(() => {})
    }
  }, [show])

  const updateArticle = (index, field, value) => {
    setArticles(prev => prev.map((a, i) => i === index ? { ...a, [field]: value } : a))
  }

  const addArticle = () => setArticles(prev => [...prev, { articleNumber: '', name: '', priceNet: '', taxType: 'Tax19', unitType: 'Pieces', quantity: '1' }])

  const removeArticle = (index) => {
    if (articles.length > 1) setArticles(prev => prev.filter((_, i) => i !== index))
  }

  const articleGross = (a) => {
    const price = parseFloat(a.priceNet) || 0
    return price * (1 + (TAX_RATES[a.taxType] ?? 0))
  }

  const articleTax = (a) => {
    const price = parseFloat(a.priceNet) || 0
    return price * (TAX_RATES[a.taxType] ?? 0)
  }

  const calcTotalNet = () => articles.reduce((sum, a) => {
    const price = parseFloat(a.priceNet) || 0
    const qty = parseInt(a.quantity) || 0
    return sum + price * qty
  }, 0)

  const calcTotalGross = () => articles.reduce((sum, a) => {
    const qty = parseInt(a.quantity) || 0
    return sum + articleGross(a) * qty
  }, 0)

  const calcTotalTax = () => articles.reduce((sum, a) => {
    const qty = parseInt(a.quantity) || 0
    return sum + articleTax(a) * qty
  }, 0)

  const handleSave = () => {
    const totalNet = calcTotalNet()
    onSave(invoice.id, { number, date, supplier, articles, totalNet, payed })
  }

	if (!show || !invoice)
		return null
	return (
    <div style={{
      position: 'fixed', inset: 0, zIndex: 1050,
      display: 'flex', alignItems: 'center', justifyContent: 'center',
      backgroundColor: 'rgba(0,0,0,0.5)'
    }} onClick={onClose}>
      <div className="bg-white rounded shadow p-4" style={{ width: '1100px', maxHeight: '90vh', overflowY: 'auto' }} onClick={e => e.stopPropagation()}>
        <h4 className="mb-3">Edit Invoice</h4>

        <div className="row mb-3">
          <div className="col-md-4 mb-2">
            <label className="form-label">Invoice Number</label>
            <input className="form-control form-control-sm" type="text"
                   value={number} onChange={e => setNumber(e.target.value)} />
          </div>
          <div className="col-md-4 mb-2">
            <label className="form-label">Date</label>
            <input className="form-control form-control-sm" type="date"
                   value={date} onChange={e => setDate(e.target.value)} />
          </div>
          <div className="col-md-4 mb-2">
            <label className="form-label">Supplier</label>
            <input className="form-control form-control-sm" type="text" list="editSupplierList"
                   value={supplier} onChange={e => setSupplier(e.target.value)} />
            <datalist id="editSupplierList">
              {suppliers.map(s => <option key={s} value={s} />)}
            </datalist>
          </div>
        </div>

        <div className="d-flex justify-content-between align-items-center mb-2">
          <div className="form-check form-switch mb-0">
            <input className="form-check-input" type="checkbox" id="editPaidCheck"
                   checked={payed} onChange={e => setPayed(e.target.checked)} />
            <label className="form-check-label" htmlFor="editPaidCheck">
              <span className={`badge ${payed ? 'badge-paid' : 'badge-open'}`}>
                {payed ? 'Paid' : 'Open'}
              </span>
            </label>
          </div>
          <button className="btn btn-sm btn-outline-success" onClick={addArticle}>+ Add Article</button>
        </div>

        <div style={{ maxHeight: '350px', overflowY: 'auto' }}>
          <table className="table table-sm table-bordered mb-2">
            <thead className="table-light">
              <tr>
                <th style={{width:35}}>#</th>
                <th style={{width:140}}>Article No.</th>
                <th>Name</th>
                <th style={{width:110}}>Net €</th>
                <th style={{width:140}}>Tax %</th>
                <th style={{width:120}}>Gross €</th>
                <th style={{width:150}}>Unit</th>
                <th style={{width:90}}>Qty</th>
                <th style={{width:45}}></th>
              </tr>
            </thead>
            <tbody>
              {articles.map((article, i) => (
                <tr key={i}>
                  <td>{i + 1}</td>
                  <td>
                    <input className="form-control form-control-sm" type="text"
                           value={article.articleNumber}
                           onChange={e => updateArticle(i, 'articleNumber', e.target.value)} />
                  </td>
                  <td>
                    <input className="form-control form-control-sm" type="text"
                           value={article.name}
                           onChange={e => updateArticle(i, 'name', e.target.value)} />
                  </td>
                  <td>
                    <input className="form-control form-control-sm" type="number" step="0.01" min="0"
                           value={article.priceNet}
                           onChange={e => updateArticle(i, 'priceNet', e.target.value)} />
                  </td>
                  <td>
                    <select className="form-select form-select-sm" value={article.taxType}
                            onChange={e => updateArticle(i, 'taxType', e.target.value)}>
                      {TAX_OPTIONS.map(t => <option key={t.value} value={t.value}>{t.label}</option>)}
                    </select>
                  </td>
                  <td className="text-end text-nowrap">{articleGross(article).toFixed(2)} €</td>
                  <td>
                    <select className="form-select form-select-sm" value={article.unitType}
                            onChange={e => updateArticle(i, 'unitType', e.target.value)}>
                      {UNIT_OPTIONS.map(u => <option key={u.value} value={u.value}>{u.label}</option>)}
                    </select>
                  </td>
                  <td>
                    <input className="form-control form-control-sm" type="number" min="1"
                           value={article.quantity}
                           onChange={e => updateArticle(i, 'quantity', e.target.value)} />
                  </td>
                  <td>
                    {articles.length > 1 && (
                      <button className="btn btn-sm btn-outline-danger" onClick={() => removeArticle(i)}>✕</button>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        <div className="text-end mb-3">
          <strong>Net: {calcTotalNet().toFixed(2)} €</strong>
          &nbsp;|&nbsp;
          <strong>Tax: {calcTotalTax().toFixed(2)} €</strong>
          &nbsp;|&nbsp;
          <strong>Gross: {calcTotalGross().toFixed(2)} €</strong>
        </div>

        <div className="d-flex justify-content-end gap-2">
          <button className="btn btn-secondary" onClick={onClose}>Cancel</button>
          <button className="btn btn-primary" disabled={!number || !date || !supplier}
                  onClick={handleSave}>Save Changes</button>
        </div>
      </div>
    </div>
  )
}