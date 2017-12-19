package swapp.items.com.swappify.components.actiondrawable.util

class Vector(a: Point, b: Point) {
    var x: Float = 0.toFloat()
    var y: Float = 0.toFloat()

    init {
        x = b.x - a.x
        y = b.y - a.y
    }

    /**
     *
     * @param vector to compare
     * @return the relative angle between [-pi..pi]
     */
    fun angle(vector: Vector): Double {
        var angle = Math.atan2(vector.y.toDouble(), vector.x.toDouble()) - Math.atan2(y.toDouble(), x.toDouble())
        if (angle > Math.PI) angle -= 2 * Math.PI
        if (angle < -Math.PI) angle += 2 * Math.PI
        return angle
    }

    override fun toString(): String = super.toString() + "{" + x + "," + y + "}"
}
