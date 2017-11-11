package swapp.items.com.swappify.controllers.additem.ui

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import swapp.items.com.swappify.BR
import swapp.items.com.swappify.R
import swapp.items.com.swappify.controllers.additem.viewmodel.AddItemViewModel
import swapp.items.com.swappify.controllers.base.BaseActivity
import swapp.items.com.swappify.databinding.ActivityAddItemBinding
import javax.inject.Inject


class AddItemActivity : BaseActivity<ActivityAddItemBinding, AddItemViewModel>(), HasSupportFragmentInjector {

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var viewFactory: ViewModelProvider.Factory

    @Inject
    lateinit var addItemViewModel: AddItemViewModel

    private lateinit var activityAddItemBinding: ActivityAddItemBinding


    override fun getViewModel(): AddItemViewModel {
        addItemViewModel = ViewModelProviders.of(this@AddItemActivity, viewFactory)
                .get(AddItemViewModel::class.java)
        return addItemViewModel
    }

    override fun getLayoutId(): Int = R.layout.activity_add_item;

    override fun executePendingVariablesBinding() {
        activityAddItemBinding = getViewDataBinding()
        activityAddItemBinding.setVariable(BR.addItemViewModel, addItemViewModel)
        activityAddItemBinding.executePendingBindings()
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentDispatchingAndroidInjector

}