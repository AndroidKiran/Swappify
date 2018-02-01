package swapp.items.com.swappify.components.search

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import paperparcel.PaperParcel
import paperparcel.PaperParcelable
import swapp.items.com.swappify.room.AppDatabase.Companion.COLUMN_COUNT
import swapp.items.com.swappify.room.AppDatabase.Companion.COLUMN_ID
import swapp.items.com.swappify.room.AppDatabase.Companion.COLUMN_TEXT
import swapp.items.com.swappify.room.AppDatabase.Companion.SEARCH_TABLE_NAME

@PaperParcel
@Entity(tableName = SEARCH_TABLE_NAME, indices = [(Index(COLUMN_TEXT, unique = true))])
data class SearchItem constructor( @PrimaryKey(autoGenerate = true)
                                   @ColumnInfo(name = COLUMN_ID)
                                   var id: Long = 0): PaperParcelable {


    @ColumnInfo(name = COLUMN_TEXT)
    var text: String = ""

    @ColumnInfo(name = COLUMN_COUNT)
    var count: Int = 0

    constructor(text: String, count: Int) : this() {
        this.text = text
        this.count = count
    }

    companion object {
        @JvmField
        val CREATOR = PaperParcelSearchItem.CREATOR
    }
}
