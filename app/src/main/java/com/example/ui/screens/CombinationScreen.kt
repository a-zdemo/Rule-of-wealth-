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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CardDefinition
import com.example.data.model.PlayerInventoryCard
import com.example.data.model.Recipe
import com.example.data.model.RecipeDiscovery
import com.example.data.model.Wallet
import com.example.ui.components.getRarityColor
import com.example.ui.theme.CyanSecondary
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.EmeraldTertiary
import com.example.ui.theme.GoldPrimary

@Composable
fun CombinationScreen(
    inventory: List<PlayerInventoryCard>,
    cards: List<CardDefinition>,
    recipes: List<Recipe>,
    discoveries: List<RecipeDiscovery>,
    wallet: Wallet?,
    onCombine: (String) -> Unit
) {
    var slotOneCardDefId by remember { mutableStateOf<String?>(null) }
    var slotTwoCardDefId by remember { mutableStateOf<String?>(null) }
    var selectedTab by remember { mutableIntStateOf(0) } // 0 = Inventory Picker, 1 = Recipe Codex

    val discoveredRecipeIds = remember(discoveries) { discoveries.map { it.recipeId }.toSet() }

    // Find if selected slots match any recipe
    val matchedRecipe = remember(slotOneCardDefId, slotTwoCardDefId, recipes) {
        if (slotOneCardDefId == null || slotTwoCardDefId == null) null
        else {
            val selectedSet = setOf(slotOneCardDefId!!, slotTwoCardDefId!!)
            recipes.find { recipe ->
                val inputSet = recipe.inputCardDefIdsJson.split(",").map { it.trim() }.toSet()
                inputSet == selectedSet
            }
        }
    }

    val outputCard = remember(matchedRecipe, cards) {
        matchedRecipe?.let { r -> cards.find { it.id == r.outputCardDefId } }
    }

    val availableInventoryCards = remember(inventory, cards) {
        inventory.filter { !it.isLocked }.mapNotNull { inv ->
            cards.find { it.id == inv.cardDefId }
        }.distinctBy { it.id }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
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
                    text = "SYNTHESIS FORGE",
                    color = GoldPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Idea Combination Engine",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = "${discoveries.size} / ${recipes.size} Discovered",
                color = CyanSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Alchemy Matrix / Combination Workspace
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.2.dp, GoldPrimary.copy(alpha = 0.5f), RoundedCornerShape(18.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF141F36), Color(0xFF0C1322))
                        )
                    )
                    .padding(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Dual Slot Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Slot 1
                    CombinationSlot(
                        cardDef = cards.find { it.id == slotOneCardDefId },
                        label = "Component A",
                        onClear = { slotOneCardDefId = null },
                        modifier = Modifier.weight(1f)
                    )

                    // Plus Connector
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1E293B)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            tint = GoldPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Slot 2
                    CombinationSlot(
                        cardDef = cards.find { it.id == slotTwoCardDefId },
                        label = "Component B",
                        onClear = { slotTwoCardDefId = null },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Outcome / Synthesis Preview
                if (matchedRecipe != null && outputCard != null) {
                    val userBalance = wallet?.balance ?: 0L
                    val canAfford = userBalance >= matchedRecipe.coinCost

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF0A1826))
                            .border(1.dp, CyanSecondary.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                            .padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Synergy Detected: ${matchedRecipe.name}",
                                color = GoldPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            text = "Produces: ${outputCard.name} (${outputCard.rarity.label})",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            text = if (matchedRecipe.isConsuming) "⚠️ Consuming Recipe: Component assets consumed" else "✨ Non-consuming: Assets remain in your vault",
                            color = if (matchedRecipe.isConsuming) Color(0xFFF59E0B) else EmeraldTertiary,
                            fontSize = 10.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = { onCombine(matchedRecipe.id) },
                            enabled = canAfford,
                            colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("synthesize_button")
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Synthesize (${matchedRecipe.coinCost} Coins)",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                } else if (slotOneCardDefId != null && slotTwoCardDefId != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF1E1E2E))
                            .padding(10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No recorded synergy found between these two principles. Try other combinations or inspect the Recipe Codex!",
                            color = Color(0xFF94A3B8),
                            fontSize = 11.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    Text(
                        text = "Tap cards from your vault below to load components into the Forge.",
                        color = Color(0xFF64748B),
                        fontSize = 11.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Tabs: Vault Cards Picker vs Recipe Codex
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = DarkSurface,
            contentColor = GoldPrimary,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = GoldPrimary
                )
            }
        ) {
            Tab(
                selected = (selectedTab == 0),
                onClick = { selectedTab = 0 },
                text = { Text("Available Cards (${availableInventoryCards.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = (selectedTab == 1),
                onClick = { selectedTab = 1 },
                text = { Text("Recipe Codex (${recipes.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (selectedTab == 0) {
            // Inventory Cards horizontal list & quick selector
            if (availableInventoryCards.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No cards available in your vault. Obtain cards from the exchange or missions!",
                        color = Color(0xFF64748B),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(availableInventoryCards, key = { it.id }) { cardDef ->
                        val isSlotOne = (slotOneCardDefId == cardDef.id)
                        val isSlotTwo = (slotTwoCardDefId == cardDef.id)
                        val rarityColor = getRarityColor(cardDef.rarity)

                        Card(
                            onClick = {
                                if (isSlotOne) slotOneCardDefId = null
                                else if (isSlotTwo) slotTwoCardDefId = null
                                else if (slotOneCardDefId == null) slotOneCardDefId = cardDef.id
                                else if (slotTwoCardDefId == null) slotTwoCardDefId = cardDef.id
                                else slotOneCardDefId = cardDef.id
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSlotOne || isSlotTwo) Color(0xFF1E293B) else DarkSurface
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    width = if (isSlotOne || isSlotTwo) 1.5.dp else 0.5.dp,
                                    color = if (isSlotOne || isSlotTwo) GoldPrimary else Color(0xFF334155),
                                    shape = RoundedCornerShape(10.dp)
                                )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = cardDef.name,
                                            color = Color.White,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = cardDef.rarity.label,
                                            color = rarityColor,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                    Text(
                                        text = cardDef.shortDescription,
                                        color = Color(0xFF94A3B8),
                                        fontSize = 11.sp,
                                        maxLines = 1
                                    )
                                }

                                Text(
                                    text = if (isSlotOne) "Slot A" else if (isSlotTwo) "Slot B" else "Select",
                                    color = if (isSlotOne || isSlotTwo) GoldPrimary else CyanSecondary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        } else {
            // Recipe Codex List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(recipes, key = { it.id }) { recipe ->
                    val isDiscovered = discoveredRecipeIds.contains(recipe.id)
                    val outputCardDef = cards.find { it.id == recipe.outputCardDefId }

                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkSurface),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = if (isDiscovered) EmeraldTertiary.copy(alpha = 0.5f) else Color(0xFF334155),
                                shape = RoundedCornerShape(12.dp)
                            )
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (isDiscovered) Icons.Default.CheckCircle else Icons.Default.HelpOutline,
                                        contentDescription = null,
                                        tint = if (isDiscovered) EmeraldTertiary else Color(0xFF94A3B8),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (isDiscovered) recipe.name else "Undiscovered Opportunity",
                                        color = if (isDiscovered) Color.White else Color(0xFFCBD5E1),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(text = "${recipe.coinCost}", color = GoldPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = if (isDiscovered) "Recipe: ${recipe.clue}" else "Clue: ${recipe.clue}",
                                color = if (isDiscovered) CyanSecondary else Color(0xFFF59E0B),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )

                            if (isDiscovered && outputCardDef != null) {
                                Text(
                                    text = "Produces '${outputCardDef.name}' (${outputCardDef.rarity.label})",
                                    color = Color(0xFFA7F3D0),
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CombinationSlot(
    cardDef: CardDefinition?,
    label: String,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (cardDef != null) Color(0xFF162036) else Color(0xFF0F172A)
        ),
        modifier = modifier
            .height(95.dp)
            .border(
                width = if (cardDef != null) 1.2.dp else 0.8.dp,
                color = if (cardDef != null) GoldPrimary else Color(0xFF334155),
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(8.dp)) {
            if (cardDef != null) {
                IconButton(
                    onClick = onClear,
                    modifier = Modifier.size(20.dp).align(Alignment.TopEnd)
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Clear", tint = Color.Gray, modifier = Modifier.size(14.dp))
                }

                Column(
                    modifier = Modifier.fillMaxSize().padding(end = 16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = cardDef.name,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2
                    )
                    Text(
                        text = cardDef.rarity.label,
                        color = getRarityColor(cardDef.rarity),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = label,
                        color = Color(0xFF64748B),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Empty",
                        color = Color(0xFF475569),
                        fontSize = 9.sp
                    )
                }
            }
        }
    }
}
