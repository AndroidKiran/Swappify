package swapp.items.com.swappify.firebase.listeners.FirebaseDatabaseListeners


import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import swapp.items.com.swappify.data.user.model.User
import java.lang.Exception

class SetValueOnSubscribe constructor(
        private val task: Task<Void>?,
        private val returnValue: User?) : ObservableOnSubscribe<User> {


    override fun subscribe(emitter: ObservableEmitter<User>) {
        task?.addOnSuccessListener(RxSetValueListener(
                emitter = emitter,
                returnValue = returnValue))?.addOnFailureListener(
                RxSetValueListener(emitter = emitter, returnValue = returnValue))

    }

    inner class RxSetValueListener constructor(
            private val emitter: ObservableEmitter<User>?,
            private val returnValue: User?) : OnSuccessListener<Void>, OnFailureListener {

        override fun onSuccess(p0: Void?) {
            if (emitter!!.isDisposed) {
                return
            }
            emitter.onNext(returnValue!!)
            emitter.onComplete()
        }


        override fun onFailure(exception: Exception) {
            if (emitter!!.isDisposed) {
                return
            }
            emitter.onError(exception)
        }
    }
}