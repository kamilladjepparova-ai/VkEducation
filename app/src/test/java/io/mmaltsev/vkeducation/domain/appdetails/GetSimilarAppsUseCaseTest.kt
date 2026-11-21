package io.mmaltsev.vkeducation.domain.appdetails

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetSimilarAppsUseCaseTest {
    private val repository: SimilarAppsRepository = mockk()
    private val useCase = GetSimilarAppsUseCase(repository)

    @Test
    fun `invoke returns list from repository`() = runTest {
        val expected = listOf(SimilarApp("1", "App1", "url1"))
        coEvery { repository.getSimilarApps() } returns expected

        val result = useCase()
        assertEquals(expected, result)
    }

    @Test
    fun `invoke returns empty list when repository returns empty list`() = runTest {
        coEvery { repository.getSimilarApps() } returns emptyList()
        val result = useCase()
        assertEquals(emptyList<SimilarApp>(), result)
    }

    @Test
    fun `invoke propagates exceptions from repository`() = runTest {
        coEvery { repository.getSimilarApps() } throws RuntimeException("UseCase error")
        val result = runCatching { useCase() }
        assertTrue(result.isFailure)
        assertEquals("UseCase error", result.exceptionOrNull()?.message)
    }

    @Test
    fun `invoke calls repository exactly once`() = runTest {
        coEvery { repository.getSimilarApps() } returns emptyList()
        useCase()
        coVerify(exactly = 1) { repository.getSimilarApps() }
    }

    @Test
    fun `invoke handles large lists correctly`() = runTest {
        val largeList = List(100) { SimilarApp(it.toString(), "App$it", "url$it") }
        coEvery { repository.getSimilarApps() } returns largeList

        val result = useCase()
        assertEquals(100, result.size)
        assertEquals("App99", result[99].name)
    }
}