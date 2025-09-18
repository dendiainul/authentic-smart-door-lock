# Project Documentation

## Authentic Smart Door Lock - PBL Project

### Gambaran Umum
Sistem Smart Door Authentication dengan aplikasi mobile Android dan web admin dashboard yang terintegrasi.

### Pembagian Tim

#### Tim Pagi (6 orang)
**Focus: Mobile-First Development**
- Android App Core Features (4 orang)
- Mock Backend API untuk testing (2 orang)
- Spesialisasi: Mobile development mendalam

#### Tim Malam (6 orang)  
**Focus: Full-Stack Integration**
- Android App Advanced Features (3 orang)
- Web Admin Dashboard + Production API (3 orang)
- Spesialisasi: Full-stack development

### Teknologi Stack
```
Mobile: Kotlin + Jetpack Compose + Material Design 3
Web: React.js + TypeScript + Material-UI
Backend: Node.js + Express + PostgreSQL
Real-time: Socket.io + WebSocket
Authentication: Google OAuth 2.0 + JWT
```

### Panduan Kolaborasi

#### Repository Structure
```
authentic-smart-door-lock/
├── android-app/
│   ├── core-features/        # Tim Pagi
│   ├── advanced-features/    # Tim Malam
│   └── shared/              # Komponen bersama
├── backend/
│   ├── mock-api/            # Tim Pagi
│   └── production-api/      # Tim Malam
├── web-admin/               # Tim Malam
└── documentation/           # Project docs
```

#### Workflow Kolaborasi
1. **Setup Awal (Week 1)**
   - API contract agreement
   - UI design system
   - Database schema
   - Git branching strategy

2. **Development (Week 2-12)**
   - Feature branches per tim
   - Weekly integration meetings
   - Bi-weekly demos
   - Cross-team code reviews

3. **Integration (Week 13-16)**
   - System integration testing
   - Bug fixes & optimization
   - Documentation completion
   - Final presentation prep

#### Communication Tools
- GitHub: Repository dan issue tracking
- Discord/Slack: Daily communication
- Google Meet: Weekly meetings
- Figma: UI/UX collaboration

#### Coordination Rules
- Shared components: koordinasi sebelum modifikasi
- API changes: notify semua tim
- Database schema: agreement dari kedua tim
- UI design: follow design system
- Testing: cross-team integration testing

### Timeline Project (16 Minggu)
```
Week 1-2:   Setup & initial development
Week 3-6:   Core features development
Week 7-10:  Advanced features & integration
Week 11-13: System integration & testing
Week 14-16: Final polish & presentation
```

### Deliverables
- Android Application (lengkap)
- Web Admin Dashboard
- Production Backend API
- Testing Environment
- Project Documentation
- Final Presentation
