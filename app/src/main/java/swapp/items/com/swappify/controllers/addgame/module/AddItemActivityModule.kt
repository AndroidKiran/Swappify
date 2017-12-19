package swapp.items.com.swappify.controllers.addgame.module

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import swapp.items.com.swappify.components.search.SearchAdapter
import swapp.items.com.swappify.injection.scopes.PerActivity
import swapp.items.com.swappify.repo.game.IGameApi
import swapp.items.com.swappify.room.SearchDao

@Module
class AddItemActivityModule {

    @PerActivity
    @Provides
    fun provideGameApi(retrofit: Retrofit): IGameApi = retrofit.create(IGameApi::class.java)

    @PerActivity
    @Provides
    fun provideSearchAdapter(searchDao: SearchDao) : SearchAdapter<SearchAdapter.SearchViewItemListener>
            = SearchAdapter(searchDao = searchDao)
}