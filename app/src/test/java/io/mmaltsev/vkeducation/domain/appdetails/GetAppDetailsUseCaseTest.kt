package io.mmaltsev.vkeducation.domain.appdetails

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetAppDetailsUseCaseTest {
    private val repository: AppDetailsRepository = mockk()
    private val useCase = GetAppDetailsUseCase(repository)

    @Test
    fun `invoke returns flow from repository`() = runTest {
        val domain = AppDetails("1", "Name", "Dev", Category.APP, 12, 100.0f, "icon", null, "desc")
        coEvery { repository.getAppDetails("1") } returns flowOf(domain)

        val result = useCase("1").first()
        assertEquals(domain, result)
    }

    @Test
    fun `invoke passes correct ID to repository`() = runTest {
        coEvery { repository.getAppDetails("test_id") } returns flowOf(mockk())
        useCase("test_id").first()
        coVerify { repository.getAppDetails("test_id") }
    }

    @Test
    fun `invoke propagates exceptions from repository`() = runTest {
        coEvery { repository.getAppDetails("1") } throws RuntimeException("Repo error")
        val result = runCatching { useCase("1").first() }
        assertTrue(result.isFailure)
        assertEquals("Repo error", result.exceptionOrNull()?.message)
    }

    @Test
    fun `invoke can be called multiple times with different IDs`() = runTest {
        val domain1 = AppDetails("1", "Name1", "Dev", Category.APP, 12, 100.0f, "icon", null, "desc")
        val domain2 = AppDetails("2", "Name2", "Dev", Category.GAME, 18, 200.0f, "icon2", null, "desc2")

        coEvery { repository.getAppDetails("1") } returns flowOf(domain1)
        coEvery { repository.getAppDetails("2") } returns flowOf(domain2)

        assertEquals(domain1, useCase("1").first())
        assertEquals(domain2, useCase("2").first())
    }

    @Test
    fun `invoke handles null screenshots correctly from repository`() = runTest {
        val domain = AppDetails("1", "Name", "Dev", Category.APP, 12, 100.0f, "icon", null, "desc")
        coEvery { repository.getAppDetails("1") } returns flowOf(domain)

        val result = useCase("1").first()
        assertEquals(null, result.screenshotUrlList)
    }
}