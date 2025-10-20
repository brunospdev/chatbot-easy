import { Router } from "express";
import solicitacoesController from "../controllers/solicitacoesController";

const router = Router();

router.post("/criarsolic", solicitacoesController.createSolicitacao);
router.get("/listarsolic", solicitacoesController.listarSolicitacoes);

export default router;
