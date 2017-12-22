package swapp.items.com.swappify.controllers.addgame.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import swapp.items.com.swappify.controllers.addgame.model.GameModel
import swapp.items.com.swappify.controllers.base.BaseViewHolder
import swapp.items.com.swappify.databinding.SearchResultItemBinding
import swapp.items.com.swappify.injection.scopes.PerFragment
import javax.inject.Inject

@PerFragment
class SearchResultAdapter<N : SearchResultAdapter.SearchResultItemListener> @Inject constructor(): RecyclerView.Adapter<BaseViewHolder>() {

    var searchResults = arrayListOf<GameModel>()
    private var clickNavigator: N? = null

    var navigator: N?
        get() = clickNavigator
        set(navigator) {
            if (clickNavigator == null) {
                clickNavigator = navigator
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder {
        val searchResultBinding = SearchResultItemBinding.inflate(
                LayoutInflater.from(parent?.context),
                parent,
                false
        )
        return SearchResultViewHolder(searchResultBinding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder?, position: Int) {
        holder?.onBind(position)
    }

    override fun getItemCount(): Int = searchResults.size

    fun setData(list: List<GameModel>?) {
        searchResults = list as ArrayList<GameModel>
        notifyDataSetChanged()
    }

    private inner class SearchResultViewHolder : BaseViewHolder {
        val binding: SearchResultItemBinding

        constructor(binding: SearchResultItemBinding): super(binding.root) {
            this.binding = binding
        }

        override fun onBind(position: Int) {
            val gameModel = searchResults[position]
            gameModel.update(gameModel.cover?.url, gameModel.firstReleaseDate)
            binding.gameViewModel = gameModel
            binding.executePendingBindings()
            binding.root.setOnClickListener({
                clickNavigator?.onItemClick(gameModel)

            })
        }
    }

    interface SearchResultItemListener {
        fun onItemClick(gameModel: GameModel?)
    }
}
