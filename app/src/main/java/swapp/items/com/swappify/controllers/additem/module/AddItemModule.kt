package swapp.items.com.swappify.controllers.additem.module

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import swapp.items.com.swappify.data.game.IGameApi

@Module
class AddItemModule {

    @Provides
    fun provideGameApi(retrofit: Retrofit): IGameApi = retrofit.create(IGameApi::class.java)
}