const axios = require('axios');
const fs = require('fs');
const path = require('path');

const BASE_URL = 'http://localhost:3000/api';

// Helper function to read current door status from file
const getCurrentDoorStatus = (doorId) => {
  try {
    const filePath = path.join(__dirname, 'data', 'sample-data.json');
    const data = JSON.parse(fs.readFileSync(filePath, 'utf8'));
    const door = data.doors.find(d => d.id === parseInt(doorId));
    return door;
  } catch (error) {
    console.log('Error reading file:', error.message);
    return null;
  }
};

// Test function
async function testDoorControl() {
  console.log('🔧 Debug Door Control Test\n');
  
  // Test door ID 1 (should have battery > 0)
  const testDoorId = 1;
  
  console.log('📋 Current door status from file:');
  const doorBefore = getCurrentDoorStatus(testDoorId);
  if (doorBefore) {
    console.log(`Door ${testDoorId}: ${doorBefore.locked ? 'LOCKED' : 'UNLOCKED'}, Battery: ${doorBefore.battery_level}%`);
  } else {
    console.log('❌ Could not read door status from file');
    return;
  }
  
  // Try to login first
  console.log('\n🔐 Attempting login...');
  try {
    const loginResponse = await axios.post(`${BASE_URL}/auth/login`, {
      email: 'hafizganzzxd@gmail.com',
      password: 'password123'
    });
    
    if (!loginResponse.data.success) {
      console.log('❌ Login failed:', loginResponse.data.message);
      return;
    }
    
    const token = loginResponse.data.data.token;
    console.log('✅ Login successful');
    
    // Test door control
    console.log('\n🚪 Testing door control...');
    const action = doorBefore.locked ? 'unlock' : 'lock';
    console.log(`Sending action: ${action}`);
    
    const controlResponse = await axios.post(`${BASE_URL}/door/control`, {
      door_id: testDoorId,
      action: action
    }, {
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    });
    
    console.log('📡 API Response:');
    console.log('Status:', controlResponse.status);
    console.log('Success:', controlResponse.data.success);
    console.log('Message:', controlResponse.data.message);
    
    if (controlResponse.data.success) {
      console.log('Data:', JSON.stringify(controlResponse.data.data, null, 2));
      
      // Check file again
      console.log('\n📋 Door status after API call:');
      const doorAfter = getCurrentDoorStatus(testDoorId);
      if (doorAfter) {
        console.log(`Door ${testDoorId}: ${doorAfter.locked ? 'LOCKED' : 'UNLOCKED'}, Last Update: ${doorAfter.last_update}`);
        
        // Verify the change
        const expectedLocked = (action === 'lock');
        if (doorAfter.locked === expectedLocked) {
          console.log('✅ Door status updated correctly in file!');
        } else {
          console.log('❌ Door status NOT updated in file!');
          console.log(`Expected locked: ${expectedLocked}, Actual: ${doorAfter.locked}`);
        }
      } else {
        console.log('❌ Could not read updated door status');
      }
    } else {
      console.log('❌ API call failed');
    }
    
  } catch (error) {
    console.log('❌ Error during test:');
    if (error.response) {
      console.log('Status:', error.response.status);
      console.log('Data:', error.response.data);
    } else {
      console.log('Error:', error.message);
    }
  }
}

// Check if server is running
async function checkServer() {
  try {
    const response = await axios.get(`${BASE_URL}/auth/login`);
    return true;
  } catch (error) {
    if (error.code === 'ECONNREFUSED') {
      console.log('❌ Server is not running! Please start with: npm start');
      return false;
    }
    return true; // Server is running but endpoint returned error (expected)
  }
}

// Main execution
async function main() {
  const serverRunning = await checkServer();
  if (serverRunning) {
    await testDoorControl();
  }
}

main().catch(console.error);
