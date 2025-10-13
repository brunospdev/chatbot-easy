import { Request, Response } from "express";
import { criarAdministrador, listarAdministradores } from "../models/adminModel";

export async function criarAdministradorController(req: Request, res: Response) {
  const { nome, email, senha } = req.body;

  if (!nome || !email || !senha) {
    return res.status(400).json({ error: "Todos os campos são obrigatórios." });
  }

  try {
    const id_admin = await criarAdministrador(nome, email, senha);
    return res.status(201).json({ id_admin, nome, email });
  } catch (err: any) {
    console.error("Erro ao criar administrador:", err);

    if (err.code === 'ER_DUP_ENTRY') {
      return res.status(409).json({ error: "Este email já está em uso." });
    }

    return res.status(500).json({ error: err.message || "Erro interno do servidor." });
  }
}

export async function listarAdministradoresController(_req: Request, res: Response) {
  try {
    const admins = await listarAdministradores();
    return res.json(admins);
  } catch (err: any) {
    console.error("Erro ao listar administradores:", err);
    return res.status(500).json({ error: err.message || "Erro interno do servidor." });
  }
}
