package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.db.AppDatabase
import com.example.data.repository.GameRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    private lateinit var context: Context
    private lateinit var testDb: AppDatabase
    private lateinit var repository: GameRepository

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext<Context>()
        testDb = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = GameRepository(context, testDb)
    }

    @After
    fun tearDown() {
        testDb.close()
    }

    @Test
    fun testAppNameString() {
        val appName = context.getString(R.string.app_name)
        assertEquals("Rules of Wealth", appName)
    }

    @Test
    fun testInitializationAndStartingBalance() = runBlocking {
        repository.initializeIfNeeded()

        val user = repository.currentUserFlow.first()
        assertNotNull(user)
        assertEquals("Alex Vance", user?.displayName)

        val wallet = repository.getWalletFlow(user!!.id).first()
        assertNotNull(wallet)
        assertEquals(500L, wallet?.balance)

        val ledger = repository.getLedgerEntriesFlow(user.id).first()
        assertTrue(ledger.isNotEmpty())
        assertEquals(500L, ledger.first().balanceAfter)

        val inventory = repository.getInventoryFlow(user.id).first()
        assertEquals(5, inventory.size)
    }

    @Test
    fun testPermanentMasteryQuiz() = runBlocking {
        repository.initializeIfNeeded()
        val user = repository.currentUserFlow.first()!!

        // Submit quiz for "card_saver" (correct answer index is 1)
        val result = repository.submitCardQuiz("card_saver", 1)
        assertTrue(result.isSuccess)

        val masteries = repository.getMasteriesFlow(user.id).first()
        assertTrue(masteries.any { it.cardDefId == "card_saver" && it.isMastered })

        val wallet = repository.getWalletFlow(user.id).first()!!
        assertTrue(wallet.balance > 500L) // Earned mastery bonus
    }

    @Test
    fun testCardCombinationSynthesis() = runBlocking {
        repository.initializeIfNeeded()
        val user = repository.currentUserFlow.first()!!

        // Initial inventory has "card_product_idea" and "card_digital_distribution"
        // Recipe "rec_digital_product" combines them to make "card_digital_product"
        val comboResult = repository.executeCombination("rec_digital_product")
        assertTrue(comboResult.isSuccess)

        val discoveries = repository.getDiscoveriesFlow(user.id).first()
        assertTrue(discoveries.any { it.recipeId == "rec_digital_product" })

        val inventory = repository.getInventoryFlow(user.id).first()
        assertTrue(inventory.any { it.cardDefId == "card_digital_product" })
    }

    @Test
    fun testBoosterPackPurchase() = runBlocking {
        repository.initializeIfNeeded()
        val user = repository.currentUserFlow.first()!!

        val initialInvSize = repository.getInventoryFlow(user.id).first().size
        val boosterResult = repository.unlockCardBooster()
        assertTrue(boosterResult.isSuccess)

        val updatedInv = repository.getInventoryFlow(user.id).first()
        assertEquals(initialInvSize + 1, updatedInv.size)

        val ledger = repository.getLedgerEntriesFlow(user.id).first()
        assertTrue(ledger.any { it.description.contains("Booster") })
    }
}
