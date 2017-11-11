package swapp.items.com.swappify.data.user.dataSource

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Observable
import io.reactivex.functions.Function
import swapp.items.com.swappify.data.user.model.User
import swapp.items.com.swappify.firebase.listeners.FirebaseObservableListeners
import swapp.items.com.swappify.firebase.utils.DatabaseResult
import swapp.items.com.swappify.injection.scopes.PerActivity
import javax.inject.Inject

@PerActivity
class UserDataBase @Inject constructor(private val firebaseFirestore: FirebaseFirestore,
                                       private val firebaseObservableListeners: FirebaseObservableListeners) {

    fun writeUser(user: User?): Observable<DatabaseResult<User>> {
        val task: Task<Void>? = firebaseFirestore.collection("users")
                .document(user?.phoneNumber!!).set(user)
        return firebaseObservableListeners.setValue(task, user)
                .map(toUserDatabaseResults())
                .onErrorReturn { throwable: Throwable -> errorAsDatabaseResult(throwable) }
    }

    private fun toUserDatabaseResults(): Function<User, DatabaseResult<User>> =
            Function { DatabaseResult(it) }

    private fun errorAsDatabaseResult(throwable: Throwable?): DatabaseResult<User> =
            DatabaseResult(throwable ?: Throwable("Database error is missing"))
}
