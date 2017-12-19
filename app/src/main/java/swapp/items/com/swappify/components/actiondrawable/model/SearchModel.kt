package swapp.items.com.swappify.components.actiondrawable.model

import swapp.items.com.swappify.components.actiondrawable.util.Bezier

class SearchModel(radius: Float, barLength: Float) {

    val topLeftQuadrant: Bezier
    val topRightQuadrant: Bezier
    val bottomLeftQuadrant: Bezier
    val bottomRightQuadrant: Bezier
    val handle: Bezier

    init {

        val size = ((2 * radius + barLength) / Math.sqrt(2.0)).toFloat()
        val center = radius - size / 2

        // quadrants
        bottomRightQuadrant = Bezier.quadrant(radius, 0f).offset(center, center)
        bottomLeftQuadrant = Bezier.quadrant(radius, 90f).offset(center, center)
        topLeftQuadrant = Bezier.quadrant(radius, 180f).offset(center, center)
        topRightQuadrant = Bezier.quadrant(radius, 270f).offset(center, center)

        // handle
        handle = Bezier.line(0f, 0f, barLength, 0f)
                .offset(radius, 0f)
                .rotate(0f, 0f, HANDLE_ANGLE.toFloat())
                .offset(center, center)

    }

    companion object {
        internal val HANDLE_ANGLE = 45
    }
}
