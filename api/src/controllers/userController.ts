import { Request, Response } from "express";
import userService from "../services/userService";

const criarUsuario = async (req: Request, res: Response) => {
    const user = req.body;
    const { type, message, status } = await userService.createUsuario(user);
    if (type) {
        return res.status(status).json({ message });
    }
    return res.status(201).json({ message });
}

const listarUsuarios = async (req: Request, res: Response) => {
    const { type, message, status } = await userService.getAllUser();
    if (type) {
        return res.status(status).json({ message });
    }
    return res.status(200).json({ message });
}

const updateUsuario = async (req: Request, res: Response) => {
    const { id } = req.params;
    const nId = Number(id)
    const usuario = req.body;
    const { type, message, status } = await userService.updateUsuario(nId, usuario);
    if (type) {
        return res.status(status).json({ message });
    }
    return res.status(201).json({ message });
}

const deleteUsuario = async (req: Request, res: Response) => {
    const { id } = req.params;
    const nId = Number(id)
    const { type, message, status } = await userService.deleteUsuario(nId);
    if (type) {
        return res.status(status).json({ message });
    }
    return res.status(201).json({ message });
}

const getUsuarioById = async (req: Request, res: Response) => {
    const { id } = req.params;
    const nId = Number(id)
    const { type, message, status } = await userService.getUserById(nId);
    if (type) {
        return res.status(status).json({ message })
    }
    return res.status(status).json(message)
}

const getUsuariosByEmp = async (req: Request, res: Response) => {
  try {
    const { id_empresa } = req.params;
    const nIdEmpresa = Number(id_empresa);
    const { type, message, status } = await userService.getUsuariosByEmp(nIdEmpresa);
if (type) {
  return res.status(status).json({ message });
}
return res.status(status).json({ data: message });

  } catch (error) {
    console.error(error);
    return res.status(500).json({ message: 'Erro interno no servidor' });
  }
};


export default {
    criarUsuario,
    listarUsuarios,
    updateUsuario,
    deleteUsuario,
    getUsuarioById,
    getUsuariosByEmp
}