import { Request, Response } from "express";
import empresaService from "../services/empresaService";

const getAllEmpresa = async (req: Request, res: Response) => {
  const { message, status } = await empresaService.getAllEmpresa();
  return res.status(status).json(message)
}

const getEmpresaById = async (req: Request, res: Response) => {
  const { id } = req.params;
  const nId = Number(id)
  const { type, message, status } = await empresaService.getEmpresaById(nId);
  if (type) {
  return res.status(status).json({ message })
  }
  return res.status(status).json(message)
}

const createEmpresa = async (req: Request, res: Response) => {
  const empresa = req.body;
  const { type, message, status } = await empresaService.createEmpresa(empresa);
  if (type) {
    return res.status(status).json({ message });
  }
  return res.status(201).json({ message });
}

const updateEmpresa = async (req: Request, res: Response) => {
  const { id } = req.params;
  const nId = Number(id)
  const empresa = req.body;
  const { type, message, status } = await empresaService.updateEmpresa(nId, empresa);
  if (type) {
    return res.status(status).json({ message });
  }
  return res.status(201).json({ message });
}

const deleteEmpresa = async  (req: Request, res: Response) => {
const { id } = req.params;
  const nId = Number(id)
  const { type, message, status } = await empresaService.deleteEmpresa(nId);
  if (type) {
    return res.status(status).json({ message });
  }
  return res.status(201).json({ message });
}

export default {
  getAllEmpresa,
  getEmpresaById,
  createEmpresa,
  updateEmpresa,
  deleteEmpresa
}