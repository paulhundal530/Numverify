package com.phundal.numverify.feature.numverify.impl

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * The wire-to-domain boundary is the whole point of the api/impl split, so it is worth pinning:
 * these mappers are the only place a response model is allowed to leak out of this module.
 */
class ResponseMappersTest {

    @Test
    fun `countries are keyed by country code`() {
        val countries = mapOf(
            "GB" to CountryInformationResponseModel(countryName = "United Kingdom", diallingCode = "+44"),
            "US" to CountryInformationResponseModel(countryName = "United States", diallingCode = "+1"),
        ).toCountries()

        assertEquals(setOf("GB", "US"), countries.countries.keys)
        assertEquals("United Kingdom", countries.countries.getValue("GB").countryName)
        assertEquals("+1", countries.countries.getValue("US").diallingCode)
    }

    @Test
    fun `validation response maps every field across`() {
        val validation = NumberValidationResponseModel(
            valid = true,
            number = "14158586273",
            localFormat = "4158586273",
            internationalFormat = "+14158586273",
            countryPrefix = "+1",
            countryCode = "US",
            location = "Novato",
            carrier = "AT&T Mobility LLC",
            lineType = "mobile",
        ).toNumberValidation()

        assertEquals(true, validation.valid)
        assertEquals("14158586273", validation.number)
        assertEquals("4158586273", validation.localFormat)
        assertEquals("+14158586273", validation.internationalFormat)
        assertEquals("+1", validation.countryPrefix)
        assertEquals("US", validation.countryCode)
        assertEquals("Novato", validation.location)
        assertEquals("AT&T Mobility LLC", validation.carrier)
        assertEquals("mobile", validation.lineType)
    }

    @Test
    fun `absent optional fields map to empty strings`() {
        val validation = NumberValidationResponseModel(valid = false, number = "123").toNumberValidation()

        assertEquals(false, validation.valid)
        assertEquals("", validation.carrier)
        assertEquals("", validation.location)
    }
}
