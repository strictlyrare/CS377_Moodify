package javatpoint.app.moodify.model

import javatpoint.app.moodify.BuildConfig

object Constants {
    const val CLIENT_ID     = BuildConfig.SPOTIFY_CLIENT_ID
    const val CLIENT_SECRET = BuildConfig.SPOTIFY_CLIENT_SECRET
    const val REDIRECT_URI  = "moodify://callback"
    const val TOKEN_ENDPOINT = "https://accounts.spotify.com/api/token"

    fun generateCodeVerifier(): String {
        val bytes = ByteArray(32)
        java.security.SecureRandom().nextBytes(bytes)
        return android.util.Base64.encodeToString(bytes, android.util.Base64.URL_SAFE or android.util.Base64.NO_WRAP or android.util.Base64.NO_PADDING)
    }

    fun generateCodeChallenge(verifier: String): String {
        val digest = java.security.MessageDigest.getInstance("SHA-256").digest(verifier.toByteArray())
        return android.util.Base64.encodeToString(digest, android.util.Base64.URL_SAFE or android.util.Base64.NO_WRAP or android.util.Base64.NO_PADDING)
    }

    data class GenreSeedResponse(
        val genres: List<String>
    )

}
