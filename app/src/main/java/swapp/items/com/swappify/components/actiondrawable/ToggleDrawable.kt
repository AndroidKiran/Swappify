package swapp.items.com.swappify.components.actiondrawable

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.Drawable
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import swapp.items.com.swappify.R
import swapp.items.com.swappify.components.actiondrawable.util.Bezier
import swapp.items.com.swappify.components.actiondrawable.util.Point
import swapp.items.com.swappify.components.actiondrawable.util.Vector
import java.util.*

open class ToggleDrawable @JvmOverloads constructor(context: Context, @AttrRes defStyleAttr: Int = 0, @StyleRes defStyleRes: Int = R.style.ToggleDrawable) : Drawable() {

    val paint = Paint()

    // The thickness of strokes
    private var paintStrokeWidth: Float = 0.toFloat()
    // Whether the canvas should spin or not during progress
    private var drawableSpin: Boolean = false
    // Use Path instead of canvas operations so that if color has transparency,
    // overlapping section wont look different
    private val paintPath = Path()
    // The interpolated version of the original progress
    private var drawableProgress: Float = 0.toFloat()
    // The reported intrinsic size of the drawable
    private val strokeSize: Int

    private var drawableVerticalMirror: Boolean = false

    private var drawableTint: ColorStateList? = null
    private var drawableTintMode: PorterDuff.Mode? = PorterDuff.Mode.SRC_IN

    private val bezierStartList = ArrayList<Bezier>()
    private val bezierEndList = ArrayList<Bezier>()
    private val bezierCurrentList = ArrayList<Bezier>()

    var strokeWidth: Float
        get() = paintStrokeWidth
        set(strokeWidth) {
            if (paintStrokeWidth != strokeWidth) {
                paintStrokeWidth = strokeWidth
                paint.strokeWidth = strokeWidth
                invalidateSelf()
            }
        }

    var isSpinEnabled: Boolean
        get() = drawableSpin
        set(spin) {
            if (drawableSpin != spin) {
                drawableSpin = spin
                invalidateSelf()
            }
        }

    protected val isLayoutRtl: Boolean
        get() = false

    var progress: Float
        get() = drawableProgress
        set(progress) {
            if (drawableProgress != progress) {
                drawableProgress = progress
                invalidateSelf()
            }
        }

    var verticalMirror: Boolean
    get() = drawableVerticalMirror
    set(verticalMirror) {
        if (drawableVerticalMirror != verticalMirror) {
            drawableVerticalMirror = verticalMirror
            invalidateSelf()
        }
    }

    init {
        val typedArray = context.theme
                .obtainStyledAttributes(null, R.styleable.ToggleDrawable,
                        defStyleAttr, defStyleRes)

        paint.isAntiAlias = true
        paint.color = typedArray.getColor(R.styleable.ToggleDrawable_td_color, 0)
        strokeSize = typedArray.getDimensionPixelSize(R.styleable.ToggleDrawable_td_drawableSize, 0)
        paintStrokeWidth = typedArray.getDimension(R.styleable.ToggleDrawable_td_stroke, 0f)
        drawableSpin = typedArray.getBoolean(R.styleable.ToggleDrawable_td_spin, true)
        typedArray.recycle()

        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.MITER
        paint.strokeCap = Paint.Cap.BUTT
        paint.strokeWidth = paintStrokeWidth

        updateTintFilter()
    }

    fun reset() {
        bezierStartList.clear()
        bezierEndList.clear()
        bezierCurrentList.clear()
    }

    fun add(start: Bezier, end: Bezier) {
        var start = start
        val v1 = Vector(start.p1, start.p2)
        val v2 = Vector(end.p1, end.p2)
        if (Math.abs(v1.angle(v2)) >= Math.PI / 2)
            start = Bezier(start).swap()
        bezierStartList.add(start)
        bezierEndList.add(end)
        bezierCurrentList.add(Bezier())
    }

    override fun draw(canvas: Canvas) {
        val bounds = bounds
        val isRtl = isLayoutRtl
        val canvasRotate = lerp((if (isRtl) -180 else 0).toFloat(), (if (isRtl) 0 else 180).toFloat(), drawableProgress)

        paintPath.rewind()

        for (i in bezierCurrentList.indices) {
            val current = bezierCurrentList[i]
            lerp(bezierStartList[i], bezierEndList[i], bezierCurrentList[i], drawableProgress)
            current.addTo(paintPath)
        }

        canvas.save()
        canvas.translate(bounds.centerX().toFloat(), bounds.centerY().toFloat())

        if (drawableSpin) {
            canvas.rotate(canvasRotate * if (drawableVerticalMirror xor isRtl) -1 else 1)
        } else if (isRtl) {
            canvas.rotate(180f)
        }

        canvas.drawPath(paintPath, paint)
        canvas.restore()
    }

    override fun getIntrinsicHeight(): Int = strokeSize

    override fun getIntrinsicWidth(): Int = strokeSize

    override fun isAutoMirrored(): Boolean = true

    override fun setAlpha(alpha: Int) {
        if (alpha != paint.alpha) {
            paint.alpha = alpha
            invalidateSelf()
        }
    }

    override fun getAlpha(): Int = paint.alpha

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
        invalidateSelf()
    }

    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

    override fun setTintList(tint: ColorStateList?) {
        drawableTint = tint
        updateTintFilter()
    }

    override fun setTintMode(tintMode: PorterDuff.Mode) {
        if (drawableTintMode != tintMode) {
            drawableTintMode = tintMode
            updateTintFilter()
        }
    }

    override fun onStateChange(state: IntArray): Boolean {
        updateTintFilter()
        return true
    }

    override fun isStateful(): Boolean = drawableTint != null && drawableTint!!.isStateful

    private fun updateTintFilter() {
        if (drawableTint == null || drawableTintMode == null) {
            colorFilter = null
            return
        }

        val color = drawableTint!!.getColorForState(state, Color.TRANSPARENT)
        colorFilter = PorterDuffColorFilter(color, drawableTintMode!!)
    }

    private fun lerp(a: Bezier, b: Bezier, out: Bezier, t: Float) {
        lerp(a.p1, b.p1, out.p1, t)
        lerp(a.p2, b.p2, out.p2, t)
        lerp(a.e1, b.e1, out.e1, t)
        lerp(a.e2, b.e2, out.e2, t)
    }

    private fun lerp(a: Point, b: Point, out: Point, t: Float) {
        out.x = lerp(a.x, b.x, t)
        out.y = lerp(a.y, b.y, t)
    }

    private fun lerp(a: Float, b: Float, t: Float): Float = a + (b - a) * t
}
