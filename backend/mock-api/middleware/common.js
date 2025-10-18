// Common middleware for the Mock API
const jwt = require('jsonwebtoken');

// Add realistic delay to simulate network latency
const delayMiddleware = (req, res, next) => {
  const delay = Math.floor(Math.random() * 400) + 100; // 100-500ms delay
  setTimeout(next, delay);
};

// JWT Secret for mock authentication
const JWT_SECRET = 'mock-secret-key-for-testing';

// Mock JWT authentication middleware
const authenticateToken = (req, res, next) => {
  const authHeader = req.headers['authorization'];
  const token = authHeader && authHeader.split(' ')[1];

  if (!token) {
    return res.status(401).json({
      error: 'Access token required',
      code: 'TOKEN_MISSING'
    });
  }

  try {
    const decoded = jwt.verify(token, JWT_SECRET);
    req.user = decoded;
    next();
  } catch (err) {
    return res.status(403).json({
      error: 'Invalid or expired token',
      code: 'TOKEN_INVALID'
    });
  }
};

// Error handler middleware
const errorHandler = (err, req, res, next) => {
  console.error('Error:', err);

  res.status(500).json({
    error: 'Internal server error',
    message: err.message,
    timestamp: new Date().toISOString(),
    path: req.path
  });
};

// Random error simulation for testing edge cases
const randomErrorMiddleware = (req, res, next) => {
  // 5% chance of random error for testing
  if (Math.random() < 0.05) {
    const errors = [
      { status: 500, message: 'Database connection timeout' },
      { status: 503, message: 'Service temporarily unavailable' },
      { status: 429, message: 'Rate limit exceeded' }
    ];

    const randomError = errors[Math.floor(Math.random() * errors.length)];
    return res.status(randomError.status).json({
      error: randomError.message,
      code: 'RANDOM_ERROR_SIMULATION',
      timestamp: new Date().toISOString()
    });
  }
  next();
};

module.exports = {
  delayMiddleware,
  authenticateToken,
  errorHandler,
  randomErrorMiddleware,
  JWT_SECRET
};