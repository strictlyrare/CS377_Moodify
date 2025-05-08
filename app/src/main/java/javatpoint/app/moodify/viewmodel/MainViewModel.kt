package javatpoint.app.moodify.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javatpoint.app.moodify.util.Mood
import javatpoint.app.moodify.util.MoodMapper
import javatpoint.app.moodify.model.MusicRepository
import javatpoint.app.moodify.model.Track
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: MusicRepository
) : ViewModel() {

    private val _tracks = MutableLiveData<List<Track>>()
    val tracks: LiveData<List<Track>> get() = _tracks

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun searchTracksByMood(mood: Mood) {
        viewModelScope.launch {
            try {
                val genreKeyword = MoodMapper.mapMoodToGenre(mood.name)
                val trackList = repository.searchTracksByMood(genreKeyword)

                if (trackList.isEmpty()) {
                    _error.value = "No songs found for this mood."
                } else {
                    _tracks.value = trackList
                }
            } catch (e: Exception) {
                _error.value = "Failed to load tracks: ${e.message}"
            }
        }
    }
}
