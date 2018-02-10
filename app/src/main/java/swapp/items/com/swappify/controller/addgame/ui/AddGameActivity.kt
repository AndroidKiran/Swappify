package swapp.items.com.swappify.controller.addgame.ui

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.app.Fragment
import android.view.View
import android.widget.FrameLayout
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import swapp.items.com.swappify.BR
import swapp.items.com.swappify.R
import swapp.items.com.swappify.common.extension.addFragmentSafely
import swapp.items.com.swappify.common.extension.observe
import swapp.items.com.swappify.common.extension.recyclerViewBinding
import swapp.items.com.swappify.components.search.ISearchOnClickListener
import swapp.items.com.swappify.components.search.ISearchOnQueryChangeListener
import swapp.items.com.swappify.components.search.SearchAdapter
import swapp.items.com.swappify.components.search.SearchItem
import swapp.items.com.swappify.components.search.SearchView.Constants.SPEECH_REQUEST_CODE
import swapp.items.com.swappify.controller.addgame.model.SearchGameModel
import swapp.items.com.swappify.controller.addgame.viewmodel.AddGameViewModel
import swapp.items.com.swappify.controller.base.BaseActivity
import swapp.items.com.swappify.controller.configs.RecyclerViewConfiguration
import swapp.items.com.swappify.databinding.ActivityAddGameBinding
import javax.inject.Inject


class AddGameActivity : BaseActivity<ActivityAddGameBinding, AddGameViewModel>(), HasSupportFragmentInjector,
        SearchAdapter.SearchViewItemListener {

    @Inject
    lateinit var searchAdapter: SearchAdapter<SearchAdapter.SearchViewItemListener>

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var viewFactory: ViewModelProvider.Factory

    @Inject
    lateinit var addGameViewModel: AddGameViewModel

    private var recyclerViewConfiguration = RecyclerViewConfiguration()

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

        observeSearchQueryChange()
        observeFinishActivityChange()

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

    private val bottomSheetCallBack = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onSlide(bottomSheet: View, slideOffset: Float) {

        }

        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_COLLAPSED || newState == BottomSheetBehavior.STATE_HIDDEN) {
                addGameViewModel.searchQueryLiveData.value = ""
            }
        }
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentDispatchingAndroidInjector

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            data?.let {
                data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.let {
                    if (it.isNotEmpty()) {
                        val searchText = it[0]
                        if (!searchText.isNullOrEmpty()) {
                            activityAddGameBinding.searchView.setQuery(searchText)
                        }
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
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

        val gameModel = savedInstanceState?.getParcelable<SearchGameModel>(GAME_MODEL)
        addGameViewModel.searchGameModel.set(gameModel)

    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.apply {
            val queryEditable = addGameViewModel.searchInputText.get()
            queryEditable?.let {
                editable -> putString(SEARCH_QUERY, editable)
            }
            putParcelable(GAME_MODEL, addGameViewModel.searchGameModel.get())
        }

        super.onSaveInstanceState(outState)
    }

    companion object {

        const val SEARCH_QUERY = "search_query"

        const val GAME_MODEL = "game_model"

        fun start(context: Context) = Intent(context, AddGameActivity::class.java)
    }
}