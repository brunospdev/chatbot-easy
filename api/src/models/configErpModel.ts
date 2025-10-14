import connection from '../db'

export interface ConfiguracaoErp {
  id_empresa: number
  url_api: string
  token_api: string
  status: string
}

const createConfiguracaoErp = async ({
  id_empresa,
  url_api,
  token_api,
  status,
}: ConfiguracaoErp) => {
  const [{ insertId }]: any = await connection.execute(
    `INSERT INTO ConfiguracaoERP (id_empresa, url_api, token_api, status) VALUES (?, ?, ?, ?)`,
    [id_empresa, url_api, token_api, status]
  )
  return insertId
}

const getAllConfiguracoesErp = async () => {
  const [rows]: any = await connection.execute('SELECT * FROM ConfiguracaoERP')
  return rows
}

const getConfiguracaoErpById = async (id: number) => {
  const [[result]]: any = await connection.execute(
    'SELECT * FROM ConfiguracaoERP WHERE id = ?',
    [id]
  )
  return result
}

const updateConfiguracaoErp = async (
  id: number,
  config: Partial<ConfiguracaoErp>
) => {
  const fields = ['id_empresa', 'url_api', 'token_api', 'status']

  const { setClauses, values } = Object.entries(config).reduce(
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

  const query = `UPDATE ConfiguracaoERP SET ${setClauses.join(', ')} WHERE id = ?`
  values.push(id)

  const [{ affectedRows }]: any = await connection.execute(query, values)
  return affectedRows
}

const deleteConfiguracaoErp = async (id: number) => {
  const [{ affectedRows }]: any = await connection.execute(
    'DELETE FROM ConfiguracaoERP WHERE id = ?',
    [id]
  )
  return affectedRows
}

export default {
  createConfiguracaoErp,
  getAllConfiguracoesErp,
  getConfiguracaoErpById,
  updateConfiguracaoErp,
  deleteConfiguracaoErp,
}
