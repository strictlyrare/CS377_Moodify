package javatpoint.app.moodify.model

import okhttp3.Interceptor
import okhttp3.Response
import kotlinx.coroutines.runBlocking

class TokenInterceptor(private val tokenStore: TokenStore) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { tokenStore.accessToken() }
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()

        return chain.proceed(request)
    }
}
