package dvp.manga.views.screens

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import dvp.manga.BaseActivity
import dvp.manga.R
import dvp.manga.adapters.MangaAdapter
import kotlinx.android.synthetic.main.activity_genre.*

class GenreActivity : BaseActivity() {
    private lateinit var mangaAdapter: MangaAdapter

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out)
    }

    override fun getLayout(): Int {
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out)
        return R.layout.activity_genre
    }

    override fun setUpViews() {
        val url = intent.getStringExtra("genreUrl")
        val layoutManager = GridLayoutManager(this, 3)
        (listView as RecyclerView).layoutManager = layoutManager
        mangaAdapter = MangaAdapter(this, (listView as RecyclerView), url)
        (listView as RecyclerView).adapter = mangaAdapter
    }

    override fun loadData() {
        mangaAdapter.startLoadMore()

    }

}
