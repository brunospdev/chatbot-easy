import pool from "../db";
import bcrypt from "bcrypt";

export async function criarAdministrador(nome: string, email: string, senha: string) {
  const salt = await bcrypt.genSalt(12);
  const senhaHash = await bcrypt.hash(senha, salt);

  const [result] = await pool.query<any>(
    "INSERT INTO Administrador (nome, email, senha) VALUES (?, ?, ?)",
    [nome, email, senhaHash]
  );

  return result.insertId;
}

export async function listarAdministradores() {
  const [rows] = await pool.query("SELECT * FROM Administrador");
  return rows;
}
