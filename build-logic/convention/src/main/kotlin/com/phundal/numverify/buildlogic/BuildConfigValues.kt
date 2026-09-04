package com.phundal.numverify.buildlogic

/**
 * Pure helpers used by the convention plugins.
 *
 * Deliberately free of any Gradle API so that the rules encoded here can be unit tested without
 * spinning up a build (see `BuildConfigValuesTest`).
 */
internal object BuildConfigValues {

    /** Renders [value] as a Java string literal suitable for a generated `BuildConfig` field. */
    fun stringLiteral(value: String): String = buildString {
        append('"')
        for (character in value) {
            when (character) {
                '\\' -> append("\\\\")
                '"' -> append("\\\"")
                '\n' -> append("\\n")
                '\r' -> append("\\r")
                '\t' -> append("\\t")
                else -> append(character)
            }
        }
        append('"')
    }

    /**
     * Validates the configured API key.
     *
     * @throws IllegalArgumentException when the key is blank, which otherwise surfaces much later
     * as a confusing HTTP 101 response from the NumVerify service.
     */
    fun requireApiKey(value: String): String {
        require(value.isNotBlank()) {
            "The NumVerify API key is not set. Set `$API_KEY_PROPERTY` in gradle.properties " +
                "(or ~/.gradle/gradle.properties), or configure `numverify { apiKey = \"...\" }` " +
                "in the module build script."
        }
        return value.trim()
    }

    /**
     * Normalises the API base URL: Retrofit requires an absolute URL with a trailing slash, so
     * accept either form and repair the missing slash rather than failing the build.
     */
    fun normalizeBaseUrl(value: String): String {
        val trimmed = value.trim()
        require(trimmed.isNotEmpty()) {
            "The NumVerify base URL is not set. Set `$BASE_URL_PROPERTY` in gradle.properties " +
                "(or ~/.gradle/gradle.properties), or configure `numverify { baseUrl = \"...\" }` " +
                "in the module build script."
        }
        require(trimmed.startsWith("http://") || trimmed.startsWith("https://")) {
            "The NumVerify base URL must be absolute (http:// or https://), but was `$trimmed`."
        }
        return if (trimmed.endsWith("/")) trimmed else "$trimmed/"
    }
}

/** Gradle property holding the NumVerify API access key. */
internal const val API_KEY_PROPERTY = "numverify.apiKey"

/** Gradle property holding the NumVerify API base URL. */
internal const val BASE_URL_PROPERTY = "numverify.baseUrl"
