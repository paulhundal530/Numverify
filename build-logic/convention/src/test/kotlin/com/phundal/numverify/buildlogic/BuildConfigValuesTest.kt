package com.phundal.numverify.buildlogic

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

/**
 * Unit tests for the rules the convention plugins encode. These run without a Gradle build, so
 * they stay fast enough to be part of the normal edit/compile loop on the build logic itself.
 */
class BuildConfigValuesTest {

    @Test
    fun `string literal is quoted`() {
        assertEquals("\"abc\"", BuildConfigValues.stringLiteral("abc"))
    }

    @Test
    fun `string literal escapes quotes and backslashes`() {
        assertEquals(
            """"a\"b\\c"""",
            BuildConfigValues.stringLiteral("""a"b\c"""),
        )
    }

    @Test
    fun `string literal escapes control characters`() {
        assertEquals("\"a\\nb\\rc\\td\"", BuildConfigValues.stringLiteral("a\nb\rc\td"))
    }

    @Test
    fun `api key is trimmed`() {
        assertEquals("key", BuildConfigValues.requireApiKey("  key  "))
    }

    @Test
    fun `blank api key is rejected`() {
        val failure = assertThrows(IllegalArgumentException::class.java) {
            BuildConfigValues.requireApiKey("   ")
        }
        assertEquals(true, failure.message!!.contains(API_KEY_PROPERTY))
    }

    @Test
    fun `base url keeps an existing trailing slash`() {
        assertEquals(
            "https://apilayer.net/api/",
            BuildConfigValues.normalizeBaseUrl("https://apilayer.net/api/"),
        )
    }

    @Test
    fun `base url gains a trailing slash`() {
        assertEquals(
            "https://apilayer.net/api/",
            BuildConfigValues.normalizeBaseUrl("  https://apilayer.net/api  "),
        )
    }

    @Test
    fun `base url must be absolute`() {
        val failure = assertThrows(IllegalArgumentException::class.java) {
            BuildConfigValues.normalizeBaseUrl("apilayer.net/api/")
        }
        assertEquals(true, failure.message!!.contains("absolute"))
    }

    @Test
    fun `blank base url is rejected`() {
        val failure = assertThrows(IllegalArgumentException::class.java) {
            BuildConfigValues.normalizeBaseUrl("")
        }
        assertEquals(true, failure.message!!.contains(BASE_URL_PROPERTY))
    }
}
