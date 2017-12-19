package swapp.items.com.swappify.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import swapp.items.com.swappify.components.search.SearchItem

@Database(entities = arrayOf(SearchItem::class), version = 1)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        const val DB_NAME = "gamer_creed.db"

        const val SEARCH_TABLE_NAME = "search"
        const val COLUMN_ID = "id"
        const val COLUMN_TEXT = "text"
        const val COLUMN_COUNT = "count"
    }

    abstract fun searchDao(): SearchDao
}
