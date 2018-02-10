package swapp.items.com.swappify.controller.profile.ui

import android.Manifest
import android.app.Activity
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.annotation.NonNull
import android.support.design.widget.AppBarLayout
import android.text.Editable
import android.view.View
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.ui.PlacePicker
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import swapp.items.com.swappify.BR
import swapp.items.com.swappify.R
import swapp.items.com.swappify.common.extension.observe
import swapp.items.com.swappify.common.extension.scaleAnimation
import swapp.items.com.swappify.common.extension.startHomeActivity
import swapp.items.com.swappify.common.extension.translateAnimation
import swapp.items.com.swappify.components.ViewTooltip
import swapp.items.com.swappify.components.imagepicker.OverlapPickerActivity
import swapp.items.com.swappify.components.imagepicker.Sources
import swapp.items.com.swappify.controller.addgame.ui.AddGameFragment
import swapp.items.com.swappify.controller.base.BaseActivity
import swapp.items.com.swappify.controller.profile.model.Place
import swapp.items.com.swappify.controller.profile.viewmodel.EditProfileViewModel
import swapp.items.com.swappify.databinding.ActivityEditProfileBinding
import swapp.items.com.swappify.mvvm.PlacePickerLifeCyclerObserver
import javax.inject.Inject


class EditProfileActivity : BaseActivity<ActivityEditProfileBinding, EditProfileViewModel>(), AppBarLayout.OnOffsetChangedListener, EasyPermissions.PermissionCallbacks {

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
        activityEditProfileBinding = getViewDataBinding().apply {
            setVariable(BR.viewModel, this@EditProfileActivity.editProfileViewModel)
            setVariable(BR.callBack, this@EditProfileActivity.callBack)
            setVariable(BR.snackBarConfig, this@EditProfileActivity.snackBarConfiguration)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeFinishActivity()
        activityEditProfileBinding.appbar.addOnOffsetChangedListener(this@EditProfileActivity)
        PlacePickerLifeCyclerObserver(lifecycle, this)
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        if (maxScrollSize == 0)
            maxScrollSize = activityEditProfileBinding.appbar.totalScrollRange

        val currentScrollPercentage = Math.abs(verticalOffset) * 100 / maxScrollSize

        scaleViewOnScroll(activityEditProfileBinding.anchorLayout, currentScrollPercentage)
        handleVisibilityOnScroll(activityEditProfileBinding.sliderView, currentScrollPercentage)
    }

    private val callBack = object : IEditProfileNavigator {

        override fun onLocationInfoClick() {
            showToolTip()
        }

        override fun onGetLocationClick() {
            editProfileViewModel.errorLocation.apply { set(false) }
            seekLocationPermission()
        }

        override fun onCameraClick() {
            startPickerActivity(OverlapPickerActivity.TAKE_PHOTO, Sources.CAMERA)
        }

        override fun onGalleryClick() {
            startPickerActivity(OverlapPickerActivity.SELECT_PHOTO, Sources.GALLERY)
        }

        override fun onSaveProfileClick() {
            if(editProfileViewModel.validate()) {
                editProfileViewModel.verifyAndUpdate()
            }
        }

        override fun afterTextChanged(editable: Editable) {
            if (activityEditProfileBinding.nameEditText.isFocused) {
                editProfileViewModel.name.set(editable.toString())
                editProfileViewModel.errorName.set(false)
            }
        }
    }

    fun startPickerActivity(requestCode: Int, source: Sources) {
        startActivityForResult(OverlapPickerActivity.start(this, source, false), requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PLACE_PICKER_REQUEST -> {
                    PlacePicker.getPlace(this@EditProfileActivity, data).let {
                        it?.apply {
                            editProfileViewModel.setLocationUri(Place(this.id, this.name, this.address, this.latLng.latitude, this.latLng.longitude))
                        }
                    }
                }

                else -> {
                    data?.getStringExtra(OverlapPickerActivity.URI).let {
                        Uri.parse(it)?.apply {
                            editProfileViewModel.picUri.set("$this")
                        }
                    }
                }
            }
        }
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

    private fun startPlacePicker() {
        PlacePicker.IntentBuilder().run {
            try {
                startActivityForResult(build(this@EditProfileActivity), PLACE_PICKER_REQUEST)
            } catch (e: GooglePlayServicesRepairableException) {
                e.printStackTrace()
            } catch (e: GooglePlayServicesNotAvailableException) {
                e.printStackTrace()
            }
        }

    }

    private fun showToolTip() {
        ViewTooltip.on(activityEditProfileBinding.infoIcon)
                .autoHide(true, 5000)
                .clickToHide(true)
                .align(ViewTooltip.ALIGN.START)
                .position(ViewTooltip.Position.BOTTOM)
                .text(getString(R.string.str_location_info))
                .textColor(Color.WHITE)
                .color(Color.BLACK)
                .corner(3)
                .animation(ViewTooltip.FadeTooltipAnimation(500))
                .show()

    }

    private fun observeFinishActivity() =
            editProfileViewModel.finishActivityLiveData.observe(this) {
                it?.let {
                    if (it) {
                        editProfileViewModel.markProfileComplete()
                        startHomeActivity()
                    }
                }
            }

    @AfterPermissionGranted(RC_LOCATION_PERM)
    private fun seekLocationPermission() {
        if (EasyPermissions.hasPermissions(this@EditProfileActivity, Manifest.permission.ACCESS_FINE_LOCATION)) {
            startPlacePicker()
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.perm_location),
                    RC_LOCATION_PERM, Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>,
                                            @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>?) {
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>?) {
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState?.getString(URI_EXTRA, "")?.let {
            editProfileViewModel.picUri.set(it)
        }
        savedInstanceState?.getString(NAME, "")?.let {
            editProfileViewModel.name.set(it)
        }
        savedInstanceState?.getParcelable<Place>(PLACE_EXTRA)?.let {
            editProfileViewModel.setLocationUri(it)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.apply {
            putParcelable(PLACE_EXTRA, editProfileViewModel.place?.get())
            putString(URI_EXTRA, editProfileViewModel.picUri.get())
            putString(NAME, editProfileViewModel.name.get())
        }
        super.onSaveInstanceState(outState)
    }

    companion object {
        const val PLACE_PICKER_REQUEST = 117
        const val RC_LOCATION_PERM = 118
        const val NAME = "name"
        const val URI_EXTRA = "uri_extra"
        const val PLACE_EXTRA = "place_extra"
        fun start(context: Context) = Intent(context, EditProfileActivity::class.java)
    }
}
