package com.mak.feastit.data.repository

import com.mak.feastit.database.FeastDB
import com.mak.feastit.domain.model.SyncType
import com.mak.feastit.domain.util.DispatcherProvider
import com.mak.feastit.remote.FeastAPIService
import com.mak.feastit.remote.dto.ComplexSearchDTO
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test


class RealRecipesRepositoryTest {

//    TODO instead of mocking object create mock server
    @MockK
    private lateinit var api: FeastAPIService

    @MockK
    private lateinit var db: FeastDB

    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher: DispatcherProvider = object : DispatcherProvider {
        override val io: CoroutineDispatcher
            get() = UnconfinedTestDispatcher()
        override val computation: CoroutineDispatcher
            get() = UnconfinedTestDispatcher()
        override val main: CoroutineDispatcher
            get() = UnconfinedTestDispatcher()
    }

    private val sut = RealRecipesRepository(
        api = api,
        db = db,
        dispatcher = dispatcher
    )

    @BeforeEach
    fun before() {
        clearAllMocks()
    }
    companion object {
        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            MockKAnnotations.init()
        }
    }

    @Test
    @DisplayName("Get recipes for category and store in local database")
    fun refreshRecipesSuccess() = runTest {
        coEvery { api.searchRecipes(any()) } returns ComplexSearchDTO()

        sut.refreshRecipes(SyncType.POPULAR_RECIPES, 1, false)

        coVerifyOrder {
//            sut.fetchRecipes()
            api.searchRecipes(any())
//            sut.saveRemoteRecipes()
        }
    }
}