package javatpoint.app.moodify.model

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SpotifyWebService {
    @GET("v1/search")
    suspend fun searchByGenre(
        @Header("Authorization") authHeader: String,
        @Query("q") genreQuery: String,
        @Query("type") type: String = "track",
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int // <— Add this
    ): TrackSearchResponse

    @GET("v1/recommendations/available-genre-seeds")
    suspend fun getAvailableGenres(
        @Header("Authorization") authHeader: String
    ): Constants.GenreSeedResponse

}
