import pool from "../db";

export async function criarUsuario(nome: string, celular: string | null, id_empresa: number) {
  const [result] = await pool.query<any>(
    "INSERT INTO Usuario (nome, celular, id_empresa) VALUES (?, ?, ?)",
    [nome, celular, id_empresa]
  );
  return result.insertId;
}

export async function listarUsuarios() {
  const [rows] = await pool.query("SELECT * FROM Usuario");
  return rows;
}
