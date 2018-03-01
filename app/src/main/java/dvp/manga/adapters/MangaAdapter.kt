package dvp.manga.adapters

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import dvp.manga.GetDataCallback
import dvp.manga.R
import dvp.manga.model.Manga
import dvp.manga.model.VnSharing
import dvp.manga.views.screens.MangaDetailActivity

/**
 * @author Zero on 1/30/2018.
 */
class MangaAdapter(val context: Context?, recyclerView: RecyclerView, private val genreUrl: String?) : EndlessScrollAdapter<Manga>(recyclerView) {

    private var imgLoader = Glide.with(context!!)

    override fun setSpan(layoutManager: GridLayoutManager) {
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (getItemViewType(position)) {
                    LOADING -> 3
                    else -> 1
                }
            }
        }
    }

    override fun implCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent?.context).inflate(R.layout.item_manga, parent, false)
        return MangaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        super.onBindViewHolder(holder, position)
        if (holder is MangaViewHolder) {
            holder.bindData(arrayList[position])
        }
    }

    override fun setDataSource() {
        if (genreUrl.isNullOrEmpty()) {
            VnSharing().getMangas(pageIndex, object : GetDataCallback() {
                override fun onGetMangaFinished(mangas: List<Manga>) {
                    updateList(mangas)
                }
            })
        } else {
            VnSharing().getMangasByGenre(genreUrl!!, pageIndex, object : GetDataCallback() {
                override fun onGetMangaFinished(mangas: List<Manga>) {
                    updateList(mangas)
                }
            })
        }
    }

    private fun gotoDetail(manga: Manga, vararg elements: View) {
        val intent = Intent(context, MangaDetailActivity::class.java)
        intent.putExtra("manga", manga)
        val options = ActivityOptions.makeSceneTransitionAnimation((context as Activity), Pair.create(elements[0], "cover"), Pair.create(elements[0], "background"))
        context.startActivityForResult(intent, 999, options.toBundle())
    }

    inner class MangaViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        override fun onClick(v: View?) {
            gotoDetail(arrayList[adapterPosition], imgCover)
        }

        private var imgCover: ImageView = view.findViewById(R.id.imgCover)
        private var tvName: TextView = view.findViewById(R.id.tvName)
        private var tvLastChap: TextView = view.findViewById(R.id.tvLastChap)

        init {
            view.setOnClickListener(this)
        }

        fun bindData(item: Manga) {
            imgLoader.load(item.cover_url).into(imgCover)
            tvName.text = item.title
            tvLastChap.text = item.last_chap
        }
    }
}