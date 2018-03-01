package dvp.manga.views.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View

/**
 * @author Zero on 2/7/2018.
 */

abstract  class BaseViewConstructor : View {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
}