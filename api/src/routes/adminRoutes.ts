import { Router } from "express";
import adminController from "../controllers/adminController";
const router = Router();

router.get("/ladm", adminController.getAllAdmin);
router.get("/ladm/:id", adminController.getAdminById);
router.post("/cadm", adminController.createAdmin);
router.put("/uadm/:id", adminController.updateAdmin);
router.delete("/dadm/:id", adminController.deleteAdmin);

export default router;
