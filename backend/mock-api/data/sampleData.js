const faker = require('faker');
const { v4: uuidv4 } = require('uuid');
const fs = require('fs');
const path = require('path');

// Set locale to Indonesia for realistic Indonesian names
faker.setLocale('id_ID');

// Indonesian names arrays for more authentic data
const indonesianFirstNames = [
  'Ahmad', 'Budi', 'Sari', 'Dewi', 'Eko', 'Fitri', 'Gita', 'Hendra', 'Indira', 'Joko',
  'Kartika', 'Lestari', 'Made', 'Nurul', 'Okta', 'Putri', 'Qori', 'Rizki', 'Sinta', 'Toni',
  'Umi', 'Vera', 'Wulan', 'Yoga', 'Zahra', 'Andi', 'Bayu', 'Citra', 'Dian', 'Eka',
  'Fajar', 'Galih', 'Hani', 'Ilham', 'Jihan', 'Kiki', 'Laila', 'Maya', 'Nina', 'Oka',
  'Pandu', 'Qonita', 'Reza', 'Siska', 'Tika', 'Udin', 'Vina', 'Wira', 'Yanti', 'Zaki'
];

const indonesianLastNames = [
  'Pratama', 'Sari', 'Wijaya', 'Putri', 'Santoso', 'Wati', 'Kusuma', 'Dewi', 'Permana', 'Lestari',
  'Setiawan', 'Anggraeni', 'Nugroho', 'Maharani', 'Susanto', 'Rahayu', 'Hidayat', 'Safitri', 'Kurniawan', 'Puspita',
  'Gunawan', 'Oktavia', 'Handoko', 'Melati', 'Suryanto', 'Cahyani', 'Firmansyah', 'Nursyah', 'Budiono', 'Kartini'
];

// Generate Indonesian name
const generateIndonesianName = () => {
  const firstName = indonesianFirstNames[Math.floor(Math.random() * indonesianFirstNames.length)];
  const lastName = indonesianLastNames[Math.floor(Math.random() * indonesianLastNames.length)];
  return `${firstName} ${lastName}`;
};

// Generate sample users
const generateUsers = (count = 10) => {
  const users = [];
  const roles = ['admin', 'user'];

  for (let i = 0; i < count; i++) {
    const name = generateIndonesianName();
    const email = `${name.toLowerCase().replace(' ', '.')}@gmail.com`;

    users.push({
      id: i + 1,
      google_id: uuidv4(),
      name,
      email,
      role: roles[Math.floor(Math.random() * roles.length)],
      face_registered: Math.random() > 0.2, // 80% have face registered
      created_at: faker.date.between('2023-01-01', new Date()).toISOString(),
      phone: `+62${Math.floor(Math.random() * 900000000) + 100000000}`,
      avatar: `https://randomuser.me/api/portraits/${Math.random() > 0.5 ? 'men' : 'women'}/${Math.floor(Math.random() * 100)}.jpg`
    });
  }

  return users;
};

// Generate access logs
const generateAccessLogs = (users, doors, count = 50) => {
  const logs = [];
  const actions = ['buka', 'tutup'];
  const methods = ['face_recognition', 'mobile_app'];

  for (let i = 0; i < count; i++) {
    const user = users[Math.floor(Math.random() * users.length)];
    const door = doors[Math.floor(Math.random() * doors.length)];
    const action = actions[Math.floor(Math.random() * actions.length)];

    logs.push({
      id: i + 1,
      user_id: user.id,
      door_id: door.id,
      action,
      timestamp: faker.date.recent(30).toISOString(), // Last 30 days
      success: Math.random() > 0.1, // 90% true, 10% false
      method: methods[Math.floor(Math.random() * methods.length)],
      ip_address: faker.internet.ip(),
      camera_capture_id: Math.random() > 0.3 ? Math.floor(Math.random() * 40) + 1 : null, // 70% have camera capture, 30% null
    });
  }

  return logs.sort((a, b) => new Date(b.timestamp) - new Date(a.timestamp));
};

const generateDoors = (count = 10) => {
  const doors = [];

  // Nama pintu seperti kampus
  const doorNames = [
    'Pintu Lab TA 1',
    'Pintu Lab TA 2',
    'Pintu Workshop 1',
    'Pintu Workshop 2',
    'Pintu Ruang Kelas 1',
    'Pintu Ruang Kelas 2',
    'Pintu Ruang Dosen',
    'Pintu Ruang Server',
    'Pintu Perpustakaan',
    'Pintu Auditorium',
    'Pintu Kantin',
    'Pintu Ruang Administrasi'
  ];

  // Lokasi seperti gedung kampus
  const locations = [
    'Gedung Teknik Lantai 1 Ruang 101',
    'Gedung Teknik Lantai 2 Ruang 202',
    'Gedung Informatika Lantai 3 Ruang 305',
    'Gedung Riset Lantai 2 Ruang 210',
    'Gedung Workshop Lantai 1 Area Produksi',
    'Gedung Dosen Lantai 2 Ruang 215',
    'Gedung Perpustakaan Lantai 1',
    'Gedung Aula Lantai 2 Ruang Auditorium',
    'Gedung Administrasi Lantai 1',
    'Gedung Kantin Area Tengah Kampus'
  ];

  const wifiLevels = ['Excellent', 'Good', 'Fair', 'Weak', 'No Signal'];

  for (let i = 0; i < count; i++) {
    doors.push({
      id: i + 1,
      name: doorNames[Math.floor(Math.random() * doorNames.length)],
      location: locations[Math.floor(Math.random() * locations.length)],
      locked: Math.random() > 0.2, // 80% terkunci
      battery_level: Math.floor(Math.random() * 101), // 0–100
      last_update: new Date().toISOString(),
      wifi_strength: wifiLevels[Math.floor(Math.random() * wifiLevels.length)],
      camera_active: Math.random() > 0.3, // 70% aktif
    });
  }

  return doors;
};



// Generate user-door access relationships (pivot table)
const generateUserDoorAccess = (users, doors) => {
  const userDoorAccess = [];

  users.forEach(user => {
    // Each user can have access to multiple doors (many-to-many)
    const doorCount = Math.floor(Math.random() * 3) + 1; // 1-3 doors per user
    const selectedDoors = [];

    for (let i = 0; i < doorCount; i++) {
      const randomDoor = doors[Math.floor(Math.random() * doors.length)];
      if (!selectedDoors.includes(randomDoor.id)) {
        selectedDoors.push(randomDoor.id);
        
        userDoorAccess.push({
          id: i + 1,
          user_id: user.id,
          door_id: randomDoor.id,
          created_at: faker.date.between('2023-01-01', new Date()).toISOString()
        });
      }
    }
  });

  return userDoorAccess;
};

// Generate notifications
const generateNotifications = (users, doors, count = 20) => {
  const notifications = [];
  const types = ['access_granted', 'access_denied', 'low_battery', 'system_update', 'maintenance_required', 'camera_offline'];
  const messages = {
    access_granted: (user, door) => `${user} berhasil membuka pintu ${door}`,
    access_denied: (user, door) => `Akses ditolak untuk ${user} di pintu ${door}`,
    low_battery: (door) => `Baterai pintu ${door} rendah (${Math.floor(Math.random() * 20)}%)`,
    system_update: (door) => `Pembaruan sistem tersedia untuk pintu ${door}`,
    maintenance_required: (door) => `Perawatan rutin diperlukan untuk pintu ${door}`,
    camera_offline: (door) => `Kamera pintu ${door} tidak terhubung`
  };

  for (let i = 0; i < count; i++) {
    const type = types[Math.floor(Math.random() * types.length)];
    const user = users[Math.floor(Math.random() * users.length)];
    const door = doors[Math.floor(Math.random() * doors.length)];

    notifications.push({
      id: i + 1,
      user_id: type.includes('access') ? user.id : null,
      type,
      message: messages[type](user.name, door.name),
      read: Math.random() > 0.3, // 70% read
      created_at: faker.date.recent(7).toISOString(), // Last 7 days
    });
  }

  return notifications.sort((a, b) => new Date(b.created_at) - new Date(a.created_at));
};

// Generate camera photos/captures
const generateCameraCaptures = (users, doors, count = 30) => {
  const captures = [];

  for (let i = 0; i < count; i++) {
    const door = doors[Math.floor(Math.random() * doors.length)];

    // buat nama file realistis
    const datePart = new Date().toISOString().replace(/[:.]/g, '-');
    const safeDoorName = door.name.toLowerCase().replace(/\s+/g, '_');
    const filename = `capture_${safeDoorName}_${datePart}_${i + 1}.jpg`;

    captures.push({
      id: i + 1,
      door_id: door.id,
      filename,
      timestamp: faker.date.recent(14).toISOString(), // Last 14 days
      event_type: ['motion_detected', 'face_scan', 'manual_capture', 'access_attempt'][Math.floor(Math.random() * 4)],
      file_size: Math.floor(Math.random() * 900000) + 100000, // ukuran 100KB–1MB
    });
  }

  return captures.sort((a, b) => new Date(b.timestamp) - new Date(a.timestamp));
};

// Initialize and save sample data
const initializeSampleData = () => {
  console.log('🔄 Generating sample data dengan Faker.js...');

  const users = generateUsers(15);
  const doors = generateDoors(10); 
  const accessLogs = generateAccessLogs(users, doors, 100);
  const userDoor = generateUserDoorAccess(users, doors); // Generate user-door relationships
  const notifications = generateNotifications(users, doors, 25);
  const cameraCaptures = generateCameraCaptures(users, doors, 40);

  const data = {
    users,
    doors,
    accessLogs,
    userDoor, // Pivot table for many-to-many relationship
    notifications,  
    cameraCaptures,
    lastUpdated: new Date().toISOString()
  };

  // Save to JSON file
  const dataPath = path.join(__dirname, 'sample-data.json');
  fs.writeFileSync(dataPath, JSON.stringify(data, null, 2));

  console.log('✅ Sample data generated and saved to sample-data.json');
  console.log(`📊 Generated: ${users.length} users, ${doors.length} doors, ${accessLogs.length} access logs, ${userDoor.length} user-door relationships, ${notifications.length} notifications, ${cameraCaptures.length} camera captures`);

  return data;
};

// Load sample data
const loadSampleData = () => {
  const dataPath = path.join(__dirname, 'sample-data.json');

  if (fs.existsSync(dataPath)) {
    const data = JSON.parse(fs.readFileSync(dataPath, 'utf8'));
    console.log('📂 Sample data loaded from file');
    return data;
  } else {
    console.log('📂 No existing sample data found, generating new data...');
    return initializeSampleData();
  }
};

// Export functions
module.exports = {
  generateUsers,
  generateDoors,
  generateAccessLogs,
  generateUserDoorAccess,
  generateNotifications,
  generateCameraCaptures,
  generateIndonesianName,
  initializeSampleData,
  loadSampleData
};