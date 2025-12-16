package com.jason.alp_vp.utils

/**
 * XP Level System:
 * - Level 1-9: Requires 100 XP per level
 * - Level 10-19: Requires 150 XP per level
 * - Level 20-29: Requires 200 XP per level
 * - And so on... (+50 XP requirement every 10 levels)
 */
object XpLevelCalculator {

    /**
     * Calculate the level and progress from total XP
     */
    data class LevelInfo(
        val level: Int,
        val currentLevelXp: Int,      // XP in current level
        val xpForNextLevel: Int,       // XP needed to reach next level
        val progressPercent: Float     // Progress to next level (0.0 - 1.0)
    )

    /**
     * Calculate level info from total XP earned
     */
    fun calculateLevel(totalXp: Int): LevelInfo {
        var remainingXp = totalXp
        var currentLevel = 1

        // Calculate level by subtracting XP requirements
        while (remainingXp >= getXpForLevel(currentLevel)) {
            remainingXp -= getXpForLevel(currentLevel)
            currentLevel++
        }

        val xpForNextLevel = getXpForLevel(currentLevel)
        val progressPercent = if (xpForNextLevel > 0) {
            remainingXp.toFloat() / xpForNextLevel.toFloat()
        } else {
            0f
        }

        return LevelInfo(
            level = currentLevel,
            currentLevelXp = remainingXp,
            xpForNextLevel = xpForNextLevel,
            progressPercent = progressPercent
        )
    }

    /**
     * Get XP required for a specific level
     * Level 1-9: 100 XP
     * Level 10-19: 150 XP
     * Level 20-29: 200 XP
     * etc.
     */
    private fun getXpForLevel(level: Int): Int {
        val tier = (level - 1) / 10  // 0 for levels 1-9, 1 for 10-19, etc.
        return 100 + (tier * 50)
    }

    /**
     * Get total XP needed to reach a specific level
     */
    fun getTotalXpForLevel(targetLevel: Int): Int {
        var totalXp = 0
        for (level in 1 until targetLevel) {
            totalXp += getXpForLevel(level)
        }
        return totalXp
    }

    /**
     * Format level display (e.g., "Level 8")
     */
    fun formatLevel(level: Int): String = "Level $level"

    /**
     * Format XP progress (e.g., "450 / 550 XP")
     */
    fun formatXpProgress(currentLevelXp: Int, xpForNextLevel: Int): String {
        return "$currentLevelXp / $xpForNextLevel XP"
    }
}

