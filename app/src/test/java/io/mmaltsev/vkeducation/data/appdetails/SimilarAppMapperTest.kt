package io.mmaltsev.vkeducation.data.appdetails

import io.mmaltsev.vkeducation.data.appdetails.SimilarAppDto // ⬅️ БЕЗ .network
import io.mmaltsev.vkeducation.domain.appdetails.SimilarApp
import org.junit.Assert.assertEquals
import org.junit.Test

class SimilarAppMapperTest {
    private val mapper = SimilarAppMapper()

    @Test
    fun `toDomain maps all fields correctly`() {
        val dto = SimilarAppDto("1", "App", "url")
        val domain = mapper.toDomain(dto)
        assertEquals("1", domain.id)
        assertEquals("App", domain.name)
        assertEquals("url", domain.iconUrl)
    }

    @Test
    fun `toDto maps all fields correctly`() {
        val domain = SimilarApp("1", "App", "url")
        val dto = mapper.toDto(domain)
        assertEquals("1", dto.id)
        assertEquals("App", dto.name)
        assertEquals("url", dto.iconUrl)
    }

    @Test
    fun `round trip conversion preserves data`() {
        val original = SimilarApp("2", "Test", "test_url")
        val dto = mapper.toDto(original)
        val result = mapper.toDomain(dto)
        assertEquals(original, result)
    }

    @Test
    fun `toDomain handles empty strings`() {
        val dto = SimilarAppDto("", "", "")
        val domain = mapper.toDomain(dto)
        assertEquals("", domain.id)
        assertEquals("", domain.name)
    }

    @Test
    fun `toDto handles empty strings`() {
        val domain = SimilarApp("", "", "")
        val dto = mapper.toDto(domain)
        assertEquals("", dto.id)
        assertEquals("", dto.name)
    }
}