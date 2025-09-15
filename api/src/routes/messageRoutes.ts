import { Router } from "express"
import { listMessage, sendMessage } from "../controllers/messageController"

const router = Router()

router.get("/listar", listMessage);
router.post("/enviar", sendMessage)

export default router
