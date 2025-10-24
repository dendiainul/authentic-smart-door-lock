const express = require('express');
const jwt = require('jsonwebtoken');
const { authenticateToken, randomErrorMiddleware } = require('../middleware/common');
const { loadSampleData } = require('../data/sampleData');
const { v4: uuidv4 } = require('uuid');

const router = express.Router();

// Apply authentication and random error simulation
router.use(authenticateToken);
router.use(randomErrorMiddleware);

// GET /api/users - Get all users
router.get('/', async (req, res) => {
  try {
    const data = loadSampleData();
    
    // Get users from the new sample data structure
    const users = data.users || [];
    
    res.json({
      success: true,
      data: users,
      message: 'Users retrieved successfully'
    });

  } catch (error) {
    console.error('Get users error:', error);
    res.status(500).json({
      success: false,
      error: 'Failed to get users',
      message: error.message,
      code: 'USERS_ERROR'
    });
  }
});

// POST /api/users - Create new user
router.post('/', async (req, res) => {
  try {
    const { name, email, role = 'user', phone } = req.body;

    if (!name || !email) {
      return res.status(400).json({
        success: false,
        error: 'Name and email are required',
        code: 'MISSING_REQUIRED_FIELDS'
      });
    }

    // Mock user creation with new sample data structure
    const newUser = {
      id: Math.floor(Math.random() * 1000) + 100, // Generate random ID
      google_id: uuidv4(),
      name,
      email,
      role,
      face_registered: false,
      created_at: new Date().toISOString(),
      phone: phone || null,
      avatar: `https://randomuser.me/api/portraits/${Math.random() > 0.5 ? 'men' : 'women'}/${Math.floor(Math.random() * 100)}.jpg`
    };

    res.status(201).json({
      success: true,
      data: newUser,
      message: 'User created successfully'
    });

  } catch (error) {
    console.error('Create user error:', error);
    res.status(500).json({
      success: false,
      error: 'Failed to create user',
      message: error.message,
      code: 'CREATE_USER_ERROR'
    });
  }
});

// GET /api/users/profile - Get current user profile
router.get('/profile', async (req, res) => {
  try {
    // Get user ID from JWT token
    const authHeader = req.headers.authorization;
    if (!authHeader || !authHeader.startsWith('Bearer ')) {
      return res.status(401).json({
        success: false,
        error: 'Access token required',
        code: 'TOKEN_MISSING'
      });
    }

    const token = authHeader.substring(7);
    let userId;
    try {
      const decoded = jwt.verify(token, 'mock-secret-key-for-testing');
      userId = decoded.userId;
    } catch (error) {
      return res.status(401).json({
        success: false,
        error: 'Invalid or expired token',
        code: 'TOKEN_INVALID'
      });
    }

    const data = loadSampleData();
    const user = data.users.find(u => u.id === parseInt(userId));
    
    if (!user) {
      return res.status(404).json({
        success: false,
        error: 'User not found',
        code: 'USER_NOT_FOUND'
      });
    }
    
    res.json({
      success: true,
      data: user,
      message: 'User profile retrieved successfully'
    });

  } catch (error) {
    console.error('Get user profile error:', error);
    res.status(500).json({
      success: false,
      error: 'Failed to get user profile',
      message: error.message,
      code: 'USER_PROFILE_ERROR'
    });
  }
});

// DELETE /api/users/:id - Delete user
router.delete('/:id', async (req, res) => {
  try {
    const { id } = req.params;

    if (!id) {
      return res.status(400).json({
        success: false,
        error: 'User ID is required',
        code: 'MISSING_USER_ID'
      });
    }

    // Mock user deletion
    res.json({
      success: true,
      message: 'User deleted successfully'
    });

  } catch (error) {
    console.error('Delete user error:', error);
    res.status(500).json({
      success: false,
      error: 'Failed to delete user',
      message: error.message,
      code: 'DELETE_USER_ERROR'
    });
  }
});

module.exports = router;