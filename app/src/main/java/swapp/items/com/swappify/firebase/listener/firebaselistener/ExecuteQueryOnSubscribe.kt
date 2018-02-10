package swapp.items.com.swappify.firebase.listener.firebaselistener

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import io.reactivex.SingleEmitter
import io.reactivex.SingleOnSubscribe
import io.reactivex.functions.Function
import java.lang.Exception

class ExecuteQueryOnSubscribe<T> constructor(private val query: Query, private val marshaller: Function<QuerySnapshot, T>) :SingleOnSubscribe<T> {

    override fun subscribe(emitter: SingleEmitter<T>) {
        val rxGetValueListener = RxGetValueListener(emitter, marshaller)
        query.get()
                .addOnSuccessListener(rxGetValueListener)
                .addOnFailureListener(rxGetValueListener)
    }

    inner class RxGetValueListener<T> constructor(private val emitter: SingleEmitter<T>, private val marshaller: Function<QuerySnapshot, T>): OnSuccessListener<QuerySnapshot>, OnFailureListener {

        override fun onSuccess(querySnapshot: QuerySnapshot?) {
            if (emitter.isDisposed) {
                return
            }
            querySnapshot?.run {
                if (!querySnapshot.isEmpty) {
                    emitter.onSuccess(marshaller.apply(querySnapshot))
                } else {
                    emitter.onError(FirebaseFirestoreException("No data found", FirebaseFirestoreException.Code.NOT_FOUND))
                }
            }
        }

        override fun onFailure(exception: Exception) {
            if (emitter.isDisposed) {
                return
            }
            emitter.onError(exception)
        }

    }
}
