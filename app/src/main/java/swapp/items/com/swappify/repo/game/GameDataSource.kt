package swapp.items.com.swappify.repo.game

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Single
import io.reactivex.functions.Function
import swapp.items.com.swappify.controllers.addgame.model.PostGameModel
import swapp.items.com.swappify.firebase.listener.FirebaseObservableListener
import swapp.items.com.swappify.injection.scopes.PerActivity
import javax.inject.Inject

@PerActivity
class GameDataSource @Inject constructor(val firestore: FirebaseFirestore,
                                         val firebaseObservableListener: FirebaseObservableListener) {

    private val collectionReference: CollectionReference = firestore.collection("games")

    fun addGame(gameModel: PostGameModel?): Single<PostGameModel> =
            firebaseObservableListener.addValueOnSubscribe(collectionReference.add(gameModel!!), asGameModel(gameModel))

    private fun asGameModel(gameModel: PostGameModel?): Function<DocumentReference, PostGameModel> {
        return Function {
            return@Function gameModel!!
        }
    }
}
