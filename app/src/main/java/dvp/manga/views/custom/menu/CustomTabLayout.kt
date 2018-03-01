package dvp.manga.views.custom.menu

import android.content.Context
import android.graphics.PorterDuff
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import dvp.manga.R
import dvp.manga.extensions.colorInt


/**
 * @author Zero on 2/2/2018.
 */

class CustomTabLayout : TabLayout, TabLayout.OnTabSelectedListener {

    private var defColor = colorInt(R.color.grey_02)
    private var priColor = colorInt(R.color.colorPrimary)

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun setMenus(list: List<BottomMenuModel>) {
        for (i in list.indices) {
            getTabAt(i)!!.customView = createLabel(list[i].label, list[i].drawable)
        }
        addOnTabSelectedListener(this)
    }

    override fun onTabSelected(tab: TabLayout.Tab) {
        setSelectedTabColor(tab)
    }

    override fun onTabUnselected(tab: TabLayout.Tab) {
        setTabColorDefault(tab)
    }

    override fun onTabReselected(tab: TabLayout.Tab) {}

    private fun setTabColorDefault(tab: Tab){
        (tab.customView as TextView).setTextColor(defColor)
        (tab.customView as TextView).compoundDrawables[1].setColorFilter(defColor, PorterDuff.Mode.SRC_IN)
    }

    private fun setSelectedTabColor(tab: Tab){
        (tab.customView as TextView).setTextColor(priColor)
        (tab.customView as TextView).compoundDrawables[1].setColorFilter(priColor, PorterDuff.Mode.SRC_IN)
    }

    private fun createLabel(label: String, icon: Int): TextView {
        val params = LinearLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        params.gravity = Gravity.CENTER
        val tab = TextView(context)
        tab.text = label
        tab.setTextColor(defColor)
        tab.layoutParams = params
        tab.gravity = Gravity.CENTER

        val drawable = ContextCompat.getDrawable(context, icon)
        drawable?.setColorFilter(defColor, PorterDuff.Mode.SRC_IN)
        tab.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
        return tab
    }

}
