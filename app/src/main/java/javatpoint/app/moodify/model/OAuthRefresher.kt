// OAuthRefresher.kt
package javatpoint.app.moodify.model

import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import org.json.JSONObject
import java.io.IOException
import android.util.Base64

class OAuthRefresher(
    private val tokenStore: TokenStore
) : Authenticator {

    private val client = OkHttpClient()

    override fun authenticate(route: Route?, response: Response): Request? {
        // don’t retry more than once
        if (responseCount(response) > 1) return null

        // perform blocking refresh
        val (newAccess, newRefresh, expiresIn) = runBlocking {
            val creds = "${Constants.CLIENT_ID}:${Constants.CLIENT_SECRET}"
                .toByteArray()
                .let { Base64.encodeToString(it, Base64.NO_WRAP) }

            val body = FormBody.Builder()
                .add("grant_type", "refresh_token")
                .add("refresh_token", tokenStore.refreshToken() ?: error("No refresh token"))
                .build()

            val req = Request.Builder()
                .url(Constants.TOKEN_ENDPOINT)
                .header("Authorization", "Basic $creds")
                .post(body)
                .build()

            client.newCall(req).execute().use { resp ->
                if (!resp.isSuccessful) throw IOException("Refresh failed: ${resp.code}")
                val j = JSONObject(resp.body!!.string())
                Triple(
                    j.getString("access_token"),
                    j.getString("refresh_token"),
                    j.getLong("expires_in")
                )
            }
        }

        // persist freshly minted tokens (blocking)
        runBlocking {
            tokenStore.store(
                access       = newAccess,
                refresh      = newRefresh,
                expiresInSec = expiresIn
                )
            }

        // re-build original request with new bearer token
        return response.request.newBuilder()
            .header("Authorization", "Bearer $newAccess")
            .build()
    }

    private fun responseCount(response: Response): Int {
        var prior = response.priorResponse
        var count = 1
        while (prior != null) {
            count++
            prior = prior.priorResponse
        }
        return count
    }
}
