package swapp.items.com.swappify.components.search

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View

internal class SearchDivider(context: Context) : RecyclerView.ItemDecoration() {

    private var divider: Drawable? = null
    private var dividerHeight: Int = 0
    private var dividerWidth: Int = 0

    init {
        val a = context.obtainStyledAttributes(null, intArrayOf(android.R.attr.listDivider))
        setDivider(a.getDrawable(0))
        a.recycle()
    }

    private fun setDivider(divider: Drawable?) {
        this.divider = divider
        this.dividerHeight = divider?.intrinsicHeight ?: 0
        this.dividerWidth = divider?.intrinsicWidth ?: 0
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        if (divider == null) {
            super.getItemOffsets(outRect, view, parent, state)
            return
        }

        val position = (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition
        val firstItem = position == 0
        val dividerBefore = !firstItem

        if (getOrientation(parent) == LinearLayoutManager.VERTICAL) {
            outRect.top = if (dividerBefore) dividerHeight else 0
            outRect.bottom = 0
        } else {
            outRect.left = if (dividerBefore) dividerWidth else 0
            outRect.right = 0
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        if (divider == null) {
            super.onDraw(c, parent, state)
            return
        }

        var left = 0
        var right = 0
        var top = 0
        var bottom = 0

        val orientation = getOrientation(parent)
        val childCount = parent.childCount

        val vertical = orientation == LinearLayoutManager.VERTICAL
        val size: Int
        if (vertical) {
            size = dividerHeight
            left = parent.paddingLeft
            right = parent.width - parent.paddingRight
        } else {
            size = dividerWidth
            top = parent.paddingTop
            bottom = parent.height - parent.paddingBottom
        }

        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val position = params.viewLayoutPosition
            if (position == 0) {
                continue
            }
            if (vertical) {
                top = child.top - params.topMargin - size
                bottom = top + size
            } else {
                left = child.left - params.leftMargin - size
                right = left + size
            }
            divider!!.setBounds(left, top, right, bottom)
            divider!!.draw(c)
        }
    }

    private fun getOrientation(parent: RecyclerView): Int {
        val lm = parent.layoutManager
        return (lm as? LinearLayoutManager)?.orientation ?: throw IllegalStateException("Use only with a LinearLayoutManager!")
    }

}
