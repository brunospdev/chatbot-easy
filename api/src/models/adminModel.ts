import connection from '../db'
import bcrypt from 'bcrypt'

export interface Admin {
  nome: string
  email: string
  senha: string
}

const getAllAdmins = async () => {
  const [rows]: any = await connection.execute('SELECT id, nome, email FROM Administrador')
  return rows
}

const getAdminById = async (id: number) => {
  const [[result]]: any = await connection.execute(
    'SELECT id, nome, email FROM Administrador WHERE id = ?',
    [id]
  )
  return result
}

const getAdminByEmail = async (email: string) => {
  const [[result]]: any = await connection.execute(
    'SELECT id, nome, email FROM Administrador WHERE email = ?',
    [email]
  )
  return result
}

const createAdmin = async ({ nome, email, senha }: Admin) => {
  const salt = await bcrypt.genSalt(12)
  const senhaHash = await bcrypt.hash(senha, salt)

  const [{ insertId }]: any = await connection.execute(
    'INSERT INTO Administrador (nome, email, senha) VALUES (?, ?, ?)',
    [nome, email, senhaHash]
  )

  return insertId
}

const updateAdmin = async (id: number, admin: Partial<Admin>) => {
  const allowedFields = ['nome', 'email', 'senha']

  const { setClauses, values } = await Object.entries(admin).reduce(
    (acc, [key, value]) => {
      if (allowedFields.includes(key)) {
        acc.setClauses.push(`${key} = ?`)
        acc.values.push(value)
      }
      return acc
    },
    { setClauses: [] as string[], values: [] as any[] }
  )

  if (setClauses.length === 0) {
    throw new Error('Nenhum campo válido para atualização.')
  }

  const senhaIndex = Object.keys(admin).indexOf('senha')
  if (senhaIndex !== -1 && admin.senha) {
    const salt = await bcrypt.genSalt(12)
    const senhaHash = await bcrypt.hash(admin.senha, salt)
    const senhaPos = allowedFields.indexOf('senha')
    values[senhaPos] = senhaHash
  }

  const query = `UPDATE Administrador SET ${setClauses.join(', ')} WHERE id = ?`
  values.push(id)

  const [{ affectedRows }]: any = await connection.execute(query, values)
  return affectedRows
}

const deleteAdmin = async (id: number) => {
  const [{ affectedRows }]: any = await connection.execute(
    'DELETE FROM Administrador WHERE id = ?',
    [id]
  )
  return affectedRows
}

export default {
  getAllAdmins,
  getAdminById,
  getAdminByEmail,
  createAdmin,
  updateAdmin,
  deleteAdmin,
}
