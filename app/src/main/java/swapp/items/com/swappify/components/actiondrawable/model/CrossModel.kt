package swapp.items.com.swappify.components.actiondrawable.model


import swapp.items.com.swappify.components.actiondrawable.util.Bezier

class CrossModel(length: Float) {

    val upLine: Bezier
    val downLine: Bezier

    init {
        upLine = Bezier.line(-length / 2, 0f, length / 2, 0f)
        upLine.rotate(0f, 0f, ANGLE.toFloat())
        downLine = Bezier.line(-length / 2, 0f, length / 2, 0f)
        downLine.rotate(0f, 0f, (-ANGLE).toFloat())
    }

    companion object {

        private val ANGLE = 45
    }
}