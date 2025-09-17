import { Router, Request, Response } from "express";
import pool from "../db";
const router = Router();

router.post("/", async (req: Request, res: Response) => {
    const { nome_empresa, cnpj, telefone, id_plataforma } = req.body;
    if (!nome_empresa || !cnpj || !telefone || !id_plataforma) {
        return res.status(400).json({ error: "Todos os campos são obrigatórios." });
    }

    try {
        const [result] = await pool.query<any>(
            "INSERT INTO Empresa (nome_empresa, cnpj, telefone, id_plataforma) VALUES (?, ?, ?, ?)",
            [nome_empresa, cnpj, telefone, id_plataforma]
        );
        res.status(201).json({
            id_empresa: result.insertId,
            nome_empresa,
            cnpj,
            telefone,
            id_plataforma,
        });
    } catch (err: any) {
        res.status(500).json({ error: err.message });
    }
});

router.get("/", async (_req: Request, res: Response) => {
    try {
        const [rows] = await pool.query("SELECT * FROM Empresa");
        res.json(rows);
    } catch (err: any) {
        res.status(500).json({ error: err.message });
    }
});

export default router;