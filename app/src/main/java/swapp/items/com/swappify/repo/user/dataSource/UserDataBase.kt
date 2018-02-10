package swapp.items.com.swappify.repo.user.dataSource

import com.google.firebase.firestore.*
import io.reactivex.Single
import io.reactivex.functions.Function
import swapp.items.com.swappify.common.extension.firebaseResponseToResult
import swapp.items.com.swappify.firebase.listener.FirebaseObservableListener
import swapp.items.com.swappify.injection.scopes.PerActivity
import swapp.items.com.swappify.repo.user.model.User
import javax.inject.Inject

@PerActivity
class UserDataBase @Inject constructor(private val fireStore: FirebaseFirestore,
                                       private val fireBaseObservableListener: FirebaseObservableListener) {

    private val collectionReference: CollectionReference = fireStore.collection("users")

    fun write(user: User?) =
            fireBaseObservableListener.setValue(collectionReference.document(user?.userNumber!!), user, user)
                    .firebaseResponseToResult()

    fun fetch(mobile: String) =
            fireBaseObservableListener.getValue(collectionReference.document(mobile), toUser())
                    .firebaseResponseToResult()


    fun updateUser(user: User?) =
            fireBaseObservableListener.saveOrUpdateUser(fireStore, collectionReference.document(user?.userNumber!!), user)
                    .firebaseResponseToResult()


    fun getNearByGamers(lesserGeoPoint: GeoPoint, greaterGeoPoint: GeoPoint): Single<List<User>> {
        val query = collectionReference
                .whereEqualTo("geoPoint", lesserGeoPoint)

        return fireBaseObservableListener.executeQuery(query, toUserList())
    }

    private fun toUser(): Function<DocumentSnapshot, User> =
            Function { it.toObject(User::class.java) }

    private fun toUserList(): Function<QuerySnapshot, List<User>> =
            Function { querySnapShot: QuerySnapshot ->
                val userList = mutableListOf<User>()
                querySnapShot.forEach {
                    if (it.exists()) {
                        val user = it.toObject(User::class.java)
                        userList.add(user)
                    }
                }
                userList
            }

}
