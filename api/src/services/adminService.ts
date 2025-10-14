import adminModel from "../models/adminModel";
import schemas from "./validations/schemas";
import type { Admin } from '../models/adminModel';

const getAllAdmin = async () => {
  const allAdmin = await adminModel.getAllAdmins();
  return {
    type: null,
    message: allAdmin,
    status: 200
  }
};

const getAdminById = async (id: number) => {
  const adminById = await adminModel.getAdminById(id);
  if(!adminById) {
    return {
      type: 'error',
      message: 'Admin não foi encontrado',
      status: 404
    }
  }
  return {
    type: null,
    message: adminById,
    status: 200
  }
}

const createAdmin = async (userAdmin: Admin) => {
  const validateAdmin = schemas.adminSchema.validate(userAdmin);
  if(validateAdmin.error) {
    return {
    type: 'error',
    message: validateAdmin.error.details[0].message,
    status: 422
    }
  }
  const insertIdAdmin = await adminModel.createAdmin(userAdmin);
  return {
    type: null,
    message: `Administrador inserido com sucesso no id: ${ insertIdAdmin }`,
    status: 200
  }
}

const updateAdmin = async (id: number, userAdmin: Partial<Admin>) => {
  const userAdminExists = await adminModel.getAdminById(id);
  if(!userAdminExists) {
    return {
      type: 'error',
      message: 'Administrador não foi encontrado',
      status: 404
    }
  }

  const validateAdmin = schemas.adminSchema.validate(userAdmin);
  if(validateAdmin.error) {
    return {
    type: 'error',
    message: validateAdmin.error.details[0].message,
    status: 422
    }
  }

  await adminModel.updateAdmin(id, userAdmin);
  return {
    type: null,
    message: 'Administrador atualizado com sucesso',
    status: 201
  } 
}

const deleteAdmin = async (id: number) => {
  const userAdminExists = await adminModel.getAdminById(id);
  if(!userAdminExists) {
    return {
      type: 'error',
      message: 'Administrador não foi encontrado',
      status: 404
    }
  }

  await adminModel.deleteAdmin(id);
  return {
    type: null,
    message: "Administrador deletado com sucesso",
    status: 200
  }
}

export default {
  getAllAdmin,
  getAdminById,
  createAdmin,
  updateAdmin,
  deleteAdmin
}