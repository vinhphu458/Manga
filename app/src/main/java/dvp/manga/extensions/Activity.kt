package dvp.manga.extensions

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.res.Resources
import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.util.DisplayMetrics
import android.util.Pair
import android.util.TypedValue
import android.view.View
import android.widget.Toast


/**
 * @author dvphu on 1/24/2018.
 */

fun Activity.toast(msg: Any?) {
    if (msg is String)
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    else if (msg is @StringRes Int)
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun <T : View> T.colorInt(@ColorRes id: Int): Int {
    return ContextCompat.getColor(context, id)
}

fun Activity.sharedElements(vararg views: View): ActivityOptions {
    val listPair = arrayOfNulls<Pair<View, String>>(views.size)
    for (i in views.indices) {
        listPair.set(i, Pair(views[i], ViewCompat.getTransitionName(views[i])))
    }
    return ActivityOptions.makeSceneTransitionAnimation(this, *listPair)
}

fun Fragment.sharedElements(vararg views: View): ActivityOptions {
    val listPair = arrayOfNulls<Pair<View, String>>(views.size)
    for (i in views.indices) {
        listPair.set(i, Pair(views[i], ViewCompat.getTransitionName(views[i])))
    }
    return ActivityOptions.makeSceneTransitionAnimation(this.activity, *listPair)
}

fun px2dp(px: Float): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, DisplayMetrics()).toInt()
}

fun dp2px(dp: Int): Float {
    return (dp * Resources.getSystem().displayMetrics.density)
}

fun Context.getColorFromRes(@ColorRes id: Int): Int {
    return ContextCompat.getColor(this, id)
}