# Authentic Smart Door Lock

**PBL Project: Smart Door Authentication System with Mobile App and Web Admin Dashboard**

## Deskripsi Project
Sistem pintu cerdas dengan autentikasi face recognition, dilengkapi aplikasi mobile Android dan web admin dashboard untuk monitoring dan kontrol akses.

## Tim Development

### Tim Pagi - Mobile-First Development (6 orang)
**GitHub Users:** fizzxyz, afifahfathonah, putriput15, navitadamayantisyarif, thariq24, geannnnn

**Tanggung Jawab:**
- Android App Core Features (Authentication, Dashboard, Camera, Storage)
- Mock Backend API untuk testing dan development
- Focus: Mobile development excellence

### Tim Malam - Full-Stack Integration (6 orang)  
**GitHub Users:** Nduee, nabilnaufaldo848, alfnssyyhh, aurelliaazzahra, zahraa2901, nicojw

**Tanggung Jawab:**
- Android App Advanced Features (Notifications, Analytics, Integration)
- Web Admin Dashboard dengan React.js
- Production Backend API dengan Node.js + PostgreSQL
- Focus: Full-stack system integration

## Teknologi Stack

### Mobile Development
- **Language:** Kotlin
- **UI Framework:** Jetpack Compose
- **Design System:** Material Design 3
- **Architecture:** MVVM + Repository Pattern

### Web Development
- **Frontend:** React.js + TypeScript
- **UI Library:** Material-UI / Ant Design
- **State Management:** Redux / Context API

### Backend Development
- **Runtime:** Node.js + Express.js
- **Database:** PostgreSQL
- **Authentication:** Google OAuth 2.0 + JWT
- **Real-time:** Socket.io + WebSocket

## Struktur Repository

```
├── android-app/
│   ├── core-features/      # Tim Pagi - Core Android features
│   ├── advanced-features/  # Tim Malam - Advanced Android features  
│   └── shared/            # Komponen bersama kedua tim
├── backend/
│   ├── mock-api/          # Tim Pagi - Testing API
│   └── production-api/    # Tim Malam - Production API
├── web-admin/             # Tim Malam - Web Dashboard
└── documentation/         # Project documentation
```

## Fitur Utama

### Aplikasi Android
- Google Sign-In authentication
- Door control interface (lock/unlock)
- Live camera streaming
- Access history & analytics
- Push notifications
- User management
- Indonesian localization

### Web Admin Dashboard
- Real-time door monitoring
- User management interface
- Access logs & reports
- Camera management
- System settings & configuration
- Analytics dashboard

### Backend System
- RESTful API architecture
- Real-time WebSocket communication
- Google OAuth integration
- Face recognition data management
- Access logging & analytics
- Notification system

## Getting Started

### Prerequisites
- Android Studio (untuk mobile development)
- Node.js & npm (untuk backend)
- PostgreSQL (untuk production database)
- Git untuk version control

### Installation
1. Clone repository:
   ```bash
   git clone https://github.com/dendiainul/authentic-smart-door-lock.git
   ```

2. Setup development environment sesuai tim masing-masing
3. Baca dokumentasi di folder `documentation/`
4. Follow collaboration guidelines untuk koordinasi antar tim

## Panduan Kolaborasi
- **Communication:** Discord/Slack untuk daily updates
- **Meetings:** Weekly integration sync setiap Jumat
- **Code Review:** Cross-team review untuk shared components  
- **Testing:** Integration testing setiap 2 minggu
- **Documentation:** Update progress di README masing-masing folder

## Timeline Project
- **Week 1-2:** Setup & initial development
- **Week 3-6:** Core features development  
- **Week 7-10:** Advanced features & integration
- **Week 11-13:** System integration testing
- **Week 14-16:** Final polish & presentation

## Kontribusi
Setiap tim member diharapkan:
1. Follow Git workflow yang disepakati
2. Update progress di README folder masing-masing
3. Koordinasi untuk shared components
4. Participate dalam weekly integration meetings
5. Document semua changes dan decisions

---

**Project Supervisor:** Dendi Ainul  
**Institution:** Universitas/Institut [Nama Institusi]  
**Semester:** [Semester & Tahun]
