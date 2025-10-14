import connection from '../db'

interface Solicitacao {
  id_usuario: number
  tipo_solicitacao: string
  status: string
  data_solicitacao?: Date
}

const createSolicitacao = async ({ id_usuario, tipo_solicitacao, status }: Solicitacao) => {
  const [{ insertId }]: any = await connection.execute(
    `INSERT INTO Solicitacao (id_usuario, tipo_solicitacao, status, data_solicitacao) VALUES (?, ?, ?, NOW())`,
    [id_usuario, tipo_solicitacao, status]
  )
  return insertId
}

const getAllSolicitacoes = async () => {
  const [rows]: any = await connection.execute('SELECT * FROM Solicitacao')
  return rows
}

const getSolicitacaoById = async (id: number) => {
  const [[result]]: any = await connection.execute(
    'SELECT * FROM Solicitacao WHERE id = ?',
    [id]
  )
  return result
}

const getSolicitacoesByUsuario = async (id_usuario: number) => {
  const [rows]: any = await connection.execute(
    'SELECT * FROM Solicitacao WHERE id_usuario = ?',
    [id_usuario]
  )
  return rows
}

const updateSolicitacao = async (id: number, solicitacao: Partial<Solicitacao>) => {
  const fields = ['id_usuario', 'tipo_solicitacao', 'status']

  const { setClauses, values } = Object.entries(solicitacao).reduce(
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

  const query = `UPDATE Solicitacao SET ${setClauses.join(', ')} WHERE id = ?`
  values.push(id)

  const [{ affectedRows }]: any = await connection.execute(query, values)
  return affectedRows
}

const deleteSolicitacao = async (id: number) => {
  const [{ affectedRows }]: any = await connection.execute(
    'DELETE FROM Solicitacao WHERE id = ?',
    [id]
  )
  return affectedRows
}

export default {
  createSolicitacao,
  getAllSolicitacoes,
  getSolicitacaoById,
  getSolicitacoesByUsuario,
  updateSolicitacao,
  deleteSolicitacao,
}
