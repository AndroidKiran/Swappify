package swapp.items.com.swappify.controller.chat.ui

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import swapp.items.com.swappify.R
import swapp.items.com.swappify.controller.base.BaseFragment
import swapp.items.com.swappify.controller.home.viewmodel.HomeViewModel
import swapp.items.com.swappify.databinding.FragmentChatBinding
import javax.inject.Inject

class ChatFragment: BaseFragment<FragmentChatBinding, HomeViewModel>() {

    @Inject
    lateinit var viewFactory: ViewModelProvider.Factory

    @Inject
    lateinit var homeViewModel: HomeViewModel

    private lateinit var fragmentChatBinding: FragmentChatBinding

    override fun getViewModel(): HomeViewModel {
        homeViewModel = ViewModelProviders.of(this@ChatFragment,
                viewFactory).get(homeViewModel::class.java)
        return homeViewModel
    }

    override fun getLayoutId() = R.layout.fragment_chat


    override fun executePendingVariablesBinding() {
       /* fragmentChatBinding = getViewDataBinding().also {
            it.setVariable(BR.chatViewModel, homeViewModel.chatViewModel)
        }*/
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
