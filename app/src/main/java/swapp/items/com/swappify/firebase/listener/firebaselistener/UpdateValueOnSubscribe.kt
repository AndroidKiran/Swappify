package swapp.items.com.swappify.firebase.listener.firebaselistener

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentReference
import io.reactivex.SingleEmitter
import io.reactivex.SingleOnSubscribe
import java.lang.Exception

class UpdateValueOnSubscribe constructor(private val documentReference: DocumentReference,
                                         private val value: Map<String, Any>) : SingleOnSubscribe<Map<String, Any>> {

    override fun subscribe(emitter: SingleEmitter<Map<String, Any>>) {
        val rxUpdateValueListener = RxUpdateValueListener(emitter, value)
        documentReference.update(value)
                .addOnSuccessListener(rxUpdateValueListener)
                .addOnFailureListener(rxUpdateValueListener)


    }

    private inner class RxUpdateValueListener constructor(private val emitter: SingleEmitter<Map<String, Any>>, private val returnValue: Map<String, Any>) : OnSuccessListener<Void>, OnFailureListener {

        override fun onSuccess(void: Void?) {
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
