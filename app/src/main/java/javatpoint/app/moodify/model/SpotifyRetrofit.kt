// SpotifyRetrofit.kt
package javatpoint.app.moodify.model

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object SpotifyRetrofit {

    private const val BASE_URL = "https://api.spotify.com/"

    /**
     * Creates a fully-configured Retrofit service that
     * automatically refreshes expired tokens.
     */
    fun provide(tokenStore: TokenStore): SpotifyWebService {
        val refresher = OAuthRefresher(tokenStore)

        val logger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // OkHttp client with both interceptor and authenticator
        val okClient = OkHttpClient.Builder()
            .authenticator(refresher)
            .addInterceptor(TokenInterceptor(tokenStore))
            .addInterceptor(logger)
            .addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("Accept", "application/json")
                    .addHeader("Content-Type", "application/json")
                    .build()
                chain.proceed(newRequest)
            }
            .build()

        // Build a Moshi instance that knows how to handle Kotlin data classes
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        // Finally create Retrofit using Moshi as the converter
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okClient)
            .build()
            .create(SpotifyWebService::class.java)
    }
}
