package javatpoint.app.moodify.util

enum class Mood(
    val targetValence: Float,
    val targetEnergy:  Float,
    val targetDance:   Float
) {
    HAPPY   (0.95f, 0.80f, 0.75f),
    CHILL   (0.80f, 0.30f, 0.40f),
    SAD     (0.20f, 0.25f, 0.15f),
    ANGRY   (0.15f, 0.80f, 0.20f),
    FOCUSED (0.55f, 0.45f, 0.10f),
    ENERGETIC(0.90f, 0.95f, 0.85f),
    RELAXED (0.60f, 0.20f, 0.20f),
    ROMANTIC(0.70f, 0.50f, 0.60f),
    PARTY   (0.90f, 0.85f, 0.95f),
    SLEEPY  (0.30f, 0.10f, 0.05f),
    UPBEAT  (0.85f, 0.75f, 0.70f)
}
