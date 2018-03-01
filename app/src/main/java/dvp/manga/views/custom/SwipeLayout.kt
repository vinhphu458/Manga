package dvp.manga.views.custom

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v4.view.GestureDetectorCompat
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPager
import android.support.v4.widget.ViewDragHelper
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.view.*
import android.view.animation.*
import android.widget.FrameLayout
import dvp.manga.R

/**
 * @author Zero on 2/17/2018.
 */

class SwipeLayout : FrameLayout {

    private var mViewDragHelper: ViewDragHelper? = null
    private var mViewPager: ViewPager? = null

    private var preNextViewWidth: Int = 0
    private var mDetectorCompat: GestureDetectorCompat? = null
    private var preView: CircleProgress? = null
    private var nextView: CircleProgress? = null
    private var arrowMargin: Int = 0
    private var percentage = 0f

    private var isReached100 = false

    companion object Const {
        const val FINISH_ANIM_DURATION: Long = 500
    }

    private val dragCallback = object : ViewDragHelper.Callback() {

        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            return child == mViewPager
        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            var range = 0

            if (isFirstItem) {
                if (dx > 0)
                    range = Math.min(preNextViewWidth, left)
                else
                    range = Math.max(0, left)
            }
            if (isLastItem) {
                if (dx < 0)
                    range = Math.max(-preNextViewWidth, left)
                else
                    range = Math.min(0, left)
            }
            handleProgress(range)
            isReached100 = Math.abs(range) == preNextViewWidth
            return range
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            if (Math.abs(percentage) == 1f) {
                handleFinish()
                Handler().postDelayed({
                    mViewDragHelper!!.smoothSlideViewTo(releasedChild, 0, 0)
                    invalidate()
                }, FINISH_ANIM_DURATION)
            } else {
                mViewDragHelper!!.settleCapturedViewAt(0, 0)
                invalidate()
            }
            isReached100 = false
        }

        override fun getViewHorizontalDragRange(child: View): Int {
            return 1 //trick enable drag
        }
    }

    private val isFirstItem: Boolean get() = mViewPager!!.currentItem == 0

    private val isLastItem: Boolean get() = mViewPager!!.currentItem == mViewPager!!.adapter!!.count - 1

    private var isRemovedTouchEvent = false

    private var listener: OnSwipeListener? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {
        mViewDragHelper = ViewDragHelper.create(this, dragCallback)
        mDetectorCompat = GestureDetectorCompat(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
                val isSwipeHorizontal = Math.abs(distanceX) > Math.abs(distanceY)
                return isSwipeHorizontal && (isFirstItem && distanceX < 0 || isLastItem && distanceX > 0)
            }
        })

        preNextViewWidth = dpToPx(150)
        arrowMargin = dpToPx(55)

        val preViewParam = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        preViewParam.gravity = Gravity.CENTER_VERTICAL or Gravity.START
        preViewParam.marginStart = arrowMargin

        val nextViewParam = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        nextViewParam.gravity = Gravity.CENTER_VERTICAL or Gravity.END
        nextViewParam.marginEnd = arrowMargin


        preView = CircleProgress(context)
        preView!!.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_arrow_left))
        preView!!.layoutParams = preViewParam

        nextView = CircleProgress(context)
        nextView!!.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_arrow_right))
        nextView!!.layoutParams = nextViewParam

        addView(preView)
        addView(nextView)
    }

    override fun computeScroll() {
        if (mViewDragHelper!!.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return mViewDragHelper!!.shouldInterceptTouchEvent(ev) and mViewPager!!.onTouchEvent(ev) && !isRemovedTouchEvent
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isRemovedTouchEvent)
            mViewDragHelper!!.processTouchEvent(event)
        return true
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        (0 until childCount)
                .filter { getChildAt(it) is ViewPager }
                .forEach { mViewPager = getChildAt(it) as ViewPager }
        mViewPager!!.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                if (isFirstItem || isLastItem)
                    mViewDragHelper!!.abort()
            }
        })
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

    private fun handleProgress(range: Int) {
        if (isReached100) return
        percentage = range.toFloat() / preNextViewWidth
        if (percentage > 0) {
            preView!!.translationX = formulaPre(percentage).toFloat()
            preView!!.setProgress(percentage)
        } else {
            nextView!!.translationX = formulaNext(percentage).toFloat()
            nextView!!.setProgress(percentage)
        }
        if (listener != null) {
            listener!!.onProgress(percentage)
        }
    }

    private fun handleFinish() {
        dismissSwipe()
        if (percentage > 0) {
            preView?.finishAnim(object : AnimEndCallback {
                override fun onFinished() {
                    listener?.onPrevious()
                }
            })
            return
        } else {
            nextView?.finishAnim(object : AnimEndCallback {
                override fun onFinished() {
                    listener?.onNext()
                }
            })
        }
    }

    private fun formulaPre(x: Float): Int {
        return (arrowMargin * (x - 1)).toInt()
    }

    private fun formulaNext(x: Float): Int {
        return (arrowMargin * (x + 1)).toInt()
    }

    private fun dismissSwipe() {
        isRemovedTouchEvent = true
        Handler().postDelayed({ isRemovedTouchEvent = false }, 100)
    }

    interface OnSwipeListener {
        fun onProgress(percentage: Float)

        fun onNext()

        fun onPrevious()
    }

    fun setOnSwipeListener(listener: OnSwipeListener) {
        this.listener = listener
    }

    /**
     * @author Zero on 2/19/2018.
     */

    private inner class CircleProgress : AppCompatImageView {
        private var fadeOutIn: Animation? = null
        private var zoomFadeOut: AnimationSet? = null

        private var circlePaint: Paint? = null
        private var rect: RectF? = null

        private var startAngle = 0
        private var sweepAngle = 0

        constructor(context: Context) : super(context) {
            init()
        }

        constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
            init()
        }

        constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
            init()
        }

        private fun init() {
            circlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
            circlePaint!!.color = Color.WHITE
            circlePaint!!.strokeWidth = 5f
            circlePaint!!.style = Paint.Style.STROKE

            rect = RectF()
            val padding = dpToPx(8)
            setPadding(padding, padding, padding, padding)

            setZoomOut()
            setFadeOutIn()
        }

        private fun setFadeOutIn() {
            fadeOutIn = AlphaAnimation(1f, 0.2f)
            fadeOutIn!!.repeatMode = Animation.REVERSE
            fadeOutIn!!.repeatCount = Animation.INFINITE
            fadeOutIn!!.duration = 500
        }

        private fun setZoomOut() {
            zoomFadeOut = AnimationSet(true)
            zoomFadeOut?.fillAfter = true
            zoomFadeOut?.interpolator = DecelerateInterpolator()

            val zoom = ScaleAnimation(1f, 2f, 1f, 2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
            zoom.duration = FINISH_ANIM_DURATION

            val fade = AlphaAnimation(1f, 0f)
            fade.duration = FINISH_ANIM_DURATION

            zoomFadeOut?.addAnimation(zoom)
            zoomFadeOut?.addAnimation(fade)
            zoomFadeOut?.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {

                }

                override fun onAnimationEnd(animation: Animation?) {
                    callback.onFinished()
                }

                override fun onAnimationStart(animation: Animation?) {
                }

            })

        }

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            drawProgress(canvas)
        }

        private fun drawProgress(canvas: Canvas) {
            initRect()
            canvas.drawArc(rect!!, startAngle.toFloat(), sweepAngle.toFloat(), false, circlePaint!!)
        }

        override fun onMeasure(w: Int, h: Int) {
            super.onMeasure(w, w)
        }

        private fun initRect() {
            val padding = 10
            rect!!.left = padding.toFloat()
            rect!!.top = padding.toFloat()
            rect!!.bottom = (width - padding).toFloat()
            rect!!.right = (width - padding).toFloat()
        }

        fun setProgress(percentage: Float) {
            this.startAngle = if (percentage > 0) 180 else 0
            this.sweepAngle = (percentage * 360).toInt()
            invalidate()
            toggleAnim(Math.abs(percentage).toInt())
        }

        fun finishAnim(callback: AnimEndCallback) {
            this.callback = callback
            clearAnimation()
            startAnimation(zoomFadeOut)
        }

        private fun toggleAnim(percentage: Int) {
            if (percentage == 1) {
                startAnimation(fadeOutIn)
            } else {
                clearAnimation()
            }
        }

        private lateinit var callback: AnimEndCallback
    }

    interface AnimEndCallback {
        fun onFinished()
    }
}
