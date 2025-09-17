import { Router, Request, Response } from "express";
import pool from "../db";

const router = Router();

router.post("/", async (req: Request, res: Response) => {
    const { nome_plataforma, dominio } = req.body;
    if (!nome_plataforma || !dominio) {
        return res.status(400).json({ error: "Nome da plataforma e domínio são obrigatórios." });
    }

    try {
        const [result] = await pool.query<any>(
            "INSERT INTO Plataforma (nome_plataforma, dominio) VALUES (?, ?)",
            [nome_plataforma, dominio]
        );
        res.status(201).json({
            id_plataforma: result.insertId,
            nome_plataforma,
            dominio,
        });
    } catch (err: any) {
        res.status(500).json({ error: err.message });
    }
});

router.get("/", async (_req: Request, res: Response) => {
    try {
        const [rows] = await pool.query("SELECT * FROM Plataforma");
        res.json(rows);
    } catch (err: any) {
        res.status(500).json({ error: err.message });
    }
});

export default router;