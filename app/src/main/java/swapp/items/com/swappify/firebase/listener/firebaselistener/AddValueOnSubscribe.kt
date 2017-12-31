package swapp.items.com.swappify.firebase.listener.firebaselistener

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import io.reactivex.SingleEmitter
import io.reactivex.SingleOnSubscribe
import io.reactivex.functions.Function
import java.lang.Exception

class AddValueOnSubscribe<T> constructor(private val task: Task<DocumentReference>,
                                         private val marshaller: Function<DocumentReference, T>) : SingleOnSubscribe<T> {

    override fun subscribe(emitter: SingleEmitter<T>) {
        val rxAddValueListener = RxAddValueListener(emitter, marshaller)
        task.addOnSuccessListener(rxAddValueListener)
                .addOnFailureListener(rxAddValueListener)

    }

    private inner class RxAddValueListener<T> constructor(private val emitter: SingleEmitter<T>, val marshaller: Function<DocumentReference, T>) : OnSuccessListener<DocumentReference>, OnFailureListener {


        override fun onSuccess(documentReference: DocumentReference) {
            if (!emitter.isDisposed) {
                emitter.onSuccess(marshaller.apply(documentReference))
            }
        }

        override fun onFailure(exception: Exception) {
            if (!emitter.isDisposed) {
                emitter.onError(exception)
            }
        }
    }
}
