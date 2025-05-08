package javatpoint.app.moodify.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException
import android.widget.Toast
import javatpoint.app.moodify.model.Constants
import javatpoint.app.moodify.model.TokenStore

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val data = intent?.data
        if (data != null && data.toString().startsWith(Constants.REDIRECT_URI)) {
            val code = data.getQueryParameter("code")
            val error = data.getQueryParameter("error")

            if (code != null) {
                val verifier = getSharedPreferences("auth", MODE_PRIVATE)
                    .getString("verifier", null)

                if (verifier == null) {
                    Toast.makeText(this, "Missing code verifier", Toast.LENGTH_SHORT).show()
                    finish()
                    return
                }

                lifecycleScope.launch {
                    try {
                        exchangeCodeForToken(code, verifier, TokenStore(this@AuthActivity))
                        startActivity(Intent(this@AuthActivity, MainActivity::class.java))
                        finish()
                    } catch (e: Exception) {
                        Toast.makeText(this@AuthActivity, "Auth failed: ${e.message}", Toast.LENGTH_LONG).show()
                        finish()
                    }
                }
            } else if (error != null) {
                Toast.makeText(this, "Spotify Auth Error: $error", Toast.LENGTH_LONG).show()
                finish()
            } else {
                finish()
            }
        } else {
            finish()
        }
    }

    private suspend fun exchangeCodeForToken(
        code: String,
        verifier: String,
        tokenStore: TokenStore
    ) = withContext(Dispatchers.IO) {
        val form = FormBody.Builder()
            .add("grant_type", "authorization_code")
            .add("code", code)
            .add("redirect_uri", Constants.REDIRECT_URI)
            .add("client_id", Constants.CLIENT_ID)
            .add("code_verifier", verifier)
            .build()

        val req = Request.Builder()
            .url(Constants.TOKEN_ENDPOINT)
            .post(form)
            .build()

        val client = OkHttpClient()
        val response = client.newCall(req).execute()

        if (!response.isSuccessful) {
            throw IOException("Token exchange failed: ${response.code}")
        }

        val json = JSONObject(response.body!!.string())
        val accessToken = json.getString("access_token")
        val refresh = json.getString("refresh_token")
        val expiresIn = json.getLong("expires_in")

        tokenStore.store(
            access = accessToken,
            refresh = refresh,
            expiresInSec = expiresIn
        )
    }
}
