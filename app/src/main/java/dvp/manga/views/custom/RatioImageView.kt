package dvp.manga.views.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView

/**
 * @author Zero on 2/4/2018.
 */
class RatioImageView : ImageView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val fourThreeWidth = View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(heightMeasureSpec) * 3 / 4,
                View.MeasureSpec.EXACTLY)
        super.onMeasure(fourThreeWidth, heightMeasureSpec)
    }

}