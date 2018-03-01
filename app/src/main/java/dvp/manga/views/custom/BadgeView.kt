package dvp.manga.views.custom

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import dvp.manga.R


/**
 * @author Zero on 2/7/2018.
 */
class BadgeView : View {

    private var centerBound = RectF()
    private val textRect = Rect()
    private val path = Path()
    private var textSize: Float = 0f
    private var text: String = "0"
    private var bgColor: Int = 0
    private lateinit var txtPain: Paint
    private lateinit var bgPain: Paint

    constructor(context: Context) : super(context, null) {
        init()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(heightMeasureSpec, heightMeasureSpec)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs, -1) {
        val typed = context.obtainStyledAttributes(attrs, R.styleable.BadgeView)
        textSize = typed.getDimension(R.styleable.BadgeView_text_size, 16f)
        text = typed.getString(R.styleable.BadgeView_text)
        bgColor = typed.getColor(R.styleable.BadgeView_background_color, Color.BLACK)
        typed.recycle()
        init()
    }

    private fun init() {
        txtPain = Paint(Paint.ANTI_ALIAS_FLAG)
        txtPain.color = Color.WHITE
        txtPain.getTextBounds(text, 0, text.length, textRect)
        txtPain.textSize = textSize
        txtPain.typeface = Typeface.DEFAULT_BOLD


        bgPain = Paint(Paint.ANTI_ALIAS_FLAG)
        bgPain.style = Paint.Style.FILL
        bgPain.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)
        bgPain.color = bgColor

    }

    private fun drawText(canvas: Canvas) {
        txtPain.getTextBounds(text, 0, text.length, textRect)

        val left = (width / 4).toFloat()
        val top = (height / 4).toFloat()
        val right = width.toFloat()
        val bottom = top * 2

        centerBound = RectF(left, top, right, bottom)

        val startX = centerBound.centerX() - textRect.centerX()
        val startY = centerBound.centerY() - textRect.centerY()

        canvas.rotate(45f, centerBound.centerX(), centerBound.centerY())
        canvas.drawText(text, startX, startY, txtPain)
    }


    private fun drawBg(canvas: Canvas) {
        //Point 4-4
        val px2 = canvas.width.toFloat()
        val py2 = canvas.height.toFloat()
        //Point 4-2
        val px3 = canvas.width.toFloat()
        val py3 = (canvas.height / 2).toFloat()
        //Point 2-0
        val px4 = (canvas.width / 2).toFloat()
        val py4 = 0f

        path.lineTo(px2, py2)
        path.lineTo(px3, py3)
        path.lineTo(px4, py4)
        path.close()

        canvas.drawPath(path, bgPain)
        canvas.save()
    }

    override fun onDraw(canvas: Canvas) {
        drawBg(canvas)
        drawText(canvas)
    }

    fun setText(str: String): BadgeView {
        text = str
        postInvalidate()
        return this
    }

}
