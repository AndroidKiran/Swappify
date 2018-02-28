package swapp.items.com.swappify.repo.game.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import io.reactivex.Flowable
import io.reactivex.functions.Function
import swapp.items.com.swappify.common.extension.firebaseResponseToResult
import swapp.items.com.swappify.controller.addgame.model.GameModel
import swapp.items.com.swappify.firebase.listener.FirebaseObservableListener
import swapp.items.com.swappify.injection.scopes.PerActivity
import java.util.*
import javax.inject.Inject

@PerActivity
class GameDataBase @Inject constructor(val firestore: FirebaseFirestore,
                                       private val firebaseObservableListener: FirebaseObservableListener) {

    private val gamesCollectionReference = firestore.collection("games")

    fun setGame(gameModel: GameModel?) =
            firebaseObservableListener.setValue(gamesCollectionReference.document(), gameModel, gameModel)
                    .firebaseResponseToResult()

    fun getGames(userNumber: String?, gameName: String?, genre: String?,
                 createdAt: Date?): Flowable<List<GameModel>> {
        val query = gamesCollectionReference

        userNumber?.let {
            query.whereEqualTo("userNumber", it)
        }

        gameName?.let {
            query.whereEqualTo("name", it)
        }

        genre?.let {
            query.whereEqualTo("genre", it)
        }

        query.orderBy(GameModel.CREATED_AT)

        createdAt?.let {
            query.startAfter(createdAt)
        }

        return firebaseObservableListener.executeQuery(query, toGamesList())
    }

    private fun toGamesList(): Function<QuerySnapshot, List<GameModel>> =
            Function { querySnapshot ->
                val gamesList = mutableListOf<GameModel>()
                querySnapshot.forEach { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val gameModel = documentSnapshot.toObject(GameModel::class.java)
                        gameModel.id = documentSnapshot.id
                        gamesList.add(gameModel)
                    }
                }
                gamesList
            }


}
