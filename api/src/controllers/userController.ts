import { Request, Response } from "express";
import { criarUsuario, listarUsuarios, atualizarUsuario, deletarUsuario, buscarUsuarioPorId } from "../services/userService";

export async function criarUsuarioController(req: Request, res: Response) {
  const { nome, celular, id_empresa } = req.body;

  if (!nome || !id_empresa) {
    return res.status(400).json({ error: "Nome e id_empresa são obrigatórios." });
  }

  try {
    const id_usuario = await criarUsuario(nome, celular, id_empresa);
    res.status(201).json({ id_usuario, nome, celular, id_empresa });
  } catch (err: any) {
    res.status(500).json({ error: err.message });
  }
}

export async function listarUsuariosController(_req: Request, res: Response) {
  try {
    const usuarios = await listarUsuarios();
    res.json(usuarios);
  } catch (err: any) {
    res.status(500).json({ error: err.message });
  }
}

export async function atualizarUsuarioController(req: Request, res: Response) {
  const { id } = req.params;
  const { nome, celular, id_empresa } = req.body;

  try {
    const result = await atualizarUsuario(Number(id), nome, celular, id_empresa);
    if (result.affectedRows === 0) {
      return res.status(404).json({ error: "Usuário não encontrado." });
    }
    res.json({ message: "Usuário atualizado com sucesso." });
  } catch (err: any) {
    res.status(500).json({ error: err.message });
  }
}

export async function deletarUsuarioController(req: Request, res: Response) {
  const { id } = req.params;

  try {
    const result = await deletarUsuario(Number(id));
    if (result.affectedRows === 0) {
      return res.status(404).json({ error: "Usuário não encontrado." });
    }
    res.json({ message: "Usuário deletado com sucesso." });
  } catch (err: any) {
    res.status(500).json({ error: err.message });
  }
}

export async function buscarUsuarioPorIdController(req: Request, res: Response) {
  const { id } = req.params;

  try {
    const usuario = await buscarUsuarioPorId(Number(id));
    if (!usuario) {
      return res.status(404).json({ error: "Usuário não encontrado." });
    }
    res.json(usuario);
  } catch (err: any) {
    res.status(500).json({ error: err.message });
  }
}