import { Router } from "express";
import { criarAdministradorController, listarAdministradoresController } from "../controllers/adminController";

const router = Router();

router.post("/criaradm", criarAdministradorController);
router.get("/listaradm", listarAdministradoresController);

export default router;
