package dvp.manga.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.vicpin.krealmextensions.delete
import com.vicpin.krealmextensions.queryAll
import com.vicpin.krealmextensions.save
import dvp.manga.R
import dvp.manga.extensions.debug
import dvp.manga.model.Manga
import dvp.manga.model.SearchItem
import dvp.manga.views.screens.MangaDetailActivity

/**
 * @author Zero on 2/25/2018.
 */
class SearchAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var list = mutableListOf<Manga>()
    var isHistory = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_filtered, parent, false)
        return ItemHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as ItemHolder
        holder.bindData(list[position])
    }

    inner class ItemHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        override fun onClick(v: View?) {
            when (v) {
                btRemove -> {
                    removeHistory(adapterPosition)
                }
                this.itemView -> {
                    if (isHistory)
                        listener(list[adapterPosition])
                    else
                        gotoDetail(list[adapterPosition])
                }
            }
        }

        private val tvName: TextView = view.findViewById(R.id.tv_name)
        private val btRemove = view.findViewById<ImageView>(R.id.bt_remove)
        private val status = view.findViewById<ImageView>(R.id.image)

        init {
            btRemove.setOnClickListener(this)
            itemView.setOnClickListener(this)
        }

        fun bindData(item: Manga) {
            tvName.text = item.title
            status.setImageResource(if (isHistory) R.drawable.ic_history else R.drawable.ic_search)
            btRemove.visibility = if (isHistory) View.VISIBLE else View.GONE
        }
    }

    private fun gotoDetail(manga: Manga) {
        val intent = Intent(context, MangaDetailActivity::class.java)
        intent.putExtra("manga", manga)
        context.startActivity(intent)
    }

    fun saveToHistory(item: SearchItem) {
        item.save()
        debug("TEST", "SAVE")
    }

    fun clear() {
        if (list.isNotEmpty()) {
            list.clear()
            notifyDataSetChanged()
        }
    }

    fun removeHistory(position: Int) {
        if (position == -1) return
        SearchItem().delete { equalTo("name", list[position].title) }
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    fun showHistory() {
        list.clear()
        SearchItem().queryAll().forEach { item -> list.add(Manga(title = item.name)) }
        notifyDataSetChanged()
        isHistory = true
    }

    fun setFilteredList(list: List<Manga>) {
        isHistory = false
        this.list.clear()
        this.list.addAll(list)
        if (list.isNotEmpty())
            notifyDataSetChanged()
    }

    private lateinit var listener: (Manga) -> Unit

    fun setOnHistoryClicked(listener: (Manga) -> Unit){
        this.listener = listener
    }
}