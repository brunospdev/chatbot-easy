import { Router } from "express";
import { criarSolicitacaoController, listarSolicitacoesController } from "../controllers/solicitacoesController";

const router = Router();

router.post("/criarsolic", criarSolicitacaoController);
router.get("/listarsolic", listarSolicitacoesController);

export default router;
