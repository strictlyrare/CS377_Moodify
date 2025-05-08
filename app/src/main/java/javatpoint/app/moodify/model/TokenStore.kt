package javatpoint.app.moodify.model

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "spotify_tokens")

class TokenStore(private val ctx: Context) {

    private val ACCESS  = stringPreferencesKey("access_token")
    private val REFRESH = stringPreferencesKey("refresh_token")
    private val EXPIRES = longPreferencesKey("expires_at_epoch")

    /** Save a fresh token set (called from AuthActivity). */
    suspend fun store(access: String, refresh: String, expiresInSec: Long) {
        val epoch = System.currentTimeMillis() / 1000 + expiresInSec
        ctx.dataStore.edit { prefs ->
            prefs[ACCESS]  = access
            prefs[REFRESH] = refresh
            prefs[EXPIRES] = epoch
        }
    }

    /**
     * Returns the current non-expired access token,
     * or null if missing/expired.
     */
    suspend fun accessToken(): String? {
        val (tok, exp) = ctx.dataStore.data
            .map { it[ACCESS] to it[EXPIRES] }
            .first()
        val now = System.currentTimeMillis() / 1000
        return if (tok != null && exp != null && now < exp) tok else null
    }

    /** Refresh token – may be null on first launch */
    suspend fun refreshToken(): String? =
        ctx.dataStore.data.map { it[REFRESH] }.first()
}
