const express = require('express');
const jwt = require('jsonwebtoken');
const { randomErrorMiddleware } = require('../middleware/common');
const { loadSampleData } = require('../data/sampleData');

const router = express.Router();

// Apply random error simulation
router.use(randomErrorMiddleware);

// POST /api/auth/google - Google authentication
router.post('/google', async (req, res) => {
  try {
    const { id_token, email, name, picture } = req.body;

    if (!id_token || !email) {
      return res.status(400).json({
        success: false,
        error: 'Google token and email are required',
        code: 'MISSING_CREDENTIALS'
      });
    }

    // Mock Google token validation - check if user exists in sample data
    const data = loadSampleData();
    let userData = data.users.find(user => user.email === email);
    let isNewUser = false;
    
    if (!userData) {
      // Create new user if not found
      userData = {
        id: data.users.length + 1,
        google_id: `google_${Date.now()}`,
        email,
        name: name || 'Google User',
        role: 'user',
        face_registered: false,
        created_at: new Date().toISOString(),
        phone: null,
        avatar: picture || 'https://via.placeholder.com/150'
      };
      isNewUser = true;
    } else {
      // Update existing user with new data
      userData = {
        ...userData,
        name: name || userData.name,
        avatar: picture || userData.avatar
      };
    }

    // Check if user has door access, if not give them access to some doors
    const userDoorAccess = data.userDoor || [];
    const userAccess = userDoorAccess.filter(access => access.user_id === userData.id);
    
    if (userAccess.length === 0) {
      // Give user access to some random doors (1-3 doors)
      const doors = data.doors || [];
      const doorCount = Math.floor(Math.random() * 3) + 1; // 1-3 doors
      const selectedDoors = [];
      
      for (let i = 0; i < doorCount; i++) {
        const randomDoor = doors[Math.floor(Math.random() * doors.length)];
        if (!selectedDoors.includes(randomDoor.id)) {
          selectedDoors.push(randomDoor.id);
        }
      }
      
      // Add door access for this user (this is just for response, not persisted)
      userData.accessible_doors = selectedDoors.map(doorId => {
        const door = doors.find(d => d.id === doorId);
        return {
          id: door.id,
          name: door.name,
          location: door.location,
          locked: door.locked,
          battery_level: door.battery_level,
          camera_active: door.camera_active
        };
      });
    }

    // Generate JWT token
    const jwtToken = jwt.sign(
      { 
        userId: userData.id, 
        email: userData.email,
        name: userData.name 
      },
      'mock-secret-key-for-testing',
      { expiresIn: '24h' }
    );

    res.json({
      success: true,
      data: {
        user: userData,
        tokens: {
          access_token: jwtToken,
          refresh_token: jwtToken + '_refresh',
          token_type: 'Bearer',
          expires_in: 86400 // 24 hours in seconds
        }
      },
      message: 'Authentication successful'
    });

  } catch (error) {
    console.error('Google auth error:', error);
    res.status(500).json({
      success: false,
      error: 'Authentication failed',
      message: error.message,
      code: 'AUTH_ERROR'
    });
  }
});

// POST /api/auth/logout - Logout user
router.post('/logout', async (req, res) => {
  try {
    const authHeader = req.headers['authorization'];
    const token = authHeader && authHeader.split(' ')[1];

    if (!token) {
      return res.status(401).json({
        success: false,
        error: 'No token provided',
        code: 'NO_TOKEN'
      });
    }

    // In a real app, you would blacklist the token
    // For mock purposes, we just return success
    res.json({
      success: true,
      message: 'Logout successful'
    });

  } catch (error) {
    console.error('Logout error:', error);
    res.status(500).json({
      success: false,
      error: 'Logout failed',
      message: error.message,
      code: 'LOGOUT_ERROR'
    });
  }
});

module.exports = router;