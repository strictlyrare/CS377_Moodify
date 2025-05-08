# Moodify 🎧

Moodify is an Android application that integrates with Spotify's Web API to provide music recommendations based on the user's selected mood. The app authenticates users via Spotify, fetches personalized track data, and displays playable song suggestions.

## Features

- 🎵 Spotify Login via OAuth2
- 😄 Mood-based genre mapping
- 🔁 Token refreshing using a `TokenInterceptor`
- 📄 Displays curated track lists with RecyclerView
- 📲 Responsive UI across multiple activities
- 🧠 MVVM architecture with ViewModel and Repository pattern
- 🔧 Retrofit integration for HTTP communication


## Project Structure

Moodify/

- ├── model/ # Network and data models (Spotify APIs, DTOs, Auth)

- ├── ui/ # UI components including Activities and Adapters

- ├── util/ # Utility classes (Mood logic, Playback)

- ├── viewmodel/ # ViewModels for UI logic separation

## Architecture

Moodify follows the **MVVM (Model-View-ViewModel)** architecture pattern:

- **Model**: Handles data sources (Spotify API, token storage)
- **ViewModel**: Supplies observable data to the views and handles logic
- **View (UI)**: Renders data via Activities and RecyclerView Adapters

## Dependencies

- [Retrofit](https://square.github.io/retrofit/) for HTTP requests
- [Spotify Android SDK](https://developer.spotify.com/documentation/android/)
- [OkHttp](https://square.github.io/okhttp/) for networking
- [Glide](https://github.com/bumptech/glide) (assumed for image loading)
- [AndroidX Lifecycle](https://developer.android.com/jetpack/androidx/releases/lifecycle) for ViewModels

## Getting Started

1. **Clone this repository**:

    ```bash
    git clone https://github.com/strictlyrare/CS377_Moodify.git
    ```

2. **Open in Android Studio**

    - File > Open > Select the cloned project directory

3. **Add your Spotify credentials**:

    Replace placeholders in `AuthActivity.kt` or relevant config file:

    ```kotlin
    val clientId = "YOUR_SPOTIFY_CLIENT_ID"
    val redirectUri = "YOUR_SPOTIFY_REDIRECT_URI"
    ```

4. **Run the app**:

    Connect a device or emulator and click `Run`.

## Authors

**Noah Carges**  
Senior CS student at NAU – [GitHub](https://github.com/strictlyrare)
**Elliott Kinsley**  
Senior CS student at NAU – [GitHub](https://github.com/Ekinsley02)

## License

This project is for academic use. Contact the author for reuse permissions.
