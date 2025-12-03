package com.jason.alp_vp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortfolioDetailsScreen(
    portfolioIndex: Int,
    onNavigateBack: () -> Unit
) {
    // Mock portfolio data based on index
    val portfolioData = getPortfolioData(portfolioIndex)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Portfolio Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                // Project Title
                Text(
                    text = portfolioData.title,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            item {
                // Project Type Badge
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = portfolioData.category,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }

            item {
                // Achievement Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "XP Earned",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                                Text(
                                    text = "+${portfolioData.xpEarned} XP",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "Completed",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                                Text(
                                    text = portfolioData.completedDate,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Project Description",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            item {
                Text(
                    text = portfolioData.description,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                    lineHeight = 22.sp
                )
            }

            item {
                Text(
                    text = "Technologies Used",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            item {
                // Technologies Chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    portfolioData.technologies.forEach { tech ->
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = tech,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Key Achievements",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        portfolioData.achievements.forEach { achievement ->
                            Row(
                                modifier = Modifier.padding(vertical = 4.dp)
                            ) {
                                Text(
                                    text = "✓ ",
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.secondary,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = achievement,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    lineHeight = 20.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

data class PortfolioData(
    val title: String,
    val category: String,
    val description: String,
    val xpEarned: Int,
    val completedDate: String,
    val technologies: List<String>,
    val achievements: List<String>
)

fun getPortfolioData(index: Int): PortfolioData {
    val portfolios = listOf(
        PortfolioData(
            title = "E-Commerce Mobile App",
            category = "Mobile Development",
            description = "Built a full-featured e-commerce mobile application with payment integration, real-time inventory management, and user authentication. The app supports both Android and iOS platforms and has been downloaded over 10,000 times.",
            xpEarned = 500,
            completedDate = "Nov 2024",
            technologies = listOf("Kotlin", "Jetpack Compose", "Firebase", "Stripe API"),
            achievements = listOf(
                "Implemented secure payment gateway integration",
                "Achieved 4.5-star rating on Play Store",
                "Reduced cart abandonment by 30%",
                "Optimized app performance for smooth 60fps experience"
            )
        ),
        PortfolioData(
            title = "Brand Identity Design",
            category = "UI/UX Design",
            description = "Created a comprehensive brand identity for a tech startup including logo design, color palette, typography system, and brand guidelines. The design system is now being used across all company touchpoints.",
            xpEarned = 350,
            completedDate = "Oct 2024",
            technologies = listOf("Figma", "Adobe Illustrator", "Photoshop"),
            achievements = listOf(
                "Delivered complete brand guideline document",
                "Designed 50+ marketing assets",
                "Client satisfaction score: 5/5",
                "Design featured in Behance showcase"
            )
        ),
        PortfolioData(
            title = "Analytics Dashboard API",
            category = "Backend Development",
            description = "Developed a scalable REST API for a data analytics platform with real-time data processing, comprehensive documentation, and robust security measures. The API handles over 1 million requests per day.",
            xpEarned = 750,
            completedDate = "Sep 2024",
            technologies = listOf("Node.js", "PostgreSQL", "Redis", "Docker"),
            achievements = listOf(
                "Achieved 99.9% uptime",
                "Reduced query response time by 60%",
                "Implemented rate limiting and OAuth2",
                "Comprehensive API documentation with examples"
            )
        ),
        PortfolioData(
            title = "Tech Blog Content Series",
            category = "Content Writing",
            description = "Wrote a 10-article series on modern web development covering React, Node.js, and cloud computing. Articles received over 50,000 total views and were shared widely on social media platforms.",
            xpEarned = 200,
            completedDate = "Aug 2024",
            technologies = listOf("SEO", "Technical Writing", "Markdown"),
            achievements = listOf(
                "50,000+ total article views",
                "Average engagement time: 8 minutes",
                "Featured on Dev.to trending page",
                "Generated 500+ newsletter subscribers"
            )
        ),
        PortfolioData(
            title = "SaaS Platform Design",
            category = "UI/UX Design",
            description = "Designed a complete user interface and experience for a project management SaaS platform. Created wireframes, interactive prototypes, and a comprehensive design system that improved user satisfaction by 40%.",
            xpEarned = 450,
            completedDate = "Jul 2024",
            technologies = listOf("Figma", "Principle", "Miro", "UserTesting"),
            achievements = listOf(
                "Increased user retention by 40%",
                "Reduced onboarding time by 50%",
                "Created 100+ reusable components",
                "Conducted 20+ user testing sessions"
            )
        ),
        PortfolioData(
            title = "Freelance Portfolio Website",
            category = "Web Development",
            description = "Built a modern, responsive portfolio website showcasing freelance work with smooth animations, optimized performance, and SEO best practices. The site loads in under 2 seconds and has perfect Lighthouse scores.",
            xpEarned = 300,
            completedDate = "Jun 2024",
            technologies = listOf("React", "Next.js", "Tailwind CSS", "Vercel"),
            achievements = listOf(
                "100/100 Lighthouse performance score",
                "Fully responsive across all devices",
                "Integrated with CMS for easy updates",
                "Implemented dark/light theme toggle"
            )
        )
    )

    return portfolios.getOrElse(index) { portfolios[0] }
}

