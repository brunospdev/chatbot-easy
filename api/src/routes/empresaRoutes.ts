import { Router } from "express";
import { criarEmpresaController, listarEmpresasController } from "../controllers/empresaController";

const router = Router();

router.post("/criarEmpresa", criarEmpresaController);
router.get("/listarEmpresa", listarEmpresasController);

export default router;
