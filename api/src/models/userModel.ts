import connection from '../db'

export interface Usuario {
  nome: string
  telefone?: string | null
  papel: number
  id_empresa?: number | null
}


const createUsuario = async ({ nome, telefone, papel, id_empresa }: Usuario) => {
  const [{ insertId }]: any = await connection.execute(
    `INSERT INTO Usuario (nome, telefone, papel, id_empresa) VALUES (?, ?, ?, ?)`,
    [nome, telefone ?? null, papel, id_empresa ?? null]
  )
  return insertId
}

const getAllUsuarios = async () => {
  const [rows]: any = await connection.execute('SELECT * FROM Usuario')
  return rows
}

const getUsuarioById = async (id: number) => {
  const [[usuario]]: any = await connection.execute(
    'SELECT * FROM Usuario WHERE id_user = ?',
    [id]
  )
  return usuario
}

const updateUsuario = async (
  id: number,
  usuario: Partial<Omit<Usuario, 'papel'>> // evita alterar papel por padrão
) => {
  const fields = ['nome', 'telefone', 'id_empresa']

  const { setClauses, values } = Object.entries(usuario).reduce(
    (acc, [key, value]) => {
      if (fields.includes(key)) {
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

  const query = `UPDATE Usuario SET ${setClauses.join(', ')} WHERE id_user = ?`
  values.push(id)

  const [{ affectedRows }]: any = await connection.execute(query, values)
  return affectedRows
}

const deleteUsuario = async (id: number) => {
  const [{ affectedRows }]: any = await connection.execute(
    'DELETE FROM Usuario WHERE id_user = ?',
    [id]
  )
  return affectedRows
}

export default {
  createUsuario,
  getAllUsuarios,
  getUsuarioById,
  updateUsuario,
  deleteUsuario,
}
