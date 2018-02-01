package swapp.items.com.swappify.controller.profile.ui

import android.app.Activity
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.text.Editable
import android.view.View
import swapp.items.com.swappify.BR
import swapp.items.com.swappify.R
import swapp.items.com.swappify.common.extension.scaleAnimation
import swapp.items.com.swappify.common.extension.translateAnimation
import swapp.items.com.swappify.components.imagepicker.OverlapPickerActivity
import swapp.items.com.swappify.components.imagepicker.Sources
import swapp.items.com.swappify.controller.addgame.ui.AddGameFragment
import swapp.items.com.swappify.controller.base.BaseActivity
import swapp.items.com.swappify.controller.profile.viewmodel.EditProfileViewModel
import swapp.items.com.swappify.databinding.ActivityEditProfileBinding
import swapp.items.com.swappify.mvvm.NetworkConnectionLifeCycleObserver
import javax.inject.Inject

class EditProfileActivity : BaseActivity<ActivityEditProfileBinding, EditProfileViewModel>(), AppBarLayout.OnOffsetChangedListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var editProfileViewModel: EditProfileViewModel

    private lateinit var activityEditProfileBinding: ActivityEditProfileBinding

    private var isAnchorHidden = false

    private var isViewHidden = false

    private var maxScrollSize = 0


    override fun getViewModel(): EditProfileViewModel {
        editProfileViewModel = ViewModelProviders.of(this@EditProfileActivity, viewModelFactory)
                .get(EditProfileViewModel::class.java)
        return editProfileViewModel
    }

    override fun getLayoutId(): Int = R.layout.activity_edit_profile


    override fun executePendingVariablesBinding() {
        activityEditProfileBinding = getViewDataBinding()
        activityEditProfileBinding.setVariable(BR.viewModel, editProfileViewModel)
        activityEditProfileBinding.setVariable(BR.callBack, callBack)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityEditProfileBinding.appbar.addOnOffsetChangedListener(this@EditProfileActivity)
        NetworkConnectionLifeCycleObserver(lifecycle, editProfileViewModel.isNetConnected, this@EditProfileActivity)
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        if (maxScrollSize == 0)
            maxScrollSize = activityEditProfileBinding.appbar.totalScrollRange

        val currentScrollPercentage = Math.abs(verticalOffset) * 100 / maxScrollSize

        scaleViewOnScroll(activityEditProfileBinding.anchorLayout, currentScrollPercentage)
        handleVisibilityOnScroll(activityEditProfileBinding.sliderView, currentScrollPercentage)
    }

    private val callBack = object : IEditProfileNavigator {

        override fun onCameraClick() {
            startPickerActivity(OverlapPickerActivity.TAKE_PHOTO, Sources.CAMERA)
        }

        override fun onGalleryClick() {
            startPickerActivity(OverlapPickerActivity.SELECT_PHOTO, Sources.GALLERY)
        }

        override fun onSaveProfileClick() {
            val user = editProfileViewModel.getUser()
            if(editProfileViewModel.picUri.get().isNullOrEmpty()) {
                editProfileViewModel.updateUser(user)
            } else {
                editProfileViewModel.updateUser(user, Uri.parse(editProfileViewModel.picUri.get()))
            }
        }

        override fun afterTextChanged(editable: Editable) {
            when {
                activityEditProfileBinding.nameEditText.isFocused -> {
                    editProfileViewModel.name.set(editable.toString())
                }
                activityEditProfileBinding.countryEditText.isFocused -> {
                    editProfileViewModel.country.set(editable.toString())
                }
                activityEditProfileBinding.cityEditText.isFocused -> {
                    editProfileViewModel.city.set(editable.toString())
                }
            }
        }
    }

    fun startPickerActivity(requestCode: Int, source: Sources) {
        startActivityForResult(OverlapPickerActivity.start(this, source, false), requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            val uriString = data?.getStringExtra(OverlapPickerActivity.URI)
            if (!uriString.isNullOrEmpty()) {
                val uri = Uri.parse(uriString)
                editProfileViewModel.picUri.set("$uri")
            }
        }
    }

    companion object {

        fun start(context: Context) = Intent(context, EditProfileActivity::class.java)
    }

    private fun scaleViewOnScroll(view: View?, scrollPercent: Int) {

        if (scrollPercent >= AddGameFragment.PERCENTAGE_TO_SHOW_ANCHOR) {
            if (!isAnchorHidden) {
                isAnchorHidden = true
                view?.scaleAnimation(AddGameFragment.MIN_SCALE_VALUE)
            }
        }

        if (scrollPercent < AddGameFragment.PERCENTAGE_TO_SHOW_ANCHOR) {
            if (isAnchorHidden) {
                isAnchorHidden = false
                view?.scaleAnimation(AddGameFragment.MAX_SCALE_VALUE)
            }
        }
    }

    private fun handleVisibilityOnScroll(view: View?, scrollPercent: Int) {

        if (scrollPercent >= AddGameFragment.PERCENTAGE_TO_SHOW_ANCHOR) {
            if (!isViewHidden) {
                isViewHidden = true
                view?.translateAnimation(resources.getDimension(R.dimen.dimen_16).toInt())
            }
        }

        if (scrollPercent < AddGameFragment.PERCENTAGE_TO_SHOW_ANCHOR) {
            if (isViewHidden) {
                isViewHidden = false
                view?.translateAnimation(resources.getDimension(R.dimen.dimen_90).toInt())
            }
        }
    }
}
