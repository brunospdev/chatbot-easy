import { Request, Response } from "express";
import empresaModel from "../models/empresaModel";

export async function criarEmpresaController(req: Request, res: Response) {
  const { nome, celular, id_empresa } = req.body;

  if (!nome || !id_empresa) {
    return res.status(400).json({ error: "Nome e id_empresa são obrigatórios." });
  }

  try {
    const id_usuario = await empresaModel.createEmpresa({nome, celular, id_empresa});
    res.status(201).json({ id_usuario, nome, celular, id_empresa });
  } catch (err: any) {
    res.status(500).json({ error: err.message });
  }
}

export async function listarEmpresasController(_req: Request, res: Response) {
  try {
    const empresas = await empresaModel.getAllEmpresas();
    res.json(empresas);
  } catch (err: any) {
    res.status(500).json({ error: err.message });
  }
}
