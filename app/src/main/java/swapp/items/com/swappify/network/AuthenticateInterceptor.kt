package swapp.items.com.swappify.network

import okhttp3.Interceptor
import okhttp3.Response
import swapp.items.com.swappify.BuildConfig
import swapp.items.com.swappify.injection.scopes.PerApplication
import javax.inject.Inject

@PerApplication
class AuthenticateInterceptor @Inject constructor() : Interceptor {

    private val CONTENT_ACCEPT = "Accept"
    private val CONTENT_JSON_TYPE = "application/json"
    private val USER_KEY = "user-key"

    private var headerMap = mutableMapOf<String, String>()

    init {
        headerMap.put(CONTENT_ACCEPT, CONTENT_JSON_TYPE)
        headerMap.put(USER_KEY, BuildConfig.GAME_API_KEY )
    }

    fun setInterceptor(headerMap: MutableMap<String, String>) {
        this.headerMap.putAll(headerMap)
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().let {
            val requestBuilder = it.newBuilder()
            headerMap.forEach { (key, value) ->
                requestBuilder.addHeader(key, value)
            }
            requestBuilder.build()
        }
        return chain.proceed(request)
    }
}
