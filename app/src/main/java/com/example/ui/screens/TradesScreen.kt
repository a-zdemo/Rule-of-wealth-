package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CardDefinition
import com.example.data.model.PlayerInventoryCard
import com.example.data.model.TradeOffer
import com.example.data.model.TradeStatus
import com.example.data.model.UserAccount
import com.example.data.model.Wallet
import com.example.ui.components.CreateTradeDialog
import com.example.ui.theme.CyanSecondary
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.EmeraldTertiary
import com.example.ui.theme.GoldPrimary

@Composable
fun TradesScreen(
    user: UserAccount?,
    wallet: Wallet?,
    trades: List<TradeOffer>,
    inventory: List<PlayerInventoryCard>,
    cards: List<CardDefinition>,
    onAcceptTrade: (String) -> Unit,
    onRejectTrade: (String) -> Unit,
    onProposeTrade: (String, List<String>, Long, String, Long, String) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var showProposeDialog by remember { mutableStateOf(false) }

    val pendingTrades = remember(trades) { trades.filter { it.status == TradeStatus.PENDING } }
    val resolvedTrades = remember(trades) { trades.filter { it.status != TradeStatus.PENDING } }

    Scaffold(
        containerColor = DarkBackground,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showProposeDialog = true },
                containerColor = Color(0xFF00B4D8),
                contentColor = Color.Black,
                modifier = Modifier.testTag("open_propose_trade_dialog")
            ) {
                Row(modifier = Modifier.padding(horizontal = 14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Handshake, contentDescription = "Propose Deal", modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Propose Deal", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(14.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "PEER-TO-PEER TRADING",
                        color = GoldPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Direct Deals & Negotiations",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "${pendingTrades.size} Pending",
                    color = Color(0xFF00B4D8),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = DarkSurface,
                contentColor = Color(0xFF00B4D8),
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = Color(0xFF00B4D8)
                    )
                }
            ) {
                Tab(
                    selected = (selectedTab == 0),
                    onClick = { selectedTab = 0 },
                    text = { Text("Inbox (${pendingTrades.size})", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = (selectedTab == 1),
                    onClick = { selectedTab = 1 },
                    text = { Text("Deal History (${resolvedTrades.size})", fontWeight = FontWeight.Bold) }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            val currentList = if (selectedTab == 0) pendingTrades else resolvedTrades

            if (currentList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(30.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (selectedTab == 0) "No pending trade offers in your inbox." else "No resolved trade history recorded.",
                        color = Color(0xFF64748B),
                        fontSize = 13.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(currentList, key = { it.id }) { trade ->
                        val offeredCardNames = trade.offeredCardDefIdsJson.split(",").filter { it.isNotBlank() }
                            .map { id -> cards.find { it.id == id }?.name ?: id }
                        val requestedCardNames = trade.requestedCardDefIdsJson.split(",").filter { it.isNotBlank() }
                            .map { id -> cards.find { it.id == id }?.name ?: id }

                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = DarkSurface),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color(0xFF24334C), RoundedCornerShape(14.dp))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "From: ${trade.senderDisplayName}",
                                        color = Color.White,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Box(
                                        modifier = Modifier
                                            .background(
                                                when (trade.status) {
                                                    TradeStatus.PENDING -> Color(0xFF0077B6).copy(alpha = 0.3f)
                                                    TradeStatus.ACCEPTED -> EmeraldTertiary.copy(alpha = 0.2f)
                                                    TradeStatus.REJECTED -> Color(0xFFEF4444).copy(alpha = 0.2f)
                                                    TradeStatus.CANCELLED -> Color.Gray.copy(alpha = 0.2f)
                                                },
                                                RoundedCornerShape(6.dp)
                                            )
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = trade.status.name,
                                            color = when (trade.status) {
                                                TradeStatus.PENDING -> Color(0xFF90E0EF)
                                                TradeStatus.ACCEPTED -> EmeraldTertiary
                                                TradeStatus.REJECTED -> Color(0xFFEF4444)
                                                TradeStatus.CANCELLED -> Color.Gray
                                            },
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                if (trade.note.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "\"${trade.note}\"",
                                        color = Color(0xFF94A3B8),
                                        fontSize = 11.sp
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Terms breakdown
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color(0xFF0F172A), RoundedCornerShape(10.dp))
                                        .padding(10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = "THEY OFFER:", color = EmeraldTertiary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        Text(
                                            text = offeredCardNames.joinToString(", ").ifBlank { "No cards" },
                                            color = Color.White,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        if (trade.offeredCoins > 0) {
                                            Text(text = "+${trade.offeredCoins} Coins", color = GoldPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }

                                    Icon(Icons.Default.SwapHoriz, contentDescription = null, tint = Color(0xFF00B4D8), modifier = Modifier.size(24.dp))

                                    Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                                        Text(text = "THEY REQUEST:", color = Color(0xFFF59E0B), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        Text(
                                            text = requestedCardNames.joinToString(", ").ifBlank { "No cards" },
                                            color = Color.White,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        if (trade.requestedCoins > 0) {
                                            Text(text = "+${trade.requestedCoins} Coins", color = GoldPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }

                                if (trade.status == TradeStatus.PENDING) {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        OutlinedButton(
                                            onClick = { onRejectTrade(trade.id) },
                                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFEF4444)),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.weight(1f).height(36.dp)
                                        ) {
                                            Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Decline", fontSize = 12.sp)
                                        }

                                        Button(
                                            onClick = { onAcceptTrade(trade.id) },
                                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldTertiary),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.weight(1f).height(36.dp).testTag("accept_trade_${trade.id}")
                                        ) {
                                            Icon(Icons.Default.Check, contentDescription = null, tint = Color.Black, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Accept Terms", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showProposeDialog) {
        CreateTradeDialog(
            inventory = inventory,
            allCards = cards,
            onDismiss = { showProposeDialog = false },
            onConfirm = { peerName, offeredIds, offeredCoins, reqCardId, reqCoins, note ->
                showProposeDialog = false
                onProposeTrade(peerName, offeredIds, offeredCoins, reqCardId, reqCoins, note)
            }
        )
    }
}
