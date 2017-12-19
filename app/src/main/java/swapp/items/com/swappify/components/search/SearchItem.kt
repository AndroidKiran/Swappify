package swapp.items.com.swappify.components.search

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import swapp.items.com.swappify.room.AppDatabase.Companion.COLUMN_COUNT
import swapp.items.com.swappify.room.AppDatabase.Companion.COLUMN_ID
import swapp.items.com.swappify.room.AppDatabase.Companion.COLUMN_TEXT
import swapp.items.com.swappify.room.AppDatabase.Companion.SEARCH_TABLE_NAME

@Entity(tableName = SEARCH_TABLE_NAME, indices = [(Index(COLUMN_TEXT, unique = true))])
data class SearchItem constructor( @PrimaryKey(autoGenerate = true)
                                   @ColumnInfo(name = COLUMN_ID)
                                   var id: Long = 0): Parcelable {


    @ColumnInfo(name = COLUMN_TEXT)
    var text: String = ""

    @ColumnInfo(name = COLUMN_COUNT)
    var count: Int = 0

    constructor(parcel: Parcel) : this(parcel.readLong()) {
        text = parcel.readString()
        count = parcel.readInt()
    }

    constructor(text: String, count: Int) : this() {
        this.text = text
        this.count = count
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(text)
        parcel.writeInt(count)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<SearchItem> {
        override fun createFromParcel(parcel: Parcel): SearchItem = SearchItem(parcel)

        override fun newArray(size: Int): Array<SearchItem?> = arrayOfNulls(size)
    }
}
