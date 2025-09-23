import app from "./app"
import { connectToWhatsApp } from './utils/baileys';


const PORT = process.env.PORT || 3001;

app.listen(PORT, async () => {
  console.log(`API rodando em http://localhost:${PORT}`);
  await connectToWhatsApp();
});
