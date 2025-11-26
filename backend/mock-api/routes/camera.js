const express = require('express');
const { authenticateToken, randomErrorMiddleware } = require('../middleware/common');
const { loadSampleData } = require('../data/sampleData');

const router = express.Router();

// Apply authentication and random error simulation
router.use(authenticateToken);
router.use(randomErrorMiddleware);

// GET /api/camera/stream - Get camera stream URL
router.get('/stream', async (req, res) => {
  try {
    const { door_id } = req.query;

    if (!door_id) {
      return res.status(400).json({
        success: false,
        error: 'Door ID is required',
        code: 'MISSING_DOOR_ID'
      });
    }

    const resolvedDoorId = parseInt(door_id, 10);

    // Ambil konfigurasi IP/HOST dan PORT WebRTC dari environment
    const webrtcHost = process.env.CAMERA_WEBRTC_IP || process.env.CAMERA_WEBRTC_HOST || '172.27.3.163';
    const webrtcPort = process.env.CAMERA_WEBRTC_PORT || '8889';

    // WebRTC URL: http://ip:port/door_id
    const webrtcUrl = `http://${webrtcHost}:${webrtcPort}/${resolvedDoorId}`;

    // Mock camera stream response
    const streamData = {
      door_id: resolvedDoorId,
      webrtc_url: webrtcUrl,
      status: 'active',
      resolution: '1920x1080',
      fps: 30,
      timestamp: new Date().toISOString()
    };

    res.json({
      success: true,
      data: streamData,
      message: 'Camera stream retrieved successfully'
    });

  } catch (error) {
    console.error('Get camera stream error:', error);
    res.status(500).json({
      success: false,
      error: 'Failed to get camera stream',
      message: error.message,
      code: 'CAMERA_STREAM_ERROR'
    });
  }
});

// POST /api/camera/capture - Capture photo
router.post('/capture', async (req, res) => {
  try {
    const { door_id, trigger_type = 'manual' } = req.body;

    if (!door_id) {
      return res.status(400).json({
        success: false,
        error: 'Door ID is required',
        code: 'MISSING_DOOR_ID'
      });
    }

    const resolvedDoorId = parseInt(door_id, 10);

    // Mock camera capture response
    const captureData = {
      id: `capture_${Date.now()}`,
      door_id: resolvedDoorId,
      trigger_type,
      image_url: `https://picsum.photos/640/480?random=${Date.now()}`,
      thumbnail_url: `https://picsum.photos/160/120?random=${Date.now()}`,
      timestamp: new Date().toISOString(),
      confidence_score: Math.random() * 0.3 + 0.7, // 70-100% confidence
      location: `Door ${resolvedDoorId} Camera`
    };

    res.json({
      success: true,
      data: captureData,
      message: 'Photo captured successfully'
    });

  } catch (error) {
    console.error('Camera capture error:', error);
    res.status(500).json({
      success: false,
      error: 'Failed to capture photo',
      message: error.message,
      code: 'CAMERA_CAPTURE_ERROR'
    });
  }
});

// GET /api/camera/capture/:id - Get camera capture by ID
router.get('/capture/:id', async (req, res) => {
  try {
    const { id } = req.params;

    if (!id) {
      return res.status(400).json({
        success: false,
        error: 'Capture ID is required',
        code: 'MISSING_CAPTURE_ID'
      });
    }

    const data = loadSampleData();
    
    // Get camera captures from sample data
    const captures = data.cameraCaptures || [];
    const capture = captures.find(c => c.id === parseInt(id));
    
    if (!capture) {
      return res.status(404).json({
        success: false,
        error: 'Camera capture not found',
        code: 'CAPTURE_NOT_FOUND'
      });
    }

    // Get door information for this capture
    const doors = data.doors || [];
    const door = doors.find(d => d.id === capture.door_id);
    
    // Return capture data with additional information
    const captureData = {
      ...capture,
      image_url: `https://picsum.photos/640/480?random=${capture.id}`,
      thumbnail_url: `https://picsum.photos/160/120?random=${capture.id}`,
      door: door ? {
        id: door.id,
        name: door.name,
        location: door.location,
        locked: door.locked,
        battery_level: door.battery_level,
        camera_active: door.camera_active
      } : null
    };

    res.json({
      success: true,
      data: captureData,
      message: 'Camera capture retrieved successfully'
    });

  } catch (error) {
    console.error('Get camera capture error:', error);
    res.status(500).json({
      success: false,
      error: 'Failed to get camera capture',
      message: error.message,
      code: 'GET_CAPTURE_ERROR'
    });
  }
});

module.exports = router;