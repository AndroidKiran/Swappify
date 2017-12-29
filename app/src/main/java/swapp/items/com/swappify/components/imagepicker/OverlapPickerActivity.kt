package swapp.items.com.swappify.components.imagepicker

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.annotation.NonNull
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import swapp.items.com.swappify.R
import java.text.SimpleDateFormat
import java.util.*

class OverlapPickerActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    companion object {

        const val SOURCE = "source"

        const val ALLOW_MULTIPLE_IMAGES = "allow_multiple_IMAGES"

        const val URI_LIST = "uri_list"

        const val URI = "uri"

        const val RC_WRITE_EXT_PER = 101

        const val RC_CAMERA_PER = 102

        const val SELECT_PHOTO = 100

        const val TAKE_PHOTO = 101

        fun start(context: Context, source: Sources, allowMultipleImages: Boolean): Intent {
            val intent = Intent(context, OverlapPickerActivity::class.java)
            intent.putExtra(ALLOW_MULTIPLE_IMAGES, allowMultipleImages)
            intent.putExtra(SOURCE, source.ordinal)
            return intent
        }
    }

    private var cameraPictureUrl: Uri? = null

    private var source: Int = 0

    private var allowMultipleImages: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            source = intent.getIntExtra(SOURCE, 0)
            allowMultipleImages = intent.getBooleanExtra(ALLOW_MULTIPLE_IMAGES, false)
            pickImage()
        } else {
            val cameraUrl = savedInstanceState.getString("camera_pic_url")
            if (cameraUrl != null) {
                cameraPictureUrl = Uri.parse(cameraUrl)
            }
            source = savedInstanceState.getInt(SOURCE, 0)
            allowMultipleImages = savedInstanceState.getBoolean(ALLOW_MULTIPLE_IMAGES, false)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        pickImage()
    }

    private fun pickImage() {

        val sourceType = Sources.values()[source]

        when (sourceType) {
            Sources.CAMERA -> {
                seekCameraPermission()
            }
            Sources.GALLERY -> {
                seekGalleryPermission()
            }
        }
    }

    @AfterPermissionGranted(RC_WRITE_EXT_PER)
    private fun seekGalleryPermission() {
        return if (EasyPermissions.hasPermissions(this@OverlapPickerActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            var chooseCode = 0
            var pictureChooseIntent: Intent? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                pictureChooseIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                pictureChooseIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, allowMultipleImages)
                pictureChooseIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
            } else {
                pictureChooseIntent = Intent(Intent.ACTION_GET_CONTENT)
            }
            pictureChooseIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            pictureChooseIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            pictureChooseIntent.type = "image/*"
            chooseCode = SELECT_PHOTO
            startActivityForResult(pictureChooseIntent, chooseCode)
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_sms),
                    RC_WRITE_EXT_PER, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    @AfterPermissionGranted(RC_CAMERA_PER)
    private fun seekCameraPermission() {
        if (EasyPermissions.hasPermissions(this@OverlapPickerActivity, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            try {
                if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
                    cameraPictureUrl = createImageUri()
                    val pictureChooseIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    pictureChooseIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraPictureUrl)
                    startActivityForResult(pictureChooseIntent, TAKE_PHOTO)
                } else {
                    Toast.makeText(this, getString(R.string.cannot_take_pic),
                            Toast.LENGTH_LONG).show()
                }
            } catch (execption: RuntimeException) {
                Toast.makeText(this, getString(R.string.cannot_take_pic),
                        Toast.LENGTH_LONG).show()
            }

        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_sms),
                    RC_CAMERA_PER, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    private fun createImageUri(): Uri? {

        try {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val contentResolver = contentResolver
            val cv = ContentValues()
            cv.put(MediaStore.Images.Media.TITLE, timeStamp)
            return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv)
        } catch (exception: Exception) {
            Log.e("err", "" + exception.stackTrace)
        }
        return null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                SELECT_PHOTO -> handleGalleryResult(data)
                TAKE_PHOTO -> onImagePicked(cameraPictureUrl)
            }
        }

        finish()
    }

    private fun handleGalleryResult(data: Intent?) = if (allowMultipleImages) {
        val imageUris = arrayListOf<Uri>()
        val clipData = data!!.clipData
        if (clipData != null) {
            (0 until clipData.itemCount).mapTo(imageUris) { clipData.getItemAt(it).uri }
        } else {
            imageUris.add(data.data)
        }
        onImagesPicked(imageUris)
    } else {
        onImagePicked(data!!.data)
    }

    private fun onImagesPicked(uris: ArrayList<Uri>) {
        val bundle = Bundle()
        bundle.putParcelableArrayList(URI_LIST, uris)
        setResult(Activity.RESULT_OK, Intent().putExtras(bundle))
        finish()
    }

    private fun onImagePicked(uri: Uri?) {
        setResult(Activity.RESULT_OK, Intent().putExtra(URI, uri.toString()))
        finish()
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

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putString("camera_pic_url", cameraPictureUrl.toString())
        outState?.putBoolean(ALLOW_MULTIPLE_IMAGES, allowMultipleImages)
        outState?.putInt(SOURCE, source)
        super.onSaveInstanceState(outState)
    }
}
