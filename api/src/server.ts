import express, {Application, Request, Response} from 'express';
import { connectToWhatsApp } from './utils/baileys';
import empresasRouter from "./routes/empresas";
import usuariosRouter from "./routes/users";
import plataformasRouter from "./routes/plataformas";
import administradoresRouter from "./routes/Admins";
import solicitacoesRouter from "./routes/solicitacoes";
import configuracaoErpRouter from "./routes/configuracaoERP";



const app = express();
app.use(express.json());
const PORT = process.env.PORT || 3001;



app.use("/api/empresas", empresasRouter);
app.use("/api/usuarios", usuariosRouter);
app.use("/api/plataformas", plataformasRouter);
app.use("/api/administradores", administradoresRouter);
app.use("/api/solicitacoes", solicitacoesRouter);
app.use("/api/configuracoes-erp", configuracaoErpRouter);

app.listen(PORT, async () => {
  console.log(`API rodando em http://localhost:${PORT}`);
  await connectToWhatsApp();
});
