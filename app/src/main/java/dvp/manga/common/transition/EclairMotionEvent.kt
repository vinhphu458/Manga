package dvp.manga.common.transition

import android.view.MotionEvent


/**
 * @author Zero on 2/14/2018.
 */
class EclairMotionEvent (val event: MotionEvent) : WrapMotionEvent(event) {

    override val pointerCount: Int
        get() = event.pointerCount

    override fun getX(pointerIndex: Int): Float {
        return event.getX(pointerIndex)
    }

    override fun getY(pointerIndex: Int): Float {
        return event.getY(pointerIndex)
    }

    override fun getPointerId(pointerIndex: Int): Int {
        return event.getPointerId(pointerIndex)
    }
}