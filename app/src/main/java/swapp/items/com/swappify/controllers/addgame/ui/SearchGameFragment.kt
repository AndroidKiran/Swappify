package swapp.items.com.swappify.controllers.addgame.ui

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import swapp.items.com.swappify.BR
import swapp.items.com.swappify.R
import swapp.items.com.swappify.common.extension.observe
import swapp.items.com.swappify.controllers.addgame.model.GameModel
import swapp.items.com.swappify.controllers.addgame.viewmodel.AddGameViewModel
import swapp.items.com.swappify.controllers.base.BaseFragment
import swapp.items.com.swappify.databinding.FragmentSearchGameBinding
import javax.inject.Inject

class SearchGameFragment : BaseFragment<FragmentSearchGameBinding, AddGameViewModel>() {

    companion object {
        val FRAGMENT_TAG = SearchGameFragment::class.java.simpleName!!
    }

    @Inject
    lateinit var viewFactory: ViewModelProvider.Factory

    @Inject
    lateinit var addGameViewModel: AddGameViewModel

    private lateinit var fragmentSearchGameBinding: FragmentSearchGameBinding

    override fun getLayoutId(): Int = R.layout.fragment_search_game

    override fun getViewModel(): AddGameViewModel {
        addGameViewModel = ViewModelProviders.of(this@SearchGameFragment, viewFactory)
                .get(AddGameViewModel::class.java)
        return addGameViewModel
    }

    override fun executePendingVariablesBinding() {
        fragmentSearchGameBinding = getViewDataBinding()
        fragmentSearchGameBinding.setVariable(BR.addItemViewModel, addGameViewModel)
        fragmentSearchGameBinding.executePendingBindings()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeSearchResultChange()
    }

    fun observeSearchResultChange() {
        addGameViewModel.gamesLiveData.observe(this) { searchList: List<GameModel>? ->
            searchList?.size
        }
    }
}