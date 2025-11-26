const express = require('express');
const { authenticateToken, randomErrorMiddleware } = require('../middleware/common');
const { loadSampleData } = require('../data/sampleData');

const router = express.Router();

// Apply authentication and random error simulation
router.use(authenticateToken);
router.use(randomErrorMiddleware);

// GET /api/analytics/dashboard - Get analytics data for dashboard
router.get('/dashboard', async (req, res) => {
  try {
    const { door_id, start_date, end_date } = req.query;
    
    const data = loadSampleData();
    
    // Get user's accessible doors from the authenticated user
    const userId = req.user.userId; // From JWT token
    const userDoorAccess = data.userDoor || [];
    let userAccessibleDoorIds = userDoorAccess
      .filter(access => access.user_id === userId)
      .map(access => access.door_id);
    
    // If user doesn't have door access in userDoor table, use all doors (for testing)
    if (userAccessibleDoorIds.length === 0) {
      console.log(`User ${userId} not found in userDoor access, using all doors for testing`);
      userAccessibleDoorIds = data.doors.map(door => door.id);
    } else {
      console.log(`User ${userId} found in userDoor access with doors: ${userAccessibleDoorIds.join(', ')}`);
    }
    
    console.log(`User ${userId} has access to doors:`, userAccessibleDoorIds);
    
    // Get access logs and doors data - filter by user's accessible doors
    let accessLogs = data.accessLogs || [];
    let doors = data.doors || [];
    
    console.log(`Total access logs before filtering: ${accessLogs.length}`);
    console.log(`User accessible door IDs (type check):`, userAccessibleDoorIds.map(id => `${id} (${typeof id})`));
    console.log(`Sample door IDs from access logs:`, accessLogs.slice(0, 5).map(log => `${log.door_id} (${typeof log.door_id})`));
    
    // Filter access logs to only include doors the user has access to
    // Ensure type consistency by converting both to numbers
    const userAccessibleDoorIdsNumbers = userAccessibleDoorIds.map(id => parseInt(id));
    accessLogs = accessLogs.filter(log => userAccessibleDoorIdsNumbers.includes(parseInt(log.door_id)));
    
    console.log(`Total access logs after filtering: ${accessLogs.length}`);
    console.log(`Sample access log actions:`, accessLogs.slice(0, 5).map(log => ({ id: log.id, action: log.action, door_id: log.door_id })));
    console.log(`Filtering verification: userAccessibleDoorIdsNumbers = [${userAccessibleDoorIdsNumbers.join(', ')}]`);
    console.log(`First few access log door_ids: [${accessLogs.slice(0, 10).map(log => log.door_id).join(', ')}]`);
    
    // Filter doors to only include doors the user has access to
    doors = doors.filter(door => userAccessibleDoorIds.includes(door.id));
    
    // Filter by specific door_id if provided
    if (door_id && userAccessibleDoorIds.includes(parseInt(door_id))) {
      accessLogs = accessLogs.filter(log => log.door_id === parseInt(door_id));
      doors = doors.filter(door => door.id === parseInt(door_id));
    }
    
    // Filter by date range if provided
    if (start_date) {
      accessLogs = accessLogs.filter(log => new Date(log.timestamp) >= new Date(start_date));
    }
    
    if (end_date) {
      accessLogs = accessLogs.filter(log => new Date(log.timestamp) <= new Date(end_date));
    }
    
    // Calculate analytics metrics based on access logs
    const totalAccess = accessLogs.length; // Total semua access logs dari pintu yang user punya akses
    const deniedAccess = accessLogs.filter(log => !log.success).length;
    const lockedDoors = accessLogs.filter(log => log.action === 'tutup').length; // Action "tutup" untuk pintu dikunci
    const openedDoors = accessLogs.filter(log => log.action === 'buka').length; // Action "buka" untuk pintu dibuka
    
    console.log(`Analytics calculation: totalAccess=${totalAccess}, deniedAccess=${deniedAccess}, lockedDoors=${lockedDoors}, openedDoors=${openedDoors}`);
    console.log(`Access logs breakdown: buka=${accessLogs.filter(log => log.action === 'buka').length}, tutup=${accessLogs.filter(log => log.action === 'tutup').length}`);
    console.log(`Verification: totalAccess should equal buka + tutup = ${openedDoors + lockedDoors}`);
    
    // Calculate access activity by hour (for chart data)
    const hourlyActivity = {};
    accessLogs.forEach(log => {
      const hour = new Date(log.timestamp).getHours();
      hourlyActivity[hour] = (hourlyActivity[hour] || 0) + 1;
    });
    
    // Generate chart data for last 24 hours
    const chartData = [];
    for (let i = 0; i < 24; i++) {
      chartData.push({
        hour: i,
        count: hourlyActivity[i] || 0
      });
    }
    
    // Calculate active hours (top 3 hours with most activity)
    const sortedHours = Object.entries(hourlyActivity)
      .sort(([,a], [,b]) => b - a)
      .slice(0, 3);
    
    const activeHours = sortedHours.map(([hour, count], index) => ({
      timeRange: `${String(hour).padStart(2, '0')}:00 - ${String(parseInt(hour) + 2).padStart(2, '0')}:00`,
      count: count,
      progress: index === 0 ? 1.0 : count / sortedHours[0][1]
    }));
    
    // Calculate percentage changes (mock data for now)
    const totalAccessChange = totalAccess > 0 ? Math.floor(Math.random() * 20) + 1 : 0;
    const deniedAccessChange = deniedAccess > 0 ? Math.floor(Math.random() * 10) - 5 : 0;
    const lockedDoorsChange = Math.floor(Math.random() * 10) + 1;
    const openedDoorsChange = Math.floor(Math.random() * 15) + 1;
    
    const analyticsData = {
      metrics: {
        totalAccess: {
          value: totalAccess,
          change: `+${totalAccessChange}%`,
          changeType: 'positive'
        },
        deniedAccess: {
          value: deniedAccess,
          change: `${deniedAccessChange >= 0 ? '+' : ''}${deniedAccessChange}%`,
          changeType: deniedAccessChange >= 0 ? 'positive' : 'negative'
        },
        lockedDoors: {
          value: lockedDoors,
          change: `+${lockedDoorsChange}%`,
          changeType: 'positive'
        },
        openedDoors: {
          value: openedDoors,
          change: `+${openedDoorsChange}%`,
          changeType: 'positive'
        }
      },
      chartData: chartData,
      activeHours: activeHours,
      availableDoors: doors.map(door => ({
        id: door.id,
        name: door.name,
        location: door.location
      })),
      accessLogs: accessLogs // Add access logs for chart data
    };
    
    res.json({
      success: true,
      data: analyticsData,
      message: 'Analytics data retrieved successfully'
    });
    
  } catch (error) {
    console.error('Error getting analytics data:', error);
    res.status(500).json({
      success: false,
      message: 'Failed to retrieve analytics data',
      error: error.message
    });
  }
});

module.exports = router;
