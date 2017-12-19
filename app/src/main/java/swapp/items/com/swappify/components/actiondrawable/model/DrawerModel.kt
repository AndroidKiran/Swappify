package swapp.items.com.swappify.components.actiondrawable.model

import swapp.items.com.swappify.components.actiondrawable.util.Bezier

class DrawerModel(length: Float, gapSize: Float) {

    val topLine: Bezier
    val middleLine: Bezier
    val bottomLine: Bezier

    init {
        topLine = Bezier.line(-length / 2, 0f, length / 2, 0f)
        topLine.offset(0f, -gapSize)

        middleLine = Bezier.line(-length / 2, 0f, length / 2, 0f)

        bottomLine = Bezier.line(-length / 2, 0f, length / 2, 0f)
        bottomLine.offset(0f, gapSize)
    }
}