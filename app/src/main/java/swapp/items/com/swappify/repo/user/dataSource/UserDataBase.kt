package swapp.items.com.swappify.repo.user.dataSource

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import swapp.items.com.swappify.common.extension.firebaseResponseToResult
import swapp.items.com.swappify.firebase.listener.FirebaseObservableListener
import swapp.items.com.swappify.injection.scopes.PerActivity
import swapp.items.com.swappify.repo.user.model.User
import javax.inject.Inject

@PerActivity
class UserDataBase @Inject constructor(private val fireStore: FirebaseFirestore,
                                       private val fireBaseObservableListener: FirebaseObservableListener) {

    private val collectionReference: CollectionReference = fireStore.collection("users")
/*
    fun write(user: User?) =
            fireBaseObservableListener.setValue(collectionReference.document(user?.userNumber!!), user, user)
                    .firebaseResponseToResult()

    fun fetch(mobile: String) =
            fireBaseObservableListener.getValue(collectionReference.document(mobile), toUser())
                    .firebaseResponseToResult()*/


    fun updateUser(user: User?) =
            fireBaseObservableListener.saveOrUpdateUser(fireStore, collectionReference.document(user?.userNumber!!), user)
                    .firebaseResponseToResult()

}
