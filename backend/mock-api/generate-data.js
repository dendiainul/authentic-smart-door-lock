#!/usr/bin/env node

/**
 * Script untuk generate sample data
 * Cara menjalankan: node generate-data.js
 */

const { initializeSampleData, loadSampleData } = require('./data/sampleData');

console.log('🚀 Starting sample data generation...\n');

try {
  // Generate fresh sample data
  const data = initializeSampleData();
  
  console.log('\n✅ Sample data generation completed successfully!');
  console.log('\n📋 Summary:');
  console.log(`   👥 Users: ${data.users.length}`);
  console.log(`   🚪 Doors: ${data.doors.length}`);
  console.log(`   📝 Access Logs: ${data.accessLogs.length}`);
  console.log(`   🔗 User-Door Access: ${data.userDoor.length}`);
  console.log(`   🔔 Notifications: ${data.notifications.length}`);
  console.log(`   📸 Camera Captures: ${data.cameraCaptures.length}`);
  console.log(`\n📁 Data saved to: backend/mock-api/data/sample-data.json`);
  
} catch (error) {
  console.error('❌ Error generating sample data:', error.message);
  process.exit(1);
}
