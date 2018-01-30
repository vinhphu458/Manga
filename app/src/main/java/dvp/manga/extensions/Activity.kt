package dvp.manga.extensions

import android.app.Activity
import android.support.annotation.StringRes
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