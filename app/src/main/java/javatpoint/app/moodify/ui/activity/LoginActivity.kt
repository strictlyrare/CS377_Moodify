package javatpoint.app.moodify.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import javatpoint.app.moodify.model.Constants
import javatpoint.app.moodify.R
import javatpoint.app.moodify.model.TokenStore
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var tokenStore: TokenStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        tokenStore = TokenStore(this)

        findViewById<MaterialButton>(R.id.loginButton).setOnClickListener {
            val codeVerifier = Constants.generateCodeVerifier()
            val codeChallenge = Constants.generateCodeChallenge(codeVerifier)

            getSharedPreferences("auth", MODE_PRIVATE)
                .edit()
                .putString("verifier", codeVerifier)
                .apply()

            val uri = Uri.Builder()
                .scheme("https")
                .authority("accounts.spotify.com")
                .path("authorize")
                .appendQueryParameter("client_id", Constants.CLIENT_ID)
                .appendQueryParameter("response_type", "code")
                .appendQueryParameter("redirect_uri", Constants.REDIRECT_URI)
                .appendQueryParameter("scope", "user-read-private user-read-email playlist-modify-public streaming")
                .appendQueryParameter("code_challenge_method", "S256")
                .appendQueryParameter("code_challenge", codeChallenge)
                .build()

            startActivity(Intent(Intent.ACTION_VIEW, uri))
        }

        findViewById<MaterialButton>(R.id.registerButton).setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.spotify.com/signup")))
        }
    }

    override fun onStart() {
        super.onStart()

        lifecycleScope.launch {
            val token = tokenStore.accessToken()
            if (!token.isNullOrBlank()) {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
        }
    }
}
