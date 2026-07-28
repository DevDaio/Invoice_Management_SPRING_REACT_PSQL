export default function RemoveInvoiceModal({ show, onClose, onConfirm, invoice }) {

	if (!show || !invoice)
		return null
	return (
    <div style={{
      position: 'fixed', inset: 0, zIndex: 1050,
      display: 'flex', alignItems: 'center', justifyContent: 'center'
    }} onClick={onClose}>
      <div style={{
        padding: '2rem', borderRadius: '8px',
        width: '380px'
      }} onClick={e => e.stopPropagation()}>
        <h4 style={{ marginBottom: '1rem' }}>Delete Invoice</h4>
        <p>Are you sure you want to delete invoice <strong>{invoice.number}</strong>?</p>
        <p>This action cannot be undone.</p>
        <button onClick={() => onConfirm(invoice.id)}>
          Delete
        </button>
        <button onClick={onClose}>
          Cancel
        </button>
      </div>
    </div>
  )
}