package swapp.items.com.swappify.controller.intro.ui

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_intro.*
import swapp.items.com.swappify.BR
import swapp.items.com.swappify.R
import swapp.items.com.swappify.controller.base.BaseFragment
import swapp.items.com.swappify.controller.intro.viewmodel.IntroViewModel
import swapp.items.com.swappify.databinding.FragmentIntroBinding
import javax.inject.Inject

class IntroFragment : BaseFragment<FragmentIntroBinding, IntroViewModel>() {

    @Inject
    lateinit var viewFactory: ViewModelProvider.Factory

    @Inject
    lateinit var introViewModel: IntroViewModel

    private lateinit var fragmentIntroBinding: FragmentIntroBinding

    override fun getViewModel(): IntroViewModel {
        introViewModel = ViewModelProviders.of(this@IntroFragment,
                viewFactory).get(IntroViewModel::class.java)
        return introViewModel
    }

    override fun getLayoutId() = R.layout.fragment_intro

    override fun executePendingVariablesBinding() {
        fragmentIntroBinding = getViewDataBinding().also {
            it.setVariable(BR.introViewModel, introViewModel)
        }
    }

    companion object {
        private const val PAGE : String = "page"

        private const val PAGE_ONE : Int = 0

        private const val PAGE_TWO : Int = 1

        infix fun newInstance(page : Int) : IntroFragment {
            val introFragment = IntroFragment()
            val bundle = Bundle()
            bundle.putInt(PAGE, page)
            introFragment.arguments = bundle
            return introFragment
        }
    }


    private var mPage: Int = PAGE_ONE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPage = arguments.getInt(PAGE, PAGE_ONE)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       initControls()
    }

    private fun initControls() {
        val icon : Int
        val titleString : String
        val descString : String

        when(mPage) {

            PAGE_ONE -> {
                icon = R.mipmap.ic_launcher
                titleString = getString(R.string.intro_page_one_title)
                descString = getString(R.string.intro_page_one_desc)
            }

            PAGE_TWO -> {
                icon = R.mipmap.ic_launcher
                titleString = getString(R.string.intro_page_two_title)
                descString = getString(R.string.intro_page_two_desc)
            }

            else -> {
                icon = R.mipmap.ic_launcher
                titleString = getString(R.string.intro_page_three_title)
                descString = getString(R.string.intro_page_three_desc)
            }
        }

        illustratorIcon.setBackgroundColor(icon)
        title.text = titleString
        desc.text = descString
    }
}