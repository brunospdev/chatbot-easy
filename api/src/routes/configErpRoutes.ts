import { Router } from "express";
import configErpController from "../controllers/configErpController";

const router = Router();

router.get("/lconfigerp", configErpController.getAllConfig);
router.get("/lconfigerp/:id", configErpController.getConfigById);
router.post("/cconfigerp", configErpController.createConfig);
router.put("/uconfigerp/:id", configErpController.updateConfig);
router.delete("/dconfigerp/:id", configErpController.deleteConfig);

export default router;
