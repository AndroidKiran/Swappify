package swapp.items.com.swappify.components.actiondrawable.util

import android.graphics.Path

import java.lang.Math.PI
import java.lang.Math.tan

class Bezier {

    val p1 = Point()
    val p2 = Point()
    val e1 = Point()
    val e2 = Point()

    constructor() {}

    constructor(bezier: Bezier) {
        p1.set(bezier.p1)
        p2.set(bezier.p2)
        e1.set(bezier.e1)
        e2.set(bezier.e2)
    }

    fun offset(x: Float, y: Float): Bezier {
        p1.offset(x, y)
        p2.offset(x, y)
        e1.offset(x, y)
        e2.offset(x, y)
        return this
    }

    fun rotate(x: Float, y: Float, degree: Float): Bezier {
        p1.rotate(x, y, degree)
        p2.rotate(x, y, degree)
        e1.rotate(x, y, degree)
        e2.rotate(x, y, degree)
        return this
    }

    fun addTo(path: Path): Bezier {
        path.moveTo(p1.x, p1.y)
        path.cubicTo(e1.x, e1.y, e2.x, e2.y, p2.x, p2.y)
        return this
    }

    fun swap(): Bezier {
        val tmp = Point()
        tmp.set(p1)
        p1.set(p2)
        p2.set(tmp)
        tmp.set(e1)
        e1.set(e2)
        e2.set(tmp)
        return this
    }

    companion object {

        fun line(x1: Float, y1: Float, x2: Float, y2: Float): Bezier {
            val bezier = Bezier()
            bezier.p1.set(x1, y1)
            bezier.e1.set((x1 + x2) / 2, (y1 + y2) / 2)
            bezier.e2.set(bezier.e1)
            bezier.p2.set(x2, y2)
            return bezier
        }

        /**
         * Construct a quadrant from a bezier line.
         * @param radius radius in pixel of the quadrant
         * @param startDegree start angle, sweep is clockwise
         * @return a quadrant
         */
        fun quadrant(radius: Float, startDegree: Float): Bezier {
            val dv = (4f / 3f * tan(PI / 8)).toFloat() * radius
            val bezier = Bezier()

            bezier.p1.set(radius, 0f)
            bezier.p2.set(0f, radius)
            bezier.e1.set(bezier.p1.x, bezier.p1.y + dv)
            bezier.e2.set(bezier.p2.x + dv, bezier.p2.y)
            if (startDegree != 0f) bezier.rotate(0f, 0f, startDegree)
            return bezier
        }
    }
}