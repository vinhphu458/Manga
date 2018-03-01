package dvp.manga.common.glide

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL

/**
 * @author Zero on 2/24/2018.
 */
open class ImageLoader(private val imgTarget: ImageView, private val progress: View, private val btRetry: View) {

    open fun from(url: String?) {
        if (url == null) return
        onConnecting()
        GlideApp.with(imgTarget.context)
                .load(url)
                .override(SIZE_ORIGINAL, SIZE_ORIGINAL)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        onFailed(url)
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        onFinished()
                        return false
                    }
                })
                .into(imgTarget)
    }


    private fun onConnecting() {
        progress.visibility = View.VISIBLE
        btRetry.visibility = View.GONE
        imgTarget.visibility = View.GONE
    }

    private fun onFinished() {
        progress.visibility = View.GONE
        btRetry.visibility = View.GONE
        imgTarget.visibility = View.VISIBLE
    }

    private fun onFailed(url: String) {
        progress.visibility = View.GONE
        btRetry.visibility = View.VISIBLE
        imgTarget.visibility = View.GONE
        btRetry.setOnClickListener {
            from(url)
        }
    }

}