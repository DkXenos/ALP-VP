# SideQuest - Gamified Freelance Platform

A modern Android application built with Jetpack Compose that gamifies the freelance experience with XP, levels, and bounties.

## 🎯 Overview

SideQuest is a gamified freelance application where:
- **Talents** can browse and apply for bounties (freelance jobs), earn XP, level up, and build their portfolio
- **Companies** can post bounties, review applicants, and manage submissions

## 🛠 Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose with Material3
- **Architecture**: MVVM (Model-View-ViewModel)
- **Navigation**: androidx.navigation.compose
- **Theme**: Dark Mode / Cyberpunk Aesthetic
  - Deep Navy Background: `#121212`
  - Neon Blue Primary: `#2A4DDC`
  - Neon Green Secondary: `#00C853`

## 📁 Project Structure

```
app/src/main/java/com/jason/alp_vp/
├── model/                  # Data classes
│   ├── User.kt            # User model with role (Talent/Company)
│   ├── Bounty.kt          # Bounty/job postings
│   ├── Application.kt     # Application to bounties
│   ├── Submission.kt      # Work submissions
│   └── ForumPost.kt       # Forum posts
│
├── repository/            # Data layer
│   └── MockRepository.kt  # Mock data source (simulates backend)
│
├── viewmodel/            # State management
│   ├── AuthViewModel.kt
│   ├── BountyViewModel.kt
│   ├── ApplicationViewModel.kt
│   ├── SubmissionViewModel.kt
│   ├── ForumViewModel.kt
│   └── ProfileViewModel.kt
│
├── navigation/           # Navigation setup
│   ├── Screen.kt         # Screen routes
│   └── NavigationGraph.kt
│
├── ui/
│   ├── screens/          # Composable screens
│   │   ├── AuthScreen.kt
│   │   ├── BountiesScreen.kt
│   │   ├── BountyDetailsScreen.kt
│   │   ├── BountyApplyScreen.kt
│   │   ├── BountySubmissionScreen.kt
│   │   ├── UserRegisteredScreen.kt
│   │   ├── ForumsScreen.kt
│   │   └── ProfileDashboardScreen.kt
│   │
│   └── theme/           # Theme configuration
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
│
└── MainActivity.kt      # Entry point
```

## 🎮 Features & Screens

### 1. **AuthScreen**
- Login/Register tabs
- Role selection: Talent vs Company
- Email and password inputs
- Quick demo login credentials displayed

**Demo Credentials:**
- Talent: `talent@test.com` (any password)
- Company: `company@test.com` (any password)

### 2. **BountiesScreen** (Home)
- Search field for filtering bounties
- Filter chips: All, Novice, Expert
- Bounty cards showing:
  - Title, company name
  - Price in Rupiah (Rp)
  - XP reward
  - Level requirement badge
  - Event indicator
- Bottom navigation bar (Home, Forums, Profile)

### 3. **BountyDetailsScreen**
- Full bounty description
- Requirements list
- Deadline information
- Price, XP, and level requirements
- **For Talents**: "Apply Now" button → navigates to BountyApplyScreen
- **For Companies**: "View Applicants" button → navigates to UserRegisteredScreen

### 4. **BountyApplyScreen** (Talent Only)
- Cover letter/note input field
- Submit application button
- Loading state during submission

### 5. **BountySubmissionScreen** (Talent Only)
- File upload placeholder with dashed border
- Submission notes input
- "Mark as Done" button
- Mock file upload functionality

### 6. **UserRegisteredScreen** (Company Only)
- List of applicants for a bounty
- Each applicant shows:
  - Name, level, XP
  - Cover letter
  - Status badge (Pending/Accepted/Rejected)
- Accept/Reject action buttons

### 7. **ForumsScreen**
- Recruitment event banner with countdown timer
- Community feed with posts
- Each post includes:
  - Author avatar and name
  - Post content
  - Upvote count
  - Timestamp

### 8. **ProfileDashboardScreen**
- User avatar (generated from first letter)
- Level display
- XP progress bar
- **Skills Radar Chart**: Custom Canvas-drawn polygon chart showing 5 skills:
  - Coding, Design, Writing, Marketing, Management
- Wallet balance display
- Portfolio grid (6 placeholder items)

## 🔧 Mock Data Repository

The `MockRepository` provides:
- 2 pre-configured users (1 Talent, 1 Company)
- 5 sample bounties with varying levels and rewards
- 3 sample applications
- 4 forum posts
- Authentication simulation with delay
- All CRUD operations for bounties, applications, and submissions

## 🎨 Design System

### Colors
```kotlin
DeepNavy = #121212      // Background
NeonBlue = #2A4DDC      // Primary
NeonGreen = #00C853     // Secondary
DarkGray = #1E1E1E      // Surface
LightGray = #B0B0B0     // Text secondary
White = #FFFFFF         // Text primary
```

### Components
- Rounded corners (8-12dp)
- Card elevation (2-4dp)
- Cyberpunk aesthetic with neon accents
- Progress bars for XP tracking
- Badge components for levels and status
- Custom radar chart using Canvas API

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog or later
- JDK 11
- Android SDK 28+

### Installation

1. Clone the repository
2. Open the project in Android Studio
3. Sync Gradle dependencies
4. Run the app on an emulator or device

### Build Commands
```bash
# Debug build
./gradlew assembleDebug

# Release build
./gradlew assembleRelease

# Run tests
./gradlew test
```

## 📱 Navigation Flow

```
AuthScreen
    ↓
BountiesScreen (Home)
    ├── → BountyDetailsScreen
    │       ├── → BountyApplyScreen (Talent)
    │       ├── → BountySubmissionScreen (Talent)
    │       └── → UserRegisteredScreen (Company)
    ├── → ForumsScreen
    └── → ProfileDashboardScreen
```

## 🎯 User Roles

### Talent (Freelancer)
- Browse and search bounties
- Apply to bounties with cover letter
- Submit completed work
- Track XP and level progression
- View personal portfolio and skills

### Company
- Post bounties (mock data)
- View applicants
- Accept/Reject applications
- Manage bounty postings

## 🔮 Future Enhancements

Since this is a frontend-only implementation with mock data, future improvements could include:
- Real backend API integration
- Real-time notifications
- Chat system between talents and companies
- Payment integration
- File upload functionality
- Advanced filtering and search
- User reviews and ratings
- Achievement system
- Leaderboards

## 📝 Notes

- All data is mocked and persists only in memory
- No actual backend API calls are made
- File uploads are simulated
- Authentication is simulated with mock users

## 👨‍💻 Development

The project follows clean architecture principles:
- **Model**: Data classes representing domain entities
- **Repository**: Data source abstraction (currently mock)
- **ViewModel**: Business logic and state management
- **View**: Composable UI components

All ViewModels use Kotlin Flows for reactive state management, and the UI automatically recomposes when state changes.

## 📄 License

This project is created for educational purposes.

