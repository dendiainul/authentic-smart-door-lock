# Getting Started - Development Tutorial

## Prerequisites untuk Semua Tim

### Software yang Dibutuhkan
```
1. Git (version control)
2. Android Studio (untuk mobile development)
3. Node.js & npm (untuk backend development)
4. Code editor (VS Code recommended)
5. Postman (untuk API testing)
```

### Account yang Diperlukan
- GitHub account (sudah diundang ke repository)
- Google Cloud Console (untuk OAuth setup)
- Discord/Slack untuk komunikasi tim

## Setup Initial untuk Semua Tim

### 1. Clone Repository
```bash
# Clone repository ke local machine
git clone https://github.com/dendiainul/authentic-smart-door-lock.git

# Masuk ke folder project
cd authentic-smart-door-lock

# Check struktur folder
ls -la
```

### 2. Git Workflow Setup
```bash
# Set git config
git config user.name "Nama Kamu"
git config user.email "email@gmail.com"

# Check remote origin
git remote -v

# Create development branch
git checkout -b dev-[nama-tim]-[nama-kamu]
# Example: git checkout -b dev-pagi-fizzxyz
```

## Panduan Khusus Tim Pagi

### Folder Kerja
```
android-app/core-features/     # Main Android development
backend/mock-api/              # Backend testing
android-app/shared/            # Shared components (koordinasi!)
```

### Development Setup Tim Pagi

#### Android Development (4 orang)
```bash
# Masuk ke folder Android
cd android-app/core-features/

# Setup Android project structure
mkdir -p app/src/main/{java,res,assets}
mkdir -p app/src/test/java
mkdir -p app/src/androidTest/java

# Create basic Kotlin files structure
mkdir -p app/src/main/java/com/authentic/smartdoor/{ui,data,domain,utils}
```

#### Mock API Development (2 orang)
```bash
# Masuk ke folder mock API
cd backend/mock-api/

# Initialize Node.js project
npm init -y

# Install dependencies
npm install express cors socket.io faker uuid jsonwebtoken
npm install -D nodemon jest

# Create folder structure
mkdir -p {routes,controllers,middleware,utils,data}
touch server.js package.json
```

### Branching Strategy Tim Pagi
```
main
├── dev-pagi-auth           # Authentication features
├── dev-pagi-dashboard      # Dashboard UI
├── dev-pagi-camera         # Camera integration
├── dev-pagi-storage        # Local storage
├── dev-pagi-mock-api       # Mock backend
└── dev-pagi-testing        # Testing & QA
```

## Panduan Khusus Tim Malam

### Folder Kerja
```
android-app/advanced-features/  # Advanced Android features
backend/production-api/         # Production backend
web-admin/                      # Web dashboard
android-app/shared/             # Shared components (koordinasi!)
```

### Development Setup Tim Malam

#### Android Advanced Features (3 orang)
```bash
# Masuk ke folder advanced features
cd android-app/advanced-features/

# Setup folder structure
mkdir -p notifications/{push,local,management}
mkdir -p analytics/{dashboard,reports,charts}
mkdir -p integration/{api,realtime,sync}
```

#### Web Admin Development (3 orang)
```bash
# Masuk ke folder web admin
cd web-admin/

# Initialize React project
npx create-react-app . --template typescript
# atau
npm create vue@latest . -- --typescript

# Install additional dependencies
npm install @mui/material @mui/icons-material
npm install axios socket.io-client chart.js
npm install react-router-dom redux @reduxjs/toolkit
```

#### Production API (Backend team)
```bash
# Masuk ke folder production API
cd backend/production-api/

# Initialize Node.js project
npm init -y

# Install dependencies
npm install express pg socket.io bcrypt jsonwebtoken
npm install cors helmet morgan winston multer
npm install -D nodemon jest supertest

# Create folder structure
mkdir -p {routes,controllers,middleware,models,utils,migrations,seeds}
```

### Branching Strategy Tim Malam
```
main
├── dev-malam-notifications    # Push notifications
├── dev-malam-analytics       # Analytics & reports
├── dev-malam-web-admin      # Web dashboard
├── dev-malam-backend        # Production API
├── dev-malam-database       # Database schema
└── dev-malam-integration    # System integration
```

## Shared Components Workflow

### Koordinasi untuk Shared Folder
```bash
# Sebelum modify shared components
cd android-app/shared/

# Pull latest changes
git pull origin main

# Create branch khusus shared
git checkout -b shared-[feature-name]

# Development...
# Test di kedua environment (pagi & malam)

# Koordinasi di Discord/Slack:
# "@everyone akan update shared component [nama], ada yang berkeberatan?"
# Tunggu approval dari kedua tim lead

# Merge ke main setelah approval
```

### Shared Components Structure
```
android-app/shared/
├── components/
│   ├── ui/              # Button, Card, Input components
│   ├── navigation/      # NavBar, Drawer, BottomNav
│   └── common/          # Loading, Error, Empty states
├── utils/
│   ├── api/            # API client, interceptors
│   ├── auth/           # Authentication helpers
│   └── helpers/        # Date, format, validation utils
├── constants/
│   ├── api.js          # API endpoints
│   ├── colors.js       # Color schemes
│   └── strings.js      # Indonesian localization
└── theme/
    ├── material.js     # Material Design 3 config
    └── typography.js   # Font & text styles
```

## API Development Coordination

### API Contract Agreement
```javascript
// Kedua tim harus sepakat dengan API structure ini:

// Authentication
POST /api/auth/google
Response: { token: string, user: User, expires_in: number }

// Door Control  
GET /api/door/status
Response: { locked: boolean, battery: number, last_update: timestamp }

POST /api/door/control
Body: { action: 'lock' | 'unlock', user_id: string }
Response: { success: boolean, message: string }

// Dan seterusnya...
```

### Testing API Integration
```bash
# Tim Pagi test dengan mock API
cd backend/mock-api/
npm start  # Running di localhost:3001

# Tim Malam test dengan production API  
cd backend/production-api/
npm run dev  # Running di localhost:3000

# Android app bisa switch environment:
# BuildConfig.API_BASE_URL = pagi ? "localhost:3001" : "localhost:3000"
```

## Communication Protocol

### Daily Workflow
```
1. Morning: Check Discord untuk updates
2. Pull latest changes: git pull origin main
3. Work di branch masing-masing
4. End of day: Push changes dan update progress
5. Tag team jika butuh koordinasi shared components
```

### Weekly Integration
```
Setiap Jumat jam 15:00:
1. Demo progress masing-masing tim
2. Integration testing
3. Resolve conflicts dan koordinasi
4. Plan untuk minggu berikutnya
5. Update documentation
```

## Troubleshooting Common Issues

### Git Conflicts di Shared Components
```bash
# Jika ada conflict di shared folder:
git status  # Check files yang conflict
git pull origin main  # Get latest changes
# Edit files manually untuk resolve conflicts
git add .
git commit -m "Resolve shared components conflict"
git push origin [branch-name]
```

### Environment Issues
```bash
# Android build issues:
# 1. Clean project: Build > Clean Project
# 2. Invalidate caches: File > Invalidate Caches
# 3. Check Gradle version compatibility

# Node.js issues:
# 1. Clear npm cache: npm cache clean --force  
# 2. Delete node_modules: rm -rf node_modules && npm install
# 3. Check Node.js version compatibility
```

### API Integration Issues
```bash
# CORS issues (common saat testing):
# Backend harus enable CORS untuk frontend domains

# Authentication issues:
# Check token expiry dan refresh mechanism
# Verify Google OAuth credentials

# Network issues:
# Check if API server running
# Verify endpoint URLs dan HTTP methods
```

## Checklist Sebelum Mulai Development

### Tim Pagi
- [ ] Android Studio installed dan configured
- [ ] Node.js installed untuk mock API
- [ ] Clone repository dan setup git config
- [ ] Create development branches
- [ ] Setup mock API server
- [ ] Read API contract documentation
- [ ] Join Discord/Slack group

### Tim Malam  
- [ ] Android Studio installed dan configured
- [ ] Node.js installed untuk backend
- [ ] PostgreSQL installed dan configured
- [ ] Clone repository dan setup git config
- [ ] Create development branches
- [ ] Setup React development environment
- [ ] Setup production database schema
- [ ] Join Discord/Slack group

### Shared Requirements
- [ ] Understand Git workflow dan branching strategy
- [ ] Read shared components guidelines
- [ ] Understand coordination protocols
- [ ] Setup communication tools
- [ ] Review project timeline dan milestones
