package com.jason.alp_vp.data

import com.jason.alp_vp.ui.model.Bounty

/**
 * Dummy data repository for development.
 * Replace this with actual API calls when backend is ready.
 */
object DummyDataRepository {

    // Extended bounty data with descriptions and requirements
    private val bountiesWithDetails = mapOf(
        "1" to BountyDetail(
            description = "We need a scalable e-commerce mobile application with Android and iOS support. The app should support user authentication, product browsing, shopping cart, and secure payment integration.",
            requirements = listOf(
                "Experience with Flutter or React Native",
                "Knowledge of REST API",
                "Familiarity with Firebase or backend services",
                "Payment gateway integration experience",
                "Available for 3-4 months"
            )
        ),
        "2" to BountyDetail(
            description = "Create a complete branding kit including logo, color palette, typography guide, and brand guidelines for our startup company.",
            requirements = listOf(
                "Proficient in Adobe Illustrator/Figma",
                "Strong portfolio in branding",
                "Understanding of modern design trends",
                "Deliver in multiple formats (AI, SVG, PNG)"
            )
        ),
        "3" to BountyDetail(
            description = "Develop a RESTful API backend for a mobile application with user management, data processing, and third-party integrations.",
            requirements = listOf(
                "Experience with Node.js or Python",
                "Knowledge of MongoDB or PostgreSQL",
                "API security best practices",
                "Docker containerization",
                "Available for 2-3 months"
            )
        ),
        "4" to BountyDetail(
            description = "Write engaging tech blog articles covering latest trends in AI, blockchain, and cloud computing. Need 10-15 articles of 1000+ words each.",
            requirements = listOf(
                "Strong writing skills in English",
                "Technical knowledge in software",
                "SEO optimization experience",
                "Portfolio of published articles"
            )
        ),
        "5" to BountyDetail(
            description = "Build a responsive website using modern frontend framework with smooth animations and excellent UX.",
            requirements = listOf(
                "Expert in React.js or Vue.js",
                "CSS/SCSS mastery",
                "Responsive design expertise",
                "Knowledge of web performance optimization"
            )
        )
    )

    fun getAllBounties(): List<Bounty> {
        return listOf(
            Bounty(
                id = "1",
                title = "Build E-Commerce Mobile App",
                company = "BlueTech Inc",
                deadline = "31 Dec 2025",
                rewardXp = 300,
                rewardMoney = 15000000,
                status = "active"
            ),
            Bounty(
                id = "2",
                title = "Design Company Branding Kit",
                company = "Aura Corp",
                deadline = "15 Jan 2026",
                rewardXp = 200,
                rewardMoney = 8500000,
                status = "active"
            ),
            Bounty(
                id = "3",
                title = "Backend API Development",
                company = "ByteForce",
                deadline = "20 Jan 2026",
                rewardXp = 450,
                rewardMoney = 12000000,
                status = "active"
            ),
            Bounty(
                id = "4",
                title = "Content Writing - Tech Blog",
                company = "TechWrite",
                deadline = "10 Jan 2026",
                rewardXp = 150,
                rewardMoney = 3500000,
                status = "active"
            ),
            Bounty(
                id = "5",
                title = "Frontend Web Development",
                company = "WebMasters",
                deadline = "25 Jan 2026",
                rewardXp = 350,
                rewardMoney = 10000000,
                status = "active"
            )
        )
    }

    /**
     * Get bounty by ID with full details
     * TODO: Replace with API call - GET /api/bounties/{id}
     */
    fun getBountyById(id: String): Bounty? {
        return getAllBounties().find { it.id == id }
    }

    /**
     * Get detailed information for a bounty
     * TODO: Replace with API call - GET /api/bounties/{id}/details
     */
    fun getBountyDetails(id: String): BountyDetail? {
        return bountiesWithDetails[id]
    }

    /**
     * Search bounties by query
     * TODO: Replace with API call - GET /api/bounties/search?q={query}
     */
    fun searchBounties(query: String): List<Bounty> {
        return getAllBounties().filter {
            it.title.contains(query, ignoreCase = true) ||
            it.company.contains(query, ignoreCase = true)
        }
    }

    /**
     * Filter bounties by type
     * TODO: Replace with API call - GET /api/bounties?type={type}
     */
    fun filterBountiesByType(type: String): List<Bounty> {
        // For now, return all bounties. Add 'type' field to Bounty model when API is ready
        return getAllBounties()
    }

    /**
     * Submit application for a bounty
     * TODO: Replace with API call - POST /api/bounties/{id}/apply
     */
    fun submitApplication(
        bountyId: String,
        portfolioLinks: List<String>,
        cvImageUrl: String,
        whyHireYou: String
    ): Boolean {
        // Simulate successful submission
        println("Submitting application for bounty $bountyId")
        println("Portfolio: $portfolioLinks")
        println("CV: $cvImageUrl")
        println("Why hire: $whyHireYou")
        return true
    }
}

/**
 * Data class for detailed bounty information
 */
data class BountyDetail(
    val description: String,
    val requirements: List<String>
)

