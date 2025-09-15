import { getSock } from "../utils/baileys";

export async function enviarMensagem(numero: string, texto: string) {
  const sock = getSock();
  if (!sock) throw new Error("Bot ainda não está conectado");

  await sock.sendMessage(`${numero}@s.whatsapp.net`, { text: texto });
}

let mensagens: any[] = [];

export function addMensagem(msg: any) {
  mensagens.push(msg);
}

export function getMensagens() {
  return mensagens;
}

export function clearMensagens() {
  mensagens = [];
}