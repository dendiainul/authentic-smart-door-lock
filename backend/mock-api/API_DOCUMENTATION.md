# 📚 Mock API Documentation

Documentation lengkap untuk Smart Door Lock Mock API dengan semua endpoint, request/response format, dan contoh penggunaan.

## 📋 Table of Contents

1. [Overview](#overview)
2. [Authentication](#authentication)
3. [Door Control](#door-control)
4. [User Management](#user-management)
5. [Camera](#camera)
6. [History](#history)
7. [Notifications](#notifications)
8. [Error Handling](#error-handling)
9. [Testing](#testing)
10. [Sample Data](#sample-data)

---

## 🔍 Overview

Mock API untuk sistem Smart Door Lock yang menyediakan endpoint untuk:
- Google Authentication
- Door Control & Status
- User Management
- Camera Operations
- Access History
- Notifications

**Base URL:** `http://localhost:3000/api`

**Authentication:** Bearer Token (JWT)

---

## 🔐 Authentication

### POST /api/auth/google
Google authentication untuk login user.

**Request Body:**
```json
{
  "id_token": "google_id_token",
  "email": "user@gmail.com",
  "name": "User Name",
  "picture": "https://profile-picture-url.com"
}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "user": {
      "id": 1,
      "google_id": "google_123456789",
      "email": "user@gmail.com",
      "name": "User Name",
      "role": "user",
      "face_registered": false,
      "created_at": "2025-10-18T11:39:53.054Z",
      "phone": null,
      "avatar": "https://profile-picture-url.com",
      "accessible_doors": [
        {
          "id": 1,
          "name": "Pintu Ruang Server",
          "location": "Gedung Informatika Lantai 3 Ruang 305",
          "locked": true,
          "battery_level": 83,
          "camera_active": false
        }
      ]
    },
    "tokens": {
      "access_token": "jwt_token_here",
      "refresh_token": "jwt_token_here_refresh",
      "token_type": "Bearer",
      "expires_in": 86400
    }
  },
  "message": "Authentication successful"
}
```

### POST /api/auth/logout
Logout user dan invalidate token.

**Headers:**
```
Authorization: Bearer <jwt_token>
```

**Response:**
```json
{
  "success": true,
  "message": "Logout successful"
}
```

---

## 🚪 Door Control

### GET /api/door/status
Mendapatkan status pintu yang bisa diakses oleh user yang sedang login.

**Headers:**
```
Authorization: Bearer <jwt_token>
```

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "name": "Pintu Ruang Server",
      "location": "Gedung Informatika Lantai 3 Ruang 305",
      "locked": true,
      "battery_level": 83,
      "last_update": "2025-10-18T11:12:55.385Z",
      "wifi_strength": "Excellent",
      "camera_active": false,
      "access_granted_at": "2025-10-18T11:39:53.054Z"
    }
  ],
  "message": "User accessible doors retrieved successfully"
}
```

### POST /api/door/control
Mengontrol pintu (buka/tutup).

**Headers:**
```
Authorization: Bearer <jwt_token>
```

**Request Body:**
```json
{
  "door_id": 1,
  "action": "buka"
}
```

**Valid Actions:**
- `"buka"` - Buka pintu
- `"kunci"` - Kunci pintu

**Response:**
```json
{
  "success": true,
  "data": {
    "door_id": 1,
    "action": "buka",
    "success": true,
    "timestamp": "2025-10-18T11:39:53.054Z",
    "message": "Door opened successfully"
  },
  "message": "Door opened successfully"
}
```

---

## 👥 User Management

### GET /api/users
Mendapatkan daftar semua users.

**Headers:**
```
Authorization: Bearer <jwt_token>
```

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "google_id": "99a22664-b076-40e7-9caa-0ede637ec9ac",
      "name": "Rizki Maharani",
      "email": "rizki.maharani@gmail.com",
      "role": "admin",
      "face_registered": true,
      "created_at": "2025-09-04T08:29:31.670Z",
      "phone": "+62120164601",
      "avatar": "https://randomuser.me/api/portraits/women/1.jpg"
    }
  ],
  "message": "Users retrieved successfully"
}
```

### POST /api/users
Membuat user baru.

**Headers:**
```
Authorization: Bearer <jwt_token>
```

**Request Body:**
```json
{
  "name": "New User",
  "email": "newuser@example.com",
  "role": "user",
  "phone": "+6281234567890"
}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "id": 16,
    "google_id": "uuid-generated",
    "name": "New User",
    "email": "newuser@example.com",
    "role": "user",
    "face_registered": false,
    "created_at": "2025-10-18T11:39:53.054Z",
    "phone": "+6281234567890",
    "avatar": "https://randomuser.me/api/portraits/men/50.jpg"
  },
  "message": "User created successfully"
}
```

### DELETE /api/users/:id
Menghapus user berdasarkan ID.

**Headers:**
```
Authorization: Bearer <jwt_token>
```

**Response:**
```json
{
  "success": true,
  "message": "User deleted successfully"
}
```

---

## 📷 Camera

### GET /api/camera/stream
Mendapatkan URL stream kamera untuk pintu tertentu.

**Headers:**
```
Authorization: Bearer <jwt_token>
```

**Query Parameters:**
- `door_id` (required) - ID pintu

**Example:** `GET /api/camera/stream?door_id=1`

**Response:**
```json
{
  "success": true,
  "data": {
    "door_id": 1,
    "stream_url": "rtsp://mock-camera-server.com/stream/1",
    "status": "active",
    "resolution": "1920x1080",
    "fps": 30,
    "timestamp": "2025-10-18T11:39:53.054Z"
  },
  "message": "Camera stream retrieved successfully"
}
```

### POST /api/camera/capture
Mengambil foto dari kamera pintu.

**Headers:**
```
Authorization: Bearer <jwt_token>
```

**Request Body:**
```json
{
  "door_id": 1,
  "trigger_type": "manual"
}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "id": "capture_1697632793054",
    "door_id": 1,
    "trigger_type": "manual",
    "image_url": "https://picsum.photos/640/480?random=1697632793054",
    "thumbnail_url": "https://picsum.photos/160/120?random=1697632793054",
    "timestamp": "2025-10-18T11:39:53.054Z",
    "confidence_score": 0.85,
    "location": "Door 1 Camera"
  },
  "message": "Photo captured successfully"
}
```

### GET /api/camera/capture/:id
Mendapatkan detail capture berdasarkan ID.

**Headers:**
```
Authorization: Bearer <jwt_token>
```

**Response:**
```json
{
  "success": true,
  "data": {
    "id": 17,
    "door_id": 1,
    "filename": "capture_pintu_ruang_server_2025-10-18T11-12-55-388Z_17.jpg",
    "timestamp": "2025-10-18T08:26:07.560Z",
    "event_type": "face_scan",
    "file_size": 263189,
    "image_url": "https://picsum.photos/640/480?random=17",
    "thumbnail_url": "https://picsum.photos/160/120?random=17",
    "door": {
      "id": 1,
      "name": "Pintu Ruang Server",
      "location": "Gedung Informatika Lantai 3 Ruang 305",
      "locked": true,
      "battery_level": 83,
      "camera_active": false
    }
  },
  "message": "Camera capture retrieved successfully"
}
```

---

## 📜 History

### GET /api/history/access
Mendapatkan riwayat akses pintu.

**Headers:**
```
Authorization: Bearer <jwt_token>
```

**Query Parameters:**
- `limit` (optional) - Jumlah data per halaman (default: 50)
- `offset` (optional) - Offset untuk pagination (default: 0)
- `door_id` (optional) - Filter berdasarkan ID pintu
- `user_id` (optional) - Filter berdasarkan ID user
- `start_date` (optional) - Filter dari tanggal
- `end_date` (optional) - Filter sampai tanggal
- `success` (optional) - Filter berdasarkan status sukses (true/false)

**Example:** `GET /api/history/access?door_id=1&limit=10&success=true`

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "id": 28,
      "user_id": 5,
      "door_id": 1,
      "action": "buka",
      "timestamp": "2025-10-18T11:09:14.652Z",
      "success": true,
      "method": "face_recognition",
      "ip_address": "176.174.192.90",
      "camera_capture_id": 17,
      "user": {
        "id": 5,
        "name": "Kartika Firmansyah",
        "email": "kartika.firmansyah@gmail.com",
        "avatar": "https://randomuser.me/api/portraits/men/23.jpg"
      },
      "door": {
        "id": 1,
        "name": "Pintu Ruang Server",
        "location": "Gedung Informatika Lantai 3 Ruang 305"
      },
      "camera_capture": {
        "id": 17,
        "filename": "capture_pintu_ruang_server_2025-10-18T11-12-55-388Z_17.jpg",
        "event_type": "face_scan",
        "timestamp": "2025-10-18T08:26:07.560Z"
      }
    }
  ],
  "pagination": {
    "total": 100,
    "limit": 10,
    "offset": 0,
    "has_more": true
  },
  "message": "Access history retrieved successfully"
}
```

### GET /api/history/photos
Mendapatkan riwayat foto kamera.

**Headers:**
```
Authorization: Bearer <jwt_token>
```

**Query Parameters:**
- `limit` (optional) - Jumlah data per halaman (default: 20)
- `offset` (optional) - Offset untuk pagination (default: 0)
- `door_id` (optional) - Filter berdasarkan ID pintu
- `start_date` (optional) - Filter dari tanggal
- `end_date` (optional) - Filter sampai tanggal
- `event_type` (optional) - Filter berdasarkan tipe event

**Example:** `GET /api/history/photos?door_id=1&limit=5&event_type=face_scan`

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "id": 2,
      "door_id": 8,
      "filename": "capture_pintu_auditorium_2025-10-18T11-12-55-388Z_2.jpg",
      "timestamp": "2025-10-18T08:32:18.977Z",
      "event_type": "access_attempt",
      "file_size": 617805,
      "door": {
        "id": 8,
        "name": "Pintu Auditorium",
        "location": "Gedung Aula Lantai 2 Ruang Auditorium"
      }
    }
  ],
  "pagination": {
    "total": 40,
    "limit": 5,
    "offset": 0,
    "has_more": true
  },
  "message": "Photo history retrieved successfully"
}
```

---

## 🔔 Notifications

### GET /api/notifications
Mendapatkan daftar notifikasi.

**Headers:**
```
Authorization: Bearer <jwt_token>
```

**Query Parameters:**
- `limit` (optional) - Jumlah data per halaman (default: 20)
- `offset` (optional) - Offset untuk pagination (default: 0)
- `read` (optional) - Filter berdasarkan status baca (true/false)
- `type` (optional) - Filter berdasarkan tipe notifikasi
- `user_id` (optional) - Filter berdasarkan ID user

**Example:** `GET /api/notifications?read=false&limit=10&type=access_granted`

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "id": 18,
      "user_id": 1,
      "type": "access_granted",
      "message": "Rizki Maharani berhasil membuka pintu Pintu Lab TA 2",
      "read": false,
      "created_at": "2025-10-18T10:43:04.095Z",
      "user": {
        "id": 1,
        "name": "Rizki Maharani",
        "email": "rizki.maharani@gmail.com",
        "avatar": "https://randomuser.me/api/portraits/women/1.jpg"
      }
    }
  ],
  "pagination": {
    "total": 25,
    "limit": 10,
    "offset": 0,
    "has_more": true
  },
  "message": "Notifications retrieved successfully"
}
```

### POST /api/notifications/mark-read
Menandai notifikasi sebagai sudah dibaca.

**Headers:**
```
Authorization: Bearer <jwt_token>
```

**Request Body:**
```json
{
  "notification_ids": [1, 2, 3]
}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "marked_count": 3,
    "notification_ids": [1, 2, 3],
    "timestamp": "2025-10-18T11:39:53.054Z"
  },
  "message": "Notifications marked as read successfully"
}
```

---

## ❌ Error Handling

### Error Response Format
Semua error mengikuti format yang sama:

```json
{
  "success": false,
  "error": "Error message",
  "code": "ERROR_CODE",
  "message": "Detailed error message"
}
```

### Common Error Codes

| Code | Status | Description |
|------|--------|-------------|
| `MISSING_CREDENTIALS` | 400 | Google token dan email diperlukan |
| `TOKEN_MISSING` | 401 | Access token tidak ada |
| `TOKEN_INVALID` | 401 | Token tidak valid atau expired |
| `MISSING_PARAMETERS` | 400 | Parameter yang diperlukan tidak ada |
| `INVALID_ACTION` | 400 | Action tidak valid |
| `MISSING_DOOR_ID` | 400 | Door ID diperlukan |
| `CAPTURE_NOT_FOUND` | 404 | Camera capture tidak ditemukan |
| `USER_NOT_FOUND` | 404 | User tidak ditemukan |
| `MISSING_REQUIRED_FIELDS` | 400 | Field yang diperlukan tidak ada |

### HTTP Status Codes

| Status | Description |
|--------|-------------|
| 200 | Success |
| 201 | Created |
| 400 | Bad Request |
| 401 | Unauthorized |
| 404 | Not Found |
| 500 | Internal Server Error |

---

## 🧪 Testing

### Menjalankan API Testing

1. **Install dependencies:**
```bash
npm install
```

2. **Start mock API server:**
```bash
npm start
```

3. **Run testing (di terminal baru):**
```bash
npm run test-api
```

### Test Coverage

Testing mencakup:
- ✅ Authentication flow
- ✅ Door control operations
- ✅ User management CRUD
- ✅ Camera operations
- ✅ History retrieval
- ✅ Notifications management
- ✅ Error handling
- ✅ Query parameters
- ✅ Pagination

---

## 📊 Sample Data

Mock API menggunakan sample data yang realistic dengan:

### Data Structure
- **Users**: 15 users dengan data Indonesia
- **Doors**: 10 pintu dengan lokasi kampus
- **Access Logs**: 100+ riwayat akses
- **Camera Captures**: 40+ foto kamera
- **Notifications**: 25+ notifikasi
- **User-Door Access**: Relasi many-to-many

### Data Features
- ✅ **Indonesian names** dan lokasi kampus
- ✅ **Realistic timestamps** (30 hari terakhir)
- ✅ **Random data generation** dengan Faker.js
- ✅ **Nested relations** untuk response yang lengkap
- ✅ **Consistent data** antar endpoint

### Regenerate Sample Data
```bash
npm run generate-data
```

---

## 🚀 Quick Start

1. **Clone dan install:**
```bash
cd backend/mock-api
npm install
```

2. **Generate sample data:**
```bash
npm run generate-data
```

3. **Start server:**
```bash
npm start
```

4. **Test API:**
```bash
npm run test-api
```

5. **API siap digunakan di:**
```
http://localhost:3000/api
```

---

## 📞 Support

Untuk pertanyaan atau masalah:
1. Periksa error codes di dokumentasi
2. Pastikan server berjalan di port 3000
3. Verifikasi sample data sudah ter-generate
4. Check network connection dan CORS settings

**Happy Coding! 🎯**

