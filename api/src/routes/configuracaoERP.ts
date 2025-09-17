import { Router, Request, Response } from "express";
import pool from "../db";
const router = Router();

router.post("/", async (req: Request, res: Response) => {
    const { id_empresa, url_api, token_api, status } = req.body;
    if (!id_empresa || !url_api || !token_api || !status) {
        return res.status(400).json({ error: "Todos os campos são obrigatórios." });
    }
    try {
        const [result] = await pool.query<any>(
            "INSERT INTO ConfiguracaoERP (id_empresa, url_api, token_api, status) VALUES (?, ?, ?, ?)",
            [id_empresa, url_api, token_api, status]
        );
        res.status(201).json({ id_config: result.insertId, ...req.body });
    } catch (err: any) {
        res.status(500).json({ error: err.message });
    }
});

router.get("/", async (_req: Request, res: Response) => {
    try {
        const [rows] = await pool.query("SELECT * FROM ConfiguracaoERP");
        res.json(rows);
    } catch (err: any) {
        res.status(500).json({ error: err.message });
    }
});
export default router;