package swapp.items.com.swappify.intro

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.intro_layout.*
import swapp.items.com.swappify.R

class IntroFragment : Fragment() {

    companion object {
        val PAGE : String = "page"

        infix fun newInstance(page : Int) : IntroFragment {
            val introFragment = IntroFragment()
            val bundle = Bundle()
            bundle.putInt(PAGE, page)
            introFragment.arguments = bundle
            return introFragment
        }
    }

    private val PAGE_ONE : Int = 0

    private val PAGE_TWO : Int = 1

    private var mPage: Int = PAGE_ONE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPage = arguments.getInt(PAGE, PAGE_ONE)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater?.inflate(R.layout.intro_layout, container, false)

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