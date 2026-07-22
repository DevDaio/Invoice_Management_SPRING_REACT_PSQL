import { useEffect, useState } from 'react'
import AddInvoiceModal from '../modals/AddInvoiceModal'
import EditInvoiceModal from '../modals/EditInvoiceModal'
import RemoveInvoiceModal from '../modals/RemoveInvoiceModal'

export default function MainPage({ invoices, onAddInvoice, onUpdateInvoice, onDeleteInvoice, activeModal, onCloseModal, onOpenModal }) {
  const [addKey, setAddKey] = useState(0)

  useEffect(() => {
    if (invoices.length === 0) {
      setAddKey(prev => prev + 1)
      onOpenModal?.('addInvoice')
    }
  }, [invoices.length])
  const editingInvoice = activeModal?.startsWith('editInvoice-')
    ? invoices.find(i => i.id === parseInt(activeModal.split('-')[1]))
    : null

  const removingInvoice = activeModal?.startsWith('removeInvoice-')
    ? invoices.find(i => i.id === parseInt(activeModal.split('-')[1]))
    : null

  return (
    <div style={{
      minHeight: 'calc(100vh - 56px)',
      background: "url('/BG1.png') center/cover no-repeat",
      padding: '2rem'
    }}>
      <div style={{
        background: 'rgba(44,44,44,0.95)',
        borderRadius: '8px',
        padding: '1.5rem',
        color: '#D3D3D3'
      }}>
        <div className="d-flex justify-content-between align-items-center mb-3">
          <h3 style={{ color: '#E8590C', margin: 0 }}>Dashboard</h3>
          <span style={{ fontSize: '0.9rem', color: '#9CA3AF' }}>
            {invoices.length} invoice{invoices.length !== 1 ? 's' : ''}
          </span>
        </div>

        {invoices.length === 0 ? (
          <p>No invoices yet. Create your first one!</p>
        ) : (
          <table className="table table-sm table-dark" style={{ marginBottom: 0 }}>
            <thead>
              <tr>
                <th style={{ color: '#9CA3AF', width: '40px' }}>#</th>
                <th style={{ color: '#9CA3AF' }}>Invoice No.</th>
                <th style={{ color: '#9CA3AF' }}>Date</th>
                <th style={{ color: '#9CA3AF' }}>Supplier</th>
                <th style={{ color: '#9CA3AF', width: '100px' }}>Total Net</th>
                <th style={{ color: '#9CA3AF', width: '100px' }}>Actions</th>
              </tr>
            </thead>
            <tbody>
              {invoices.map((inv, i) => (
                <tr key={inv.id}>
                  <td style={{ color: '#D3D3D3' }}>{i + 1}</td>
                  <td style={{ color: '#D3D3D3' }}>{inv.number}</td>
                  <td style={{ color: '#D3D3D3' }}>{inv.date}</td>
                  <td style={{ color: '#D3D3D3' }}>{inv.supplier}</td>
                  <td style={{ color: '#E8590C' }}>{inv.totalNet.toFixed(2)} €</td>
                  <td>
                    <button className="btn btn-sm me-1"
                            style={{ background: 'transparent', border: '1px solid #E8590C', color: '#E8590C', padding: '2px 8px', fontSize: '0.8rem' }}
                            onClick={() => onOpenModal(`editInvoice-${inv.id}`)}>
                      Edit
                    </button>
                    <button className="btn btn-sm"
                            style={{ background: 'transparent', border: '1px solid #E8590C', color: '#E8590C', padding: '2px 8px', fontSize: '0.8rem' }}
                            onClick={() => onOpenModal(`removeInvoice-${inv.id}`)}>
                      Delete
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>

      <AddInvoiceModal key={addKey} show={activeModal === 'addInvoice'} onClose={onCloseModal}
                       onSave={onAddInvoice} />
      <EditInvoiceModal key={activeModal} show={!!editingInvoice} onClose={onCloseModal}
                        invoice={editingInvoice} onSave={onUpdateInvoice} />
      <RemoveInvoiceModal show={!!removingInvoice} onClose={onCloseModal}
                          invoice={removingInvoice} onConfirm={onDeleteInvoice} />
    </div>
  )
}
