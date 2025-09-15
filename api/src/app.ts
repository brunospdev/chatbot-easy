import express from "express"
import messageRoutes from "./routes/messageRoutes"

const app = express()
app.use(express.json())

app.use("/wpp", messageRoutes)

export default app