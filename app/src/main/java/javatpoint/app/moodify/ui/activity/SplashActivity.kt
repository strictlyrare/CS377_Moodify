package javatpoint.app.moodify.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import javatpoint.app.moodify.model.TokenStore
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    private val store by lazy { TokenStore(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            val hasToken = store.accessToken() != null
            val next = if (hasToken) MainActivity::class.java
            else AuthActivity::class.java

            startActivity(Intent(this@SplashActivity, next))
            finish()
        }
    }
}
