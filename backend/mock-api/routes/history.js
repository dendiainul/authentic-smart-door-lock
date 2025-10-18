const express = require('express');
const { authenticateToken, randomErrorMiddleware } = require('../middleware/common');
const { loadSampleData } = require('../data/sampleData');

const router = express.Router();

// Apply authentication and random error simulation
router.use(authenticateToken);
router.use(randomErrorMiddleware);

// GET /api/history/access - Get access history
router.get('/access', async (req, res) => {
  try {
    const { 
      limit = 50, 
      offset = 0, 
      door_id, 
      user_id, 
      start_date, 
      end_date,
      success 
    } = req.query;
    
    const data = loadSampleData();
    
    // Get access logs from the new sample data structure
    let accessLogs = data.accessLogs || [];
    
    // Apply filters
    if (door_id) {
      accessLogs = accessLogs.filter(log => log.door_id === parseInt(door_id));
    }
    
    if (user_id) {
      accessLogs = accessLogs.filter(log => log.user_id === parseInt(user_id));
    }
    
    if (success !== undefined) {
      const successBool = success === 'true';
      accessLogs = accessLogs.filter(log => log.success === successBool);
    }
    
    if (start_date) {
      accessLogs = accessLogs.filter(log => new Date(log.timestamp) >= new Date(start_date));
    }
    
    if (end_date) {
      accessLogs = accessLogs.filter(log => new Date(log.timestamp) <= new Date(end_date));
    }
    
    // Apply pagination
    const totalLogs = accessLogs.length;
    const paginatedLogs = accessLogs.slice(parseInt(offset), parseInt(offset) + parseInt(limit));
    
    // Enrich with user and door information
    const users = data.users || [];
    const doors = data.doors || [];
    const cameraCaptures = data.cameraCaptures || [];
    
    const enrichedLogs = paginatedLogs.map(log => {
      const user = users.find(u => u.id === log.user_id);
      const door = doors.find(d => d.id === log.door_id);
      const cameraCapture = log.camera_capture_id ? cameraCaptures.find(c => c.id === log.camera_capture_id) : null;
      
      return {
        ...log,
        user: user ? {
          id: user.id,
          name: user.name,
          email: user.email,
          avatar: user.avatar
        } : null,
        door: door ? {
          id: door.id,
          name: door.name,
          location: door.location
        } : null,
        camera_capture: cameraCapture ? {
          id: cameraCapture.id,
          filename: cameraCapture.filename,
          event_type: cameraCapture.event_type,
          timestamp: cameraCapture.timestamp
        } : null
      };
    });
    
    res.json({
      success: true,
      data: enrichedLogs,
      pagination: {
        total: totalLogs,
        limit: parseInt(limit),
        offset: parseInt(offset),
        has_more: parseInt(offset) + parseInt(limit) < totalLogs
      },
      message: 'Access history retrieved successfully'
    });

  } catch (error) {
    console.error('Get access history error:', error);
    res.status(500).json({
      success: false,
      error: 'Failed to get access history',
      message: error.message,
      code: 'ACCESS_HISTORY_ERROR'
    });
  }
});

// GET /api/history/photos - Get photo history
router.get('/photos', async (req, res) => {
  try {
    const { 
      limit = 20, 
      offset = 0, 
      door_id, 
      start_date, 
      end_date,
      event_type 
    } = req.query;
    
    const data = loadSampleData();
    
    // Get camera captures from the new sample data structure
    let captures = data.cameraCaptures || [];
    
    // Apply filters
    if (door_id) {
      captures = captures.filter(capture => capture.door_id === parseInt(door_id));
    }
    
    if (event_type) {
      captures = captures.filter(capture => capture.event_type === event_type);
    }
    
    if (start_date) {
      captures = captures.filter(capture => new Date(capture.timestamp) >= new Date(start_date));
    }
    
    if (end_date) {
      captures = captures.filter(capture => new Date(capture.timestamp) <= new Date(end_date));
    }
    
    // Apply pagination
    const totalCaptures = captures.length;
    const paginatedCaptures = captures.slice(parseInt(offset), parseInt(offset) + parseInt(limit));
    
    // Enrich with door information
    const doors = data.doors || [];
    
    const enrichedCaptures = paginatedCaptures.map(capture => {
      const door = doors.find(d => d.id === capture.door_id);
      
      return {
        ...capture,
        door: door ? {
          id: door.id,
          name: door.name,
          location: door.location
        } : null
      };
    });
    
    res.json({
      success: true,
      data: enrichedCaptures,
      pagination: {
        total: totalCaptures,
        limit: parseInt(limit),
        offset: parseInt(offset),
        has_more: parseInt(offset) + parseInt(limit) < totalCaptures
      },
      message: 'Photo history retrieved successfully'
    });

  } catch (error) {
    console.error('Get photo history error:', error);
    res.status(500).json({
      success: false,
      error: 'Failed to get photo history',
      message: error.message,
      code: 'PHOTO_HISTORY_ERROR'
    });
  }
});

module.exports = router;