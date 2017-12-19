package swapp.items.com.swappify.components.search

import android.content.Context
import android.support.v7.widget.AppCompatEditText
import android.util.AttributeSet
import android.view.KeyEvent

class SearchEditText : AppCompatEditText {

    private lateinit var searchView: SearchView

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    fun setSearchView(searchView: SearchView) {
        this.searchView = searchView
    }

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent): Boolean {
        if (searchView.getShouldHideOnKeyboardClose()) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                if (searchView.isSearchOpen()) {
                    searchView.close(true)
                    return true
                }
            }
        }
        return super.dispatchKeyEvent(event)
    }

}
