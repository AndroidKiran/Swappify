package swapp.items.com.swappify.controllers.base


interface FragmentCallback {

    fun onFragmentAttached()

    fun onFragmentDetached(tag: String)

}