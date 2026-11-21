package io.mmaltsev.vkeducation.data.appdetails

import io.mmaltsev.vkeducation.data.appdetails.AppDetailsDto // ⬅️ БЕЗ .network
import io.mmaltsev.vkeducation.domain.appdetails.AppDetails
import io.mmaltsev.vkeducation.domain.appdetails.Category
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AppDetailsMapperTest {
    private val mapper = AppDetailsMapper()

    @Test
    fun `toDomain maps all fields correctly`() {
        val dto = AppDetailsDto("1", "Test App", "Test Dev", Category.APP, 12, 100.5, "icon", listOf("s1"), "desc")
        val domain = mapper.toDomain(dto)
        assertEquals("1", domain.id)
        assertEquals(100.5f, domain.size, 0.001f)
        assertEquals(listOf("s1"), domain.screenshotUrlList)
    }

    @Test
    fun `toDomain handles null screenshots correctly`() {
        val dto = AppDetailsDto("1", "App", "Dev", Category.APP, 12, 100.5, "icon", null, "desc")
        val domain = mapper.toDomain(dto)
        assertNull(domain.screenshotUrlList)
    }

    @Test
    fun `toDto maps all fields correctly`() {
        val domain = AppDetails("1", "Test App", "Test Dev", Category.APP, 12, 100.5f, "icon", listOf("s1"), "desc")
        val dto = mapper.toDto(domain)
        assertEquals("1", dto.id)
        // ⬇️ ИСПРАВЛЕНО: добавлена погрешность 0.001 для сравнения Double
        assertEquals(100.5, dto.size, 0.001)
        assertEquals(listOf("s1"), dto.screenshots)
    }

    @Test
    fun `toDto handles null screenshotUrlList correctly`() {
        val domain = AppDetails("1", "App", "Dev", Category.APP, 12, 100.5f, "icon", null, "desc")
        val dto = mapper.toDto(domain)
        assertNull(dto.screenshots)
    }

    @Test
    fun `round trip conversion preserves data`() {
        val original = AppDetails("2", "Round Trip", "Dev", Category.GAME, 18, 250.0f, "icon", listOf("s1"), "desc")
        val dto = mapper.toDto(original)
        val result = mapper.toDomain(dto)
        assertEquals(original.id, result.id)
        assertEquals(original.name, result.name)
        assertEquals(original.size, result.size, 0.001f)
    }
}