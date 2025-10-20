import { Request, Response } from "express";
import solicitacoesService from "../services/solicitacoesService";

const createSolicitacao = async (req: Request, res: Response) => {
    const solicitacao = req.body;
    const { type, message, status } = await solicitacoesService.createSolicitacao(solicitacao);
    if (type) {
        return res.status(status).json({ message });
    }
    return res.status(201).json({ message });
}

const listarSolicitacoes = async (req: Request, res: Response) => {
    const { type, message, status } = await solicitacoesService.getAllSolicitacoes();
    if (type) {
        return res.status(status).json({ message });
    }
    return res.status(200).json({ message });
}

export default {
    createSolicitacao,
    listarSolicitacoes
}