// RecommendationDto.kt
package javatpoint.app.moodify.model

import com.squareup.moshi.JsonClass

// Search API returns an outer "tracks" object
@JsonClass(generateAdapter = true)
data class TrackSearchResponse(
    val tracks: Tracks
)

@JsonClass(generateAdapter = true)
data class Tracks(
    val items: List<TrackDto>
)

@JsonClass(generateAdapter = true)
data class TrackDto(
    val uri: String,
    val name: String,
    val artists: List<ArtistDto>
)

@JsonClass(generateAdapter = true)
data class ArtistDto(
    val name: String
)

// Map to your domain model
fun TrackDto.toDomain() =
    Track(uri, name, artists.joinToString(", ") { it.name })

data class Track(
    val uri: String,
    val title: String,
    val artist: String
)
