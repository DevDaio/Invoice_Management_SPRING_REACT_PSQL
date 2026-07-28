import { useInvoices } from '../_context/InvoiceContext'
import { useModal } from '../_context/ModalContext'

import AddInvoiceModal from '../_modals/AddInvoiceModal'
import EditInvoiceModal from '../_modals/EditInvoiceModal'
import RemoveInvoiceModal from '../_modals/RemoveInvoiceModal'

export default function MainPage() {

  const { invoices, addInvoice, updateInvoice, deleteInvoice } = useInvoices()
  const { activeModal, selectedInvoice, openModal, closeModal } = useModal()

  return (
    <div>
      <button onClick={() => openModal('add')}>+ New Invoice</button>
      <h3>Invoices</h3>
      {invoices.length === 0 ? ( <p>No invoices yet.</p>) : 
      (<table>
          <thead>
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
                  <button onClick={() => openModal('edit', inv)}>Edit</button>
                  <button onClick={() => openModal('remove', inv)}>Delete</button>
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