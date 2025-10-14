import { Request, Response } from "express";
import solicitacoesModel from "../models/solicitacoesModel";

export async function criarSolicitacaoController(req: Request, res: Response) {
  const { id_usuario, tipo_solicitacao, status } = req.body;

  if (!id_usuario || !tipo_solicitacao || !status) {
    return res.status(400).json({ error: "Todos os campos são obrigatórios." });
  }

  try {
    const id_solicitacao = await solicitacoesModel.createSolicitacao({id_usuario, tipo_solicitacao, status});
    res.status(201).json({ id_solicitacao, id_usuario, tipo_solicitacao, status });
  } catch (err: any) {
    res.status(500).json({ error: err.message });
  }
}

export async function listarSolicitacoesController(_req: Request, res: Response) {
  try {
    const solicitacoes = await solicitacoesModel.getAllSolicitacoes();
    res.json(solicitacoes);
  } catch (err: any) {
    res.status(500).json({ error: err.message });
  }
}
