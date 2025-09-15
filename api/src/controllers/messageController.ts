import { Request, Response } from "express"
import { getMensagens, enviarMensagem } from "../services/messageService"

export async function sendMessage(req: Request, res: Response) {
  try {
    const { numero, texto } = req.body
    if (!numero || !texto) return res.status(400).json({ error: "Informe número e texto" })

    await enviarMensagem(numero, texto)
    res.json({ sucesso: true })
  } catch (error) {
    console.error(error)
    res.status(500).json({ error: "Erro ao enviar mensagem" })
  }
}

export async function listMessage(req: Request, res: Response) {
  try {
    const mensagens = getMensagens();
    res.json(mensagens);
  } catch (error) {
    res.status(500).json({ error: "Erro ao buscar mensagens" });
  }
}
