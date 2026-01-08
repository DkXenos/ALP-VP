// Quick XP Level Calculation Test
// To verify: levels 1-10 need 100 XP, 11-20 need 150 XP, etc.
fun getXpForLevel(level: Int): Int {
    val tier = level / 10
    return 100 + (tier * 50)
}
fun main() {
    println("XP Level System Test:")
    println("====================")
    // Test XP requirements
    println("\nXP Requirements per level:")
    for (level in 1..25) {
        val xp = getXpForLevel(level)
        println("Level $level: $xp XP")
    }
    // Test cumulative XP
    println("\nCumulative XP needed to reach level:")
    var totalXp = 0
    for (level in 1..15) {
        totalXp += getXpForLevel(level)
        println("Level ${level + 1}: $totalXp total XP")
    }
    // Test specific cases
    println("\n\nTest Cases:")
    println("- Level 1-10: Should need 100 XP each")
    println("  Level 5: ${getXpForLevel(5)} XP (expected: 100)")
    println("  Level 10: ${getXpForLevel(10)} XP (expected: 100)")
    println("\n- Level 11-20: Should need 150 XP each")
    println("  Level 11: ${getXpForLevel(11)} XP (expected: 150)")
    println("  Level 15: ${getXpForLevel(15)} XP (expected: 150)")
    println("  Level 20: ${getXpForLevel(20)} XP (expected: 150)")
    println("\n- Level 21-30: Should need 200 XP each")
    println("  Level 21: ${getXpForLevel(21)} XP (expected: 200)")
    println("  Level 25: ${getXpForLevel(25)} XP (expected: 200)")
    println("  Level 30: ${getXpForLevel(30)} XP (expected: 200)")
    println("\n- Level 31-40: Should need 250 XP each")
    println("  Level 31: ${getXpForLevel(31)} XP (expected: 250)")
}
main()
