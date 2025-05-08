package javatpoint.app.moodify.util

object MoodMapper {
    fun mapMoodToGenre(mood: String): String {
        return when (mood.trim().uppercase()) {
            "HAPPY"      -> "dance"
            "SAD"        -> "acoustic"
            "CHILL"      -> "lo-fi"
            "ENERGETIC"  -> "edm"
            "ANGRY"      -> "metal"
            "RELAXED"    -> "ambient"
            "ROMANTIC"   -> "r-n-b"
            "PARTY"      -> "house"
            "FOCUSED"    -> "instrumental"
            "SLEEPY"     -> "piano"
            "UPBEAT"     -> "funk"
            else         -> "pop"
        }
    }
}
