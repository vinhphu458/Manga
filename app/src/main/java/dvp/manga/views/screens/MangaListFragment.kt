package dvp.manga.views.screens

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import dvp.manga.R
import dvp.manga.adapters.MangaAdapter


class MangaListFragment : BaseFragment() {

    private lateinit var recycler: RecyclerView

    override fun initViews(root: View) {
        recycler = root.findViewById(R.id.recycler)
    }

    private lateinit var mainAdapter: MangaAdapter

    private fun setMainList() {
        val layoutManager = GridLayoutManager(context, 3)
        recycler.layoutManager = layoutManager
        mainAdapter = MangaAdapter(context, recycler, null)
        recycler.adapter = mainAdapter
        mainAdapter.startLoadMore()
    }

    private fun setSearchList(url: String) {
        val layoutManager = GridLayoutManager(context, 3)
        recycler.layoutManager = layoutManager
        val adapter = MangaAdapter(context, recycler, url)
        recycler.adapter = adapter
        adapter.startLoadMore()
    }

    override fun getLayout(): Int {
        return R.layout.list_view
    }

    override fun loadData() {
    }

    fun updateAdapter(url: String?) {
        if (url.isNullOrEmpty()) {
            setMainList()
        } else {
            setSearchList(url!!)
        }
    }
}
