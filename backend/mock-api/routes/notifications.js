const express = require('express');
const { authenticateToken, randomErrorMiddleware } = require('../middleware/common');
const { loadSampleData } = require('../data/sampleData');

const router = express.Router();

// Apply authentication and random error simulation
router.use(authenticateToken);
router.use(randomErrorMiddleware);

// GET /api/notifications - Get notifications
router.get('/', async (req, res) => {
  try {
    const { 
      limit = 20, 
      offset = 0, 
      read, 
      type,
      user_id 
    } = req.query;
    
    const data = loadSampleData();
    
    // Get notifications from the new sample data structure
    let notifications = data.notifications || [];
    
    // Apply filters
    if (read !== undefined) {
      const readBool = read === 'true';
      notifications = notifications.filter(notif => notif.read === readBool);
    }
    
    if (type) {
      notifications = notifications.filter(notif => notif.type === type);
    }
    
    if (user_id) {
      notifications = notifications.filter(notif => notif.user_id === parseInt(user_id));
    }
    
    // Apply pagination
    const totalNotifications = notifications.length;
    const paginatedNotifications = notifications.slice(parseInt(offset), parseInt(offset) + parseInt(limit));
    
    // Enrich with user information
    const users = data.users || [];
    
    const enrichedNotifications = paginatedNotifications.map(notification => {
      const user = notification.user_id ? users.find(u => u.id === notification.user_id) : null;
      
      return {
        ...notification,
        user: user ? {
          id: user.id,
          name: user.name,
          email: user.email,
          avatar: user.avatar
        } : null
      };
    });
    
    res.json({
      success: true,
      data: enrichedNotifications,
      pagination: {
        total: totalNotifications,
        limit: parseInt(limit),
        offset: parseInt(offset),
        has_more: parseInt(offset) + parseInt(limit) < totalNotifications
      },
      message: 'Notifications retrieved successfully'
    });

  } catch (error) {
    console.error('Get notifications error:', error);
    res.status(500).json({
      success: false,
      error: 'Failed to get notifications',
      message: error.message,
      code: 'NOTIFICATIONS_ERROR'
    });
  }
});

// POST /api/notifications/mark-read - Mark notifications as read
router.post('/mark-read', async (req, res) => {
  try {
    const { notification_ids } = req.body;

    if (!notification_ids || !Array.isArray(notification_ids)) {
      return res.status(400).json({
        success: false,
        error: 'Notification IDs array is required',
        code: 'MISSING_NOTIFICATION_IDS'
      });
    }

    // Mock mark as read response
    const result = {
      marked_count: notification_ids.length,
      notification_ids,
      timestamp: new Date().toISOString()
    };

    res.json({
      success: true,
      data: result,
      message: 'Notifications marked as read successfully'
    });

  } catch (error) {
    console.error('Mark notifications as read error:', error);
    res.status(500).json({
      success: false,
      error: 'Failed to mark notifications as read',
      message: error.message,
      code: 'MARK_READ_ERROR'
    });
  }
});

module.exports = router;