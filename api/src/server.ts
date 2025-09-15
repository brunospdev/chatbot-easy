import app from "./app"
import { connectToWhatsApp } from "./utils/baileys"

const PORT = 3000

app.listen(PORT, async () => {
  console.log(`API rodando em http://localhost:${PORT}`)
  await connectToWhatsApp()
})