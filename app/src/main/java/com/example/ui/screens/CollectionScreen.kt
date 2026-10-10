package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
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
import com.example.data.model.CardCategory
import com.example.data.model.CardDefinition
import com.example.data.model.CardMastery
import com.example.data.model.PlayerInventoryCard
import com.example.ui.components.WealthCardView
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.EmeraldTertiary
import com.example.ui.theme.GoldPrimary

import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults

enum class VaultFilter(val label: String) {
    ALL("All Catalogue"),
    OWNED("Owned Assets"),
    MASTERED("Mastered Principles")
}

@Composable
fun CollectionScreen(
    cards: List<CardDefinition>,
    inventory: List<PlayerInventoryCard>,
    masteries: List<CardMastery>,
    onSelectCard: (CardDefinition) -> Unit,
    onUnlockBooster: () -> Unit
) {
    var selectedFilter by remember { mutableStateOf(VaultFilter.ALL) }
    var selectedCategory by remember { mutableStateOf<CardCategory?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    val ownedCardMap = remember(inventory) {
        inventory.groupingBy { it.cardDefId }.eachCount()
    }
    val masteredCardSet = remember(masteries) {
        masteries.filter { it.isMastered }.map { it.cardDefId }.toSet()
    }

    val filteredCards = remember(cards, selectedFilter, selectedCategory, searchQuery, ownedCardMap, masteredCardSet) {
        cards.filter { card ->
            val matchesFilter = when (selectedFilter) {
                VaultFilter.ALL -> true
                VaultFilter.OWNED -> (ownedCardMap[card.id] ?: 0) > 0
                VaultFilter.MASTERED -> masteredCardSet.contains(card.id)
            }
            val matchesCategory = (selectedCategory == null || card.category == selectedCategory)
            val matchesSearch = searchQuery.isBlank() ||
                    card.name.contains(searchQuery, ignoreCase = true) ||
                    card.shortDescription.contains(searchQuery, ignoreCase = true)

            matchesFilter && matchesCategory && matchesSearch
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(14.dp)
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "VAULT & CATALOGUE",
                    color = GoldPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Wealth Principles Matrix",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = "${inventory.size} Owned • ${masteredCardSet.size} Mastered",
                color = Color(0xFF94A3B8),
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Opportunity Booster Card
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF16233B)),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, GoldPrimary.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Icon(
                        imageVector = Icons.Default.CardGiftcard,
                        contentDescription = null,
                        tint = GoldPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Opportunity Booster Pack",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Draw a new random wealth card asset (120 Coins)",
                            color = Color(0xFFCBD5E1),
                            fontSize = 10.sp
                        )
                    }
                }

                Button(
                    onClick = onUnlockBooster,
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(32.dp).testTag("open_booster_pack_button")
                ) {
                    Text("Unlock (120)", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search concept or keyword...", fontSize = 12.sp, color = Color(0xFF64748B)) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF94A3B8), modifier = Modifier.size(18.dp)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = DarkSurface,
                unfocusedContainerColor = DarkSurface,
                focusedBorderColor = GoldPrimary,
                unfocusedBorderColor = Color(0xFF1E293B),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("collection_search_field")
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Vault Primary Filter Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            VaultFilter.values().forEach { filter ->
                FilterChip(
                    selected = (selectedFilter == filter),
                    onClick = { selectedFilter = filter },
                    label = { Text(filter.label, fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = GoldPrimary,
                        selectedLabelColor = Color.Black,
                        containerColor = DarkSurface,
                        labelColor = Color(0xFF94A3B8)
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = (selectedFilter == filter),
                        borderColor = Color(0xFF334155),
                        selectedBorderColor = GoldPrimary
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Category Filter Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            FilterChip(
                selected = (selectedCategory == null),
                onClick = { selectedCategory = null },
                label = { Text("All Categories", fontSize = 10.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF1E293B),
                    selectedLabelColor = Color.White,
                    containerColor = Color(0xFF0F172A),
                    labelColor = Color(0xFF64748B)
                )
            )

            CardCategory.values().forEach { cat ->
                FilterChip(
                    selected = (selectedCategory == cat),
                    onClick = { selectedCategory = if (selectedCategory == cat) null else cat },
                    label = { Text(cat.label, fontSize = 10.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF1E293B),
                        selectedLabelColor = Color.White,
                        containerColor = Color(0xFF0F172A),
                        labelColor = Color(0xFF64748B)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (filteredCards.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(30.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No cards matching the selected filters.",
                    color = Color(0xFF64748B),
                    fontSize = 13.sp
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(bottom = 80.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize().testTag("cards_grid")
            ) {
                items(filteredCards, key = { it.id }) { card ->
                    val ownedCount = ownedCardMap[card.id] ?: 0
                    val isMastered = masteredCardSet.contains(card.id)

                    WealthCardView(
                        card = card,
                        ownedCount = ownedCount,
                        isMastered = isMastered,
                        onClick = { onSelectCard(card) },
                        modifier = Modifier.height(230.dp)
                    )
                }
            }
        }
    }
}
