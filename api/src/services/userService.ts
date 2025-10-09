import pool from "../db";

export async function criarUsuario(nome: string, telefone: string | null, papel: number, id_empresa: number, ) {
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
