import { useState } from 'react'

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

const SUPPLIERS = [
  'MOCK TechSupply GmbH',
  'MOCK OfficeWorld AG',
  'LogiStar e.K.',
  'BuildMaster KG',
]

const emptyArticle = {
  articleNumber: '',
  name: '',
  priceNet: '',
  taxType: 'Tax19',
  unitType: 'Pieces',
  quantity: '1',
}

export default function AddInvoiceModal({ show, onClose, onSave }) {

  const [number, setNumber] = useState('')

  const [date, setDate] = useState('')

  const [supplier, setSupplier] = useState(SUPPLIERS[0])

  const [articles, setArticles] = useState([{ ...emptyArticle }])

  const handleSave = () => {
    onSave({ number, date, supplier, articles, totalNet })
  }

  const updateArticle = (index, field, value) => {
    setArticles(prev => prev.map((a, i) =>
      i === index ? { ...a, [field]: value } : a
    ))
  }

  const addArticle = () => setArticles(prev => [...prev, { ...emptyArticle }])

  const removeArticle = (index) => {
    if (articles.length > 1) {
      setArticles(prev => prev.filter((_, i) => i !== index))
    }
  }

  const totalNet = articles.reduce((sum, a) => {
    const price = parseFloat(a.priceNet) || 0
    const qty = parseInt(a.quantity) || 0
    return sum + price * qty
  }, 0)

  if (!show) return null

  return (
    <div style={{
      position: 'fixed', inset: 0, zIndex: 1050,
      display: 'flex', alignItems: 'center', justifyContent: 'center'
    }} onClick={onClose}>
      <div style={{
        padding: '1.5rem', borderRadius: '8px',
        width: '750px', maxHeight: '90vh', overflowY: 'auto'
      }} onClick={e => e.stopPropagation()}>
        <h4 style={{ marginBottom: '1rem' }}>Create Invoice</h4>

        <div>
          <div>
            <label>Invoice Number</label>
            <input type="text" placeholder="e.g. INV-2024-001"
                   value={number} onChange={e => setNumber(e.target.value)} />
          </div>
          <div>
            <label>Date</label>
            <input type="date"
                   value={date} onChange={e => setDate(e.target.value)} />
          </div>
          <div>
            <label>Supplier</label>
            <input type="text" list="supplierList"
                   placeholder="Select or type new supplier"
                   value={supplier} onChange={e => setSupplier(e.target.value)} />
            <datalist id="supplierList">
              {SUPPLIERS.map(s => <option key={s} value={s} />)}
            </datalist>
          </div>
        </div>

        <div>
          <span>Articles</span>
          <button onClick={addArticle}>+</button>
        </div>

        <div style={{ maxHeight: '350px', overflowY: 'auto' }}>
          <table>
            <thead>
              <tr>
                <th>#</th>
                <th>Article No.</th>
                <th>Name</th>
                <th>Net €</th>
                <th>Tax</th>
                <th>Unit</th>
                <th>Qty</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {articles.map((article, i) => (
                <tr key={i}>
                  <td>{i + 1}</td>
                  <td>
                    <input type="text"
                           value={article.articleNumber}
                           onChange={e => updateArticle(i, 'articleNumber', e.target.value)} />
                  </td>
                  <td>
                    <input type="text"
                           value={article.name}
                           onChange={e => updateArticle(i, 'name', e.target.value)} />
                  </td>
                  <td>
                    <input type="number" step="0.01" min="0"
                           value={article.priceNet}
                           onChange={e => updateArticle(i, 'priceNet', e.target.value)} />
                  </td>
                  <td>
                    <select value={article.taxType}
                            onChange={e => updateArticle(i, 'taxType', e.target.value)}>
                      {TAX_OPTIONS.map(t => <option key={t.value} value={t.value}>{t.label}</option>)}
                    </select>
                  </td>
                  <td>
                    <select value={article.unitType}
                            onChange={e => updateArticle(i, 'unitType', e.target.value)}>
                      {UNIT_OPTIONS.map(u => <option key={u.value} value={u.value}>{u.label}</option>)}
                    </select>
                  </td>
                  <td>
                    <input type="number" min="1"
                           value={article.quantity}
                           onChange={e => updateArticle(i, 'quantity', e.target.value)} />
                  </td>
                  <td>
                    {articles.length > 1 && (
                      <button onClick={() => removeArticle(i)}>✕</button>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        <div>
          <strong>Net total: </strong>
          <span>{totalNet.toFixed(2)} €</span>
        </div>

        <button disabled={!number || !date || !supplier}
                onClick={handleSave}>
          Save
        </button>
        <button onClick={onClose}>
          Cancel
        </button>
      </div>
    </div>
  )
}