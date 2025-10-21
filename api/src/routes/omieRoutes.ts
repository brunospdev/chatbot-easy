import { Router } from "express";
import {
  gerarRelatorioFinanceiroGeralController
} from "../controllers/omieController";

const router = Router();

router.post("/relatorio-financeiro", gerarRelatorioFinanceiroGeralController);

export default router;