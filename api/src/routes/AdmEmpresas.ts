import { Router, Request, Response } from "express";
import pool from "../db";

const router = Router();

router.post("/", async (req: Request, res: Response) => {
    const { nome, celular, id_empresa, } = req.body;
    if (!nome || !id_empresa) {
        return res.status(400).json({ error: "Nome e id_empresa são obrigatórios." });
    }

    try {
        const [result] = await pool.query<any>(
            "INSERT INTO AdmEmpresa (nome, celular, id_empresa) VALUES (?, ?, ?)",
            [nome, celular, id_empresa]
        );
        res.status(201).json({
            id_usuario: result.insertId,
            nome,
            celular,
            id_empresa,
        });
    } catch (err: any) {
        res.status(500).json({ error: err.message });
    }
});
router.get("/", async (_req: Request, res: Response) => {
    try {
        const [rows] = await pool.query("SELECT * FROM AdmEmpresa");
        res.json(rows);
    } catch (err: any) {
        res.status(500).json({ error: err.message });
    }
});

export default router;