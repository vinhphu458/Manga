package dvp.manga.adapters

import android.content.Context
import android.os.Parcelable
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dvp.manga.R
import dvp.manga.common.glide.ImageLoader
import dvp.manga.views.custom.PinchImageView

/**
 * @author Zero on 2/12/2018.
 */
class MangaPagerAdapter(val context: Context, private val imageUrls: List<String>) : PagerAdapter(), View.OnClickListener {
    override fun onClick(v: View?) {
        listener!!.onClicked()
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }

    override fun getCount(): Int {
        return imageUrls.size
    }

    private lateinit var inflater: LayoutInflater

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val imageLayout = inflater.inflate(R.layout.image_layout, view, false)!!
        val imageView = imageLayout.findViewById(R.id.image) as PinchImageView
        val progress = imageLayout.findViewById(R.id.imgProgress) as View
        val btRetry = imageLayout.findViewById(R.id.btRetry) as View
        ImageLoader(imageView, progress, btRetry).from(imageUrls[position])
        view.addView(imageLayout)
        imageLayout.setOnClickListener(this)
        imageView.setOnClickListener(this)
        return imageLayout
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {}

    override fun saveState(): Parcelable? {
        return null
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    private var listener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onClicked()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}