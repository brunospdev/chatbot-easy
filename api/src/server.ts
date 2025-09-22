import express, {Application, Request, Response} from 'express';
import { connectToWhatsApp } from './utils/baileys';

const app = express();
app.use(express.json());
const PORT = process.env.PORT || 3001;

app.listen(PORT, async () => {
  console.log(`API rodando em http://localhost:${PORT}`);
  await connectToWhatsApp();
});
