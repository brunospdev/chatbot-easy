import express from "express"
import messageRoutes from "./routes/messageRoutes"
import empresaRoutes from "./routes/empresaRoutes";
import admEmpresaRoutes from "./routes/admEmpresaRoutes";
import adminRoutes from "./routes/adminRoutes";
import solicitacoesRoutes from "./routes/solicitacoesRoutes";
import configErpRoutes from "./routes/configErpRoutes";
import userRoutes from "./routes/userRoutes";

const app = express()
app.use(express.json())

app.use("/wpp", messageRoutes)
app.use("/adm-empresa", admEmpresaRoutes);
app.use("/empresa", empresaRoutes);
app.use("/admin", adminRoutes)
app.use("/solicitacoes", solicitacoesRoutes)
app.use("/config-erp", configErpRoutes)
app.use("/users", userRoutes)

export default app