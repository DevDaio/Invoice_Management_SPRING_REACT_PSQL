import { useInvoices } from '../_context/InvoiceContext'
import { useModal } from '../_context/ModalContext'

import AddInvoiceModal from '../_modals/AddInvoiceModal'
import EditInvoiceModal from '../_modals/EditInvoiceModal'
import RemoveInvoiceModal from '../_modals/RemoveInvoiceModal'
import SettingsModal from '../_modals/SettingsModal'

export default function MainPage() {

  const { invoices, addInvoice, updateInvoice, deleteInvoice } = useInvoices()
  const { activeModal, selectedInvoice, showSettings, openModal, closeModal, closeSettings } = useModal()

  const handleAdd = async (data) => {
    await addInvoice(data)
    closeModal()
  }

  const handleEdit = async (id, data) => {
    await updateInvoice(id, data)
    closeModal()
  }

  const handleDelete = async (id) => {
    await deleteInvoice(id)
    closeModal()
  }

  const totalInvoices = invoices.length
  const totalGross = invoices.reduce((sum, inv) => sum + inv.totalGross, 0)
  const openGross = invoices.filter(inv => !inv.payed).reduce((sum, inv) => sum + inv.totalGross, 0)
  const paidGross = invoices.filter(inv => inv.payed).reduce((sum, inv) => sum + inv.totalGross, 0)

  return (
    <div className="container-fluid py-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h3 className="mb-0">Invoices</h3>
        <button className="btn btn-success" onClick={() => openModal('add')}>+ New Invoice</button>
      </div>

      <div className="row g-3 mb-4">
        <div className="col-sm-6 col-lg-3">
          <div className="kpi-card">
            <div className="kpi-label">Invoices</div>
            <div className="kpi-value">{totalInvoices}</div>
          </div>
        </div>
        <div className="col-sm-6 col-lg-3">
          <div className="kpi-card">
            <div className="kpi-label">Open</div>
            <div className="kpi-value text-warning">{openGross.toFixed(2)} €</div>
          </div>
        </div>
        <div className="col-sm-6 col-lg-3">
          <div className="kpi-card">
            <div className="kpi-label">Paid</div>
            <div className="kpi-value text-success">{paidGross.toFixed(2)} €</div>
          </div>
        </div>
        <div className="col-sm-6 col-lg-3">
          <div className="kpi-card">
            <div className="kpi-label">Total Gross</div>
            <div className="kpi-value">{totalGross.toFixed(2)} €</div>
          </div>
        </div>
      </div>

      {invoices.length === 0 ? ( <p>No invoices yet.</p>) : 
      (<table className="table table-striped">
          <thead className="table-dark">
            <tr>
              <th>Number</th>
              <th>Date</th>
              <th>Supplier</th>
              <th>Status</th>
              <th>Net</th>
              <th>Tax</th>
              <th>Gross</th>
              <th></th>
              </tr>
          </thead>
          <tbody>
            {invoices.map((inv) => (
              <tr key={inv.id}>
                <td>{inv.number}</td>
                <td>{inv.date}</td>
                <td>{inv.supplier}</td>
                <td>
                  <span className={`badge ${inv.payed ? 'badge-paid' : 'badge-open'}`}>
                    {inv.payed ? 'Paid' : 'Open'}
                  </span>
                </td>
                <td>{inv.totalNet.toFixed(2)} €</td>
                <td>{inv.totalTax.toFixed(2)} €</td>
                <td>{inv.totalGross.toFixed(2)} €</td>
                <td>
                  <button className="btn btn-sm btn-outline-primary me-1" onClick={() => openModal('edit', inv)}>Edit</button>
                  <button className="btn btn-sm btn-outline-danger" onClick={() => openModal('remove', inv)}>Delete</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}

      <AddInvoiceModal show={activeModal === 'add'} onClose={closeModal} onSave={handleAdd} />
      <EditInvoiceModal key={selectedInvoice?.id} show={activeModal === 'edit'} onClose={closeModal} onSave={handleEdit} invoice={selectedInvoice} />
      <RemoveInvoiceModal show={activeModal === 'remove'} onClose={closeModal} onConfirm={handleDelete} invoice={selectedInvoice} />
      <SettingsModal show={showSettings} onClose={closeSettings} />
    </div>
  )
}
