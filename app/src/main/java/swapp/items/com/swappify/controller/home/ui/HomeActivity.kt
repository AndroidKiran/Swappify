package swapp.items.com.swappify.controller.home.ui

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import swapp.items.com.swappify.BR
import swapp.items.com.swappify.BuildConfig
import swapp.items.com.swappify.R
import swapp.items.com.swappify.controller.base.BaseActivity
import swapp.items.com.swappify.controller.home.viewmodel.HomeViewModel
import swapp.items.com.swappify.databinding.ActivityHomeBinding
import javax.inject.Inject

class HomeActivity : BaseActivity<ActivityHomeBinding, HomeViewModel>(), HasSupportFragmentInjector {

    @Inject
    lateinit var viewFactory: ViewModelProvider.Factory

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var homeViewModel: HomeViewModel

    lateinit var activityHomeBinding: ActivityHomeBinding
    
    override fun getViewModel(): HomeViewModel {
        homeViewModel = ViewModelProviders.of(this@HomeActivity,
                viewFactory).get(HomeViewModel::class.java)
        return homeViewModel
    }

    override fun getLayoutId() = R.layout.activity_home

    override fun executePendingVariablesBinding() {
        activityHomeBinding = getViewDataBinding().also {
            it.setVariable(BR.homeViewModel, homeViewModel)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    companion object {

        const val MAIN_ACTION = BuildConfig.APPLICATION_ID + ".action" + ".MAIN_ACTION"

        fun start(context: Context) = Intent(context, HomeActivity::class.java)
    }


    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentDispatchingAndroidInjector

}