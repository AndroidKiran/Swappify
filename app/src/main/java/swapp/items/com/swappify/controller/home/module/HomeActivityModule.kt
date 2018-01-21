package swapp.items.com.swappify.controller.home.module

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import swapp.items.com.swappify.controller.home.ui.HomeActivity
import swapp.items.com.swappify.controller.home.ui.HomePagerAdapter
import swapp.items.com.swappify.injection.scopes.PerActivity
import swapp.items.com.swappify.repo.game.IGameApi

@Module
class HomeActivityModule {

    @PerActivity
    @Provides
    fun provideGameApi(retrofit: Retrofit): IGameApi = retrofit.create(IGameApi::class.java)

    @Provides
    @PerActivity
    fun provideHomePagerAdapter(homeActivity: HomeActivity) : HomePagerAdapter = HomePagerAdapter(homeActivity.supportFragmentManager)

}
