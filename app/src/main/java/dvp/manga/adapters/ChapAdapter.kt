package dvp.manga.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import dvp.manga.R
import dvp.manga.common.Utils
import dvp.manga.model.Chapter
import dvp.manga.views.screens.ChapterDetailActivity
import dvp.manga.views.screens.MangaDetailActivity

/**
 * @author Zero on 2/5/2018.
 */

class ChapAdapter(val context: Context?, recyclerView: RecyclerView) : EndlessScrollAdapter<Chapter>(recyclerView) {
    override fun implCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent?.context).inflate(R.layout.item_chapter, parent, false)
        return ChapHolder(itemView)
    }

    override fun setSpan(layoutManager: GridLayoutManager?) {

    }

    override fun setDataSource() {
        (context as MangaDetailActivity).getChaps()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        super.onBindViewHolder(holder, position)
        if (holder is ChapHolder) {
            holder.bindData(arrayList[position])
        }
    }

    inner class ChapHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        override fun onClick(v: View?) {
            val intent = Intent(context, ChapterDetailActivity::class.java)
            intent.putExtra("manga_id", arrayList[adapterPosition].manga_id)
            intent.putExtra("position", adapterPosition)
//            val pair: Pair<View, String> = Pair.create(itemView, "chapter")
//            val options = ActivityOptions.makeSceneTransitionAnimation((context as Activity), pair)
//            context.startActivity(intent, options.toBundle())
            context?.startActivity(intent)
        }

        private val tvChapter: TextView = view.findViewById(R.id.tvChapter)
        private val tvPostDate: TextView = view.findViewById(R.id.tvPostDate)

        init {
            view.setOnClickListener(this)
        }

        fun bindData(item: Chapter) {
            tvChapter.text = item.title
            tvPostDate.text = Utils.getDate(item.post_date!!)
        }
    }

}