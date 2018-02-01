package swapp.items.com.swappify.firebase.listener.firebaselistener

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.Transaction
import com.google.gson.Gson
import io.reactivex.SingleEmitter
import io.reactivex.SingleOnSubscribe
import swapp.items.com.swappify.repo.user.model.User
import java.lang.Exception
import java.util.*

class RunUserTransactionOnSubscribe constructor(private val documentReference: DocumentReference, private val newUserValue: User) : SingleOnSubscribe<User> {

    override fun subscribe(emitter: SingleEmitter<User>) {
        val rxRunUserTransactionListener = RxRunUserTransactionListener(emitter, documentReference, newUserValue)
        documentReference.firestore
                .runTransaction(rxRunUserTransactionListener)
                .addOnSuccessListener(rxRunUserTransactionListener)
                .addOnFailureListener(rxRunUserTransactionListener)
    }

    inner class RxRunUserTransactionListener constructor(private val emitter: SingleEmitter<User>, private val documentReference: DocumentReference, private val newUserValue: User) :
            Transaction.Function<User>, OnSuccessListener<User>, OnFailureListener {

        override fun apply(transaction: Transaction): User? {
            val documentSnapshot= transaction.get(documentReference)
            if (documentSnapshot.exists()) {
                documentSnapshot.toObject(User::class.java).let {

                    if (it.userName.isNullOrEmpty() && !newUserValue.userName.isNullOrEmpty()) {
                        it.userName = newUserValue.userName
                    }

                    if (it.userLocation.isNullOrEmpty() && !newUserValue.userLocation.isNullOrEmpty()) {
                        it.userLocation = newUserValue.userLocation
                    }

                    if (it.userPic.isNullOrEmpty() && !newUserValue.userPic.isNullOrEmpty()) {
                        it.userPic = newUserValue.userLocation
                    }

                    if (!it.userName.isNullOrEmpty() && !it.userLocation.isNullOrEmpty()) {
                        val userJson = Gson().toJson(it)
                        val map = mutableMapOf<String, Objects>()
                        val userMap = Gson().fromJson<Map<String, Objects>>(userJson, map.javaClass)
                        transaction.update(documentReference, userMap)
                    }
                    return it
                }


            } else {
                transaction.set(documentReference, newUserValue, SetOptions.merge()).apply {
                    return newUserValue
                }
            }
            return null
        }

        override fun onSuccess(user: User?) {
            if (emitter.isDisposed) {
                return
            }
            emitter.onSuccess(user!!)
        }

        override fun onFailure(exception: Exception) {
            if (emitter.isDisposed) {
                return
            }
            emitter.onError(exception)
        }

    }

}




