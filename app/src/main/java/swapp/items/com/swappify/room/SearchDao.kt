package swapp.items.com.swappify.room

import android.arch.persistence.room.*
import swapp.items.com.swappify.components.search.SearchItem
import swapp.items.com.swappify.room.AppDatabase.Companion.COLUMN_COUNT
import swapp.items.com.swappify.room.AppDatabase.Companion.COLUMN_TEXT
import swapp.items.com.swappify.room.AppDatabase.Companion.SEARCH_TABLE_NAME

@Dao
interface SearchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(searchItem: SearchItem?)

    @Query("SELECT * FROM $SEARCH_TABLE_NAME WHERE $COLUMN_TEXT LIKE '%' || :query || '%' ORDER BY $COLUMN_COUNT DESC LIMIT 4")
    fun getSearchResults(query: String?): List<SearchItem>

    @Query("SELECT * FROM $SEARCH_TABLE_NAME ORDER BY $COLUMN_COUNT DESC LIMIT 2")
    fun getSearchResults(): List<SearchItem>

    @Query("SELECT * FROM $SEARCH_TABLE_NAME WHERE $COLUMN_TEXT = :query")
    fun getSearchItemFor(query: String?): SearchItem

    @Update
    fun updateSearchItem(searchItem: SearchItem?)
}
