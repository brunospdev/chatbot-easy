import { Request, Response } from "express";
import configErpService from "../services/configErpService";

const getAllConfig = async (req: Request, res: Response) => {
  const { message, status } = await configErpService.getAllConfig();
  return res.status(status).json(message)
}

const getConfigById = async (req: Request, res: Response) => {
  const { id } = req.params;
  const nId = Number(id)
  const { type, message, status } = await configErpService.getConfigById(nId);
  if (type) {
  return res.status(status).json({ message })
  }
  return res.status(status).json(message)
}

const createConfig = async (req: Request, res: Response) => {
  const configErp = req.body;
  const { type, message, status } = await configErpService.createConfig(configErp);
  if (type) {
    return res.status(status).json({ message });
  }
  return res.status(201).json({ message });
}

const updateConfig = async (req: Request, res: Response) => {
  const { id } = req.params;
  const nId = Number(id)
  const userAdmin = req.body;
  const { type, message, status } = await configErpService.updateConfig(nId, userAdmin);
  if (type) {
    return res.status(status).json({ message });
  }
  return res.status(201).json({ message });
}

const deleteConfig = async (req: Request, res: Response) => {
  const { id } = req.params;
  const nId = Number(id)
  const { type, message, status } = await configErpService.deleteConfig(nId);
  if (type) {
    return res.status(status).json({ message });
  }
  return res.status(201).json({ message });
}

export default {
  getAllConfig,
  getConfigById,
  createConfig,
  updateConfig,
  deleteConfig
}