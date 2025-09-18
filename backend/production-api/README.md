# Production API - Tim Malam

## Backend Production untuk Sistem Real

### Tanggung Jawab (3 orang dari tim malam)
- Setup Node.js/Express production server
- PostgreSQL database design & implementation
- Real-time WebSocket integration
- Authentication & security implementation
- API documentation & testing

### API Endpoints
```
Authentication:
POST /api/auth/google
POST /api/auth/refresh
POST /api/auth/logout

Door Control:
GET /api/door/status
POST /api/door/control
PUT /api/door/settings

User Management:
GET /api/users
POST /api/users
PUT /api/users/:id
DELETE /api/users/:id
POST /api/users/:id/face-register

Camera:
GET /api/camera/stream
POST /api/camera/capture
GET /api/camera/recordings
DELETE /api/camera/recordings/:id

History & Analytics:
GET /api/history/access
GET /api/analytics/dashboard
GET /api/analytics/reports
POST /api/analytics/export

Notifications:
GET /api/notifications
POST /api/notifications
PUT /api/notifications/:id/read
DELETE /api/notifications/:id
```

### Database Schema
```
Users: id, google_id, email, name, role, face_data, created_at, updated_at
Access_Logs: id, user_id, action, timestamp, success, method, ip_address
Door_Status: id, locked, battery_level, last_update, camera_active
Notifications: id, user_id, type, title, message, read, created_at
Camera_Records: id, filename, timestamp, event_type, file_size
System_Settings: id, key, value, updated_at, updated_by
```

### Technology Stack
- Node.js + Express.js
- PostgreSQL database
- JWT authentication
- Socket.io untuk real-time features
- Multer untuk file upload
- Bcrypt untuk password hashing
- Rate limiting & CORS
- Logging dengan Winston

### Security Features
- JWT token dengan refresh mechanism
- API rate limiting
- Input validation & sanitization
- SQL injection prevention
- HTTPS enforcement
- Error handling tanpa info exposure
- Session management

### Development Guidelines
- RESTful API design patterns
- Comprehensive error handling
- API documentation dengan Swagger
- Unit testing dengan Jest
- Environment-based configuration
- Database migrations & seeds
- Monitoring & logging
