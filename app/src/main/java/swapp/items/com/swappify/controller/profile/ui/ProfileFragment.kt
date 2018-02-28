package swapp.items.com.swappify.controller.profile.ui

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import swapp.items.com.swappify.R
import swapp.items.com.swappify.controller.base.BaseFragment
import swapp.items.com.swappify.controller.home.viewmodel.HomeViewModel
import swapp.items.com.swappify.databinding.FragmentProfileBinding
import javax.inject.Inject


class ProfileFragment: BaseFragment<FragmentProfileBinding, HomeViewModel>() {

    @Inject
    lateinit var viewFactory: ViewModelProvider.Factory

    @Inject
    lateinit var homeViewModel: HomeViewModel

    private lateinit var fragmentProfileBinding: FragmentProfileBinding

    override fun getViewModel(): HomeViewModel {
        homeViewModel = ViewModelProviders.of(this@ProfileFragment,
                viewFactory).get(homeViewModel::class.java)
        return homeViewModel
    }

    override fun getLayoutId() = R.layout.fragment_profile


    override fun executePendingVariablesBinding() {
       /* fragmentProfileBinding = getViewDataBinding().also {
            it.setVariable(BR.profileViewModel, homeViewModel.profileViewModel)
        }*/
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}