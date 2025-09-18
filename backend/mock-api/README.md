# Mock API - Tim Pagi

## Backend Simulasi untuk Testing

### Tanggung Jawab (2 orang dari tim pagi)
- Setup Node.js/Express server
- Create fake data generators
- Mock semua API endpoints
- Testing environment configuration

### API Endpoints yang harus di-mock
```
Authentication:
POST /api/auth/google
POST /api/auth/logout

Door Control:
GET /api/door/status
POST /api/door/control

User Management:
GET /api/users
POST /api/users
DELETE /api/users/:id

Camera:
GET /api/camera/stream
POST /api/camera/capture

History:
GET /api/history/access
GET /api/history/photos

Notifications:
GET /api/notifications
POST /api/notifications/mark-read
```

### Mock Data Structure
```
User: { id, name, email, role, face_registered, created_at }
Access Log: { id, user_id, action, timestamp, success, method }
Door Status: { locked, battery_level, last_update, camera_active }
Notification: { id, type, message, read, created_at }
```

### Technology Stack
- Node.js + Express
- JSON file storage (no real database)
- Faker.js untuk generate data
- Socket.io untuk real-time simulation
- CORS enabled untuk Android testing

### Development Guidelines
- Response format harus sama dengan production API
- Include realistic delays (100-500ms)
- Generate realistic Indonesian names/data
- Error responses untuk testing edge cases
- Logging untuk debugging
