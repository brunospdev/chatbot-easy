import { Request, Response } from "express";
import adminService from "../services/adminService";

const getAllAdmin = async (req: Request, res: Response) => {
  const { message, status } = await adminService.getAllAdmin();
  return res.status(status).json(message)
}

const getAdminById = async (req: Request, res: Response) => {
  const { id } = req.params;
  const nId = Number(id)
  const { type, message, status } = await adminService.getAdminById(nId);
  if (type) {
  return res.status(status).json({ message })
  }
  return res.status(status).json(message)
}

const createAdmin = async (req: Request, res: Response) => {
  const userAdmin = req.body;
  const { type, message, status } = await adminService.createAdmin(userAdmin);
  if (type) {
    return res.status(status).json({ message });
  }
  return res.status(201).json({ message });
}

const updateAdmin = async (req: Request, res: Response) => {
  const { id } = req.params;
  const nId = Number(id)
  const userAdmin = req.body;
  const { type, message, status } = await adminService.updateAdmin(nId, userAdmin);
  if (type) {
    return res.status(status).json({ message });
  }
  return res.status(201).json({ message });
}

const deleteAdmin = async (req: Request, res: Response) => {
  const { id } = req.params;
  const nId = Number(id)
  const { type, message, status } = await adminService.deleteAdmin(nId);
  if (type) {
    return res.status(status).json({ message });
  }
  return res.status(201).json({ message });
}

export default {
  getAllAdmin,
  getAdminById,
  createAdmin,
  updateAdmin,
  deleteAdmin
}