package swapp.items.com.swappify.controllers.addgame.ui

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.app.Fragment
import android.view.View
import android.widget.FrameLayout
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasBroadcastReceiverInjector
import dagger.android.support.HasSupportFragmentInjector
import swapp.items.com.swappify.BR
import swapp.items.com.swappify.R
import swapp.items.com.swappify.common.extension.*
import swapp.items.com.swappify.components.networkconnection.NetworkConnectionLifeCycleObserver
import swapp.items.com.swappify.components.search.ISearchOnClickListener
import swapp.items.com.swappify.components.search.ISearchOnQueryChangeListener
import swapp.items.com.swappify.components.search.SearchAdapter
import swapp.items.com.swappify.components.search.SearchItem
import swapp.items.com.swappify.controllers.addgame.model.GameModel
import swapp.items.com.swappify.controllers.addgame.viewmodel.AddGameViewModel
import swapp.items.com.swappify.controllers.base.BaseActivity
import swapp.items.com.swappify.controllers.configs.RecyclerViewConfiguration
import swapp.items.com.swappify.controllers.configs.SnackbarConfiguration
import swapp.items.com.swappify.databinding.ActivityAddGameBinding
import javax.inject.Inject


class AddGameActivity : BaseActivity<ActivityAddGameBinding, AddGameViewModel>(), HasSupportFragmentInjector,
        SearchAdapter.SearchViewItemListener, HasBroadcastReceiverInjector {

    @Inject
    lateinit var searchAdapter: SearchAdapter<SearchAdapter.SearchViewItemListener>

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var viewFactory: ViewModelProvider.Factory

    @Inject
    lateinit var addGameViewModel: AddGameViewModel

    @Inject
    lateinit var broadcastReceiverDispatchingAndroidInjector: DispatchingAndroidInjector<BroadcastReceiver>

    private var recyclerViewConfiguration = RecyclerViewConfiguration()

    private val snackBarConfiguration = SnackbarConfiguration()

    private lateinit var activityAddGameBinding: ActivityAddGameBinding

    private var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>? = null

    override fun getViewModel(): AddGameViewModel {
        addGameViewModel = ViewModelProviders.of(this@AddGameActivity, viewFactory)
                .get(AddGameViewModel::class.java)
        return addGameViewModel
    }

    override fun getLayoutId(): Int = R.layout.activity_add_game

    override fun executePendingVariablesBinding() {
        activityAddGameBinding = getViewDataBinding()
        activityAddGameBinding.setVariable(BR.addGameViewModel, addGameViewModel)
        activityAddGameBinding.setVariable(BR.searchOnClickListener, onSearchClickListener)
        activityAddGameBinding.setVariable(BR.searchOnQueryChangeListener, onSearchQueryChangeListeners)
        activityAddGameBinding.setVariable(BR.searchRecyclerViewConfig, recyclerViewConfiguration)
        activityAddGameBinding.setVariable(BR.snackBarConfig, snackBarConfiguration)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {

            addFragmentSafely(SearchGameFragment(), R.id.fragment_container_search_game,
                    SearchGameFragment.FRAGMENT_TAG, true, true)
        }

        NetworkConnectionLifeCycleObserver(lifecycle, addGameViewModel.isNetConnected, this@AddGameActivity)

        observeSearchQueryChange()
        observeFinishActivityChange()
        observerNetworkChange()
        observerApiCallErrorChange()

        recyclerViewConfiguration.recyclerViewBinding(searchAdapter, null)
        bottomSheetBehavior = BottomSheetBehavior.from<FrameLayout>(activityAddGameBinding.fragmentContainerSearchGame)
        bottomSheetBehavior?.setBottomSheetCallback(bottomSheetCallBack)
        activityAddGameBinding.searchView.setNavigationIconArrowSearch()
        searchAdapter.navigator = this@AddGameActivity
    }

    override fun onResume() {
        super.onResume()
        activityAddGameBinding.mainContainer.requestFocus()
    }


    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentDispatchingAndroidInjector


    /* Search Actions */

    private val onSearchQueryChangeListeners = object : ISearchOnQueryChangeListener {

        override fun onQueryTextChange(newText: String): Boolean = false

        override fun onQueryTextSubmit(query: String): Boolean {
            val searchItem = SearchItem(query, 0)
            searchAdapter.insertSearchItem(searchItem)
            addGameViewModel.searchQueryLiveData.value = searchItem.text
            return false
        }
    }

    override fun onItemClick(searchItem: SearchItem?) {
        addGameViewModel.searchQueryLiveData.value = searchItem?.text
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

    private fun observeSearchQueryChange() = addGameViewModel.searchQueryLiveData.observe(this) {
        addGameViewModel.searchInputText.set(it)

        val isBottomSheetHidden = bottomSheetBehavior?.state == BottomSheetBehavior.STATE_COLLAPSED
        if (!it.isNullOrEmpty() && isBottomSheetHidden) {
            bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }


    private fun observeFinishActivityChange() = addGameViewModel.finishActivityLiveData.observe(this) {
        if (it!!) {
            onBackPressed()
        }
    }

    private fun observerNetworkChange() = addGameViewModel.isNetConnected.observe(this@AddGameActivity) {
        if (it == false) {
            addGameViewModel.isSnackBarAlive = true
            hideKeyboard()
            snackBarConfiguration.showNoNetworkSnackBar(getString(R.string.str_no_internet_title),
                    getString(R.string.str_dismiss), View.OnClickListener { })
        } else {
            if (addGameViewModel.isSnackBarAlive) {
                snackBarConfiguration.showSnackBar(getString(R.string.str_internet_title), SnackbarConfiguration.Type.VALID)
            }
        }
    }

    private fun observerApiCallErrorChange() = addGameViewModel.apiError.observe(this@AddGameActivity) {
        if (it == true) {
            hideKeyboard()
            snackBarConfiguration.showSnackBar(getString(R.string.str_something_wrong_msg), SnackbarConfiguration.Type.NEUTRAL)
        }
    }

    private val bottomSheetCallBack = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onSlide(bottomSheet: View, slideOffset: Float) {

        }

        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_COLLAPSED || newState == BottomSheetBehavior.STATE_HIDDEN) {
                addGameViewModel.searchQueryLiveData.value = ""
            }
        }
    }

    override fun onBackPressed() {
        if (bottomSheetBehavior?.state == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
        } else {
            finish()
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)

        val searchQuery = savedInstanceState?.getString(SEARCH_QUERY, "")
        addGameViewModel.searchQueryLiveData.value = searchQuery

        val gameModel = savedInstanceState?.getParcelable<GameModel>(GAME_MODEL)
        addGameViewModel.gameModel.set(gameModel)

    }

    override fun broadcastReceiverInjector(): AndroidInjector<BroadcastReceiver> = broadcastReceiverDispatchingAndroidInjector

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putString(SEARCH_QUERY, addGameViewModel.searchInputText.get())
        outState?.putParcelable(GAME_MODEL, addGameViewModel.gameModel.get())
        super.onSaveInstanceState(outState)
    }


    companion object {

        const val SEARCH_QUERY = "search_query"

        const val GAME_MODEL = "game_model"

        fun start(context: Context)
                = Intent(context, AddGameActivity::class.java)
    }
}