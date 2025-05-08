package javatpoint.app.moodify.model

import android.util.Log

class MusicRepository(
    private val api: SpotifyWebService,
    private val tokenStore: TokenStore
) {
    suspend fun searchTracksByMood(mood: String): List<Track> {
        val accessToken = tokenStore.accessToken()
            ?: throw IllegalStateException("Missing access token")

        val authHeader = "Bearer $accessToken"
        Log.d("SearchQuery", "Searching for: genre:$mood")
        val query = "genre:$mood"
        val randomOffset = (0..100).random() // You can increase this up to 1000 if needed

        val response = api.searchByGenre(
            authHeader = authHeader,
            genreQuery = query,
            type = "track",
            limit = 10,
            offset = randomOffset
        )

        return response.tracks.items.map { it.toDomain() }
    }
}
