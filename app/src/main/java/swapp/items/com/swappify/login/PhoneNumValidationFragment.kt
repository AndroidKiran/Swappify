package swapp.items.com.swappify.login

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_phone_num_validation.*
import swapp.items.com.swappify.R

class PhoneNumValidationFragment : Fragment() {

    companion object {
        val TAG: String = PhoneNumValidationFragment.javaClass.simpleName

        fun newInstance(fm: FragmentManager?, bundle: Bundle?, container: Int) {
            val fragmentTransaction: FragmentTransaction? = fm?.beginTransaction()
            val phoneNumValidationFragment = PhoneNumValidationFragment()
            phoneNumValidationFragment.arguments = bundle
            fragmentTransaction?.add(container, phoneNumValidationFragment, TAG)
            fragmentTransaction?.commitAllowingStateLoss()
        }
    }

    private var mListener: SignUpActionListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater?.inflate(R.layout.fragment_phone_num_validation, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initNextButton()
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mListener = context as? SignUpActionListener
    }

    override fun onDetach() {
        mListener = null
        super.onDetach()
    }

    private fun initNextButton() {
        nextButton.setOnClickListener({ view -> mListener?.onNextButtonClicked() })
    }
}
