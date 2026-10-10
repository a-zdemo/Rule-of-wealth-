package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.CyanSecondary
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.EmeraldTertiary
import com.example.ui.theme.GoldPrimary

@Composable
fun RulebookDialog(
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .border(1.2.dp, CyanSecondary.copy(alpha = 0.6f), RoundedCornerShape(18.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(18.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = null,
                            tint = CyanSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Rules of Wealth Manual",
                            color = Color.White,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                RuleSection(
                    icon = Icons.Default.MonetizationOn,
                    iconColor = GoldPrimary,
                    title = "1. Authoritative Currency & Starting Balance",
                    body = "Every new investor receives 500 virtual coins upon onboarding. Coins represent in-game economic liquidity. Every inflow and outflow is permanently recorded on the server-authoritative double-entry ledger. Client state can never fake balances."
                )

                Spacer(modifier = Modifier.height(10.dp))

                RuleSection(
                    icon = Icons.Default.School,
                    iconColor = EmeraldTertiary,
                    title = "2. Hybrid Ownership vs Knowledge Mastery",
                    body = "Knowledge Mastery is permanent! Passing a card's Academy comprehension exam certifies your understanding forever and grants bonus coins. Selling or consuming physical card assets in combinations will NEVER delete your permanent knowledge mastery or demote your tier."
                )

                Spacer(modifier = Modifier.height(10.dp))

                RuleSection(
                    icon = Icons.Default.AutoAwesome,
                    iconColor = GoldPrimary,
                    title = "3. The Synthesis Forge (Recipes)",
                    body = "Combine compatible cards in the Forge to discover new business opportunities! Example: Product Idea + Digital Distribution = Digital Product. Non-consuming recipes keep your input cards; consuming recipes convert component assets into higher-tier opportunities."
                )

                Spacer(modifier = Modifier.height(10.dp))

                RuleSection(
                    icon = Icons.Default.Storefront,
                    iconColor = CyanSecondary,
                    title = "4. Marketplace & P2P Trading",
                    body = "List un-locked cards from your vault on the open exchange, browse listings from other players, or send direct peer-to-peer trade propositions with custom coin and card terms."
                )

                Spacer(modifier = Modifier.height(10.dp))

                RuleSection(
                    icon = Icons.Default.Handshake,
                    iconColor = Color(0xFFA855F7),
                    title = "5. Executive Tier Advancement",
                    body = "Progress through five executive tiers: Starter (T1) -> Emerging (T2) -> Advanced (T3) -> Strategic (T4) -> Elite (T5) by mastering required concepts and discovering new business recipes."
                )
            }
        }
    }
}

@Composable
private fun RuleSection(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    body: String
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = title, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = body, color = Color(0xFFCBD5E1), fontSize = 11.sp, lineHeight = 15.sp)
        }
    }
}
