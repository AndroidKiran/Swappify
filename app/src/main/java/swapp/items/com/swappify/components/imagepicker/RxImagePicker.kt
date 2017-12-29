package swapp.items.com.swappify.components.imagepicker

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity.RESULT_OK
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Toast
import io.reactivex.subjects.PublishSubject
import swapp.items.com.swappify.R
import java.text.SimpleDateFormat
import java.util.*


class RxImagePicker : Fragment() {

    private var publishSubjectMultipleImages: PublishSubject<List<Uri>>? = null

    private var allowMultipleImages = false
    private var imageSource: Sources? = null


    fun requestImage(source: Sources): PublishSubject<Uri>? {
        allowMultipleImages = false
        imageSource = source
        requestPickImage()
        return RxEventBus.getObservable()
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun requestMultipleImages(): PublishSubject<List<Uri>>? {
        publishSubjectMultipleImages = PublishSubject.create<List<Uri>>()
        imageSource = Sources.GALLERY
        allowMultipleImages = true
        requestPickImage()
        return publishSubjectMultipleImages
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        Log.e("test", "Permission granted")
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pickImage()
            Log.e("test", "pick image")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                SELECT_PHOTO -> handleGalleryResult(data)
                TAKE_PHOTO -> onImagePicked(cameraPictureUrl)
            }
        }
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

    private fun requestPickImage() {
        if (!isAdded) {
            RxEventBus.getObservable()?.subscribe({ pickImage() })
        } else {
            pickImage()
        }
    }

    private fun pickImage() {
        if (activity == null && !checkPermission(imageSource)) return

        var chooseCode = 0
        var pictureChooseIntent: Intent? = null

        when (imageSource) {
            Sources.CAMERA -> {
                try {
                    if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
                        cameraPictureUrl = createImageUri()
                        pictureChooseIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        pictureChooseIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraPictureUrl)
                        chooseCode = TAKE_PHOTO
                    } else {
                        Toast.makeText(context, getString(R.string.cannot_take_pic),
                                Toast.LENGTH_LONG).show()
                    }
                } catch (execption: RuntimeException) {
                    Toast.makeText(context, getString(R.string.cannot_take_pic),
                            Toast.LENGTH_LONG).show()
                }

            }
            Sources.GALLERY -> {
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
            }
        }

        startActivityForResult(pictureChooseIntent, chooseCode)
    }

    private fun checkPermission(source: Sources?): Boolean =
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    var permissionArray = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    if (Sources.CAMERA == source) {
                        permissionArray = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                    }
                    requestPermissions(permissionArray, 0)
                }
                false
            } else {
                true
            }

    private fun createImageUri(): Uri? {

        try {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val contentResolver = activity.contentResolver
            val cv = ContentValues()
            cv.put(MediaStore.Images.Media.TITLE, timeStamp)
            return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv)
        } catch (exception: Exception) {
            Log.e("err",""+exception.stackTrace)
        }
        return null
    }

    private fun onImagesPicked(uris: List<Uri>) {
        if (publishSubjectMultipleImages != null) {
            publishSubjectMultipleImages!!.onNext(uris)
            publishSubjectMultipleImages!!.onComplete()
        }
    }

    private fun onImagePicked(uri: Uri?) {
        RxEventBus.getObservable()?.onNext(uri!!)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            val cameraUrl = savedInstanceState.getString("camera_pic_url")
            if (cameraUrl != null) {
                cameraPictureUrl = Uri.parse(cameraUrl)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putString("camera_pic_url", cameraPictureUrl.toString())
        super.onSaveInstanceState(outState)
    }


    companion object {

        private val SELECT_PHOTO = 100
        private val TAKE_PHOTO = 101

        private val TAG = RxImagePicker::class.java.simpleName
        private var cameraPictureUrl: Uri? = null

        fun with(fragmentManager: FragmentManager): RxImagePicker {
            var rxImagePickerFragment = fragmentManager.findFragmentByTag(TAG)
            if (rxImagePickerFragment == null) {
                rxImagePickerFragment = RxImagePicker()
                fragmentManager.beginTransaction()
                        .add(rxImagePickerFragment, TAG)
                        .commit()
            }
            return rxImagePickerFragment as RxImagePicker
        }
    }

}
