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
  'TechSupply GmbH',
  'OfficeWorld AG',
  'LogiStar e.K.',
  'BuildMaster KG',
]

export default function EditInvoiceModal({ show, onClose, onSave, invoice }) {
  const [number, setNumber] = useState(invoice?.number || '')
  const [date, setDate] = useState(invoice?.date || '')
  const [supplier, setSupplier] = useState(invoice?.supplier || SUPPLIERS[0])
  const [articles, setArticles] = useState(
    invoice?.articles || [{ articleNumber: '', name: '', priceNet: '', taxType: 'Tax19', unitType: 'Pieces', quantity: '1' }]
  )

  const updateArticle = (index, field, value) => {
    setArticles(prev => prev.map((a, i) => i === index ? { ...a, [field]: value } : a))
  }

  const addArticle = () => setArticles(prev => [...prev, { articleNumber: '', name: '', priceNet: '', taxType: 'Tax19', unitType: 'Pieces', quantity: '1' }])

  const removeArticle = (index) => {
    if (articles.length > 1) setArticles(prev => prev.filter((_, i) => i !== index))
  }

  const calcTotalNet = () => articles.reduce((sum, a) => {
    const price = parseFloat(a.priceNet) || 0
    const qty = parseInt(a.quantity) || 0
    return sum + price * qty
  }, 0)

  const handleSave = () => {
    const totalNet = calcTotalNet()
    onSave(invoice.id, { number, date, supplier, articles, totalNet })
  }

  if (!show || !invoice) return null

  return (
    <div style={{
      position: 'fixed', inset: 0, zIndex: 1050,
      display: 'flex', alignItems: 'center', justifyContent: 'center',
      background: 'rgba(0,0,0,0.6)'
    }} onClick={onClose}>
      <div style={{
        background: '#2C2C2C', padding: '1.5rem', borderRadius: '8px',
        width: '750px', maxHeight: '90vh', overflowY: 'auto', color: '#D3D3D3'
      }} onClick={e => e.stopPropagation()}>

        <h4 style={{ color: '#E8590C', marginBottom: '1rem' }}>Edit Invoice</h4>

        <div className="row g-2 mb-3">
          <div className="col-4">
            <label className="mb-1" style={{ fontSize: '0.85rem' }}>Invoice Number</label>
            <input className="form-control form-control-sm" type="text"
                   value={number} onChange={e => setNumber(e.target.value)}
                   style={{ background: '#3A3A3A', color: '#D3D3D3', border: 'none' }} />
          </div>
          <div className="col-4">
            <label className="mb-1" style={{ fontSize: '0.85rem' }}>Date</label>
            <input className="form-control form-control-sm" type="date"
                   value={date} onChange={e => setDate(e.target.value)}
                   style={{ background: '#3A3A3A', color: '#D3D3D3', border: 'none' }} />
          </div>
          <div className="col-4">
            <label className="mb-1" style={{ fontSize: '0.85rem' }}>Supplier</label>
            <input className="form-control form-control-sm" type="text" list="editSupplierList"
                   value={supplier} onChange={e => setSupplier(e.target.value)}
                   style={{ background: '#3A3A3A', color: '#D3D3D3', border: 'none' }} />
            <datalist id="editSupplierList">
              {SUPPLIERS.map(s => <option key={s} value={s} />)}
            </datalist>
          </div>
        </div>

        <div className="d-flex justify-content-between align-items-center mb-2">
          <span style={{ color: '#E8590C', fontWeight: 500 }}>Articles</span>
          <button className="btn btn-sm" onClick={addArticle}
                  style={{ background: '#E8590C', color: '#fff', border: 'none' }}>+</button>
        </div>

        <div style={{ maxHeight: '350px', overflowY: 'auto' }}>
          <table className="table table-sm table-dark" style={{ marginBottom: 0 }}>
            <thead>
              <tr>
                <th style={{ width: '30px', color: '#9CA3AF' }}>#</th>
                <th style={{ color: '#9CA3AF' }}>Article No.</th>
                <th style={{ color: '#9CA3AF' }}>Name</th>
                <th style={{ width: '90px', color: '#9CA3AF' }}>Net €</th>
                <th style={{ width: '70px', color: '#9CA3AF' }}>Tax</th>
                <th style={{ width: '100px', color: '#9CA3AF' }}>Unit</th>
                <th style={{ width: '60px', color: '#9CA3AF' }}>Qty</th>
                <th style={{ width: '30px' }}></th>
              </tr>
            </thead>
            <tbody>
              {articles.map((article, i) => (
                <tr key={i}>
                  <td style={{ color: '#D3D3D3' }}>{i + 1}</td>
                  <td>
                    <input className="form-control form-control-sm" type="text"
                           value={article.articleNumber}
                           onChange={e => updateArticle(i, 'articleNumber', e.target.value)}
                           style={{ background: '#3A3A3A', color: '#D3D3D3', border: 'none', width: '80px' }} />
                  </td>
                  <td>
                    <input className="form-control form-control-sm" type="text"
                           value={article.name}
                           onChange={e => updateArticle(i, 'name', e.target.value)}
                           style={{ background: '#3A3A3A', color: '#D3D3D3', border: 'none', width: '120px' }} />
                  </td>
                  <td>
                    <input className="form-control form-control-sm" type="number" step="0.01" min="0"
                           value={article.priceNet}
                           onChange={e => updateArticle(i, 'priceNet', e.target.value)}
                           style={{ background: '#3A3A3A', color: '#D3D3D3', border: 'none', width: '80px' }} />
                  </td>
                  <td>
                    <select className="form-select form-select-sm"
                            value={article.taxType}
                            onChange={e => updateArticle(i, 'taxType', e.target.value)}
                            style={{ background: '#3A3A3A', color: '#D3D3D3', border: 'none' }}>
                      {TAX_OPTIONS.map(t => <option key={t.value} value={t.value}>{t.label}</option>)}
                    </select>
                  </td>
                  <td>
                    <select className="form-select form-select-sm"
                            value={article.unitType}
                            onChange={e => updateArticle(i, 'unitType', e.target.value)}
                            style={{ background: '#3A3A3A', color: '#D3D3D3', border: 'none' }}>
                      {UNIT_OPTIONS.map(u => <option key={u.value} value={u.value}>{u.label}</option>)}
                    </select>
                  </td>
                  <td>
                    <input className="form-control form-control-sm" type="number" min="1"
                           value={article.quantity}
                           onChange={e => updateArticle(i, 'quantity', e.target.value)}
                           style={{ background: '#3A3A3A', color: '#D3D3D3', border: 'none', width: '55px' }} />
                  </td>
                  <td>
                    {articles.length > 1 && (
                      <button className="btn btn-sm" onClick={() => removeArticle(i)}
                              style={{ background: 'transparent', border: 'none', color: '#E8590C', padding: 0 }}>
                        ✕
                      </button>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        <div className="d-flex justify-content-end mt-2 mb-3" style={{ fontSize: '1rem', color: '#D3D3D3' }}>
          <strong>Net total:&nbsp;</strong>
          <span style={{ color: '#E8590C' }}>{calcTotalNet().toFixed(2)} €</span>
        </div>

        <button className="btn w-100" disabled={!number || !date || !supplier}
                style={{
                  background: !number || !date || !supplier ? undefined : '#E8590C',
                  border: 'none', color: '#fff'
                }} onClick={handleSave}>
          Save Changes
        </button>
        <button className="btn w-100 mt-2"
                style={{ background: 'transparent', border: '1px solid #E8590C', color: '#E8590C' }}
                onClick={onClose}>
          Cancel
        </button>
      </div>
    </div>
  )
}
