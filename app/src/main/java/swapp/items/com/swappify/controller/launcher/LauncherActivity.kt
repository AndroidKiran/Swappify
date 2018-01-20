package swapp.items.com.swappify.controller.launcher

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import swapp.items.com.swappify.BR
import swapp.items.com.swappify.R
import swapp.items.com.swappify.common.AppUtils
import swapp.items.com.swappify.controller.home.HomeActivity
import swapp.items.com.swappify.controller.base.BaseActivity
import swapp.items.com.swappify.controller.intro.ui.IntroActivity
import swapp.items.com.swappify.controller.launcher.viewmodel.LauncherViewModel
import swapp.items.com.swappify.controller.signup.ui.LoginActivity
import swapp.items.com.swappify.databinding.ActivityLauncherBinding
import javax.inject.Inject

class LauncherActivity : BaseActivity<ActivityLauncherBinding, LauncherViewModel>() {

    @Inject
    lateinit var viewFactory: ViewModelProvider.Factory

    private lateinit var launcherViewModel: LauncherViewModel

    private lateinit var activityLauncherBinding: ActivityLauncherBinding

    override fun getViewModel(): LauncherViewModel {
        launcherViewModel = ViewModelProviders.of(this@LauncherActivity, viewFactory)
                .get(LauncherViewModel::class.java)
        return launcherViewModel
    }

    override fun getLayoutId() = R.layout.activity_launcher

    override fun executePendingVariablesBinding() {
        activityLauncherBinding = getViewDataBinding()
        activityLauncherBinding.setVariable(BR.viewModel,launcherViewModel)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!AppUtils.isIntroDone(launcherViewModel.preferenceHelper)!!) {
            startActivity(IntroActivity.start(this@LauncherActivity))
        } else if(AppUtils.isLoggedIn(launcherViewModel.preferenceHelper)) {
            startActivity(HomeActivity.start(this@LauncherActivity))
        } else {
            startActivity(LoginActivity.start(this@LauncherActivity))
        }
    }
}
