package io.mmaltsev.vkeducation.data.appdetails

import io.mmaltsev.vkeducation.data.appdetails.AppDetailsDto // ⬅️ БЕЗ .network
import io.mmaltsev.vkeducation.data.appdetails.local.AppDetailsDao
import io.mmaltsev.vkeducation.data.appdetails.local.AppDetailsEntity
import io.mmaltsev.vkeducation.data.appdetails.local.AppDetailsEntityMapper
import io.mmaltsev.vkeducation.data.appdetails.network.AppApi
import io.mmaltsev.vkeducation.domain.appdetails.AppDetails
import io.mmaltsev.vkeducation.domain.appdetails.Category
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AppDetailsRepositoryImplTest {
    private val appApi: AppApi = mockk()
    private val dao: AppDetailsDao = mockk()
    private val mapper: AppDetailsMapper = mockk()
    private val entityMapper: AppDetailsEntityMapper = mockk()
    private val repository = AppDetailsRepositoryImpl(appApi, dao, mapper, entityMapper)

    @Test
    fun `getAppDetails returns domain from DB when entity exists`() = runTest {
        val entity = AppDetailsEntity("1", "Name", "Dev", Category.APP, 12, 100.0f, "icon", "screenshots", "desc")
        val domain = AppDetails("1", "Name", "Dev", Category.APP, 12, 100.0f, "icon", null, "desc")
        coEvery { dao.getAppDetails("1") } returns flowOf(entity)
        coEvery { entityMapper.toDomain(entity) } returns domain

        val result = repository.getAppDetails("1").first()
        assertEquals(domain, result)
        coVerify(exactly = 0) { appApi.getAppDetails(any()) }
    }

    @Test
    fun `getAppDetails fetches from API and saves to DB when entity is null`() = runTest {
        val dto = AppDetailsDto("1", "Name", "Dev", Category.APP, 12, 100.0, "icon", listOf("s"), "desc")
        val domain = AppDetails("1", "Name", "Dev", Category.APP, 12, 100.0f, "icon", listOf("s"), "desc")
        val entity = AppDetailsEntity("1", "Name", "Dev", Category.APP, 12, 100.0f, "icon", null, "desc")

        coEvery { dao.getAppDetails("1") } returns flowOf(null)
        coEvery { appApi.getAppDetails("1") } returns dto
        coEvery { mapper.toDomain(dto) } returns domain
        coEvery { entityMapper.toEntity(domain) } returns entity
        coEvery { dao.insertAppDetails(entity) } returns Unit

        val result = repository.getAppDetails("1").first()
        assertEquals(domain, result)
        coVerify { appApi.getAppDetails("1") }
        coVerify { dao.insertAppDetails(entity) }
    }

    @Test
    fun `getAppDetails throws exception when API fails and DB is empty`() = runTest {
        coEvery { dao.getAppDetails("1") } returns flowOf(null)
        coEvery { appApi.getAppDetails("1") } throws RuntimeException("Network error")

        val result = runCatching { repository.getAppDetails("1").first() }
        assertTrue(result.isFailure)
        assertEquals("Network error", result.exceptionOrNull()?.message)
    }

    @Test
    fun `getAppDetails verifies mapper is called with correct DTO`() = runTest {
        val dto = AppDetailsDto("1", "Name", "Dev", Category.APP, 12, 100.0, "icon", null, "desc")
        val domain = AppDetails("1", "Name", "Dev", Category.APP, 12, 100.0f, "icon", null, "desc")
        val entity = AppDetailsEntity("1", "Name", "Dev", Category.APP, 12, 100.0f, "icon", null, "desc")

        coEvery { dao.getAppDetails("1") } returns flowOf(null)
        coEvery { appApi.getAppDetails("1") } returns dto
        coEvery { mapper.toDomain(dto) } returns domain
        coEvery { entityMapper.toEntity(domain) } returns entity
        coEvery { dao.insertAppDetails(entity) } returns Unit

        repository.getAppDetails("1").first()
        coVerify { mapper.toDomain(dto) }
    }

    @Test
    fun `getAppDetails verifies DB insertion is called exactly once`() = runTest {
        val dto = AppDetailsDto("1", "Name", "Dev", Category.APP, 12, 100.0, "icon", null, "desc")
        val domain = AppDetails("1", "Name", "Dev", Category.APP, 12, 100.0f, "icon", null, "desc")
        val entity = AppDetailsEntity("1", "Name", "Dev", Category.APP, 12, 100.0f, "icon", null, "desc")

        coEvery { dao.getAppDetails("1") } returns flowOf(null)
        coEvery { appApi.getAppDetails("1") } returns dto
        coEvery { mapper.toDomain(dto) } returns domain
        coEvery { entityMapper.toEntity(domain) } returns entity
        coEvery { dao.insertAppDetails(entity) } returns Unit

        repository.getAppDetails("1").first()
        coVerify(exactly = 1) { dao.insertAppDetails(entity) }
    }
}