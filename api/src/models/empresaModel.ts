import connection from '../db'

interface Empresa {
  nome: string
  celular?: string | null
  id_empresa: number
}

const createEmpresa = async ({ nome, celular, id_empresa }: Empresa) => {
  const [{ insertId }]: any = await connection.execute(
    `INSERT INTO Empresa (nome, celular, id_empresa) VALUES (?, ?, ?)`,
    [nome, celular ?? null, id_empresa]
  );
  return insertId;
};

const getAllEmpresas = async () => {
  const [rows]: any = await connection.execute('SELECT * FROM Empresa')
  return rows
}

const getEmpresaById = async (id: number) => {
  const [[result]]: any = await connection.execute(
    'SELECT * FROM Empresa WHERE id = ?',
    [id]
  )
  return result
}

const getEmpresaByNome = async (nome: string) => {
  const [rows]: any = await connection.execute(
    'SELECT * FROM Empresa WHERE nome COLLATE utf8mb4_general_ci LIKE ?',
    [`%${nome}%`]
  )
  return rows
}

const updateEmpresa = async (id: number, empresa: Partial<Empresa>) => {
  const fields = ['nome', 'celular', 'id_empresa']

  const { setClauses, values } = Object.entries(empresa).reduce(
    (acc, [key, value]) => {
      if (fields.includes(key)) {
        acc.setClauses.push(`${key} = ?`)
        acc.values.push(value ?? null)
      }
      return acc
    },
    { setClauses: [] as string[], values: [] as any[] }
  )

  if (setClauses.length === 0) {
    throw new Error('Nenhum campo válido para atualização.')
  }

  const query = `UPDATE Empresa SET ${setClauses.join(', ')} WHERE id = ?`
  values.push(id)

  const [{ affectedRows }]: any = await connection.execute(query, values)
  return affectedRows
}

const deleteEmpresa = async (id: number) => {
  const [{ affectedRows }]: any = await connection.execute(
    'DELETE FROM Empresa WHERE id = ?',
    [id]
  )
  return affectedRows
}

export default {
  createEmpresa,
  getAllEmpresas,
  getEmpresaById,
  getEmpresaByNome,
  updateEmpresa,
  deleteEmpresa,
}
