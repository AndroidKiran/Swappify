package swapp.items.com.swappify.controllers.addgame.ui

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import swapp.items.com.swappify.BR
import swapp.items.com.swappify.R
import swapp.items.com.swappify.common.extension.*
import swapp.items.com.swappify.components.BindedMultiStateView
import swapp.items.com.swappify.controllers.addgame.model.GameModel
import swapp.items.com.swappify.controllers.addgame.viewmodel.AddGameViewModel
import swapp.items.com.swappify.controllers.base.BaseFragment
import swapp.items.com.swappify.controllers.configs.ContentLoadingConfiguration
import swapp.items.com.swappify.controllers.configs.EmptyViewConfiguration
import swapp.items.com.swappify.controllers.configs.ErrorViewConfiguration
import swapp.items.com.swappify.controllers.configs.RecyclerViewConfiguration
import swapp.items.com.swappify.databinding.FragmentSearchGameBinding
import javax.inject.Inject

class SearchGameFragment : BaseFragment<FragmentSearchGameBinding, AddGameViewModel>(), SearchResultAdapter.SearchResultItemListener {

    companion object {
        val FRAGMENT_TAG = SearchGameFragment::class.java.simpleName!!
    }

    @Inject
    lateinit var viewFactory: ViewModelProvider.Factory

    @Inject
    lateinit var addGameViewModel: AddGameViewModel

    @Inject
    lateinit var searchResultAdapter: SearchResultAdapter<SearchResultAdapter.SearchResultItemListener>

    @Inject
    lateinit var linearLayoutManager: LinearLayoutManager

    private lateinit var fragmentSearchGameBinding: FragmentSearchGameBinding

    private lateinit var emptyViewConfiguration: EmptyViewConfiguration

    private lateinit var errorViewConfiguration: ErrorViewConfiguration

    private lateinit var recyclerViewConfiguration: RecyclerViewConfiguration

    private lateinit var contentLoadingConfiguration: ContentLoadingConfiguration

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
        initUI()
        observeSearchQueryChange()
        observeSearchResultChange()
        observeGameModelChange()
    }

    private fun initUI() {
        emptyViewConfiguration = EmptyViewConfiguration()
        emptyViewConfiguration.emptyViewBinding(
                ContextCompat.getDrawable(context, R.drawable.vc_replay_black),
                getString(R.string.str_game_search_empty)
        )

        errorViewConfiguration = ErrorViewConfiguration()
        errorViewConfiguration.errorViewBinding(
                ContextCompat.getDrawable(context, R.drawable.vc_replay_black),
                getString(R.string.str_game_search_empty)
        )

        contentLoadingConfiguration = ContentLoadingConfiguration()
        contentLoadingConfiguration.contentLoadingBinding(
                getString(R.string.str_searching, addGameViewModel.searchInputText.get())
        )

        recyclerViewConfiguration = RecyclerViewConfiguration()
        recyclerViewConfiguration.recyclerViewBinding(linearLayoutManager, searchResultAdapter)

        searchResultAdapter.navigator = this@SearchGameFragment
    }

    private fun observeSearchQueryChange() {
        addGameViewModel.searchQueryLiveData.observe(this) {
            addGameViewModel.searchInputText.set(it)
            contentLoadingConfiguration.contentLoadingBinding(
                    getString(R.string.str_searching, it)
            )
            fragmentSearchGameBinding.multiStateViewLayout.multiStateView.setViewState(BindedMultiStateView.VIEW_STATE_LOADING)
        }
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

}