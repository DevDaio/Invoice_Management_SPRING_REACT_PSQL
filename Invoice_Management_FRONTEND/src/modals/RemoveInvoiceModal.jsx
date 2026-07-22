export default function RemoveInvoiceModal({ show, onClose, onConfirm, invoice }) {
  if (!show || !invoice) return null

  return (
    <div style={{
      position: 'fixed', inset: 0, zIndex: 1050,
      display: 'flex', alignItems: 'center', justifyContent: 'center',
      background: 'rgba(0,0,0,0.6)'
    }} onClick={onClose}>
      <div style={{
        background: '#2C2C2C', padding: '2rem', borderRadius: '8px',
        width: '380px', color: '#D3D3D3'
      }} onClick={e => e.stopPropagation()}>
        <h4 style={{ color: '#E8590C', marginBottom: '1rem' }}>Delete Invoice</h4>
        <p>Are you sure you want to delete invoice <strong>{invoice.number}</strong>?</p>
        <p style={{ fontSize: '0.9rem', color: '#9CA3AF' }}>This action cannot be undone.</p>

        <button className="btn w-100" onClick={() => onConfirm(invoice.id)}
                style={{ background: '#E8590C', border: 'none', color: '#fff' }}>
          Delete
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
