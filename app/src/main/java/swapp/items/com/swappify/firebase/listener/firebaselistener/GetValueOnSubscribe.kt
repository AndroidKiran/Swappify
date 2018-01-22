package swapp.items.com.swappify.firebase.listener.firebaselistener

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import io.reactivex.SingleEmitter
import io.reactivex.SingleOnSubscribe
import io.reactivex.functions.Function
import java.lang.Exception

class GetValueOnSubscribe<T> constructor(private val documentReference: DocumentReference, private val marshaller: Function<DocumentSnapshot, T>) :SingleOnSubscribe<T> {

    override fun subscribe(emitter: SingleEmitter<T>) {
        val rxGetValueListener = RxGetValueListener(emitter, marshaller)
        documentReference.get()
                .addOnSuccessListener(rxGetValueListener)
                .addOnFailureListener(rxGetValueListener)
    }

    inner class RxGetValueListener<T> constructor(private val emitter: SingleEmitter<T>, private val marshaller: Function<DocumentSnapshot, T>): OnSuccessListener<DocumentSnapshot>, OnFailureListener {

        override fun onSuccess(documentSnapshot: DocumentSnapshot?) {
            if (emitter.isDisposed) {
                return
            }

            emitter.onSuccess(marshaller.apply(documentSnapshot!!))
        }

        override fun onFailure(exception: Exception) {
            if (emitter.isDisposed) {
                return
            }
            emitter.onError(exception)
        }

    }
}
