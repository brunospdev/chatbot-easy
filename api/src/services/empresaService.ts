import empresaModel from "../models/empresaModel";
import schemas from "./validations/schemas";
import { Empresa } from "../models/empresaModel";


const getAllEmpresa = async () => {
    const allEmpresa = await empresaModel.getAllEmpresas();
    return{
        type: null,
        message: allEmpresa,
        status: 200
    }    
}
const getEmpresaById = async (id:number) => {
    const EmpresaById =  await empresaModel.getEmpresaById(id);
    if(!EmpresaById){
        return{
            type: 'error',
            message: 'Empresa não encontrada',
            status: 404
        }
    }
    return{
        type: null,
        message: EmpresaById,
        status: 200
    }
    
}
const getEmpresaByNome = async (nome:string) => {
    const empresaByNome = await empresaModel.getEmpresaByNome(nome);
    if(!empresaByNome){
        return{
            type: 'error',
            message: "Empresa não encontrada",
            status: 404
        }
    }
    return{
        type: null,
        message: empresaByNome,
        status: 200
    }
    
}
const createEmpresa = async (Empresa:Empresa) => {
    const validateEmpresa = schemas.empresaSchema.validate(Empresa);
    if(validateEmpresa.error){
        return{
            type: 'error',
            message: validateEmpresa.error.details[0].message,
            status: 422
        }
    }
    const insertIdEmpresa = await empresaModel.createEmpresa(Empresa);
    return{
        type: null,
        message: `Empresa criada com sucesso no id ${insertIdEmpresa}` ,
        status: 201
    }
}
const deleteEmpresa = async (id: number) => {
    const empresaExists = await empresaModel.getEmpresaById(id);
    if(!empresaExists){
        return{
            type: 'error',
            message: 'Empresa não encontrada',
            status: 404
        }
    }
    await empresaModel.deleteEmpresa(id);
    return{
        type: null,
        message: 'Empresa deletada com sucesso',
        status: 200
    }
    
}

const updateEmpresa = async (id: number, empresa: Partial<Empresa>) => {
  const empresaExists = await empresaModel.getEmpresaById(id);
  if(!empresaExists) {
    return {
      type: 'error',
      message: 'Empresa não foi encontrada',
      status: 404
    }
  }
  const validateEmpresa = schemas.empresaSchema.validate(empresa);
    if(validateEmpresa.error) {
      return {
        type: 'error',
        message: validateEmpresa.error.details[0].message,
        status: 422
      }
    }

    await empresaModel.updateEmpresa(id, empresa);
    return {
      type: null,
      message: 'Empresa atualizada com sucesso',
      status: 200
    }
}

export default {
    getAllEmpresa,
    getEmpresaById,
    getEmpresaByNome,
    createEmpresa,
    deleteEmpresa,
    updateEmpresa
}