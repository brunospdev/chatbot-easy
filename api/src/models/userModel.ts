import pool from "../db";

export async function criarUsuario(nome: string, telefone: string | null, papel: number, id_empresa?: number, ) {
  const [result] = await pool.query<any>(
    "INSERT INTO Usuario (nome, telefone, papel, id_empresa) VALUES (?, ?, ?, ?)",
    [nome, telefone, papel, id_empresa]
  );
  return result.insertId;
}

export async function listarUsuarios() {
  const [rows] = await pool.query("SELECT * FROM Usuario");
  return rows;
}

export async function atualizarUsuario(id: number, nome: string, telefone: string, id_empresa: number) {
  const [result]: any = await pool.query(
    "UPDATE Usuario SET nome = ?, telefone = ?, id_empresa = ? WHERE id_user = ?",
    [nome, telefone, id_empresa, id]
  );
  return result;
}

export async function deletarUsuario(id: number) {
  const [result]: any = await pool.query(
    "DELETE FROM Usuario WHERE id_user = ?",
    [id]
  );
  return result;
}

export async function buscarUsuarioPorId(id: number) {
  const [rows]: any = await pool.query(
    "SELECT * FROM Usuario WHERE id_user = ?",
    [id]
  );
  return Array.isArray(rows) && rows.length > 0 ? rows[0] : null;
}