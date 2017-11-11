package swapp.items.com.swappify.controllers.additem.ui

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import swapp.items.com.swappify.BR
import swapp.items.com.swappify.R
import swapp.items.com.swappify.controllers.additem.viewmodel.AddItemViewModel
import swapp.items.com.swappify.controllers.base.BaseFragment
import swapp.items.com.swappify.databinding.FragmentAddItemApiBinding
import javax.inject.Inject

class AddItemByApiFragment : BaseFragment<FragmentAddItemApiBinding, AddItemViewModel>() {

    @Inject
    lateinit var viewFactory: ViewModelProvider.Factory

    @Inject
    lateinit var addItemViewModel: AddItemViewModel

    private lateinit var fragmentAddItemByApiBinding: FragmentAddItemApiBinding

    override fun getLayoutId(): Int = R.layout.fragment_add_item_api

    override fun getViewModel(): AddItemViewModel {
        addItemViewModel = ViewModelProviders.of(this@AddItemByApiFragment, viewFactory)
                .get(AddItemViewModel::class.java)
        return addItemViewModel
    }

    override fun executePendingVariablesBinding() {
        fragmentAddItemByApiBinding = getViewDataBinding()
        fragmentAddItemByApiBinding.setVariable(BR.addItemViewModel, addItemViewModel)
        fragmentAddItemByApiBinding.executePendingBindings()
    }

}