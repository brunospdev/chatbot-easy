import mysql from "mysql2/promise";
const pool = mysql.createPool({
    host: process.env.MYSQL_HOST || "database",
    user: process.env.MYSQL_USER || "root",
    password: process.env.MYSQL_PASSWORD || "root",
    database: process.env.MYSQL_DATABASE || "auth_chatbot",
    port: Number(process.env.MYSQL_PORT) || 3306,
    waitForConnections: true,
    connectionLimit: 10,
    queueLimit: 0,
})
export default pool;
