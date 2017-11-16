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
import swapp.items.com.swappify.injection.scopes.PerApplication
import swapp.items.com.swappify.network.AuthenticateInterceptor
import swapp.items.com.swappify.network.HostSelectionInterceptor
import swapp.items.com.swappify.utils.Constant
import java.util.concurrent.TimeUnit


@Module
class NetworkModule {

    @PerApplication
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        if (BuildConfig.DEBUG) {
            level = HttpLoggingInterceptor.Level.HEADERS
            level = HttpLoggingInterceptor.Level.BODY
        } else {
            level = HttpLoggingInterceptor.Level.NONE
        }

    }

    @PerApplication
    @Provides
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor,
                            authenticatorInterceptor: AuthenticateInterceptor,
                            hostSelectionInterceptor: HostSelectionInterceptor): OkHttpClient =
            OkHttpClient.Builder()
                    .connectTimeout(Constant.TIMEOUT_IN_SEC, TimeUnit.SECONDS)
                    .addNetworkInterceptor(authenticatorInterceptor)
                    .addNetworkInterceptor(hostSelectionInterceptor)
                    .addInterceptor(loggingInterceptor)
                    .build()

    @PerApplication
    @Provides
    fun provideGson(): Gson = GsonBuilder().setLenient().create()

    @PerApplication
    @Provides
    fun providesGsonConverterFactory(gson: Gson): Converter.Factory =
            GsonConverterFactory.create(gson)


    @PerApplication
    @Provides
    fun providesCallAdapterFactory(): CallAdapter.Factory = RxJava2CallAdapterFactory.create()

    @PerApplication
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient,
                        gsonConverterFactory: Converter.Factory,
                        callAdapterFactor: CallAdapter.Factory) =
            Retrofit.Builder()
                    .baseUrl(BuildConfig.GAME_API_URL)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(callAdapterFactor)
                    .client(okHttpClient)
                    .build()

}