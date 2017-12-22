package swapp.items.com.swappify.controllers.addgame.ui

import android.app.Activity
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import swapp.items.com.swappify.BR
import swapp.items.com.swappify.R
import swapp.items.com.swappify.common.extension.isFragmentVisible
import swapp.items.com.swappify.common.extension.observe
import swapp.items.com.swappify.common.extension.replaceFragmentSafely
import swapp.items.com.swappify.components.search.ISearchOnClickListener
import swapp.items.com.swappify.components.search.ISearchOnQueryChangeListener
import swapp.items.com.swappify.components.search.SearchAdapter
import swapp.items.com.swappify.components.search.SearchItem
import swapp.items.com.swappify.controllers.addgame.viewmodel.AddGameViewModel
import swapp.items.com.swappify.controllers.base.BaseActivity
import swapp.items.com.swappify.controllers.configs.RecyclerViewConfiguration
import swapp.items.com.swappify.databinding.ActivityAddGameBinding
import javax.inject.Inject


class AddGameActivity : BaseActivity<ActivityAddGameBinding, AddGameViewModel>(), HasSupportFragmentInjector, SearchAdapter.SearchViewItemListener {

    companion object {
        fun startAddItemActivity(activity: Activity) {
            val intent: Intent = Intent(activity, AddGameActivity::class.java)
            activity.startActivity(intent)
            activity.finish()
        }
    }

    private var recyclerViewConfiguration = RecyclerViewConfiguration()

    @Inject
    lateinit var searchAdapter: SearchAdapter<SearchAdapter.SearchViewItemListener>

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var viewFactory: ViewModelProvider.Factory

    @Inject
    lateinit var addGameViewModel: AddGameViewModel

    private lateinit var activityAddGameBinding: ActivityAddGameBinding


    override fun getViewModel(): AddGameViewModel {
        addGameViewModel = ViewModelProviders.of(this@AddGameActivity, viewFactory)
                .get(AddGameViewModel::class.java)
        return addGameViewModel
    }

    override fun getLayoutId(): Int = R.layout.activity_add_game;

    override fun executePendingVariablesBinding() {
        activityAddGameBinding = getViewDataBinding()
        activityAddGameBinding.setVariable(BR.addGameViewModel, addGameViewModel)
        activityAddGameBinding.setVariable(BR.searchOnClickListener, onSearchClickListener)
        activityAddGameBinding.setVariable(BR.searchOnQueryChangeListener, onSearchQueryChangeListeners)
        activityAddGameBinding.setVariable(BR.searchRecyclerViewConfig, recyclerViewConfiguration)
        activityAddGameBinding.executePendingBindings()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            replaceFragmentSafely(AddGameFragment(), R.id.fragment_container,
                    AddGameFragment.FRAGMENT_TAG, true, true)
        }
        initRecyclerView()
        observeSearchQueryChange()
        activityAddGameBinding.searchView.setNavigationIconArrowSearch()
        searchAdapter.navigator = this@AddGameActivity
    }


    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentDispatchingAndroidInjector


    /*Search Actions*/

    private fun initRecyclerView() {
        recyclerViewConfiguration.setRecyclerConfig(searchAdapter)
    }

    private val onSearchQueryChangeListeners = object : ISearchOnQueryChangeListener {

        override fun onQueryTextChange(newText: String): Boolean = false

        override fun onQueryTextSubmit(query: String): Boolean {
            val searchItem = SearchItem(query, 0)
            searchAdapter.insertSearchItem(searchItem)
            addGameViewModel.searchQueryLiveData.value = searchItem.text
            if(!isFragmentVisible(SearchGameFragment.FRAGMENT_TAG)) {
                addSearchFragment()
            }
            return false
        }
    }

    override fun onItemClick(searchItem: SearchItem?) {
        addGameViewModel.searchQueryLiveData.value = searchItem?.text
        if(!isFragmentVisible(SearchGameFragment.FRAGMENT_TAG)) {
            addSearchFragment()
        }
    }

    private val onSearchClickListener = object : ISearchOnClickListener {
        override fun onMenuClick() {
        }

        override fun onVoiceClick() {

        }

        override fun onClose() {
        }

        override fun onOpen() {
        }

    }

    private fun addSearchFragment() {
        replaceFragmentSafely(SearchGameFragment(),
                R.id.fragment_container,
                SearchGameFragment.FRAGMENT_TAG,
                true,
                true,
                R.animator.slide_up,
                R.animator.slide_down,
                R.animator.slide_up,
                R.animator.slide_down)
    }

    fun observeSearchQueryChange() {
        addGameViewModel.searchQueryLiveData.observe(this) {
            addGameViewModel.searchInputText.set(it)
        }
    }

    override fun onBackPressed() {
        addGameViewModel.searchQueryLiveData.value = ""
        super.onBackPressed()
    }
}