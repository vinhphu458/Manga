package dvp.manga.views.screens

import android.graphics.Color
import android.support.v7.widget.GridLayoutManager
import android.text.Html
import android.view.View
import com.bumptech.glide.Glide
import dvp.manga.BaseActivity
import dvp.manga.GetDataCallback
import dvp.manga.R
import dvp.manga.adapters.ChapAdapter
import dvp.manga.common.Linker
import dvp.manga.extensions.toast
import dvp.manga.model.Chapter
import dvp.manga.model.Manga
import dvp.manga.model.MangaInfo
import dvp.manga.model.VnSharing
import kotlinx.android.synthetic.main.activity_manga_detail.*


class MangaDetailActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v) {
            btContent -> toast("under construction")
            btBookmark -> toast("under construction")
            btRead -> toast("under construction")
            btBack -> {
                setResult(HomeActivity.ViewType.LAST_UPDATE.ordinal)
                finishAfterTransition()
            }
            btDownload -> toast("under construction")
        }
    }

    private lateinit var manga: Manga
    private lateinit var adapter: ChapAdapter

    override fun getLayout(): Int {
        manga = intent.extras.getParcelable("manga") as Manga
        return R.layout.activity_manga_detail
    }

    @Suppress("DEPRECATION")
    private fun setCategoriesLink(categories: String) {
        tvCategory.append(Html.fromHtml(categories))
        tvCategory.setLinkTextColor(Color.YELLOW)
        tvCategory.movementMethod = object : Linker() {
            override fun onLinkClick(url: String) {
                gotoGenre(url)
            }
        }
    }

    private fun gotoGenre(url: String) {
        setResult(HomeActivity.ViewType.GENRE.ordinal, intent.putExtra("genreUrl", url))
        finish()
    }

    override fun setUpViews() {
        adapter = ChapAdapter(this, rvChap)
        rvChap.setHasFixedSize(true)
        rvChap.layoutManager = GridLayoutManager(this, 1)
        rvChap.adapter = adapter
        adapter.startLoadMore()


        btContent.setOnClickListener(this)
        btRead.setOnClickListener(this)
        btBookmark.setOnClickListener(this)
        btBack.setOnClickListener(this)
        btDownload.setOnClickListener(this)
    }

    override fun loadData() {
        tvName.text = manga.title

        Glide.with(this)
                .load(manga.cover_url)
                .into(imgCover)
    }

    fun getChaps() {
        VnSharing().getChaps(manga, object : GetDataCallback() {

            override fun onGetChapterFinished(chapters: List<Chapter>) {
                adapter.updateList(chapters)
            }

            override fun onGetMangaInfoFinished(info: MangaInfo) {
                tvAuthor.text = info.author
                setCategoriesLink(info.categories!!)
                tvStatus.append(info.status)
                tvViews.append(info.views)
            }
        })
    }

}
