package javatpoint.app.moodify.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import javatpoint.app.moodify.R
import javatpoint.app.moodify.model.Track

class TrackAdapter : RecyclerView.Adapter<TrackAdapter.ViewHolder>() {

    private var tracks: List<Track> = emptyList()

    fun setTracks(newTracks: List<Track>) {
        tracks = newTracks
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.trackTitle)
        val artist: TextView = itemView.findViewById(R.id.trackArtist)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_track, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val t = tracks[position]
        holder.title.text = t.title
        holder.artist.text = t.artist
    }
    override fun getItemCount() = tracks.size
}
