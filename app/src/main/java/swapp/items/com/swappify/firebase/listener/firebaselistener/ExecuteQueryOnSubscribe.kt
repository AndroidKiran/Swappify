package swapp.items.com.swappify.firebase.listener.firebaselistener

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import io.reactivex.FlowableEmitter
import io.reactivex.FlowableOnSubscribe
import io.reactivex.functions.Function
import java.lang.Exception

class ExecuteQueryOnSubscribe<T> constructor(private val query: Query, private val marshaller: Function<QuerySnapshot, T>) :FlowableOnSubscribe<T> {

    override fun subscribe(emitter: FlowableEmitter<T>) {
        val rxGetValueListener = RxGetValueListener(emitter, marshaller)
        query.get()
                .addOnSuccessListener(rxGetValueListener)
                .addOnFailureListener(rxGetValueListener)
    }

    inner class RxGetValueListener<T> constructor(private val emitter: FlowableEmitter<T>, private val marshaller: Function<QuerySnapshot, T>): OnSuccessListener<QuerySnapshot>, OnFailureListener {

        override fun onSuccess(querySnapshot: QuerySnapshot?) {
            if (emitter.isCancelled) {
                return
            }

            querySnapshot?.run {
                if (!querySnapshot.isEmpty) {
                    emitter.onNext(marshaller.apply(querySnapshot))
                } else {
                    emitter.onError(FirebaseFirestoreException("No data found", FirebaseFirestoreException.Code.NOT_FOUND))
                }
            }
        }

        override fun onFailure(exception: Exception) {
            if (emitter.isCancelled) {
                return
            }
            emitter.onError(exception)
        }

    }
}
