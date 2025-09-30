import { Router } from "express";
import { criarUsuarioController, listarUsuariosController } from "../controllers/userController";

const router = Router();

router.post("/cuser", criarUsuarioController);
router.get("/luser", listarUsuariosController);

export default router;
