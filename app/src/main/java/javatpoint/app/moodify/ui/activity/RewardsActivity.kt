// RewardsActivity.kt
package javatpoint.app.moodify.ui.activity

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import javatpoint.app.moodify.R

class RewardsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rewards)

        val moodText = findViewById<TextView>(R.id.moodText)
        val rewardMessage = findViewById<TextView>(R.id.rewardMessage)
        val backButton = findViewById<MaterialButton>(R.id.backButton)

        // Get mood from Intent
        val mood = intent.getStringExtra("MOOD") ?: "Unknown"
        moodText.text = "You selected the mood: $mood"
        rewardMessage.text = "Thanks for vibing with us! 🎧"

        backButton.setOnClickListener {
            finish() // Go back to MainActivity
        }
    }
}
