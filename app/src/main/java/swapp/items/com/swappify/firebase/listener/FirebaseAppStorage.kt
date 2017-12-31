package swapp.items.com.swappify.firebase.listener

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.UploadTask
import io.reactivex.Single
import io.reactivex.functions.Function
import swapp.items.com.swappify.injection.scopes.PerActivity
import java.util.*
import javax.inject.Inject

@PerActivity
class FirebaseAppStorage @Inject constructor(private val storage: FirebaseStorage,
                                             private val listener: FirebaseObservableListener) {

    fun uploadListener(uri: Uri?, fileName: String?, userId: String?): Single<String> {
        val metadata = StorageMetadata.Builder()
                .setContentType("image/jpg")
                .setContentDisposition("GAME_COVER")
                .build()

        return listener.storageUploadEvent(storage.reference.child(getPath(fileName!!, userId!!))
                        .putFile(uri!!, metadata), asUploadUrl())
    }

    private fun asUploadUrl(): Function<UploadTask.TaskSnapshot, String> =
            Function { taskSnapshot -> taskSnapshot.downloadUrl.toString() }


    private fun getPath(fileName: String, userId: String): String {
        val currentTimeInMillis = Calendar.getInstance().timeInMillis
        return userId + "/" + currentTimeInMillis + "_" + fileName
    }
}
