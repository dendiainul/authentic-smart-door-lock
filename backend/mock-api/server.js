const express = require('express');
const cors = require('cors');
const http = require('http');
const socketIo = require('socket.io');

// Import routes
const authRoutes = require('./routes/auth');
const doorRoutes = require('./routes/door');
const userRoutes = require('./routes/users');
const cameraRoutes = require('./routes/camera');
const historyRoutes = require('./routes/history');
const notificationRoutes = require('./routes/notifications');
const analyticsRoutes = require('./routes/analytics');

// Import middleware
const { delayMiddleware, errorHandler } = require('./middleware/common');

// Initialize Express app
const app = express();
const server = http.createServer(app);
const io = socketIo(server, {
  cors: {
    origin: "*",
    methods: ["GET", "POST"]
  }
});

// Port configuration
const PORT = process.env.PORT || 3000;

// Middleware
app.use(cors());
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

// Add realistic delay to all requests
app.use(delayMiddleware);

// Logging middleware
app.use((req, res, next) => {
  console.log(`${new Date().toISOString()} - ${req.method} ${req.path}`);
  console.log('Headers:', req.headers);
  if (req.body && Object.keys(req.body).length > 0) {
    console.log('Body:', req.body);
  }
  next();
});

// Routes
app.use('/api/auth', authRoutes);
app.use('/api/door', doorRoutes);
app.use('/api/users', userRoutes);
app.use('/api/camera', cameraRoutes);
app.use('/api/history', historyRoutes);
app.use('/api/notifications', notificationRoutes);
app.use('/api/analytics', analyticsRoutes);

// Health check endpoint
app.get('/api/health', (req, res) => {
  res.json({
    status: 'OK',
    timestamp: new Date().toISOString(),
    service: 'Smart Door Lock Mock API',
    version: '1.0.0'
  });
});

// 404 handler - must be the last route
app.use((req, res) => {
  res.status(404).json({
    error: 'Endpoint not found',
    path: req.originalUrl,
    method: req.method,
    timestamp: new Date().toISOString()
  });
});

// Error handling middleware
app.use(errorHandler);

// Socket.IO for real-time updates
io.on('connection', (socket) => {
  console.log('Client connected:', socket.id);

  // Send initial door status
  socket.emit('door-status', {
    locked: true,
    battery_level: Math.floor(Math.random() * 100),
    last_update: new Date().toISOString(),
    camera_active: true
  });

  socket.on('disconnect', () => {
    console.log('Client disconnected:', socket.id);
  });
});

// Simulate real-time door status updates
setInterval(() => {
  io.emit('door-status', {
    locked: Math.random() > 0.5,
    battery_level: Math.floor(Math.random() * 100),
    last_update: new Date().toISOString(),
    camera_active: Math.random() > 0.3
  });
}, 30000); // Every 30 seconds

// Start server
server.listen(PORT, '0.0.0.0', () => {
  console.log(`=� Smart Door Lock Mock API running on port ${PORT}`);
  console.log(`=� Socket.IO enabled for real-time updates`);
  console.log(`< API Base URL: http://localhost:${PORT}/api`);
  console.log(`=� Health Check: http://localhost:${PORT}/api/health`);
});

module.exports = { app, io };