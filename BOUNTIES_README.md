# BountiesPage - Implementation Guide

## Overview
The BountiesPage is the homepage that fetches and displays bounties from your local Express API backend.

## Architecture

### Network Layer
- **ApiService** ([network/ApiService.kt](app/src/main/java/com/jason/alp_vp/network/ApiService.kt)): Retrofit interface with `getBounties()` endpoint
- **RetrofitInstance** ([network/RetrofitInstance.kt](app/src/main/java/com/jason/alp_vp/network/RetrofitInstance.kt)): Singleton with base URL `http://10.0.2.2:3000/` for Android Emulator

### Data Layer
- **BountyRepository** ([repository/BountyRepository.kt](app/src/main/java/com/jason/alp_vp/repository/BountyRepository.kt)): Handles API calls and returns `Result<List<Bounty>>`

### Presentation Layer
- **HomeViewModel** ([viewmodel/HomeViewModel.kt](app/src/main/java/com/jason/alp_vp/viewmodel/HomeViewModel.kt)): Exposes `StateFlow<BountyUiState>` with Loading/Success/Error states
- **BountiesPage** ([ui/screens/BountiesPage.kt](app/src/main/java/com/jason/alp_vp/ui/screens/BountiesPage.kt)): Main screen with LazyColumn of BountyCards

## Features

### Authentication
- Authorization header `Bearer test-token` is automatically added to all requests

### UI States
- **Loading**: Shows circular progress indicator with "Loading Bounties..." text
- **Success**: Displays bounties in a scrollable list
- **Error**: Shows error message with retry button

### BountyCard Design
- **Dark Mode Cyberpunk Theme**:
  - Navy Blue background (gradient from `#071422` to `#0A1929`)
  - Neon Green accents (`#00FF41`)
  - Card background: `#0F2234`
  - Border: Semi-transparent neon green

- **Card Layout**:
  - Title (bold, neon green)
  - Company name (white with opacity)
  - Deadline
  - Reward chips (XP in green, Money in lighter green)
  - Status badge (color-coded: active=green, completed=blue, expired=red)

## Usage

### In Your Navigation
```kotlin
@Composable
fun AppNavigation() {
    NavHost(navController = rememberNavController(), startDestination = "bounties") {
        composable("bounties") {
            BountiesPage()
        }
    }
}
```

### Backend API Requirements
Your Express API should have an endpoint:

```
GET /bounties
Authorization: Bearer test-token
```

Expected JSON response:
```json
[
  {
    "id": "1",
    "title": "Fix Authentication Bug",
    "company": "TechCorp",
    "deadline": "2025-12-31",
    "rewardXp": 500,
    "rewardMoney": 1000,
    "status": "active"
  }
]
```

## Dependencies Added
- `com.squareup.retrofit2:retrofit:2.9.0`
- `com.squareup.retrofit2:converter-gson:2.9.0`
- `com.squareup.okhttp3:logging-interceptor:4.11.0`

## Testing

### 1. Start your Express backend on port 3000
```bash
cd your-backend-folder
npm start
```

### 2. Run the Android app in the emulator
The app will automatically connect to `http://10.0.2.2:3000/` which maps to `localhost:3000` on your machine.

### 3. Pull to refresh
The screen includes a retry button in the error state to reload bounties.

## Troubleshooting

### Connection Issues
- Ensure your backend is running on port 3000
- Check Android Logcat for network errors
- Verify the Authorization header is being sent

### JSON Mapping Issues
If your backend uses different field names, add `@SerializedName` annotations to the Bounty model:
```kotlin
data class Bounty(
    @SerializedName("_id") val id: String,
    // ... other fields
)
```

## Color Palette
- Dark Navy: `#071422`
- Navy Blue: `#0A1929`
- Card Background: `#0F2234`
- Neon Green: `#00FF41`
- Neon Green Dim: `#00CC33`
