package swapp.items.com.swappify.repo.user.dataSource

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Single
import swapp.items.com.swappify.common.extension.firebaseResponseToResult
import swapp.items.com.swappify.firebase.listener.FirebaseObservableListener
import swapp.items.com.swappify.firebase.utils.Result
import swapp.items.com.swappify.injection.scopes.PerActivity
import swapp.items.com.swappify.repo.user.model.User
import javax.inject.Inject

@PerActivity
class UserDataBase @Inject constructor(firestore: FirebaseFirestore,
                                       private val firebaseObservableListener: FirebaseObservableListener) {

    private val collectionReference: CollectionReference = firestore.collection("users")

    fun write(user: User?): Single<Result<User>>
        = firebaseObservableListener.setValue(collectionReference.document(user?.phoneNumber!!), user, user)
                .firebaseResponseToResult()

}
