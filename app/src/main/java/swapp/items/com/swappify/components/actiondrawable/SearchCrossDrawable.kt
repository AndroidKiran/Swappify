package swapp.items.com.swappify.components.actiondrawable

import android.content.Context
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import swapp.items.com.swappify.R
import swapp.items.com.swappify.components.actiondrawable.model.CrossModel
import swapp.items.com.swappify.components.actiondrawable.model.SearchModel
import java.lang.Math.round

open class SearchCrossDrawable @JvmOverloads constructor(context: Context, @AttrRes defStyleAttr: Int = R.attr.searchCrossDrawableStyle, @StyleRes defStyleRes: Int = R.style.SearchCrossDrawable) : ToggleDrawable(context, defStyleAttr, defStyleRes) {

    init {

        val typedArray = context.theme
                .obtainStyledAttributes(null, R.styleable.SearchCrossToggle, defStyleAttr, defStyleRes)

        val radius = round(typedArray.getDimension(R.styleable.SearchCrossToggle_td_searchRadius, 0f)).toFloat()
        val searchLength = round(typedArray.getDimension(R.styleable.SearchCrossToggle_td_searchLength, 0f)).toFloat()
        val crossLength = round(typedArray.getDimension(R.styleable.SearchCrossToggle_td_crossLength, 0f)).toFloat()
        typedArray.recycle()

        val search = SearchModel(radius, searchLength)
        val cross = CrossModel(crossLength)

        add(search.handle, cross.upLine)
        add(search.topRightQuadrant, cross.upLine)
        add(search.bottomRightQuadrant, cross.upLine)
        add(search.topLeftQuadrant, cross.downLine)
        add(search.bottomLeftQuadrant, cross.downLine)
    }
}
