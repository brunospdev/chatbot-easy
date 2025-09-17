import { Router, Request, Response } from "express";
import pool from "../db";
import bcrypt from "bcrypt";

const router = Router();

router.post("/", async (req: Request, res: Response) => {
    const { id_plataforma, nome, email, senha } = req.body;
    if (!id_plataforma || !nome || !email || !senha) {
        return res.status(400).json({ error: "Todos os campos são obrigatórios." });
    }

    try {
        const salt = await bcrypt.genSalt(12); // USANDO 12 PQ SEGUNDO A DOC EH O MAIOR NUMERO DE HASH ONDE NAO CAI EFICIENCIA 2/3 HASHES/SEC
        const senhaHash = await bcrypt.hash(senha, salt);

        const [result] = await pool.query<any>(
            "INSERT INTO Administrador (id_plataforma, nome, email, senha) VALUES (?, ?, ?, ?)",
            [id_plataforma, nome, email, senhaHash]
        );
        res.status(201).json({
            id_admin: result.insertId,
            id_plataforma,
            nome,
            email,
        });
    }
    catch (err: any) {
        if (err.code === 'ER_DUP_ENTRY') {
            return res.status(409).json({ error: "Este email já está em uso." });
        }
        res.status(500).json({ error: err.message });
    }
});

router.get("/", async (_req: Request, res: Response) => {
    try {
        const [rows] = await pool.query("SELECT id_admin, id_plataforma, nome, email FROM Administrador");
        res.json(rows);
    } catch (err: any) {
        res.status(500).json({ error: err.message });
    }
});

export default router;