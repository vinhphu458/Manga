package dvp.manga.common

import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import dvp.manga.App
import dvp.manga.common.Utils.DateFormat.fromFormat
import dvp.manga.common.Utils.DateFormat.toFormat
import java.text.SimpleDateFormat
import java.util.*


/**
 * @author dvphu on 1/24/2018.
 */
object Utils {

    fun blur(context: Context, image: Bitmap?): Bitmap? {
        if (null == image) return null

        val outputBitmap = Bitmap.createBitmap(image)
        val renderScript = RenderScript.create(context)
        val tmpIn = Allocation.createFromBitmap(renderScript, image)
        val tmpOut = Allocation.createFromBitmap(renderScript, outputBitmap)
        val theIntrinsic = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript))
        theIntrinsic.setRadius(1f)
        theIntrinsic.setInput(tmpIn)
        theIntrinsic.forEach(tmpOut)
        tmpOut.copyTo(outputBitmap)
        return outputBitmap
    }

    fun isOnline(): Boolean {
        val cm = App.context().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null && cm.activeNetworkInfo.isConnectedOrConnecting
    }

    object DateFormat {
        var fromFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault())
        var toFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    }

    fun getDate(date_time: String): String {
        return toFormat.format(fromFormat.parse(date_time))
    }


}