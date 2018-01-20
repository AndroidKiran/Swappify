package swapp.items.com.swappify.controller.addgame.module

import android.support.v7.widget.LinearLayoutManager
import dagger.Module
import dagger.Provides
import swapp.items.com.swappify.controller.addgame.ui.SearchGameFragment
import swapp.items.com.swappify.controller.addgame.ui.SearchResultAdapter
import swapp.items.com.swappify.injection.scopes.PerFragment

@Module
class SearchGameFragmentModule {

    @PerFragment
    @Provides
    fun provideSearchResultAdapter(): SearchResultAdapter<SearchResultAdapter.SearchResultItemListener>
            = SearchResultAdapter()

    @PerFragment
    @Provides
    fun provideLinearLayoutManager(searchGameFragment: SearchGameFragment): LinearLayoutManager
            = LinearLayoutManager(searchGameFragment.context)
}
