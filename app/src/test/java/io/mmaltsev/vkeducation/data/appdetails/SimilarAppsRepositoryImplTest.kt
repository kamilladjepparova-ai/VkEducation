package io.mmaltsev.vkeducation.data.appdetails

import io.mmaltsev.vkeducation.data.appdetails.SimilarAppDto // ⬅️ БЕЗ .network
import io.mmaltsev.vkeducation.data.appdetails.SimilarAppsApi // ⬅️ БЕЗ .network
import io.mmaltsev.vkeducation.domain.appdetails.SimilarApp
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SimilarAppsRepositoryImplTest {
    private val api: SimilarAppsApi = mockk()
    private val mapper: SimilarAppMapper = mockk()
    private val repository = SimilarAppsRepositoryImpl(api, mapper)

    @Test
    fun `getSimilarApps returns mapped list when API returns data`() = runTest {
        val dtoList = listOf(SimilarAppDto("1", "App1", "url1"), SimilarAppDto("2", "App2", "url2"))
        val domainList = listOf(SimilarApp("1", "App1", "url1"), SimilarApp("2", "App2", "url2"))

        coEvery { api.getSimilarApps() } returns dtoList
        coEvery { mapper.toDomain(dtoList[0]) } returns domainList[0]
        coEvery { mapper.toDomain(dtoList[1]) } returns domainList[1]

        val result = repository.getSimilarApps()
        assertEquals(domainList, result)
    }

    @Test
    fun `getSimilarApps returns empty list when API returns empty list`() = runTest {
        coEvery { api.getSimilarApps() } returns emptyList()
        val result = repository.getSimilarApps()
        assertEquals(emptyList<SimilarApp>(), result)
    }

    @Test
    fun `getSimilarApps throws exception when API throws exception`() = runTest {
        coEvery { api.getSimilarApps() } throws RuntimeException("API Error")
        val result = runCatching { repository.getSimilarApps() }
        assertTrue(result.isFailure)
        assertEquals("API Error", result.exceptionOrNull()?.message)
    }

    @Test
    fun `getSimilarApps verifies mapper is called for each item`() = runTest {
        val dtoList = listOf(SimilarAppDto("1", "App1", "url1"))
        val domainList = listOf(SimilarApp("1", "App1", "url1"))

        coEvery { api.getSimilarApps() } returns dtoList
        coEvery { mapper.toDomain(dtoList[0]) } returns domainList[0]

        repository.getSimilarApps()
        coVerify { mapper.toDomain(dtoList[0]) }
    }

    @Test
    fun `getSimilarApps verifies API is called correctly on multiple invocations`() = runTest {
        coEvery { api.getSimilarApps() } returns emptyList()
        repository.getSimilarApps()
        repository.getSimilarApps()
        coVerify(exactly = 2) { api.getSimilarApps() }
    }
}