package swapp.items.com.swappify.firebase.listener.firebaselistener

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import io.reactivex.SingleEmitter
import io.reactivex.SingleOnSubscribe
import java.lang.Exception

class AddValueOnSubscribe constructor(private val collectionReference: CollectionReference,
                                         private val value: Any?) : SingleOnSubscribe<String> {

    override fun subscribe(emitter: SingleEmitter<String>) {
        val rxSetValueListener = RxSetValueListener(emitter)

        value?.run {
            collectionReference.add(this)
                    .addOnSuccessListener(rxSetValueListener)
                    .addOnFailureListener(rxSetValueListener)
        }

    }

    private inner class RxSetValueListener constructor(private val emitter: SingleEmitter<String>) : OnSuccessListener<DocumentReference>, OnFailureListener {

        override fun onSuccess(documentReference: DocumentReference?) {
            if (!emitter.isDisposed) {
                documentReference?.let {
                    emitter.onSuccess(it.id)
                }
            }
        }

        override fun onFailure(exception: Exception) {
            if (!emitter.isDisposed) {
                emitter.onError(exception)
            }
        }
    }
}
