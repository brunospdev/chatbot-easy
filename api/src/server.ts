import express, { Request, Response } from 'express';
import { PrismaClient } from '@prisma/client';
import { connectToWhatsApp } from './utils/baileys';

const prisma = new PrismaClient();
const app = express();
const PORT = process.env.PORT || 3001;

app.use(express.json());

app.post('/usuarios', async (req: Request, res: Response) => {
  try {
    const { nome, celular, id_empresa } = req.body;

    if (!nome || !id_empresa) {
      return res.status(400).json({ error: "Nome e token da empresa são obrigatórios." });
    }

    const novoUsuario = await prisma.usuario.create({
      data: {
        nome,
        celular,
        empresa: {
          connect: {
            id_empresa: id_empresa,
          },
        },
      },
    });
    res.status(201).json(novoUsuario);
  }
  catch (error) {
    console.error(error);
    res.status(500).json({ error: "Não foi possível criar o usuário." });
  }
});


app.get('/usuarios', async (req: Request, res: Response) => {
  try {

    const usuarios = await prisma.usuario.findMany();
    res.json(usuarios);
  }
  catch (error) {
    res.status(500).json({ error: 'Erro ao buscar dados.' });
  }
});

app.listen(PORT, async () => {
  console.log(`🚀 API rodando em http://localhost:${PORT}`);
  await connectToWhatsApp();
});