import { Request, Response } from "express";
import userModel from "../models/userModel";

export async function criarUsuarioController(req: Request, res: Response) {
  const { nome, telefone, papel, id_empresa } = req.body;

  if (!nome || !papel) {
    return res.status(400).json({ error: "Nome e papel são obrigatórios." });
  }

  try {
    const id_usuario = await userModel.createUsuario({ nome, telefone, papel, id_empresa });
    res.status(201).json({ id_usuario, nome, telefone, papel, id_empresa });
  } catch (err: any) {
    console.error("Erro ao criar usuário:", err);
    res.status(500).json({ error: err.message || "Erro interno do servidor." });
  }
}

export async function listarUsuariosController(_req: Request, res: Response) {
  try {
    const usuarios = await userModel.getAllUsuarios();
    res.json(usuarios);
  } catch (err: any) {
    console.error("Erro ao listar usuários:", err);
    res.status(500).json({ error: err.message || "Erro interno do servidor." });
  }
}

(req: Request, res: Response) {
  const { id } = req.params;
  const { nome, telefone, id_empresa } = req.body;

  try {
    const affectedRows = await userModel.updateUsuario(Number(id), { nome, telefone, id_empresa });
    if (affectedRows === 0) {
      return res.status(404).json({ error: "Usuário não encontrado." });
    }
    res.json({ message: "Usuário atualizado com sucesso." });
  } catch (err: any) {
    console.error("Erro ao atualizar usuário:", err);
    res.status(500).json({ error: err.message || "Erro interno do servidor." });
  }
}

export async function deletarUsuarioController(req: Request, res: Response) {
  const { id } = req.params;

  try {
    const result = await userModel.deleteUsuario(Number(id));
    if (result.affectedRows === 0) {
      return res.status(404).json({ error: "Usuário não encontrado." });
    }
    res.json({ message: "Usuário deletado com sucesso." });
  } catch (err: any) {
    console.error("Erro ao deletar usuário:", err);
    res.status(500).json({ error: err.message || "Erro interno do servidor." });
  }
}

export async function buscarUsuarioPorIdController(req: Request, res: Response) {
  const { id } = req.params;

  try {
    const usuario = await userModel.getUsuarioById(Number(id));
    if (!usuario) {
      return res.status(404).json({ error: "Usuário não encontrado." });
    }
    res.json(usuario);
  } catch (err: any) {
    console.error("Erro ao buscar usuário por ID:", err);
    res.status(500).json({ error: err.message || "Erro interno do servidor." });
  }
}
