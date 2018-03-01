package dvp.manga.common.glide

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions

/**
 * @author Zero on 2/23/2018.
 */
@GlideModule
class GlideAppModule : AppGlideModule() {

    @SuppressLint("CheckResult")
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        val defaultOptions = RequestOptions()
        val activityManager = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        defaultOptions.format(if (activityManager.isLowRamDevice) DecodeFormat.PREFER_RGB_565 else DecodeFormat.PREFER_ARGB_8888)
        defaultOptions.disallowHardwareConfig()
        builder.setDefaultRequestOptions(defaultOptions)
    }

    override fun isManifestParsingEnabled(): Boolean {
        return false
    }
}
