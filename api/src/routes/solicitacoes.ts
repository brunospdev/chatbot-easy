import { Router, Request, Response } from "express";
import pool from "../db";
const router = Router();

router.post("/", async (req: Request, res: Response) => {
    const { id_usuario, tipo_solicitacao, status } = req.body;
    if (!id_usuario || !tipo_solicitacao || !status) {
        return res.status(400).json({ error: "Todos os campos são obrigatórios." });
    }
    try {
        const [result] = await pool.query<any>(
            "INSERT INTO Solicitacao (id_usuario, tipo_solicitacao, status, data_solicitacao) VALUES (?, ?, ?, NOW())",
            [id_usuario, tipo_solicitacao, status]
        );
        res.status(201).json({ id_solicitacao: result.insertId, ...req.body });
    } catch (err: any) {
        res.status(500).json({ error: err.message });
    }
});

router.get("/", async (_req: Request, res: Response) => {
    try {
        const [rows] = await pool.query("SELECT * FROM Solicitacao");
        res.json(rows);
    } catch (err: any) {
        res.status(500).json({ error: err.message });
    }
});
export default router;