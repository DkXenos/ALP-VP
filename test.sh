#!/bin/bash

# Simple Test Script for ALPVP Android App

echo "🚀 ALPVP Testing Script"
echo "======================"
echo ""

# Check if we're in the right directory
if [ ! -f "gradlew" ]; then
    echo "❌ Error: Please run this script from the project root directory"
    echo "   cd /Users/jaysn/Documents/VP/ALPVP"
    exit 1
fi

echo "📋 Choose what to test:"
echo ""
echo "1) Test app with DUMMY DATA (safe, no backend needed)"
echo "2) Test BACKEND connection (requires backend running)"
echo "3) Check current configuration"
echo "4) View app logs (requires device/emulator connected)"
echo ""
read -p "Enter choice (1-4): " choice

case $choice in
    1)
        echo ""
        echo "🔨 Building and installing app with DUMMY DATA..."
        echo "   This will test your UI without backend"
        echo ""
        ./gradlew clean installDebug
        echo ""
        echo "✅ Done! Check your device/emulator"
        echo "   App should show bounties from local data"
        ;;

    2)
        echo ""
        echo "⚠️  Make sure:"
        echo "   1. Your backend is running (npm run dev)"
        echo "   2. BASE_URL is updated in ApiConfig.kt"
        echo "   3. USE_API_DATA = true in BountyRepository.kt"
        echo ""
        read -p "Ready? (y/n): " ready

        if [ "$ready" = "y" ]; then
            echo ""
            echo "🔨 Building app with API MODE..."
            ./gradlew clean installDebug
            echo ""
            echo "✅ Done! Check your device/emulator"
            echo "   Watch Logcat for API calls (filter: OkHttp)"
        else
            echo "Cancelled"
        fi
        ;;

    3)
        echo ""
        echo "📝 Current Configuration:"
        echo ""
        echo "BASE_URL:"
        grep "const val BASE_URL" app/src/main/java/com/jason/alp_vp/data/api/ApiConfig.kt
        echo ""
        echo "API Mode:"
        grep "USE_API_DATA" app/src/main/java/com/jason/alp_vp/data/repository/BountyRepository.kt | head -1
        echo ""
        ;;

    4)
        echo ""
        echo "📱 Starting log viewer..."
        echo "   Press Ctrl+C to stop"
        echo "   Showing OkHttp logs (API calls)"
        echo ""
        adb logcat | grep -E "OkHttp|Bounty"
        ;;

    *)
        echo "Invalid choice"
        exit 1
        ;;
esac

echo ""
echo "Done! 🎉"

