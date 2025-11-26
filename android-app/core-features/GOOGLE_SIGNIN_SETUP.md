# Google Sign-In Setup Instructions

## 🔧 Setup Google Console

### 1. Create Google Cloud Project
1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project or select existing one
3. Enable **Google Sign-In API**

### 2. Configure OAuth 2.0 Client ID
1. Go to **APIs & Services** > **Credentials**
2. Click **Create Credentials** > **OAuth 2.0 Client ID**
3. Select **Android** as application type
4. Fill in the following data:
   - **Package name**: `com.authentic.smartdoor`
   - **SHA-1 fingerprint**: `8D:32:48:2E:98:64:C2:2C:90:85:01:9D:61:6E:AD:2D:88:9D:8C:6F`

### 3. Download google-services.json
1. After creating the OAuth client, download the `google-services.json` file
2. Replace the existing file in `android-app/core-features/app/google-services.json`

## 📱 Current Configuration

### SHA-1 Fingerprint
```
8D:32:48:2E:98:64:C2:2C:90:85:01:9D:61:6E:AD:2D:88:9D:8C:6F
```

### Package Name
```
com.authentic.smartdoor
```

### Current Client ID (Web)
```
904749622966-5u5875kdik0vir2v8monrjja972f8ud0.apps.googleusercontent.com
```

## 🚨 Important Notes

1. **Replace the google-services.json** with the one downloaded from Google Console
2. **Update the Client ID** in `strings.xml` if needed
3. **Clean and rebuild** the project after configuration changes
4. **Test on a real device** for proper Google Sign-In functionality

## 🔍 Troubleshooting

### Error 10:10 - DEVELOPER_ERROR
- ✅ **Fixed**: Added Google Services plugin
- ✅ **Fixed**: Created google-services.json
- ✅ **Fixed**: Updated AndroidManifest.xml
- ✅ **Fixed**: Configured SHA-1 fingerprint

### Next Steps
1. Complete Google Console setup
2. Download proper google-services.json
3. Test Google Sign-In functionality
