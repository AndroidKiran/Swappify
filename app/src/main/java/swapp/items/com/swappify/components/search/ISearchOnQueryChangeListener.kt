package swapp.items.com.swappify.components.search

/**
 * Created by ravi on 10/12/17.
 */
interface ISearchOnQueryChangeListener {

    fun onQueryTextChange(newText: String): Boolean

    fun onQueryTextSubmit(query: String): Boolean

}