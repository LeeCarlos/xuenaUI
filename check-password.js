const mysql = require('mysql2/promise');

async function main() {
  const connection = await mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: '123456',
    database: 'example_db'
  });

  const [rows] = await connection.execute('SELECT username, password FROM sp_user WHERE username = ?', ['admin']);
  console.log('Admin user:', rows);

  await connection.end();
}

main().catch(console.error);