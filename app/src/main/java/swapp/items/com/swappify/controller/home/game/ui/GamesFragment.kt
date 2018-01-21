package swapp.items.com.swappify.controller.home.game.ui

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import swapp.items.com.swappify.BR
import swapp.items.com.swappify.R
import swapp.items.com.swappify.controller.base.BaseFragment
import swapp.items.com.swappify.controller.home.game.viewmodel.GamesViewModel
import swapp.items.com.swappify.databinding.FragmentGamesBinding
import javax.inject.Inject

class GamesFragment: BaseFragment<FragmentGamesBinding, GamesViewModel>() {

    @Inject
    lateinit var viewFactory: ViewModelProvider.Factory

    @Inject
    lateinit var gamesViewModel: GamesViewModel

    private lateinit var fragmentGamesBinding: FragmentGamesBinding

    override fun getViewModel(): GamesViewModel {
        gamesViewModel = ViewModelProviders.of(this@GamesFragment,
                viewFactory).get(GamesViewModel::class.java)
        return gamesViewModel
    }

    override fun getLayoutId() = R.layout.fragment_games


    override fun executePendingVariablesBinding() {
        fragmentGamesBinding = getViewDataBinding()
        fragmentGamesBinding.setVariable(BR.viewModel, gamesViewModel)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}
