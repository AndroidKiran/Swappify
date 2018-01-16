package swapp.items.com.swappify.repo.game

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import swapp.items.com.swappify.common.extension.firebaseResponseToResult
import swapp.items.com.swappify.controllers.addgame.model.PostGameModel
import swapp.items.com.swappify.firebase.listener.FirebaseObservableListener
import swapp.items.com.swappify.injection.scopes.PerActivity
import javax.inject.Inject

@PerActivity
class GameDataSource @Inject constructor(val firestore: FirebaseFirestore,
                                         private val firebaseObservableListener: FirebaseObservableListener) {

    private val collectionReference: CollectionReference = firestore.collection("games")

    fun addGame(gameModel: PostGameModel?) =
            firebaseObservableListener.setValue(collectionReference.document(), gameModel, gameModel)
                    .firebaseResponseToResult()
}
