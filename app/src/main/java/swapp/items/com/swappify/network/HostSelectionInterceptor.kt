package swapp.items.com.swappify.network

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HostSelectionInterceptor @Inject constructor() : Interceptor {
    private var host: String? = null
    private var scheme: String? = null

    fun setInterceptor(url: String?) {
        val httpUrl = HttpUrl.parse(url)
        scheme = httpUrl?.scheme()
        host = httpUrl?.host()
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var original = chain.request()

        // If new Base URL is properly formatted then replace the old one
        if (scheme != null && host != null) {
            val newUrl = original.url().newBuilder()
                    .scheme(scheme)
                    .host(host)
                    .build()
            original = original.newBuilder()
                    .url(newUrl)
                    .build()
        }
        return chain.proceed(original)
    }
}