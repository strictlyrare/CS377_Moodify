package javatpoint.app.moodify.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch
import android.widget.AdapterView
import android.widget.Toast
import javatpoint.app.moodify.util.Mood
import javatpoint.app.moodify.util.MoodMapper
import javatpoint.app.moodify.model.MusicRepository
import javatpoint.app.moodify.util.Player
import javatpoint.app.moodify.R
import javatpoint.app.moodify.model.SpotifyRetrofit
import javatpoint.app.moodify.model.TokenStore
import javatpoint.app.moodify.ui.adapter.TrackAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var moodSpinner: Spinner
    private lateinit var playButton: MaterialButton
    private lateinit var trackRecycler: RecyclerView

    private val trackAdapter = TrackAdapter()

    private val store by lazy { TokenStore(this) }
    private val api by lazy { SpotifyRetrofit.provide(store) }
    private val repo by lazy { MusicRepository(api, store) }
    private val player by lazy { Player(this) }

    private var selectedMood: Mood = Mood.HAPPY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        moodSpinner = findViewById(R.id.moodSpinner)
        playButton = findViewById(R.id.playButton)
        trackRecycler = findViewById(R.id.trackRecycler)

        lifecycleScope.launch {
            try {
                val token = store.accessToken()
                if (token != null) {
                    val genres = api.getAvailableGenres("Bearer $token").genres
                    genres.forEach { Log.d("GenreSeed", it) }
                }
            } catch (e: Exception) {
                Log.e("GenreSeed", "Failed to fetch genres: ${e.message}")
            }
        }


        // — Spinner setup —
        moodSpinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            Mood.values()
        )
        moodSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>,
                view: android.view.View?,
                pos: Int,
                id: Long
            ) {
                selectedMood = Mood.entries.toTypedArray()[pos]
            }
        }

        // — RecyclerView setup —
        trackRecycler.layoutManager = LinearLayoutManager(this)
        trackRecycler.adapter = trackAdapter

        // — Play button handler —
        playButton.setOnClickListener {
            lifecycleScope.launch {
                try {
                    val moodKeyword = MoodMapper.mapMoodToGenre(selectedMood.name)
                    val tracks = repo.searchTracksByMood(moodKeyword)

                    if (tracks.isEmpty()) {
                        Toast.makeText(
                            this@MainActivity,
                            "No songs found for this mood. Try another!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        trackAdapter.setTracks(tracks)
                        player.play(tracks.map { it.uri })

                        val intent = Intent(this@MainActivity, RewardsActivity::class.java)
                        intent.putExtra("MOOD", selectedMood.name)
                        startActivity(intent)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(
                        this@MainActivity,
                        "Error loading tracks: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        player.disconnect()
    }
}