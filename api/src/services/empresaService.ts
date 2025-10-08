import pool from "../db";

export async function criarEmpresa(nome: string, celular: string | null, id_empresa: number) {
  const [result] = await pool.query<any>(
    "INSERT INTO Empresa (nome, celular, id_empresa) VALUES (?, ?, ?)",
    [nome, celular, id_empresa]
  );
  return result.insertId;
}

export async function listarEmpresas() {
  const [rows] = await pool.query("SELECT * FROM Empresa");
  return rows;
}
