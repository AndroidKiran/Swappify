package swapp.items.com.swappify.login

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import swapp.items.com.swappify.R


class OtpVerificationFragment : Fragment() {

    companion object {

        val TAG : String = OtpVerificationFragment.javaClass.simpleName

        fun newInstance(fm : FragmentManager?, bundle: Bundle?, container: Int) {
            val otpVerificationFragment = OtpVerificationFragment()
            val fragmentTransaction = fm?.beginTransaction()
            otpVerificationFragment.arguments = bundle
            fragmentTransaction?.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down,
                    R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_bottom )
            fragmentTransaction?.add(container, otpVerificationFragment, TAG)
            fragmentTransaction?.addToBackStack(TAG)
            fragmentTransaction?.commitAllowingStateLoss()
        }
    }

    private var mListener: SignUpActionListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater?.inflate(R.layout.fragment_otp_validation, container, false)


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mListener = context as? SignUpActionListener
    }

    override fun onDetach() {
        mListener = null
        super.onDetach()
    }
}