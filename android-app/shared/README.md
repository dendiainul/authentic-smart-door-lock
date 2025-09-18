# Shared Components

## Komponen Bersama untuk Kedua Tim

### UI Components
- Material Design 3 theme
- Common buttons, cards, layouts
- Loading states dan error handling
- Navigation components

### Utility Functions  
- API client configuration
- Authentication helpers
- Date/time formatting (WIB timezone)
- Image/video processing utils
- Local storage management

### Constants & Configuration
- API endpoints
- Color schemes
- Typography styles
- Indonesian localization strings

## File Structure
```
shared/
├── components/
│   ├── ui/
│   ├── navigation/
│   └── common/
├── utils/
│   ├── api/
│   ├── auth/
│   └── helpers/
├── constants/
├── theme/
└── localization/
```

## Usage Guidelines
- Tim manapun yang modify shared components harus koordinasi dulu
- Testing required untuk semua shared components
- Documentation wajib untuk setiap komponen baru
- Code review dari kedua tim lead sebelum merge

## Coordination Rules
1. Discuss di group chat sebelum modify
2. Create issue di GitHub untuk changes
3. Tag kedua tim untuk review
4. Test di kedua environment (pagi & malam)
