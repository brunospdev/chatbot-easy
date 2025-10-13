import { Router } from "express";
import { 
  criarUsuarioController, 
  listarUsuariosController, 
  atualizarUsuarioController, 
  deletarUsuarioController, 
  buscarUsuarioPorIdController 
} from "../controllers/userController";

const router = Router();

router.post("/cuser", criarUsuarioController);
router.get("/luser", listarUsuariosController);
router.put("/uuser/:id", atualizarUsuarioController);
router.delete("/duser/:id", deletarUsuarioController);
router.get("/guser/:id", buscarUsuarioPorIdController);


export default router;