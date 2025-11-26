const fs = require('fs');
const path = require('path');

const filePath = path.join(__dirname, 'data', 'sample-data.json');

console.log('👀 Watching for changes in sample-data.json...');
console.log('File path:', filePath);

// Function to get door status
const getDoorStatus = (doorId) => {
  try {
    const data = JSON.parse(fs.readFileSync(filePath, 'utf8'));
    const door = data.doors.find(d => d.id === doorId);
    return door;
  } catch (error) {
    console.log('Error reading file:', error.message);
    return null;
  }
};

// Function to get latest access log
const getLatestAccessLog = () => {
  try {
    const data = JSON.parse(fs.readFileSync(filePath, 'utf8'));
    return data.accessLogs[0]; // First item is the latest
  } catch (error) {
    console.log('Error reading file:', error.message);
    return null;
  }
};

// Show initial status
console.log('\n📋 Initial Status:');
for (let i = 1; i <= 5; i++) {
  const door = getDoorStatus(i);
  if (door) {
    console.log(`Door ${i}: ${door.locked ? 'LOCKED' : 'UNLOCKED'} (Battery: ${door.battery_level}%)`);
  }
}

const latestLog = getLatestAccessLog();
if (latestLog) {
  console.log(`\nLatest access log: User ${latestLog.user_id} → Door ${latestLog.door_id} → ${latestLog.action} at ${latestLog.timestamp}`);
}

console.log('\n🔄 Watching for changes... (Press Ctrl+C to stop)\n');

// Watch for file changes
fs.watchFile(filePath, (curr, prev) => {
  const timestamp = new Date().toLocaleTimeString();
  console.log(`\n⚡ [${timestamp}] File changed!`);
  
  // Show updated door status
  console.log('📋 Updated Door Status:');
  for (let i = 1; i <= 5; i++) {
    const door = getDoorStatus(i);
    if (door) {
      console.log(`Door ${i}: ${door.locked ? 'LOCKED' : 'UNLOCKED'} (Last update: ${door.last_update})`);
    }
  }
  
  // Show latest access log
  const newLatestLog = getLatestAccessLog();
  if (newLatestLog) {
    console.log(`\n📝 Latest access log: User ${newLatestLog.user_id} → Door ${newLatestLog.door_id} → ${newLatestLog.action} at ${newLatestLog.timestamp}`);
  }
  
  console.log('\n🔄 Continuing to watch...\n');
});

// Handle Ctrl+C gracefully
process.on('SIGINT', () => {
  console.log('\n👋 Stopping file watcher...');
  fs.unwatchFile(filePath);
  process.exit(0);
});
