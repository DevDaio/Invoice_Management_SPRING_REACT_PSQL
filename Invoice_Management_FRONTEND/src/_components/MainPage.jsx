import { useInvoices } from '../_context/InvoiceContext'
import { useModal } from '../_context/ModalContext'

import AddInvoiceModal from '../_modals/AddInvoiceModal'
import EditInvoiceModal from '../_modals/EditInvoiceModal'
import RemoveInvoiceModal from '../_modals/RemoveInvoiceModal'

export default function MainPage() {

  const { invoices, addInvoice, updateInvoice, deleteInvoice } = useInvoices()
  const { activeModal, selectedInvoice, openModal, closeModal } = useModal()

  return (
    <div className="container mt-4">
      <div className="d-flex justify-content-between align-items-center mb-3">
        <h3>Invoices</h3>
        <button className="btn btn-success" onClick={() => openModal('add')}>+ New Invoice</button>
      </div>
      {invoices.length === 0 ? ( <p>No invoices yet.</p>) : 
      (<table className="table table-striped">
          <thead className="table-dark">
            <tr>
              <th>Number</th>
              <th>Date</th>
              <th>Supplier</th>
              <th>Total</th>
              <th></th>
              </tr>
          </thead>
          <tbody>
            {invoices.map((inv) => (
              <tr key={inv.id}>
                <td>{inv.number}</td>
                <td>{inv.date}</td>
                <td>{inv.supplier}</td>
                <td>{inv.totalNet.toFixed(2)} €</td>
                <td>
                  <button className="btn btn-sm btn-outline-primary me-1" onClick={() => openModal('edit', inv)}>Edit</button>
                  <button className="btn btn-sm btn-outline-danger" onClick={() => openModal('remove', inv)}>Delete</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}

      <AddInvoiceModal show={activeModal === 'add'} onClose={closeModal} onSave={addInvoice} />
      <EditInvoiceModal show={activeModal === 'edit'} onClose={closeModal} onSave={updateInvoice} invoice={selectedInvoice} />
      <RemoveInvoiceModal show={activeModal === 'remove'} onClose={closeModal} onConfirm={deleteInvoice} invoice={selectedInvoice} />
    </div>
  )
}