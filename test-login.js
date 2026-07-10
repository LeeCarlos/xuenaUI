const http = require('http');

const data = JSON.stringify({
  username: 'test_user_' + Date.now(),
  password: '123456',
  realName: '测试用户',
  department: '测试部',
  email: 'test@example.com',
  isEnabled: 1
});

const options = {
  hostname: 'localhost',
  port: 8080,
  path: '/api/pool',
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'Content-Length': data.length
  }
};

const req = http.request(options, (res) => {
  let body = '';
  res.on('data', (chunk) => {
    body += chunk;
  });
  res.on('end', () => {
    console.log('Create user status:', res.statusCode);
    console.log('Create user body:', body);
    
    const loginData = JSON.stringify({
      username: JSON.parse(data).username,
      password: '123456'
    });
    
    const loginOptions = {
      hostname: 'localhost',
      port: 8080,
      path: '/api/auth/login',
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Content-Length': loginData.length
      }
    };
    
    const loginReq = http.request(loginOptions, (loginRes) => {
      let loginBody = '';
      loginRes.on('data', (chunk) => {
        loginBody += chunk;
      });
      loginRes.on('end', () => {
        console.log('Login status:', loginRes.statusCode);
        console.log('Login body:', loginBody);
      });
    });
    
    loginReq.on('error', (e) => {
      console.error('Login error:', e.message);
    });
    
    loginReq.write(loginData);
    loginReq.end();
  });
});

req.on('error', (e) => {
  console.error('Create user error:', e.message);
});

req.write(data);
req.end();