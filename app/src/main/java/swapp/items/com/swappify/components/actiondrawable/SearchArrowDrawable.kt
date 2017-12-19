package swapp.items.com.swappify.components.actiondrawable

import android.content.Context
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import swapp.items.com.swappify.R
import swapp.items.com.swappify.components.actiondrawable.model.ArrowModel
import swapp.items.com.swappify.components.actiondrawable.model.SearchModel
import java.lang.Math.round

open class SearchArrowDrawable @JvmOverloads constructor(val context: Context, @AttrRes defStyleAttr: Int = R.attr.searchArrowDrawableStyle, @StyleRes defStyleRes: Int = R.style.SearchArrowDrawable) : ToggleDrawable(context, defStyleAttr, defStyleRes) {

    init {

        val typedArray = context.theme
                .obtainStyledAttributes(null, R.styleable.SearchArrowToggle, defStyleAttr, defStyleRes)

        val radius = round(typedArray.getDimension(R.styleable.SearchArrowToggle_td_searchRadius, 0f)).toFloat()
        val barLength = round(typedArray.getDimension(R.styleable.SearchArrowToggle_td_searchLength, 0f)).toFloat()
        val arrowHeadLength = round(typedArray.getDimension(
                R.styleable.SearchArrowToggle_td_arrowHeadLength, 0f)).toFloat()
        val arrowShaftLength = typedArray
                .getDimension(R.styleable.SearchArrowToggle_td_arrowShaftLength, 0f)
        typedArray.recycle()

        val search = SearchModel(radius, barLength)
        val arrow = ArrowModel(arrowShaftLength, arrowHeadLength, strokeWidth)

        add(search.handle, arrow.bottomHead)
        add(search.topRightQuadrant, arrow.topHead)
        add(search.bottomRightQuadrant, arrow.bottomHead)
        add(search.topLeftQuadrant, arrow.body)
        add(search.bottomLeftQuadrant, arrow.body)
    }
}
