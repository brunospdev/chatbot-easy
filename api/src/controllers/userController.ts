import { Request, Response } from "express";
import { criarUsuario, listarUsuarios } from "../services/userService";

export async function criarUsuarioController(req: Request, res: Response) {
  const { nome, celular, id_empresa } = req.body;

  if (!nome || !id_empresa) {
    return res.status(400).json({ error: "Nome e id_empresa são obrigatórios." });
  }

  try {
    const id_usuario = await criarUsuario(nome, celular || null, id_empresa);
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
