# SideQuest - Quick Start Guide

## 🚀 Running the Application

### Option 1: Android Studio (Recommended)

1. **Open the project:**
   ```
   File → Open → Select /Users/jaysn/Documents/VP/ALPVP
   ```

2. **Wait for Gradle sync to complete**

3. **Select a device:**
   - Use an Android emulator (API 28+)
   - Or connect a physical device with USB debugging enabled

4. **Click the Run button (▶️)** or press `Shift + F10`

### Option 2: Command Line

```bash
cd /Users/jaysn/Documents/VP/ALPVP

# Build the debug APK
./gradlew assembleDebug

# Install on connected device
./gradlew installDebug

# Or build and install in one command
./gradlew installDebug
```

## 🎮 Testing the Application

### 1. Authentication Screen
**Test Login:**
- Email: `talent@test.com` (or `company@test.com`)
- Password: any password works
- Select role: Talent or Company
- Click "Login"

**Test Register:**
- Switch to "Register" tab
- Fill in name, email, password
- Select role
- Click "Register"

### 2. Bounties Screen (Talent Flow)

**Browse Bounties:**
- View 5 pre-loaded bounties
- Use search bar to filter by title/company/description
- Click filter chips: "All", "Novice", "Expert"

**Apply for Bounty:**
1. Click on any bounty card
2. Read the details
3. Click "Apply Now" (Talent only)
4. Write a cover letter
5. Click "Submit Application"
6. Success! Navigate back to see confirmation

**Submit Work:**
1. Navigate to BountySubmission screen (via navigation)
2. See the file upload placeholder
3. Add submission notes
4. Click "Mark as Done"

### 3. Bounties Screen (Company Flow)

**Manage Applications:**
1. Login as company (`company@test.com`)
2. Click on "Build E-Commerce Mobile App" bounty
3. Click "View Applicants"
4. See 3 applicants with their details
5. Click ✓ to Accept or ✗ to Reject

### 4. Forums Screen

**View Community:**
- Click "Forums" in bottom navigation
- See the "Recruitment Event" banner with countdown
- Browse 4 community posts
- View upvote counts and timestamps

### 5. Profile Screen

**View Profile:**
- Click "Profile" in bottom navigation
- See user avatar with first letter
- View Level 12 badge
- Check XP progress bar (8450 / 13000)
- **See the Skills Radar Chart** with 5 skills
- Check wallet balance (Rp 2,500,000)
- Browse 6 portfolio items in grid

## 🎯 Key Features to Test

### Navigation Flow
```
Auth → Bounties → Details → Apply → Back
                           ↓
                        Forums
                           ↓
                        Profile
```

### Role-Based Features

**As Talent:**
- ✅ Can apply for bounties
- ✅ Can submit work
- ✅ See "Apply Now" button
- ✅ Track XP and level

**As Company:**
- ✅ Can view applicants
- ✅ Can accept/reject applications
- ✅ See "View Applicants" button
- ✅ Manage bounties

### UI Features to Notice

**Cyberpunk Theme:**
- Deep navy background (#121212)
- Neon blue primary color (#2A4DDC)
- Neon green accents (#00C853)
- Glowing effects on cards

**Gamification:**
- XP rewards on bounties
- Level badges everywhere
- Progress bars for XP
- Skills radar chart

**Components:**
- Filter chips with selection
- Bottom navigation bar
- Status badges (Pending/Accepted/Rejected)
- Event bounty indicators
- Upvote buttons in forums

## 🔍 Testing Scenarios

### Scenario 1: Talent Journey
1. Login as talent
2. Search for "mobile" bounties
3. Apply to "Build E-Commerce Mobile App"
4. Check profile to see XP progress
5. Visit forums to see community

### Scenario 2: Company Journey
1. Login as company
2. View "Build E-Commerce Mobile App" bounty
3. See 3 applicants
4. Accept "Mike Rodriguez" (highest XP)
5. Reject "Emma Davis"

### Scenario 3: Explore Features
1. Test search functionality
2. Test filter chips
3. Navigate between all screens
4. Check the radar chart animation
5. View portfolio grid

## 📱 Expected Behavior

### What Works:
- ✅ All navigation transitions
- ✅ Search and filters
- ✅ Form submissions
- ✅ State management
- ✅ Loading states
- ✅ Error handling
- ✅ Role-based rendering
- ✅ Mock data persistence (in memory)

### What's Simulated:
- 🔄 Authentication (no real backend)
- 🔄 File uploads (placeholder only)
- 🔄 Data persistence (clears on app restart)
- 🔄 Network delays (simulated with coroutine delays)

## 🐛 Troubleshooting

### Build Issues:
```bash
# Clean and rebuild
./gradlew clean build

# Check for Gradle sync
# In Android Studio: File → Sync Project with Gradle Files
```

### APK Not Installing:
```bash
# Uninstall existing version first
adb uninstall com.jason.alp_vp

# Then install
./gradlew installDebug
```

### Navigation Issues:
- If navigation seems stuck, restart the app
- All navigation should work with back button

## 🎨 UI Elements to Appreciate

1. **Custom Radar Chart** - Uses Canvas API to draw polygon
2. **Progress Bars** - Animated XP progression
3. **Card Designs** - Elevated with rounded corners
4. **Badge Components** - For levels and status
5. **Bottom Navigation** - Smooth transitions
6. **Filter Chips** - Material3 selection
7. **Form Validation** - Disabled states when invalid

## 📊 Mock Data Reference

**Bounties:**
- Build E-Commerce Mobile App (Novice, Lvl 10, 15M, 500 XP)
- Design Company Branding Kit (Expert, Lvl 5, 8.5M, 350 XP) [EVENT]
- Backend API Development (Expert, Lvl 15, 20M, 750 XP)
- Content Writing - Tech Blog (Novice, Lvl 3, 5M, 200 XP)
- UI/UX Design for SaaS Platform (Novice, Lvl 8, 12M, 450 XP) [EVENT]

**Users:**
- Talent: Alex Chen (Lvl 12, 8450 XP, Rp 2.5M)
- Company: TechCorp Industries (Lvl 25, 15000 XP, Rp 50M)

**Skills (Talent):**
- Coding: 85
- Design: 60
- Writing: 75
- Marketing: 50
- Management: 65

## 🎉 Enjoy Testing SideQuest!

This is a complete frontend implementation with realistic mock data. All interactions are smooth, and the UI follows Material3 design guidelines with a cyberpunk twist!

**Happy Testing! 🚀**

