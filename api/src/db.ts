import mysql from "mysql2/promise";
const pool = mysql.createPool({
    host: "mainline.proxy.rlwy.net",
    user: "root",
    password: "PgwlUECDpwscECAxZmEWJDLTxwAQCorh",
    database: "railway",
    port: Number(39748),
    waitForConnections: true,
    connectionLimit: 10,
    queueLimit: 0,
})
export default pool;

/*
const pool = mysql.createPool({
    host: process.env.MYSQLHOST || "database",
    user: process.env.MYSQLUSER || "root",
    password: process.env.MYSQLPASSWORD || "root",
    database: process.env.MYSQLDATABASE || "auth_chatbot",
    port: Number(process.env.MYSQLPORT) || 3306,
    waitForConnections: true,
    connectionLimit: 10,
    queueLimit: 0,
})
*/