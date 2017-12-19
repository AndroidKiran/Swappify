package swapp.items.com.swappify.components.actiondrawable.util

class Point {
    var x: Float = 0.toFloat()
    var y: Float = 0.toFloat()

    constructor() {}

    constructor(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    fun rotate(center: Point, angle: Float) {
        rotate(center.x, center.y, angle)
    }

    operator fun set(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    fun set(p: Point) {
        this.x = p.x
        this.y = p.y
    }

    fun offset(dx: Float, dy: Float) {
        x += dx
        y += dy
    }

    fun rotate(centerX: Float, centerY: Float, degree: Float) {
        val newX = (centerX + (Math.cos(Math.toRadians(degree.toDouble())) * (x - centerX) - Math.sin(Math.toRadians(degree.toDouble())) * (y - centerY))).toFloat()
        val newY = (centerY + (Math.sin(Math.toRadians(degree.toDouble())) * (x - centerX) + Math.cos(Math.toRadians(degree.toDouble())) * (y - centerY))).toFloat()
        this.x = newX
        this.y = newY
    }

    override fun toString(): String = super.toString() + "{" + x + "," + y + "}"
}