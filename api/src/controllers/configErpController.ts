import { Request, Response } from "express";
import configErpModel from "../models/configErpModel";

export async function criarConfiguracaoErpController(req: Request, res: Response) {
  const { id_empresa, url_api, token_api, status } = req.body;

  if (!id_empresa || !url_api || !token_api || !status) {
    return res.status(400).json({ error: "Todos os campos são obrigatórios." });
  }

  try {
    const id_config = await configErpModel.createConfiguracaoErp({id_empresa, url_api, token_api, status});
    res.status(201).json({ id_config, id_empresa, url_api, token_api, status });
  } catch (err: any) {
    res.status(500).json({ error: err.message });
  }
}

export async function listarConfiguracoesErpController(_req: Request, res: Response) {
  try {
    const configs = await configErpModel.getAllConfiguracoesErp();
    res.json(configs);
  } catch (err: any) {
    res.status(500).json({ error: err.message });
  }
}
