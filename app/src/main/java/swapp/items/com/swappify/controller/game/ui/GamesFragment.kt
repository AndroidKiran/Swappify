package swapp.items.com.swappify.controller.game.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import swapp.items.com.swappify.BR
import swapp.items.com.swappify.R
import swapp.items.com.swappify.controller.base.BaseFragment
import swapp.items.com.swappify.controller.game.viewmodel.GamesViewModel
import swapp.items.com.swappify.databinding.FragmentGamesBinding
import javax.inject.Inject

class GamesFragment : BaseFragment<FragmentGamesBinding, GamesViewModel>() {

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
        fragmentGamesBinding = getViewDataBinding().also {
            it.setVariable(BR.gamesViewModel, gamesViewModel)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observerGameLiveData()
        observerSearchGameLiveData()
        gamesViewModel.updateGameFilter()
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun observerGameLiveData() {
        gamesViewModel.gamesLiveData?.observe(this, Observer {
            it?.also {
                if (it.isNotEmpty()) {

                } else {

                }
            }
        })
    }

    private fun observerSearchGameLiveData() {
        gamesViewModel.searchGamesLiveData?.observe(this, Observer {
            it?.also {
                if (it.isNotEmpty()) {

                } else {

                }
            }
        })
    }

}
