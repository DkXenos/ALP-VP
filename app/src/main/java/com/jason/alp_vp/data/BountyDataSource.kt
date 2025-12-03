package com.jason.alp_vp.data

import com.jason.alp_vp.model.Bounty
import com.jason.alp_vp.model.BountyStatus

/**
 * Data source for bounties.
 * This makes it easy to add/remove bounties and later replace with API calls.
 */
object BountyDataSource {

    fun getAllBounties(): List<Bounty> = listOf(
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

    /**
     * Get a bounty by ID
     */
    fun getBountyById(id: String): Bounty? {
        return getAllBounties().find { it.id == id }
    }

    /**
     * Filter bounties by category
     */
    fun filterByCategory(category: String): List<Bounty> {
        return if (category == "All") {
            getAllBounties()
        } else {
            getAllBounties().filter { it.category == category }
        }
    }

    /**
     * Search bounties by query
     */
    fun searchBounties(query: String): List<Bounty> {
        if (query.isBlank()) return getAllBounties()

        return getAllBounties().filter {
            it.title.contains(query, ignoreCase = true) ||
            it.companyName.contains(query, ignoreCase = true) ||
            it.description.contains(query, ignoreCase = true)
        }
    }
}

