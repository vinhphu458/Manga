package dvp.manga.extensions

import android.util.Log
import dvp.manga.BuildConfig.DEBUG

/**
 * @author Zero on 1/30/2018.
 */
//
//inline fun <reified T> T.debug(msg: String) {
//    Log.d(T::class.java.simpleName, msg)
//}

inline fun <reified T> error(msg: String) {
    Log.d(T::class.java.simpleName, msg)
}

inline fun debug(tag: String, msg: String) {
    if (DEBUG)
        Log.d(tag, msg)
}