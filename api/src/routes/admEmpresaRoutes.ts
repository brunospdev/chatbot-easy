import { Router } from "express";
import { criarAdmEmpresaController, listarAdmEmpresasController } from "../controllers/admEmpresaController";

const router = Router();

router.post("/criarempresa", criarAdmEmpresaController);
router.get("/listarempresa", listarAdmEmpresasController);

export default router;
