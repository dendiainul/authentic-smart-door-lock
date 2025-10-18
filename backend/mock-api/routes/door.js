const express = require('express');
const jwt = require('jsonwebtoken');
const { authenticateToken, randomErrorMiddleware } = require('../middleware/common');
const { loadSampleData } = require('../data/sampleData');

const router = express.Router();

// Apply authentication and random error simulation
router.use(authenticateToken);
router.use(randomErrorMiddleware);

// GET /api/door/status - Get door status for current user
router.get('/status', async (req, res) => {
  try {
    const data = loadSampleData();
    
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

    // Get user-door access relationships for this specific user
    const userDoorAccess = data.userDoor || [];
    const userAccessibleDoors = userDoorAccess.filter(access => access.user_id === parseInt(userId));
    
    let accessibleDoors = [];
    
    if (userAccessibleDoors.length > 0) {
      // User has existing door access in userDoor data
      const doors = data.doors || [];
      accessibleDoors = userAccessibleDoors.map(userAccess => {
        const door = doors.find(d => d.id === userAccess.door_id);
        if (!door) return null;
        
        return {
          ...door,
          access_granted_at: userAccess.created_at
        };
      }).filter(door => door !== null);
    } else {
      // User doesn't have door access in userDoor data, give them access to some random doors
      const doors = data.doors || [];
      const doorCount = Math.floor(Math.random() * 3) + 1; // 1-3 doors
      const selectedDoors = [];
      
      for (let i = 0; i < doorCount; i++) {
        const randomDoor = doors[Math.floor(Math.random() * doors.length)];
        if (!selectedDoors.includes(randomDoor.id)) {
          selectedDoors.push(randomDoor.id);
        }
      }
      
      accessibleDoors = selectedDoors.map(doorId => {
        const door = doors.find(d => d.id === doorId);
        return {
          ...door,
          access_granted_at: new Date().toISOString()
        };
      });
    }
    
    if (accessibleDoors.length === 0) {
      return res.json({
        success: true,
        data: [],
        message: 'No doors accessible for this user'
      });
    }
    
    res.json({
      success: true,
      data: accessibleDoors,
      message: 'User accessible doors retrieved successfully'
    });

  } catch (error) {
    console.error('Get door status error:', error);
    res.status(500).json({
      success: false,
      error: 'Failed to get door status',
      message: error.message,
      code: 'DOOR_STATUS_ERROR'
    });
  }
});

// POST /api/door/control - Control door (lock/unlock)
router.post('/control', async (req, res) => {
  try {
    const { door_id, action } = req.body;

    if (!door_id || !action) {
      return res.status(400).json({
        success: false,
        error: 'Door ID and action are required',
        code: 'MISSING_PARAMETERS'
      });
    }

    const validActions = ['buka', 'tutup']; // Using Indonesian terms from sample data
    if (!validActions.includes(action)) {
      return res.status(400).json({
        success: false,
        error: 'Invalid action. Must be buka or tutup',
        code: 'INVALID_ACTION'
      });
    }

    // Mock door control response
    const result = {
      door_id,
      action,
      success: true,
      timestamp: new Date().toISOString(),
      message: `Door ${action === 'buka' ? 'opened' : 'closed'} successfully`
    };

    res.json({
      success: true,
      data: result,
      message: `Door ${action === 'buka' ? 'opened' : 'closed'} successfully`
    });

  } catch (error) {
    console.error('Door control error:', error);
    res.status(500).json({
      success: false,
      error: 'Failed to control door',
      message: error.message,
      code: 'DOOR_CONTROL_ERROR'
    });
  }
});

module.exports = router;