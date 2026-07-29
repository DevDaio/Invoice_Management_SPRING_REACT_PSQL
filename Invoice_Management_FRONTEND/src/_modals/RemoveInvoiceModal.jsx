export default function RemoveInvoiceModal({ show, onClose, onConfirm, invoice }) {

  if (!show || !invoice)
    return null
  return (
    <div style={{
      position: 'fixed', inset: 0, zIndex: 1050,
      display: 'flex', alignItems: 'center', justifyContent: 'center',
      backgroundColor: 'rgba(0,0,0,0.5)'
    }} onClick={onClose}>
      <div className="bg-white rounded shadow p-4" style={{ width: '380px' }} onClick={e => e.stopPropagation()}>
        <h4 className="mb-3">Delete Invoice</h4>
        <p>Are you sure you want to delete invoice <strong>{invoice.number}</strong>?</p>
        <p className="text-muted small">This action cannot be undone.</p>
        <div className="d-flex justify-content-end gap-2">
          <button className="btn btn-secondary" onClick={onClose}>Cancel</button>
          <button className="btn btn-danger" onClick={() => onConfirm(invoice.id)}>Delete</button>
        </div>
      </div>
    </div>
  )
}