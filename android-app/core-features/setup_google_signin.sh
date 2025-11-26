#!/bin/bash

echo "=== SecureDoor Google Sign-In Setup ==="
echo ""

# Check if keytool exists
if command -v keytool &> /dev/null; then
    echo "✅ Keytool found"

    # Get debug keystore SHA-1
    echo ""
    echo "🔑 Debug Keystore SHA-1 Fingerprint:"
    echo "----------------------------------------"

    if [ -f ~/.android/debug.keystore ]; then
        keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android | grep SHA1
    elif [ -f "$HOME/.android/debug.keystore" ]; then
        keytool -list -v -keystore "$HOME/.android/debug.keystore" -alias androiddebugkey -storepass android -keypass android | grep SHA1
    else
        echo "❌ Debug keystore not found at ~/.android/debug.keystore"
        echo "🔧 Try running Android Studio first to generate it"
    fi

else
    echo "❌ Keytool not found. Please install Java JDK"
fi

echo ""
echo "📋 Setup Steps:"
echo "1. Copy SHA-1 fingerprint above"
echo "2. Go to https://console.cloud.google.com/"
echo "3. Create new project or select existing"
echo "4. Enable Google Sign-In API"
echo "5. Create OAuth 2.0 Client ID (Android type)"
echo "6. Add package name: com.authentic.smartdoor"
echo "7. Add SHA-1 fingerprint from above"
echo "8. Copy Client ID to strings.xml"
echo ""
echo "📁 Package name: com.authentic.smartdoor"
echo "🏗️ Application ID: com.authentic.smartdoor"