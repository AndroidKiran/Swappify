package swapp.items.com.swappify.firebase.listener.firebaselistener

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.SetOptions
import io.reactivex.SingleEmitter
import io.reactivex.SingleOnSubscribe
import java.lang.Exception

class SetValueOnSubscribe<U> constructor(private val documentReference: DocumentReference,
                                         private val value: Any?, private val returnValue: U?) : SingleOnSubscribe<U> {

    override fun subscribe(emitter: SingleEmitter<U>) {
        val rxSetValueListener = RxSetValueListener(emitter, returnValue!!)
        documentReference.set(value!!, SetOptions.merge())
                .addOnSuccessListener(rxSetValueListener)
                .addOnFailureListener(rxSetValueListener)
    }

    private inner class RxSetValueListener<U> constructor(private val emitter: SingleEmitter<U>, private val returnValue: U) : OnSuccessListener<Void>, OnFailureListener {

        override fun onSuccess(p0: Void?) {
            if (!emitter.isDisposed) {
                emitter.onSuccess(returnValue)
            }
        }

        override fun onFailure(exception: Exception) {
            if (!emitter.isDisposed) {
                emitter.onError(exception)
            }
        }
    }
}
