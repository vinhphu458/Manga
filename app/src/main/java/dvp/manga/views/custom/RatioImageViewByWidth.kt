package dvp.manga.views.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView

/**
 * @author Zero on 2/4/2018.
 */
class RatioImageViewByWidth : ImageView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val fourThreeHeight = View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(widthMeasureSpec) * 4 / 3,
                View.MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, fourThreeHeight)
    }

}