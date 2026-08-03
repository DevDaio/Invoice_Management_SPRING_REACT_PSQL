import { useState } from 'react'
import { api } from '../_helpers/api'
import { useAuth } from '../_context/AuthContext'

export default function SettingsModal({ show, onClose }) {
  const { user } = useAuth()

  const [newMail, setNewMail] = useState('')
  const [newPass, setNewPass] = useState('')
  const [newRole, setNewRole] = useState('USER')
  const [createMsg, setCreateMsg] = useState('')

  const [curPass, setCurPass] = useState('')
  const [newOwnPass, setNewOwnPass] = useState('')
  const [ownMsg, setOwnMsg] = useState('')

  const [adminMail, setAdminMail] = useState('')
  const [adminNewPass, setAdminNewPass] = useState('')
  const [adminMsg, setAdminMsg] = useState('')

  const handleCreateUser = async () => {
    try {
      await api('/newUser', { method: 'POST', body: { mail: newMail, password: newPass, role: newRole } })
      onClose()
    } catch (e) {
      setCreateMsg(e.message)
    }
  }

  const handleOwnPassword = async () => {
    try {
      await api('/update/own-password', { method: 'PUT', body: { currentPassword: curPass, newPassword: newOwnPass } })
      onClose()
    } catch (e) {
      setOwnMsg(e.message)
    }
  }

  const handleAdminPassword = async () => {
    try {
      await api('/update/password', { method: 'PUT', body: { mail: adminMail, password: adminNewPass } })
      onClose()
    } catch (e) {
      setAdminMsg(e.message)
    }
  }

  if (!show) return null

  return (
    <div style={{
      position: 'fixed', inset: 0, zIndex: 1050,
      display: 'flex', alignItems: 'center', justifyContent: 'center',
      backgroundColor: 'rgba(0,0,0,0.5)'
    }} onClick={onClose}>
      <div className="bg-body rounded shadow p-4" style={{ width: '500px', maxHeight: '90vh', overflowY: 'auto' }} onClick={e => e.stopPropagation()}>
        <h4 className="mb-3">Settings</h4>

        <div className="mb-4">
          <h5>Create User</h5>
          <form onSubmit={e => { e.preventDefault(); handleCreateUser() }}>
            <input className="form-control form-control-sm mb-2" type="email" placeholder="Email"
                   value={newMail} onChange={e => setNewMail(e.target.value)} />
            <input className="form-control form-control-sm mb-2" type="password" placeholder="Password"
                   value={newPass} onChange={e => setNewPass(e.target.value)} />
            <select className="form-select form-select-sm mb-2" value={newRole} onChange={e => setNewRole(e.target.value)}>
              <option value="USER">USER</option>
              <option value="ADMIN">ADMIN</option>
            </select>
            <button className="btn btn-sm btn-primary" type="submit">Create</button>
            {createMsg && <p className="mt-1 mb-0 small text-muted">{createMsg}</p>}
          </form>
        </div>

        <hr />

        <div className="mb-4">
          <h5>Change My Password</h5>
          <form onSubmit={e => { e.preventDefault(); handleOwnPassword() }}>
            <input className="form-control form-control-sm mb-2" type="password" placeholder="Current password"
                   value={curPass} onChange={e => setCurPass(e.target.value)} />
            <input className="form-control form-control-sm mb-2" type="password" placeholder="New password"
                   value={newOwnPass} onChange={e => setNewOwnPass(e.target.value)} />
            <button className="btn btn-sm btn-warning" type="submit">Change</button>
            {ownMsg && <p className="mt-1 mb-0 small text-muted">{ownMsg}</p>}
          </form>
        </div>

        <hr />

        <div className="mb-3">
          <h5>Admin: Change User Password</h5>
          <form onSubmit={e => { e.preventDefault(); handleAdminPassword() }}>
            <input className="form-control form-control-sm mb-2" type="email" placeholder="User email"
                   value={adminMail} onChange={e => setAdminMail(e.target.value)} />
            <input className="form-control form-control-sm mb-2" type="password" placeholder="New password"
                   value={adminNewPass} onChange={e => setAdminNewPass(e.target.value)} />
            <button className="btn btn-sm btn-danger" type="submit">Set Password</button>
            {adminMsg && <p className="mt-1 mb-0 small text-muted">{adminMsg}</p>}
          </form>
        </div>
      </div>
    </div>
  )
}
