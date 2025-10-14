import pool from "../db";

export async function criarSolicitacao(id_usuario: number, tipo_solicitacao: string, status: string) {
  const [result] = await pool.query<any>(
    "INSERT INTO Solicitacao (id_usuario, tipo_solicitacao, status, data_solicitacao) VALUES (?, ?, ?, NOW())",
    [id_usuario, tipo_solicitacao, status]
  );
  return result.insertId;
}

export async function listarSolicitacoes() {
  const [rows] = await pool.query("SELECT * FROM Solicitacao");
  return rows;
}
