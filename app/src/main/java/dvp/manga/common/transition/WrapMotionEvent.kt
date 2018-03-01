package dvp.manga.common.transition

import android.view.MotionEvent


/**
 * @author Zero on 2/14/2018.
 */
open class WrapMotionEvent (private var event: MotionEvent) {

    val action: Int
        get() = event.action

    val x: Float
        get() = event.x

    val y: Float
        get() = event.y

    open val pointerCount: Int
        get() = 1

    open fun getX(pointerIndex: Int): Float {
        verifyPointerIndex(pointerIndex)
        return x
    }

    open fun getY(pointerIndex: Int): Float {
        verifyPointerIndex(pointerIndex)
        return y
    }

    open fun getPointerId(pointerIndex: Int): Int {
        verifyPointerIndex(pointerIndex)
        return 0
    }

    private fun verifyPointerIndex(pointerIndex: Int) {
        if (pointerIndex > 0) {
            throw IllegalArgumentException(
                    "Invalid pointer index for Donut/Cupcake")
        }
    }

    companion object {
        fun wrap(event: MotionEvent): WrapMotionEvent {
            try {
                return EclairMotionEvent(event)
            } catch (e: VerifyError) {
                return WrapMotionEvent(event)
            }

        }
    }

}