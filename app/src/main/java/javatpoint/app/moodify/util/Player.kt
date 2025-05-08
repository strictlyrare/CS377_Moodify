// Player.kt
package javatpoint.app.moodify.util

import android.content.Context
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import javatpoint.app.moodify.model.Constants.CLIENT_ID
import javatpoint.app.moodify.model.Constants.REDIRECT_URI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class Player(private val context: Context) {

    private var remote: SpotifyAppRemote? = null

    /** Suspend until we’re connected (or throw on failure) */
    private suspend fun connect(): SpotifyAppRemote = withContext(Dispatchers.Main) {
        suspendCancellableCoroutine<SpotifyAppRemote> { cont ->
            val params = ConnectionParams.Builder(CLIENT_ID)
                .setRedirectUri(REDIRECT_URI)
                .showAuthView(false)
                .build()

            SpotifyAppRemote.connect(context, params, object : Connector.ConnectionListener {
                override fun onConnected(appRemote: SpotifyAppRemote) {
                    android.util.Log.d("Player", "Connected to Spotify App Remote successfully!")
                    cont.resume(appRemote)
                }
                override fun onFailure(throwable: Throwable) {
                    android.util.Log.e("Player", "Failed to connect to Spotify App Remote", throwable)
                    cont.resumeWithException(throwable)
                }
            })

            // If the coroutine is cancelled, disconnect
            cont.invokeOnCancellation {
                remote?.let(SpotifyAppRemote::disconnect)
            }
        }
    }

    /** Play a list of URIs in sequence */
    suspend fun play(uris: List<String>) {
        try {
            val r = remote ?: connect().also { remote = it }
            r.playerApi.play(uris.first())
            uris.drop(1).forEach { r.playerApi.queue(it) }
        } catch (e: Exception) {
            android.util.Log.e("Player", "Could not connect to Spotify: ${e.message}")
            // OPTIONAL: you could even show a Toast here if you want
        }
    }

    /** Clean up */
    fun disconnect() {
        remote?.let(SpotifyAppRemote::disconnect)
        remote = null
    }
}
