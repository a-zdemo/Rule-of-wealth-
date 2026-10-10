package com.example.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.window.Dialog
import com.example.data.model.CardDefinition
import com.example.data.model.PlayerInventoryCard
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.GoldPrimary

@Composable
fun CreateListingDialog(
    inventory: List<PlayerInventoryCard>,
    allCards: List<CardDefinition>,
    onDismiss: () -> Unit,
    onConfirm: (instanceId: String, price: Long) -> Unit
) {
    val unlistedCards = remember(inventory) { inventory.filter { !it.isLocked } }
    var selectedInstanceId by remember { mutableStateOf(unlistedCards.firstOrNull()?.instanceId ?: "") }
    var priceText by remember { mutableStateOf("120") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .border(1.2.dp, GoldPrimary.copy(alpha = 0.6f), RoundedCornerShape(18.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "List Card on Exchange",
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(26.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (unlistedCards.isEmpty()) {
                    Text(
                        text = "You have no available unlisted cards in your vault. Obtain or craft cards first!",
                        color = Color(0xFF94A3B8),
                        fontSize = 13.sp
                    )
                } else {
                    Text(
                        text = "Select Asset to List:",
                        color = Color(0xFFCBD5E1),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        unlistedCards.forEach { invCard ->
                            val cardDef = allCards.find { it.id == invCard.cardDefId }
                            val isSelected = (selectedInstanceId == invCard.instanceId)

                            Card(
                                onClick = { selectedInstanceId = invCard.instanceId },
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) Color(0xFF1E293B) else Color(0xFF0F172A)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp)
                                    .border(
                                        width = if (isSelected) 1.2.dp else 0.5.dp,
                                        color = if (isSelected) GoldPrimary else Color(0xFF334155),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = cardDef?.name ?: "Unknown Card",
                                        color = if (isSelected) GoldPrimary else Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = cardDef?.rarity?.label ?: "",
                                        color = Color(0xFF94A3B8),
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = priceText,
                        onValueChange = { priceText = it.filter { ch -> ch.isDigit() } },
                        label = { Text("Price (Virtual Coins)") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GoldPrimary,
                            unfocusedBorderColor = Color(0xFF334155),
                            focusedLabelColor = GoldPrimary,
                            unfocusedLabelColor = Color.Gray,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("listing_price_input")
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            val price = priceText.toLongOrNull() ?: 100L
                            if (selectedInstanceId.isNotBlank() && price > 0) {
                                onConfirm(selectedInstanceId, price)
                            }
                        },
                        enabled = selectedInstanceId.isNotBlank() && (priceText.toLongOrNull() ?: 0L) > 0,
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("submit_listing_button")
                    ) {
                        Icon(Icons.Default.Sell, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Publish Listing", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun CreateTradeDialog(
    inventory: List<PlayerInventoryCard>,
    allCards: List<CardDefinition>,
    onDismiss: () -> Unit,
    onConfirm: (targetPeer: String, offeredInstanceIds: List<String>, offeredCoins: Long, requestedCardDefId: String, requestedCoins: Long, note: String) -> Unit
) {
    val unlistedCards = remember(inventory) { inventory.filter { !it.isLocked } }
    var targetPeer by remember { mutableStateOf("Elena Rostova") }
    var selectedOfferedInstanceId by remember { mutableStateOf(unlistedCards.firstOrNull()?.instanceId ?: "") }
    var selectedRequestedCardDefId by remember { mutableStateOf(allCards.firstOrNull()?.id ?: "") }
    var offeredCoinsText by remember { mutableStateOf("0") }
    var requestedCoinsText by remember { mutableStateOf("0") }
    var noteText by remember { mutableStateOf("Looking to complete my portfolio synergy.") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .border(1.2.dp, Color(0xFF00B4D8), RoundedCornerShape(18.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "New Direct Peer Trade",
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = targetPeer,
                    onValueChange = { targetPeer = it },
                    label = { Text("Target Trader / Peer") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF00B4D8),
                        unfocusedBorderColor = Color(0xFF334155),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "You Offer (Card Asset):",
                    color = Color(0xFFCBD5E1),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))

                Column(modifier = Modifier.fillMaxWidth().height(90.dp).verticalScroll(rememberScrollState())) {
                    unlistedCards.forEach { invCard ->
                        val cardDef = allCards.find { it.id == invCard.cardDefId }
                        val isSelected = (selectedOfferedInstanceId == invCard.instanceId)
                        Card(
                            onClick = { selectedOfferedInstanceId = invCard.instanceId },
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) Color(0xFF1E293B) else Color(0xFF0F172A)
                            ),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp)
                                .border(
                                    if (isSelected) 1.dp else 0.4.dp,
                                    if (isSelected) GoldPrimary else Color(0xFF334155),
                                    RoundedCornerShape(6.dp)
                                )
                        ) {
                            Text(
                                text = cardDef?.name ?: "Unknown",
                                color = if (isSelected) GoldPrimary else Color.White,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = offeredCoinsText,
                        onValueChange = { offeredCoinsText = it.filter { ch -> ch.isDigit() } },
                        label = { Text("Coins Offered") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = requestedCoinsText,
                        onValueChange = { requestedCoinsText = it.filter { ch -> ch.isDigit() } },
                        label = { Text("Coins Requested") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "You Request (Card Concept):",
                    color = Color(0xFFCBD5E1),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))

                Column(modifier = Modifier.fillMaxWidth().height(90.dp).verticalScroll(rememberScrollState())) {
                    allCards.take(8).forEach { cardDef ->
                        val isSelected = (selectedRequestedCardDefId == cardDef.id)
                        Card(
                            onClick = { selectedRequestedCardDefId = cardDef.id },
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) Color(0xFF1E293B) else Color(0xFF0F172A)
                            ),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp)
                                .border(
                                    if (isSelected) 1.dp else 0.4.dp,
                                    if (isSelected) Color(0xFF00B4D8) else Color(0xFF334155),
                                    RoundedCornerShape(6.dp)
                                )
                        ) {
                            Text(
                                text = cardDef.name,
                                color = if (isSelected) Color(0xFF00B4D8) else Color.White,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = noteText,
                    onValueChange = { noteText = it },
                    label = { Text("Terms / Message to Peer") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        val offeredCoins = offeredCoinsText.toLongOrNull() ?: 0L
                        val requestedCoins = requestedCoinsText.toLongOrNull() ?: 0L
                        val offeredList = if (selectedOfferedInstanceId.isNotBlank()) listOf(selectedOfferedInstanceId) else emptyList()
                        onConfirm(targetPeer, offeredList, offeredCoins, selectedRequestedCardDefId, requestedCoins, noteText)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00B4D8)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().testTag("send_trade_offer_button")
                ) {
                    Icon(Icons.Default.Send, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Transmit Trade Offer", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
