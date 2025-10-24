const axios = require('axios');
const fs = require('fs');
const path = require('path');

const BASE_URL = 'http://localhost:3000/api';

// Test credentials
const testUser = {
  email: 'test@example.com',
  password: 'password123'
};

let authToken = '';

// Helper function to read sample data
const readSampleData = () => {
  const filePath = path.join(__dirname, 'data', 'sample-data.json');
  return JSON.parse(fs.readFileSync(filePath, 'utf8'));
};

// Helper function to get door status by ID
const getDoorById = (doorId) => {
  const data = readSampleData();
  return data.doors.find(door => door.id === parseInt(doorId));
};

// Test login
async function login() {
  try {
    console.log('🔐 Testing login...');
    const response = await axios.post(`${BASE_URL}/auth/login`, testUser);
    
    if (response.data.success && response.data.data.token) {
      authToken = response.data.data.token;
      console.log('✅ Login successful');
      return true;
    } else {
      console.log('❌ Login failed:', response.data.message);
      return false;
    }
  } catch (error) {
    console.log('❌ Login error:', error.response?.data?.message || error.message);
    return false;
  }
}

// Test door status retrieval
async function testGetDoorStatus() {
  try {
    console.log('\n📋 Testing door status retrieval...');
    const response = await axios.get(`${BASE_URL}/door/status`, {
      headers: {
        'Authorization': `Bearer ${authToken}`
      }
    });
    
    if (response.data.success && response.data.data.length > 0) {
      console.log(`✅ Retrieved ${response.data.data.length} doors`);
      console.log('First door:', {
        id: response.data.data[0].id,
        name: response.data.data[0].name,
        locked: response.data.data[0].locked,
        battery_level: response.data.data[0].battery_level
      });
      return response.data.data[0]; // Return first door for testing
    } else {
      console.log('❌ No doors found');
      return null;
    }
  } catch (error) {
    console.log('❌ Door status error:', error.response?.data?.message || error.message);
    return null;
  }
}

// Test door control
async function testDoorControl(doorId, action) {
  try {
    console.log(`\n🚪 Testing door control: ${action} door ${doorId}...`);
    
    // Get door status before control
    const doorBefore = getDoorById(doorId);
    console.log(`Door status before: ${doorBefore.locked ? 'LOCKED' : 'UNLOCKED'}`);
    
    const response = await axios.post(`${BASE_URL}/door/control`, {
      door_id: doorId,
      action: action
    }, {
      headers: {
        'Authorization': `Bearer ${authToken}`,
        'Content-Type': 'application/json'
      }
    });
    
    if (response.data.success) {
      console.log('✅ Door control successful');
      console.log('Response:', {
        action: response.data.data.action,
        door_status: response.data.data.door_status,
        timestamp: response.data.data.timestamp
      });
      
      // Verify the change in sample-data.json
      const doorAfter = getDoorById(doorId);
      console.log(`Door status after: ${doorAfter.locked ? 'LOCKED' : 'UNLOCKED'}`);
      
      // Check if status actually changed
      const expectedLocked = (action === 'lock' || action === 'tutup');
      if (doorAfter.locked === expectedLocked) {
        console.log('✅ Door status updated correctly in sample-data.json');
      } else {
        console.log('❌ Door status not updated correctly in sample-data.json');
      }
      
      return true;
    } else {
      console.log('❌ Door control failed:', response.data.message);
      return false;
    }
  } catch (error) {
    console.log('❌ Door control error:', error.response?.data?.message || error.message);
    if (error.response?.data?.code) {
      console.log('Error code:', error.response.data.code);
    }
    return false;
  }
}

// Test offline door control
async function testOfflineDoorControl() {
  try {
    console.log('\n🔋 Testing offline door control...');
    
    // Find a door with 0% battery
    const data = readSampleData();
    const offlineDoor = data.doors.find(door => door.battery_level === 0);
    
    if (!offlineDoor) {
      console.log('⚠️ No offline doors found for testing');
      return;
    }
    
    console.log(`Found offline door: ${offlineDoor.name} (ID: ${offlineDoor.id})`);
    
    const response = await axios.post(`${BASE_URL}/door/control`, {
      door_id: offlineDoor.id,
      action: 'unlock'
    }, {
      headers: {
        'Authorization': `Bearer ${authToken}`,
        'Content-Type': 'application/json'
      }
    });
    
    console.log('❌ Offline door control should have failed but succeeded');
  } catch (error) {
    if (error.response?.data?.code === 'DOOR_OFFLINE') {
      console.log('✅ Offline door control correctly rejected');
    } else {
      console.log('❌ Unexpected error:', error.response?.data?.message || error.message);
    }
  }
}

// Main test function
async function runTests() {
  console.log('🧪 Starting Door Control Tests\n');
  
  // Login first
  const loginSuccess = await login();
  if (!loginSuccess) {
    console.log('Cannot proceed without login');
    return;
  }
  
  // Get door status
  const testDoor = await testGetDoorStatus();
  if (!testDoor) {
    console.log('Cannot proceed without doors');
    return;
  }
  
  // Test door control with different actions
  await testDoorControl(testDoor.id, 'unlock');
  await new Promise(resolve => setTimeout(resolve, 1000)); // Wait 1 second
  
  await testDoorControl(testDoor.id, 'lock');
  await new Promise(resolve => setTimeout(resolve, 1000)); // Wait 1 second
  
  // Test with Indonesian terms
  await testDoorControl(testDoor.id, 'buka');
  await new Promise(resolve => setTimeout(resolve, 1000)); // Wait 1 second
  
  await testDoorControl(testDoor.id, 'tutup');
  
  // Test offline door control
  await testOfflineDoorControl();
  
  console.log('\n🎉 All tests completed!');
}

// Run tests if this script is executed directly
if (require.main === module) {
  runTests().catch(console.error);
}

module.exports = { runTests };
