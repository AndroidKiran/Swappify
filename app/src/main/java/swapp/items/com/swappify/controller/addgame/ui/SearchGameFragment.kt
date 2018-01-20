package swapp.items.com.swappify.controller.addgame.ui

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import swapp.items.com.swappify.BR
import swapp.items.com.swappify.R
import swapp.items.com.swappify.common.extension.*
import swapp.items.com.swappify.components.BindedMultiStateView
import swapp.items.com.swappify.controller.addgame.model.GameModel
import swapp.items.com.swappify.controller.addgame.viewmodel.AddGameViewModel
import swapp.items.com.swappify.controller.base.BaseFragment
import swapp.items.com.swappify.controller.configs.ContentLoadingConfiguration
import swapp.items.com.swappify.controller.configs.EmptyViewConfiguration
import swapp.items.com.swappify.controller.configs.ErrorViewConfiguration
import swapp.items.com.swappify.controller.configs.RecyclerViewConfiguration
import swapp.items.com.swappify.databinding.FragmentSearchGameBinding
import javax.inject.Inject

class SearchGameFragment : BaseFragment<FragmentSearchGameBinding, AddGameViewModel>(), SearchResultAdapter.SearchResultItemListener {

    @Inject
    lateinit var viewFactory: ViewModelProvider.Factory

    @Inject
    lateinit var addGameViewModel: AddGameViewModel

    @Inject
    lateinit var searchResultAdapter: SearchResultAdapter<SearchResultAdapter.SearchResultItemListener>

    @Inject
    lateinit var linearLayoutManager: LinearLayoutManager

    private lateinit var fragmentSearchGameBinding: FragmentSearchGameBinding

    private val contentLoadingConfiguration = ContentLoadingConfiguration()

    private val emptyViewConfiguration = EmptyViewConfiguration()

    private val errorViewConfiguration = ErrorViewConfiguration()

    private val recyclerViewConfiguration = RecyclerViewConfiguration()

    override fun getLayoutId(): Int = R.layout.fragment_search_game

    override fun getViewModel(): AddGameViewModel {
        addGameViewModel = ViewModelProviders.of(this@SearchGameFragment, viewFactory)
                .get(AddGameViewModel::class.java)
        return addGameViewModel
    }

    override fun executePendingVariablesBinding() {
        fragmentSearchGameBinding = getViewDataBinding()
        fragmentSearchGameBinding.setVariable(BR.addItemViewModel, addGameViewModel)
        fragmentSearchGameBinding.setVariable(BR.recyclerViewConfig, recyclerViewConfiguration)
        fragmentSearchGameBinding.setVariable(BR.contentLoadingBindingConfig, contentLoadingConfiguration)
        fragmentSearchGameBinding.setVariable(BR.emptyViewBindingConfig, emptyViewConfiguration)
        fragmentSearchGameBinding.setVariable(BR.errorViewBindingConfig, errorViewConfiguration)
        fragmentSearchGameBinding.executePendingBindings()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeSearchQueryChange()
        observeSearchResultChange()
        observeGameModelChange()
        searchResultAdapter.navigator = this@SearchGameFragment
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        emptyViewConfiguration.emptyViewBinding(getString(R.string.str_game_search_empty),
                ContextCompat.getDrawable(context, R.drawable.vc_replay_black))

        errorViewConfiguration.errorViewBinding(getString(R.string.str_game_search_empty),
                ContextCompat.getDrawable(context, R.drawable.vc_replay_black), null)

        contentLoadingConfiguration.contentLoadingBinding(getString(R.string.str_searching,
                addGameViewModel.searchInputText.get()))

        recyclerViewConfiguration.recyclerViewBinding(searchResultAdapter, linearLayoutManager)

    }

    private fun observeSearchQueryChange() = addGameViewModel.searchQueryLiveData.observe(this) {
            contentLoadingConfiguration.contentLoadingBinding(getString(R.string.str_searching, it))
            fragmentSearchGameBinding.multiStateViewLayout.multiStateView.setViewState(BindedMultiStateView.VIEW_STATE_LOADING)
        }


    private fun observeSearchResultChange() {
        addGameViewModel.gamesLiveData.observe(this) {
            if (it?.isNotEmpty()!!) {
                searchResultAdapter.setData(it)
                fragmentSearchGameBinding.multiStateViewLayout.multiStateView.setViewState(BindedMultiStateView.VIEW_STATE_CONTENT)
            } else {
                fragmentSearchGameBinding.multiStateViewLayout.multiStateView.setViewState(BindedMultiStateView.VIEW_STATE_EMPTY)
            }
        }
    }

    private fun observeGameModelChange() {
        addGameViewModel.gameModelLiveData.observe(this) {

            if (!it?.url.isNullOrEmpty() && it?.url!!.contains("t_thumb")) {
                it.url = it.url?.replace("t_thumb", "t_cover_big")
            }

            addGameViewModel.gameModel.set(it)

            val genres = it?.genres
            val developers = it?.developers
            val publishers = it?.publishers

            val genreId = if (genres != null) genres[0] else 0
            val developerId = if (developers != null) developers[0] else 0
            val publisherId = if (publishers != null) publishers[0] else 0

            addGameViewModel.getOptionalData(genreId, developerId, publisherId)
            addGameViewModel.disableErrorField()
        }
    }

    override fun onItemClick(gameModel: GameModel?) {
        addGameViewModel.gameModelLiveData.value = gameModel
        activity.onBackPressed()
    }

    companion object {
        val FRAGMENT_TAG = SearchGameFragment::class.java.simpleName!!
    }

}