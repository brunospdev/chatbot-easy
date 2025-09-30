import pool from "../db";

export async function criarConfiguracaoErp(
  id_empresa: number,
  url_api: string,
  token_api: string,
  status: string
) {
  const [result] = await pool.query<any>(
    "INSERT INTO ConfiguracaoERP (id_empresa, url_api, token_api, status) VALUES (?, ?, ?, ?)",
    [id_empresa, url_api, token_api, status]
  );
  return result.insertId;
}

export async function listarConfiguracoesErp() {
  const [rows] = await pool.query("SELECT * FROM ConfiguracaoERP");
  return rows;
}
