package swapp.items.com.swappify.controllers.additem.ui

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import swapp.items.com.swappify.BR
import swapp.items.com.swappify.R
import swapp.items.com.swappify.controllers.additem.viewmodel.AddItemViewModel
import swapp.items.com.swappify.controllers.base.BaseFragment
import swapp.items.com.swappify.databinding.FragmentAddItemManuallyBinding
import javax.inject.Inject

class AddItemManuallyFragment : BaseFragment<FragmentAddItemManuallyBinding, AddItemViewModel>() {

    @Inject
    lateinit var viewFactory: ViewModelProvider.Factory

    @Inject
    lateinit var addItemViewModel: AddItemViewModel

    private lateinit var fragmentAddItemManuallyBinding: FragmentAddItemManuallyBinding

    override fun getLayoutId(): Int = R.layout.fragment_add_item_manually

    override fun getViewModel(): AddItemViewModel {
        addItemViewModel = ViewModelProviders.of(this@AddItemManuallyFragment, viewFactory)
                .get(AddItemViewModel::class.java)
        return addItemViewModel
    }

    override fun executePendingVariablesBinding() {
        fragmentAddItemManuallyBinding = getViewDataBinding()
        fragmentAddItemManuallyBinding.setVariable(BR.addItemViewModel, addItemViewModel)
        fragmentAddItemManuallyBinding.executePendingBindings()
    }

}