import pool from "../db";

export async function criarAdmEmpresa(nome: string, celular: string | null, id_empresa: number) {
  const [result] = await pool.query<any>(
    "INSERT INTO AdmEmpresa (nome, celular, id_empresa) VALUES (?, ?, ?)",
    [nome, celular, id_empresa]
  );
  return result.insertId;
}

export async function listarAdmEmpresas() {
  const [rows] = await pool.query("SELECT * FROM AdmEmpresa");
  return rows;
}
