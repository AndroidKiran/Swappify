package swapp.items.com.swappify.firebase.listener.firebaselistener

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.Transaction
import io.reactivex.SingleEmitter
import io.reactivex.SingleOnSubscribe
import swapp.items.com.swappify.repo.user.model.User
import java.lang.Exception

class SaveOrUpdateUserOnSubscribe constructor(private val firestore: FirebaseFirestore, private val documentReference: DocumentReference, private val newUserValue: User) : SingleOnSubscribe<User> {

    override fun subscribe(emitter: SingleEmitter<User>) {
        val rxRunUserTransactionListener = RxRunUserTransactionListener(emitter, documentReference, newUserValue)
        firestore.runTransaction(rxRunUserTransactionListener)
                .addOnSuccessListener(rxRunUserTransactionListener)
                .addOnFailureListener(rxRunUserTransactionListener)
    }

    inner class RxRunUserTransactionListener constructor(private val emitter: SingleEmitter<User>, private val documentReference: DocumentReference, private val newUserValue: User) :
            Transaction.Function<User>, OnSuccessListener<User>, OnFailureListener {

        override fun apply(transaction: Transaction): User {

            val documentSnapshot = transaction.get(documentReference)
            if (documentSnapshot.exists()) {
                val userMap = hashMapOf<String, Any>().apply {
                    newUserValue.userName?.apply {
                        put("userName", this)
                    }

                    newUserValue.userPic?.apply {
                        put("userPic", this)
                    }

                    newUserValue.geoPoint?.apply {
                        put("geoPoint", this)
                    }

                    newUserValue.modifiedAt?.apply {
                        put("modifiedAt", this)
                    }
                }

                if (userMap.isNotEmpty()) {
                    transaction.update(documentReference, userMap)
                }
            } else {
                transaction.set(documentReference, newUserValue, SetOptions.merge())
            }
            return newUserValue
        }

        override fun onSuccess(user: User) {
            if (emitter.isDisposed) {
                return
            }
            emitter.onSuccess(user)
        }

        override fun onFailure(exception: Exception) {
            if (emitter.isDisposed) {
                return
            }
            emitter.onError(exception)
        }

    }

}




