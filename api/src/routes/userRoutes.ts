import { Router } from "express";
import userController from "../controllers/userController";
const router = Router();

router.post("/cuser", userController.criarUsuario);
router.get("/luser", userController.listarUsuarios);
router.put("/uuser/:id", userController.updateUsuario);
router.delete("/duser/:id", userController.deleteUsuario);
router.get("/guser/:id", userController.getUsuarioById);


export default router;