import solicitacoesModel from "../models/solicitacoesModel";
import userModel from "../models/userModel";
import schemas from "./validations/schemas";
import { Solicitacao } from "../models/solicitacoesModel";

const getAllSolicitacoes = async () => {
    const allSolicitacoes = await solicitacoesModel.getAllSolicitacoes();
    return{
        type: null,
        message: allSolicitacoes,
        status: 200
    }    
}
const getsolicitacaoById = async (id:number) => {
    const solicitacaoById =  await solicitacoesModel.getSolicitacaoById(id);
    if(!solicitacaoById){
        return{
            type: 'error',
            message: 'Solicitação não encontrada',
            status: 404
        }
    }
    return{
        type: null,
        message: solicitacaoById,
        status: 200
    }
    
}
const getSolicitacoesByUsuario = async (id:number) => {
    const usuarioSolicitacaoExist = await userModel.getUsuarioById(id);
    if(!usuarioSolicitacaoExist){
       return{
        type:'error',
        message:'Usuário não foi encontrado',
        status : 404
       }
    }
    const solicitacaoexist = await solicitacoesModel.getSolicitacoesByUsuario(id);
    return{
        type: null,
        message: solicitacaoexist,
        status: 200
    }
}

const createSolicitacao = async (solicitacao: Solicitacao ) => {
    const validateSolicitacao = schemas.solicitacaoSchema.validate(solicitacao);
    if(validateSolicitacao.error){
        return{
            type: 'error',
            message: validateSolicitacao.error.details[0].message,
            status: 422
        }
    }
    const insertIdSolicitacao = await solicitacoesModel.createSolicitacao(solicitacao);
    return{
        type: null,
        message: `Solicitação criada com sucesso no id ${insertIdSolicitacao}` ,
        status: 201
    }
}
const deleteSolicitacao = async (id:number) => {
    const solicitacaoExists = await solicitacoesModel.getSolicitacaoById(id);
    if(!solicitacaoExists){
        return{
            type: 'error',
            message: 'Solicitação não encontrada',
            status: 404
        }
    }
    await solicitacoesModel.deleteSolicitacao(id);
    return{
        type: null,
        message: 'Solicitação deletada com sucesso',
        status: 200
    }
    
}

const updateSolicitacao = async (id: number, solicitacao: Partial<Solicitacao>) => {
    const solicitacaoExists = await solicitacoesModel.getSolicitacaoById(id);
    if(!solicitacaoExists){
        return{
            type: 'error',
            message: 'Solicitação não encontrada',
            status: 404
        }
    }
    await solicitacoesModel.updateSolicitacao(id, solicitacao);
    return{
        type: null,
        message: "Solicitação atualizada com sucesso",
        status: 200
    }

}

export default {
    getAllSolicitacoes,
    getsolicitacaoById,
    getSolicitacoesByUsuario,
    createSolicitacao,
    deleteSolicitacao,
    updateSolicitacao
}