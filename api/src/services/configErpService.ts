import configErpModel from "../models/configErpModel";
import schemas from "./validations/schemas";
import { ConfiguracaoErp } from "../models/configErpModel";

const getAllConfig = async () => {
  const allConfig = await configErpModel.getAllConfiguracoesErp();
  return {
    type: null,
    message: allConfig,
    status:200
  }
}

const getConfigById = async (id: number) => {
  const configById = await configErpModel.getConfiguracaoErpById(id);
  if(!configById) {
    return {
      type: 'error',
      message: 'Config não foi encontrado',
      status: 404
    }
  }
  return {
    type: null,
    message: configById,
    status: 200
  }
}

const createConfig = async (configErp: ConfiguracaoErp) => {
  const validateConfig = schemas.configErpSchema.validate(configErp);
  if(validateConfig.error) {
    return {
      type: 'error',
      message: validateConfig.error.details[0].message,
      status: 422
    }
  }
  const insertIdConfig = await configErpModel.createConfiguracaoErp(configErp);
  return {
    type: null,
    message: `Configuração criada com sucesso no id: ${ insertIdConfig }`,
    status: 201
  }
}

const updateConfig = async (id: number, configErp: Partial<ConfiguracaoErp>) => {
  const configErpExists = await configErpModel.getConfiguracaoErpById(id);
  if(!configErpExists) {
    return {
      type: 'error',
      message: 'Configuração não foi encontrada',
      status: 404
    }
  }
  const validateConfig = schemas.configErpSchema.validate(configErp);
    if(validateConfig.error) {
      return {
        type: 'error',
        message: validateConfig.error.details[0].message,
        status: 422
      }
    }

    await configErpModel.updateConfiguracaoErp(id, configErp);
    return {
      type: null,
      message: 'Configuração atualizada com sucesso',
      status: 201
    }
}

const deleteConfig = async (id: number) => {
  const configErpExists = await configErpModel.getConfiguracaoErpById(id);
  if(!configErpExists) {
    return {
      type: 'error',
      message: 'Configuração não foi encontrada',
      status: 404
    }
  }

  await configErpModel.deleteConfiguracaoErp(id);
  return {
    type: null,
    message: "Configuração deletada com sucesso",
    status: 200
  }
}

export default {
  getAllConfig,
  getConfigById,
  createConfig,
  updateConfig,
  deleteConfig
}