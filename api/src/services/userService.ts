import userModel from "../models/userModel";
import schemas from "./validations/schemas";
import { Usuario } from "../models/userModel";


const getAllUser = async () => {
    const allUser = await userModel.getAllUsuarios();
    return{
        type: null,
        message: allUser,
        status: 200
    }    
}
const getUsuariosByEmp = async (id_empresa: number) => {
  const usuarios = await userModel.getUsuariosByEmp(id_empresa);
  if (!usuarios || usuarios.length === 0) {
    return {
      type: 'error',
      message: 'Nenhum usuário encontrado para essa empresa',
      status: 404,
    };
  }
  return {
    type: null,
    message: usuarios,
    status: 200,
  };
};

const getUserById = async (id:number) => {
    const userById =  await userModel.getUsuarioById(id);
    if(!userById){
        return{
            type: 'error',
            message: 'Usuário não encontrado',
            status: 404
        }
    }
    return{
        type: null,
        message: userById,
        status: 200
    }
    
}

const createUsuario = async (Usuario: Usuario ) => {
    const validateUsuario = schemas.usuarioSchema.validate(Usuario);
    if(validateUsuario.error){
        return{
            type: 'error',
            message: validateUsuario.error.details[0].message,
            status: 422
        }
    }
    const insertIdUsuario = await userModel.createUsuario(Usuario);
    return{
        type: null,
        message: `Usuario criao com sucesso no id ${insertIdUsuario}` ,
        status: 201
    }
}
const deleteUsuario = async (id:number) => {
    const usuarioExists = await userModel.getUsuarioById(id);
    if(!usuarioExists){
        return{
            type: 'error',
            message: 'Usuario não encontrado',
            status: 404
        }
    }
    await userModel.deleteUsuario(id);
    return{
        type: null,
        message: 'Usuario deletado com sucesso',
        status: 200
    }
    
}

const updateUsuario = async (id: number, usuario: Partial<Omit<Usuario, 'papel'>>) => {
    const usuarioExists = await userModel.getUsuarioById(id);
    if(!usuarioExists){
        return{
            type: 'error',
            message: 'Usuario não encontrado',
            status: 404
        }
    }
    await userModel.updateUsuario(id, usuario);
    return{
        type: null,
        message: "Usuario atualizado com sucesso",
        status: 200
    }

}

export default {
    getAllUser,
    getUserById,
    createUsuario,
    deleteUsuario,
    updateUsuario,
    getUsuariosByEmp
}