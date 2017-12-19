package swapp.items.com.swappify.components.actiondrawable.model

import swapp.items.com.swappify.components.actiondrawable.util.Bezier

class ArrowModel(length: Float, headLength: Float, stroke: Float) {

    val topHead: Bezier
    val bottomHead: Bezier
    val body: Bezier

    init {
        // top head
        topHead = Bezier.line(0f, 0f, -headLength, 0f)
        topHead.offset(stroke / 2, 0f)
        topHead.rotate(0f, 0f, HEAD_ANGLE.toFloat())
        topHead.offset(length / 2, 0f)

        // body
        body = Bezier.line(length / 2, 0f, -length / 2, 0f)

        // bottom head
        bottomHead = Bezier.line(0f, 0f, -headLength, 0f)
        bottomHead.offset(stroke / 2, 0f)
        bottomHead.rotate(0f, 0f, (-HEAD_ANGLE).toFloat())
        bottomHead.offset(length / 2, 0f)
    }

    companion object {
        internal val HEAD_ANGLE = 45
    }
}