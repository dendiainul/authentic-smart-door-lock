const express = require('express');
const jwt = require('jsonwebtoken');
const { authenticateToken, randomErrorMiddleware } = require('../middleware/common');
const { loadSampleData, updateDoorStatus } = require('../data/sampleData');

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
      const doorCount = Math.min(3, doors.length); // 1-3 doors, but not more than available
      const selectedDoors = [];
      
      // Create a copy of doors array to avoid modifying original
      const availableDoors = [...doors];
      
      for (let i = 0; i < doorCount && availableDoors.length > 0; i++) {
        const randomIndex = Math.floor(Math.random() * availableDoors.length);
        const selectedDoor = availableDoors.splice(randomIndex, 1)[0];
        selectedDoors.push(selectedDoor);
      }
      
      accessibleDoors = selectedDoors.map(door => ({
        ...door,
        access_granted_at: new Date().toISOString()
      }));
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
    console.log(`🚪 Door control request: door_id=${door_id}, action=${action}`);

    if (!door_id || !action) {
      return res.status(400).json({
        success: false,
        error: 'Door ID and action are required',
        code: 'MISSING_PARAMETERS'
      });
    }

    const validActions = ['buka', 'kunci', 'lock', 'unlock']; // Support both Indonesian and English terms
    if (!validActions.includes(action)) {
      return res.status(400).json({
        success: false,
        error: 'Invalid action. Must be buka, kunci, lock, or unlock',
        code: 'INVALID_ACTION'
      });
    }

    // Convert English actions to Indonesian for internal processing
    let internalAction = action;
    if (action === 'lock') internalAction = 'kunci';
    if (action === 'unlock') internalAction = 'buka';

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

    // Check if user has access to this door
    const data = loadSampleData();
    const userDoorAccess = data.userDoor || [];
    const hasAccess = userDoorAccess.some(access => 
      access.user_id === parseInt(userId) && access.door_id === parseInt(door_id)
    );

    // If no specific access defined, allow access (for demo purposes)
    if (userDoorAccess.length > 0 && !hasAccess) {
      return res.status(403).json({
        success: false,
        error: 'Access denied to this door',
        code: 'ACCESS_DENIED'
      });
    }

    // Check if door exists and is online (battery > 0)
    const door = data.doors.find(d => d.id === parseInt(door_id));
    if (!door) {
      return res.status(404).json({
        success: false,
        error: 'Door not found',
        code: 'DOOR_NOT_FOUND'
      });
    }

    if (door.battery_level === 0) {
      return res.status(400).json({
        success: false,
        error: 'Door is offline (battery depleted)',
        code: 'DOOR_OFFLINE'
      });
    }

    // Update door status in sample-data.json
    console.log(`📝 Updating door ${door_id} with action: ${internalAction} by user ${userId}`);
    const updateResult = updateDoorStatus(door_id, internalAction, userId);
    console.log(`📄 Update result:`, updateResult.success ? '✅ Success' : '❌ Failed', updateResult.error || '');
    
    if (!updateResult.success) {
      return res.status(500).json({
        success: false,
        error: updateResult.error,
        code: 'UPDATE_FAILED'
      });
    }

    // Prepare response
    const result = {
      door_id: parseInt(door_id),
      action: internalAction,
      success: true,
      timestamp: updateResult.accessLog.timestamp,
      message: `Door ${internalAction === 'buka' ? 'opened' : 'locked'} successfully`,
      door_status: {
        locked: updateResult.door.locked,
        last_update: updateResult.door.last_update
      }
    };

    res.json({
      success: true,
      data: result,
      message: `Door ${internalAction === 'buka' ? 'opened' : 'locked'} successfully`
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