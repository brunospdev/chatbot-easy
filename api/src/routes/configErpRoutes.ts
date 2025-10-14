import { Router } from "express";
import { criarConfiguracaoErpController, listarConfiguracoesErpController } from "../controllers/configErpController";

const router = Router();

router.post("/cconfigerp", criarConfiguracaoErpController);
router.get("/lconfigerp", listarConfiguracoesErpController);

export default router;
