package swapp.items.com.swappify.firebase.listener.firebaselistener

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.UploadTask
import io.reactivex.SingleEmitter
import io.reactivex.SingleOnSubscribe
import io.reactivex.functions.Function

class StorageUploadEventOnSubscribe<T>(private val uploadTask: UploadTask,
                                                        private val marshaller: Function<UploadTask.TaskSnapshot, T>) : SingleOnSubscribe<T> {
    override fun subscribe(emitter: SingleEmitter<T>) {
        val rxStorageListener = RxStorageListener(emitter, marshaller)
        uploadTask
                .addOnSuccessListener(rxStorageListener)
                .addOnFailureListener(rxStorageListener)
    }

    private inner class RxStorageListener<T> internal constructor(private val emitter: SingleEmitter<T>,
                                                            private val mMarshaller: Function<UploadTask.TaskSnapshot, T>) : OnSuccessListener<UploadTask.TaskSnapshot>, OnFailureListener {


        override fun onFailure(exception: Exception) {
            if (!emitter.isDisposed) {
                emitter.onError(exception)
            }
        }

        override fun onSuccess(taskSnapshot: UploadTask.TaskSnapshot) {
            if (!emitter.isDisposed) {
                try {
                    emitter.onSuccess(mMarshaller.apply(taskSnapshot))
                } catch (exception: Exception) {
                    emitter.onError(exception)
                }

            }
        }
    }
}
