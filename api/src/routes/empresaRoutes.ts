import { Router } from "express";
import empresaController from "../controllers/empresaController";

const router = Router();

router.get("/listarEmpresa", empresaController.getAllEmpresa);
router.post("/criarEmpresa", empresaController.createEmpresa);

export default router;
