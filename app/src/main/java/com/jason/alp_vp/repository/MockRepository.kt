package com.jason.alp_vp.repository

import com.jason.alp_vp.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MockRepository {

    // Current logged-in user
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    // Mock Users
    private val mockUsers = mutableListOf(
        User(
            id = "1",
            email = "talent@test.com",
            name = "Alex Chen",
            role = UserRole.TALENT,
            level = 12,
            xp = 8450,
            walletBalance = 2500000.0,
            avatarUrl = "https://i.pravatar.cc/150?img=1",
            skills = mapOf(
                "Coding" to 85,
                "Design" to 60,
                "Writing" to 75,
                "Marketing" to 50,
                "Management" to 65
            )
        ),
        User(
            id = "2",
            email = "company@test.com",
            name = "TechCorp Industries",
            role = UserRole.COMPANY,
            level = 25,
            xp = 15000,
            walletBalance = 50000000.0,
            avatarUrl = "https://i.pravatar.cc/150?img=5"
        )
    )

    // Mock Bounties
    private val _bounties = MutableStateFlow(
        listOf(
            Bounty(
                id = "b1",
                title = "Build E-Commerce Mobile App",
                companyName = "ShopNow Inc",
                description = "We need a modern e-commerce mobile application with payment integration, product catalog, and user authentication. The app should support both Android and iOS platforms.",
                price = 15000000.0,
                xp = 500,
                minLevel = 10,
                status = BountyStatus.OPEN,
                isEvent = false,
                requirements = listOf(
                    "Experience with Flutter or React Native",
                    "Knowledge of REST APIs",
                    "Previous e-commerce projects",
                    "Available for 3 months"
                ),
                deadline = "2025-12-31",
                category = "Novice"
            ),
            Bounty(
                id = "b2",
                title = "Design Company Branding Kit",
                companyName = "StartupXYZ",
                description = "Create a complete branding kit including logo, color palette, typography, and brand guidelines for a tech startup in the AI space.",
                price = 8500000.0,
                xp = 350,
                minLevel = 5,
                status = BountyStatus.OPEN,
                isEvent = true,
                requirements = listOf(
                    "Portfolio with similar work",
                    "Adobe Creative Suite expertise",
                    "Understanding of modern design trends"
                ),
                deadline = "2025-12-20",
                category = "Expert"
            ),
            Bounty(
                id = "b3",
                title = "Backend API Development",
                companyName = "DataFlow Systems",
                description = "Develop a scalable REST API for a data analytics platform. Must include authentication, database design, and comprehensive documentation.",
                price = 20000000.0,
                xp = 750,
                minLevel = 15,
                status = BountyStatus.OPEN,
                isEvent = false,
                requirements = listOf(
                    "Node.js or Python expertise",
                    "Database design skills (PostgreSQL/MongoDB)",
                    "API security best practices",
                    "Docker experience"
                ),
                deadline = "2026-01-15",
                category = "Expert"
            ),
            Bounty(
                id = "b4",
                title = "Content Writing - Tech Blog",
                companyName = "DevMedia",
                description = "Write 10 technical articles about web development trends, each 1500+ words. Topics include React, Node.js, and cloud computing.",
                price = 5000000.0,
                xp = 200,
                minLevel = 3,
                status = BountyStatus.OPEN,
                isEvent = false,
                requirements = listOf(
                    "Strong writing skills",
                    "Technical background",
                    "SEO knowledge"
                ),
                deadline = "2025-12-25",
                category = "Novice"
            ),
            Bounty(
                id = "b5",
                title = "UI/UX Design for SaaS Platform",
                companyName = "CloudBase",
                description = "Design user interface and user experience for a project management SaaS platform. Include wireframes, mockups, and interactive prototypes.",
                price = 12000000.0,
                xp = 450,
                minLevel = 8,
                status = BountyStatus.OPEN,
                isEvent = true,
                requirements = listOf(
                    "Figma proficiency",
                    "UX research experience",
                    "Responsive design skills"
                ),
                deadline = "2025-12-28",
                category = "Novice"
            )
        )
    )
    val bounties: StateFlow<List<Bounty>> = _bounties

    // Mock Applications
    private val _applications = MutableStateFlow(
        listOf(
            Application(
                id = "app1",
                bountyId = "b1",
                applicantId = "3",
                applicantName = "Sarah Johnson",
                applicantLevel = 14,
                applicantXp = 9200,
                coverLetter = "I have 5 years of experience building e-commerce platforms...",
                status = ApplicationStatus.PENDING
            ),
            Application(
                id = "app2",
                bountyId = "b1",
                applicantId = "4",
                applicantName = "Mike Rodriguez",
                applicantLevel = 18,
                applicantXp = 12500,
                coverLetter = "Built 10+ e-commerce apps with payment integration...",
                status = ApplicationStatus.PENDING
            ),
            Application(
                id = "app3",
                bountyId = "b1",
                applicantId = "5",
                applicantName = "Emma Davis",
                applicantLevel = 11,
                applicantXp = 7800,
                coverLetter = "Passionate about creating seamless shopping experiences...",
                status = ApplicationStatus.PENDING
            )
        )
    )
    val applications: StateFlow<List<Application>> = _applications

    // Mock Forum Posts
    private val _forumPosts = MutableStateFlow(
        listOf(
            ForumPost(
                id = "f1",
                authorName = "John Developer",
                authorAvatar = "https://i.pravatar.cc/150?img=10",
                content = "Just completed my first bounty! The XP system is so motivating. Anyone else working on the e-commerce project?",
                upvotes = 24,
                timestamp = "2h ago"
            ),
            ForumPost(
                id = "f2",
                authorName = "Design Pro",
                authorAvatar = "https://i.pravatar.cc/150?img=20",
                content = "Looking for team members for the UI/UX bounty. Need someone with animation skills. Let's collaborate!",
                upvotes = 18,
                timestamp = "5h ago"
            ),
            ForumPost(
                id = "f3",
                authorName = "Code Ninja",
                authorAvatar = "https://i.pravatar.cc/150?img=30",
                content = "Pro tip: Focus on event bounties for bonus XP. I leveled up twice this week! 🚀",
                upvotes = 42,
                timestamp = "1d ago"
            ),
            ForumPost(
                id = "f4",
                authorName = "Tech Writer",
                authorAvatar = "https://i.pravatar.cc/150?img=40",
                content = "The recruitment event is starting soon! Who's participating? I'm going for the content writing challenges.",
                upvotes = 15,
                timestamp = "3d ago"
            )
        )
    )
    val forumPosts: StateFlow<List<ForumPost>> = _forumPosts

    // Authentication
    suspend fun login(email: String, password: String): Result<User> {
        delay(1000) // Simulate network delay
        val user = mockUsers.find { it.email == email }
        return if (user != null) {
            _currentUser.value = user
            Result.success(user)
        } else {
            Result.failure(Exception("Invalid credentials"))
        }
    }

    suspend fun register(email: String, password: String, name: String, role: UserRole): Result<User> {
        delay(1000)
        val newUser = User(
            id = (mockUsers.size + 1).toString(),
            email = email,
            name = name,
            role = role,
            level = 1,
            xp = 0,
            walletBalance = 0.0,
            avatarUrl = "https://i.pravatar.cc/150?img=${mockUsers.size + 1}",
            skills = mapOf(
                "Coding" to 50,
                "Design" to 50,
                "Writing" to 50,
                "Marketing" to 50,
                "Management" to 50
            )
        )
        mockUsers.add(newUser)
        _currentUser.value = newUser
        return Result.success(newUser)
    }

    // Bounty Operations
    fun getBountyById(id: String): Bounty? {
        return _bounties.value.find { it.id == id }
    }

    fun filterBounties(category: String): List<Bounty> {
        return if (category == "All") {
            _bounties.value
        } else {
            _bounties.value.filter { it.category == category }
        }
    }

    fun searchBounties(query: String): List<Bounty> {
        return _bounties.value.filter {
            it.title.contains(query, ignoreCase = true) ||
            it.companyName.contains(query, ignoreCase = true) ||
            it.description.contains(query, ignoreCase = true)
        }
    }

    // Application Operations
    suspend fun submitApplication(bountyId: String, coverLetter: String): Result<Application> {
        delay(800)
        val user = _currentUser.value ?: return Result.failure(Exception("Not logged in"))

        val newApplication = Application(
            id = "app${_applications.value.size + 1}",
            bountyId = bountyId,
            applicantId = user.id,
            applicantName = user.name,
            applicantLevel = user.level,
            applicantXp = user.xp,
            coverLetter = coverLetter,
            status = ApplicationStatus.PENDING
        )

        _applications.value = _applications.value + newApplication
        return Result.success(newApplication)
    }

    fun getApplicationsForBounty(bountyId: String): List<Application> {
        return _applications.value.filter { it.bountyId == bountyId }
    }

    suspend fun updateApplicationStatus(applicationId: String, status: ApplicationStatus): Result<Unit> {
        delay(500)
        _applications.value = _applications.value.map {
            if (it.id == applicationId) it.copy(status = status) else it
        }
        return Result.success(Unit)
    }

    // Submission Operations
    suspend fun submitWork(bountyId: String, notes: String): Result<Submission> {
        delay(1000)
        val user = _currentUser.value ?: return Result.failure(Exception("Not logged in"))

        val submission = Submission(
            id = "sub${System.currentTimeMillis()}",
            bountyId = bountyId,
            talentId = user.id,
            notes = notes,
            fileUrl = "mock_file_url.pdf",
            status = SubmissionStatus.SUBMITTED
        )

        return Result.success(submission)
    }

    // Portfolio items (mock data)
    fun getPortfolioItems(): List<String> {
        return listOf(
            "https://via.placeholder.com/300x200/2A4DDC/FFFFFF?text=Project+1",
            "https://via.placeholder.com/300x200/00C853/FFFFFF?text=Project+2",
            "https://via.placeholder.com/300x200/2A4DDC/FFFFFF?text=Project+3",
            "https://via.placeholder.com/300x200/00C853/FFFFFF?text=Project+4",
            "https://via.placeholder.com/300x200/2A4DDC/FFFFFF?text=Project+5",
            "https://via.placeholder.com/300x200/00C853/FFFFFF?text=Project+6"
        )
    }
}

