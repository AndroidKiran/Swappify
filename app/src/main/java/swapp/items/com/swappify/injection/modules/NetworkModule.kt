package swapp.items.com.swappify.injection.modules

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import swapp.items.com.swappify.BuildConfig
import swapp.items.com.swappify.network.HostSelectionInterceptor
import swapp.items.com.swappify.utils.Constant
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BASIC else HttpLoggingInterceptor.Level.NONE
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor,
                            headerMap: Map<String, String>,
                            hostSelectionInterceptor: HostSelectionInterceptor): OkHttpClient =
            OkHttpClient.Builder()
                    .connectTimeout(Constant.TIMEOUT_IN_SEC, TimeUnit.SECONDS)
                    .addInterceptor(loggingInterceptor)
                    .addInterceptor(hostSelectionInterceptor)
                    .addNetworkInterceptor { chain ->
                        chain.proceed(chain.request().let {
                            val requestBuilder = it.newBuilder()
                            headerMap.forEach { (key, value) ->
                                requestBuilder.addHeader(key, value)
                            }
                            requestBuilder.build()
                        })
                    }
                    .build()

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder().setLenient().create()

    @Provides
    @Singleton
    fun providesGsonConverterFactory(gson: Gson): Converter.Factory =
            GsonConverterFactory.create(gson)


    @Provides
    @Singleton
    fun providesCallAdapterFactory(): CallAdapter.Factory = RxJava2CallAdapterFactory.create()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient,
                        gsonConverterFactory: Converter.Factory,
                        callAdapterFactor: CallAdapter.Factory) =
            Retrofit.Builder()
                    .baseUrl("")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(callAdapterFactor)
                    .client(okHttpClient)
                    .build()

}