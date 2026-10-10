package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CardDefinition
import com.example.data.model.CardMastery
import com.example.data.model.LedgerEntry
import com.example.data.model.Mission
import com.example.data.model.PlayerInventoryCard
import com.example.data.model.PlayerTier
import com.example.data.model.RecipeDiscovery
import com.example.data.model.UserAccount
import com.example.data.model.Wallet
import com.example.ui.Screen
import com.example.ui.components.CoinBalancePill
import com.example.ui.components.TierBadge
import com.example.ui.theme.CyanSecondary
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.EmeraldTertiary
import com.example.ui.theme.GoldPrimary

@Composable
fun DashboardScreen(
    user: UserAccount?,
    wallet: Wallet?,
    inventory: List<PlayerInventoryCard>,
    cards: List<CardDefinition>,
    masteries: List<CardMastery>,
    discoveries: List<RecipeDiscovery>,
    missions: List<Mission>,
    recentLedger: List<LedgerEntry>,
    onNavigate: (Screen) -> Unit,
    onClaimMission: (String) -> Unit
) {
    val tier = user?.tier ?: PlayerTier.STARTER
    val nextTier = tier.nextTier()
    val balance = wallet?.balance ?: 500L

    // Compute estimated portfolio net worth
    val inventoryValuation = remember(inventory, cards) {
        inventory.sumOf { inv ->
            cards.find { it.id == inv.cardDefId }?.basePrice ?: 100L
        }
    }
    val netWorth = balance + inventoryValuation

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Executive Header Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "RULES OF WEALTH",
                    color = GoldPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )
                Text(
                    text = user?.displayName ?: "Alex Vance",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                TierBadge(tier = tier)
                Spacer(modifier = Modifier.width(8.dp))
                CoinBalancePill(
                    balance = balance,
                    onClick = { onNavigate(Screen.LEDGER) }
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Net Worth & Portfolio Card
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.2.dp,
                    brush = Brush.horizontalGradient(listOf(GoldPrimary, CyanSecondary)),
                    shape = RoundedCornerShape(18.dp)
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.radialGradient(
                            colors = listOf(Color(0xFF1E2D4A), Color(0xFF0F172A)),
                            radius = 450f
                        )
                    )
                    .padding(18.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ESTIMATED NET WORTH",
                            color = Color(0xFF94A3B8),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.TrendingUp,
                                contentDescription = null,
                                tint = EmeraldTertiary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "Authoritative Ledger",
                                color = EmeraldTertiary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "$netWorth Coins",
                        color = Color.White,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        PortfolioStat(
                            title = "Liquid Cash",
                            value = "$balance",
                            icon = Icons.Default.MonetizationOn,
                            tint = GoldPrimary
                        )
                        PortfolioStat(
                            title = "Vault Assets",
                            value = "${inventory.size} Cards",
                            icon = Icons.Default.AccountBalance,
                            tint = CyanSecondary
                        )
                        PortfolioStat(
                            title = "Mastered",
                            value = "${masteries.size} / ${cards.size}",
                            icon = Icons.Default.School,
                            tint = EmeraldTertiary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tier Progression Box
        if (nextTier != null) {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Next Tier: ${nextTier.title}",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${masteries.size}/${nextTier.requiredMasteries} Masteries • ${discoveries.size}/${nextTier.requiredRecipes} Recipes",
                            color = Color(0xFF94A3B8),
                            fontSize = 11.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    val masteryProgress = (masteries.size.toFloat() / nextTier.requiredMasteries.coerceAtLeast(1)).coerceIn(0f, 1f)
                    LinearProgressIndicator(
                        progress = { masteryProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = GoldPrimary,
                        trackColor = Color(0xFF0F172A)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Quick Navigation Command Grid
        Text(
            text = "COMMAND MODULES",
            color = Color(0xFF94A3B8),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            QuickActionTile(
                title = "Vault",
                subtitle = "Collection",
                icon = Icons.Default.AccountBalance,
                color = CyanSecondary,
                modifier = Modifier.weight(1f),
                onClick = { onNavigate(Screen.COLLECTION) }
            )
            QuickActionTile(
                title = "Forge",
                subtitle = "Synthesis",
                icon = Icons.Default.AutoAwesome,
                color = GoldPrimary,
                modifier = Modifier.weight(1f),
                onClick = { onNavigate(Screen.COMBINATION) }
            )
            QuickActionTile(
                title = "Exchange",
                subtitle = "Marketplace",
                icon = Icons.Default.Storefront,
                color = EmeraldTertiary,
                modifier = Modifier.weight(1f),
                onClick = { onNavigate(Screen.MARKETPLACE) }
            )
            QuickActionTile(
                title = "Deals",
                subtitle = "P2P Trades",
                icon = Icons.Default.Handshake,
                color = Color(0xFFA855F7),
                modifier = Modifier.weight(1f),
                onClick = { onNavigate(Screen.TRADES) }
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Active Mission Spotlight
        val activeMission = missions.firstOrNull { !it.isClaimed }
        if (activeMission != null) {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, if (activeMission.isCompleted) EmeraldTertiary else Color(0xFF334155), RoundedCornerShape(14.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "PRIORITY MISSION",
                            color = GoldPrimary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "+${activeMission.rewardCoins}",
                                color = GoldPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = activeMission.title,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = activeMission.description,
                        color = Color(0xFF94A3B8),
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    if (activeMission.isCompleted) {
                        Button(
                            onClick = { onClaimMission(activeMission.id) },
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldTertiary),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth().testTag("claim_priority_mission")
                        ) {
                            Text("Claim Reward (+${activeMission.rewardCoins} Coins)", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onNavigate(Screen.MISSIONS) },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("View All Missions", color = CyanSecondary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = CyanSecondary, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
        }

        // Recent Ledger Movements Ticker
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "RECENT LEDGER ENTRIES",
                color = Color(0xFF94A3B8),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Text(
                text = "View Ledger",
                color = CyanSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { onNavigate(Screen.LEDGER) }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (recentLedger.isEmpty()) {
            Text(
                text = "No recent ledger activity recorded.",
                color = Color(0xFF64748B),
                fontSize = 12.sp
            )
        } else {
            recentLedger.take(3).forEach { entry ->
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = entry.description,
                                color = Color.White,
                                fontSize = 12.sp,
                                maxLines = 1
                            )
                            Text(
                                text = entry.type.label,
                                color = Color(0xFF64748B),
                                fontSize = 10.sp
                            )
                        }
                        Text(
                            text = if (entry.amount >= 0) "+${entry.amount}" else "${entry.amount}",
                            color = if (entry.amount >= 0) EmeraldTertiary else Color(0xFFEF4444),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PortfolioStat(
    title: String,
    value: String,
    icon: ImageVector,
    tint: Color
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(13.dp))
            Spacer(modifier = Modifier.width(3.dp))
            Text(text = title, color = Color(0xFF94A3B8), fontSize = 10.sp)
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = value, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun QuickActionTile(
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        modifier = modifier.border(0.8.dp, color.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = title, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Text(text = subtitle, color = Color(0xFF94A3B8), fontSize = 9.sp)
        }
    }
}
