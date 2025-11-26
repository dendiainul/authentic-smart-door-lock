const express = require('express');
const cors = require('cors');

const app = express();
const PORT = 3001; // Different port to avoid conflict

// Middleware
app.use(cors());
app.use(express.json());

// Log all requests
app.use((req, res, next) => {
  const timestamp = new Date().toISOString();
  console.log(`\n🔍 [${timestamp}] ${req.method} ${req.originalUrl}`);
  console.log('📋 Headers:', JSON.stringify(req.headers, null, 2));
  
  if (req.body && Object.keys(req.body).length > 0) {
    console.log('📦 Body:', JSON.stringify(req.body, null, 2));
  }
  
  if (req.query && Object.keys(req.query).length > 0) {
    console.log('🔗 Query:', JSON.stringify(req.query, null, 2));
  }
  
  console.log('─'.repeat(80));
  next();
});

// Catch all routes and forward to main server
app.use('*', (req, res) => {
  console.log(`🚀 Forwarding ${req.method} ${req.originalUrl} to main server...`);
  
  // Just respond with a simple message for now
  res.json({
    message: 'Request logged successfully',
    method: req.method,
    url: req.originalUrl,
    timestamp: new Date().toISOString()
  });
});

app.listen(PORT, () => {
  console.log(`🔍 Request Monitor running on port ${PORT}`);
  console.log(`📱 Point your Android app to: http://localhost:${PORT}/api`);
  console.log(`🔄 All requests will be logged here`);
  console.log('─'.repeat(80));
});

module.exports = app;
