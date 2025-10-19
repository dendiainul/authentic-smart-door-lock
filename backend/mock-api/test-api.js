const axios = require('axios');

// Base URL untuk mock API
const BASE_URL = 'http://localhost:3000/api';

// Test data
const testData = {
  googleAuth: {
    id_token: 'mock_google_token_123',
    email: 'test.user@gmail.com',
    name: 'Test User',
    picture: 'https://via.placeholder.com/150'
  },
  doorControl: {
    door_id: 1,
    action: 'buka'
  },
  newUser: {
    name: 'New Test User',
    email: 'newuser@test.com',
    role: 'user',
    phone: '+6281234567890'
  },
  cameraCapture: {
    door_id: 1,
    trigger_type: 'manual'
  },
  markRead: {
    notification_ids: [1, 2, 3]
  }
};

// Helper function untuk membuat request dengan auth token
const makeRequest = async (method, url, data = null, token = null) => {
  const config = {
    method,
    url: `${BASE_URL}${url}`,
    headers: {
      'Content-Type': 'application/json'
    }
  };

  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }

  if (data) {
    config.data = data;
  }

  try {
    const response = await axios(config);
    return { success: true, data: response.data, status: response.status };
  } catch (error) {
    return { 
      success: false, 
      error: error.response?.data || error.message, 
      status: error.response?.status || 500 
    };
  }
};

// Test functions
const testAuth = async () => {
  console.log('\n🔐 TESTING AUTHENTICATION ENDPOINTS');
  console.log('=' .repeat(50));

  // Test Google Auth
  console.log('\n1. POST /api/auth/google');
  const googleAuth = await makeRequest('POST', '/auth/google', testData.googleAuth);
  console.log('Status:', googleAuth.status);
  console.log('Response:', JSON.stringify(googleAuth.data, null, 2));
  
  if (googleAuth.success && googleAuth.data.data?.tokens?.access_token) {
    return googleAuth.data.data.tokens.access_token;
  }
  
  return null;
};

const testDoor = async (token) => {
  console.log('\n🚪 TESTING DOOR ENDPOINTS');
  console.log('=' .repeat(50));

  // Test Door Status
  console.log('\n1. GET /api/door/status');
  const doorStatus = await makeRequest('GET', '/door/status', null, token);
  console.log('Status:', doorStatus.status);
  console.log('Response:', JSON.stringify(doorStatus.data, null, 2));

  // Test Door Control
  console.log('\n2. POST /api/door/control');
  const doorControl = await makeRequest('POST', '/door/control', testData.doorControl, token);
  console.log('Status:', doorControl.status);
  console.log('Response:', JSON.stringify(doorControl.data, null, 2));
};

const testUsers = async (token) => {
  console.log('\n👥 TESTING USER ENDPOINTS');
  console.log('=' .repeat(50));

  // Test Get Users
  console.log('\n1. GET /api/users');
  const getUsers = await makeRequest('GET', '/users', null, token);
  console.log('Status:', getUsers.status);
  console.log('Response:', JSON.stringify(getUsers.data, null, 2));

  // Test Create User
  console.log('\n2. POST /api/users');
  const createUser = await makeRequest('POST', '/users', testData.newUser, token);
  console.log('Status:', createUser.status);
  console.log('Response:', JSON.stringify(createUser.data, null, 2));

  // Test Delete User (using ID 1)
  console.log('\n3. DELETE /api/users/1');
  const deleteUser = await makeRequest('DELETE', '/users/1', null, token);
  console.log('Status:', deleteUser.status);
  console.log('Response:', JSON.stringify(deleteUser.data, null, 2));
};

const testCamera = async (token) => {
  console.log('\n📷 TESTING CAMERA ENDPOINTS');
  console.log('=' .repeat(50));

  // Test Camera Stream
  console.log('\n1. GET /api/camera/stream?door_id=1');
  const cameraStream = await makeRequest('GET', '/camera/stream?door_id=1', null, token);
  console.log('Status:', cameraStream.status);
  console.log('Response:', JSON.stringify(cameraStream.data, null, 2));

  // Test Camera Capture
  console.log('\n2. POST /api/camera/capture');
  const cameraCapture = await makeRequest('POST', '/camera/capture', testData.cameraCapture, token);
  console.log('Status:', cameraCapture.status);
  console.log('Response:', JSON.stringify(cameraCapture.data, null, 2));

  // Test Get Camera Capture by ID
  console.log('\n3. GET /api/camera/capture/1');
  const getCapture = await makeRequest('GET', '/camera/capture/1', null, token);
  console.log('Status:', getCapture.status);
  console.log('Response:', JSON.stringify(getCapture.data, null, 2));
};

const testHistory = async (token) => {
  console.log('\n📜 TESTING HISTORY ENDPOINTS');
  console.log('=' .repeat(50));

  // Test Access History
  console.log('\n1. GET /api/history/access');
  const accessHistory = await makeRequest('GET', '/history/access', null, token);
  console.log('Status:', accessHistory.status);
  console.log('Response:', JSON.stringify(accessHistory.data, null, 2));

  // Test Access History with filters
  console.log('\n2. GET /api/history/access?door_id=1&limit=5');
  const filteredAccess = await makeRequest('GET', '/history/access?door_id=1&limit=5', null, token);
  console.log('Status:', filteredAccess.status);
  console.log('Response:', JSON.stringify(filteredAccess.data, null, 2));

  // Test Photo History
  console.log('\n3. GET /api/history/photos');
  const photoHistory = await makeRequest('GET', '/history/photos', null, token);
  console.log('Status:', photoHistory.status);
  console.log('Response:', JSON.stringify(photoHistory.data, null, 2));

  // Test Photo History with filters
  console.log('\n4. GET /api/history/photos?door_id=1&limit=3');
  const filteredPhotos = await makeRequest('GET', '/history/photos?door_id=1&limit=3', null, token);
  console.log('Status:', filteredPhotos.status);
  console.log('Response:', JSON.stringify(filteredPhotos.data, null, 2));
};

const testNotifications = async (token) => {
  console.log('\n🔔 TESTING NOTIFICATION ENDPOINTS');
  console.log('=' .repeat(50));

  // Test Get Notifications
  console.log('\n1. GET /api/notifications');
  const getNotifications = await makeRequest('GET', '/notifications', null, token);
  console.log('Status:', getNotifications.status);
  console.log('Response:', JSON.stringify(getNotifications.data, null, 2));

  // Test Get Notifications with filters
  console.log('\n2. GET /api/notifications?read=false&limit=5');
  const unreadNotifications = await makeRequest('GET', '/notifications?read=false&limit=5', null, token);
  console.log('Status:', unreadNotifications.status);
  console.log('Response:', JSON.stringify(unreadNotifications.data, null, 2));

  // Test Mark as Read
  console.log('\n3. POST /api/notifications/mark-read');
  const markRead = await makeRequest('POST', '/notifications/mark-read', testData.markRead, token);
  console.log('Status:', markRead.status);
  console.log('Response:', JSON.stringify(markRead.data, null, 2));
};

const testErrorCases = async (token) => {
  console.log('\n❌ TESTING ERROR CASES');
  console.log('=' .repeat(50));

  // Test without token
  console.log('\n1. GET /api/door/status (without token)');
  const noToken = await makeRequest('GET', '/door/status');
  console.log('Status:', noToken.status);
  console.log('Response:', JSON.stringify(noToken.data, null, 2));

  // Test invalid door control
  console.log('\n2. POST /api/door/control (invalid action)');
  const invalidControl = await makeRequest('POST', '/door/control', { door_id: 1, action: 'invalid' }, token);
  console.log('Status:', invalidControl.status);
  console.log('Response:', JSON.stringify(invalidControl.data, null, 2));

  // Test missing door_id for camera
  console.log('\n3. POST /api/camera/capture (missing door_id)');
  const missingDoorId = await makeRequest('POST', '/camera/capture', {}, token);
  console.log('Status:', missingDoorId.status);
  console.log('Response:', JSON.stringify(missingDoorId.data, null, 2));

  // Test non-existent capture
  console.log('\n4. GET /api/camera/capture/999 (non-existent)');
  const nonExistentCapture = await makeRequest('GET', '/camera/capture/999', null, token);
  console.log('Status:', nonExistentCapture.status);
  console.log('Response:', JSON.stringify(nonExistentCapture.data, null, 2));
};

// Main test function
const runAllTests = async () => {
  console.log('🧪 MOCK API TESTING SUITE');
  console.log('=' .repeat(50));
  console.log('Base URL:', BASE_URL);
  console.log('Starting tests...\n');

  try {
    // Test authentication first to get token
    const token = await testAuth();
    
    if (!token) {
      console.log('❌ Failed to get authentication token. Stopping tests.');
      return;
    }

    console.log('✅ Authentication successful. Token obtained.');

    // Run all other tests
    await testDoor(token);
    await testUsers(token);
    await testCamera(token);
    await testHistory(token);
    await testNotifications(token);
    await testErrorCases(token);

    console.log('\n🎉 ALL TESTS COMPLETED!');
    console.log('=' .repeat(50));

  } catch (error) {
    console.error('❌ Test suite failed:', error.message);
  }
};

// Export for use in other files
module.exports = {
  runAllTests,
  testAuth,
  testDoor,
  testUsers,
  testCamera,
  testHistory,
  testNotifications,
  testErrorCases
};

// Run tests if this file is executed directly
if (require.main === module) {
  runAllTests();
}
