package com.example.data.model

enum class CardRarity(val label: String, val multiplier: Float) {
    COMMON("Common", 1.0f),
    UNCOMMON("Uncommon", 1.5f),
    RARE("Rare", 2.5f),
    EPIC("Epic", 4.0f),
    LEGENDARY("Legendary", 8.0f)
}

enum class CardCategory(val label: String) {
    STARTER("Starter Concept"),
    BUSINESS_OPPORTUNITY("Business Opportunity"),
    SKILL("High-Income Skill"),
    STRATEGY("Growth Strategy"),
    ADVANCED_OPPORTUNITY("Advanced Model"),
    SPECIAL_EVENT("Capital & Scaling")
}

enum class PlayerTier(val level: Int, val title: String, val requiredMasteries: Int, val requiredRecipes: Int) {
    STARTER(1, "Starter", 0, 0),
    EMERGING(2, "Emerging", 3, 1),
    ADVANCED(3, "Advanced", 7, 3),
    STRATEGIC(4, "Strategic", 12, 6),
    ELITE(5, "Elite", 18, 9);

    fun nextTier(): PlayerTier? = when (this) {
        STARTER -> EMERGING
        EMERGING -> ADVANCED
        ADVANCED -> STRATEGIC
        STRATEGIC -> ELITE
        ELITE -> null
    }
}

enum class LedgerEntryType(val label: String) {
    ONBOARDING("Onboarding Grant"),
    DAILY_REWARD("Daily Reward"),
    MISSION_REWARD("Mission Reward"),
    ACHIEVEMENT_REWARD("Achievement Reward"),
    MASTERY_BONUS("Mastery Quiz Bonus"),
    MARKET_BUY("Marketplace Purchase"),
    MARKET_SELL("Marketplace Sale Payout"),
    TRADE_SETTLEMENT("Direct Trade Settlement"),
    RECIPE_CRAFT("Combination Crafting Cost"),
    ADMIN_BONUS("Admin Grant / Sandbox")
}

enum class ListingStatus {
    ACTIVE,
    SOLD,
    CANCELLED
}

enum class TradeStatus {
    PENDING,
    ACCEPTED,
    REJECTED,
    CANCELLED
}

enum class MissionCategory(val label: String) {
    TUTORIAL("Getting Started"),
    DAILY("Daily Mission"),
    WEEKLY("Weekly Challenge"),
    MILESTONE("Tier Milestone")
}

enum class NotificationType {
    MARKETPLACE,
    TRADE,
    ACHIEVEMENT,
    MISSION,
    MASTERY,
    SYSTEM
}
