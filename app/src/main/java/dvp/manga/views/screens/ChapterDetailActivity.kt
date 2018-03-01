package dvp.manga.views.screens

import android.content.pm.ActivityInfo
import android.os.Handler
import android.provider.Settings
import android.support.v4.view.ViewPager
import android.view.View
import com.vicpin.krealmextensions.equalToValue
import com.vicpin.krealmextensions.query
import dvp.manga.BaseActivity
import dvp.manga.GetDataCallback
import dvp.manga.R
import dvp.manga.adapters.MangaPagerAdapter
import dvp.manga.extensions.dp2px
import dvp.manga.extensions.toast
import dvp.manga.model.Chapter
import dvp.manga.model.Page
import dvp.manga.model.VnSharing
import dvp.manga.views.custom.SwipeLayout
import kotlinx.android.synthetic.main.activity_chapter_detail.*

class ChapterDetailActivity : BaseActivity(), View.OnClickListener, SwipeLayout.OnSwipeListener, ViewPager.OnPageChangeListener {

    private lateinit var chapters: List<Chapter>
    private var imageUrls = mutableListOf<String>()
    private lateinit var adapter: MangaPagerAdapter
    private var startPosition: Int = 0
    private var mangaId: Int = 0
    private var show: Boolean = false
    private var portrait: Boolean = true

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out)
    }

    override fun getLayout(): Int {
        startPosition = intent.getIntExtra("position", 0)
        mangaId = intent.getIntExtra("manga_id", 0)
        chapters = Chapter().query { equalToValue("manga_id", mangaId) }
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out)
        return R.layout.activity_chapter_detail
    }


    override fun setUpViews() {
        adapter = MangaPagerAdapter(this@ChapterDetailActivity, imageUrls)
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 3
        adapter.setOnItemClickListener(object : MangaPagerAdapter.OnItemClickListener {
            override fun onClicked() {
                toggleToolbars()
            }
        })

        viewPager.addOnPageChangeListener(this)
        btNextChap.setOnClickListener(this)
        btPreChap.setOnClickListener(this)
        btBrightness.setOnClickListener(this)
        btRotate.setOnClickListener(this)

        Handler().postDelayed({
            hideToolbar()
        }, 1000)

        page_container.setOnSwipeListener(this)
    }

    override fun loadData() {
        loadPages(startPosition, true)
    }

    override fun onPageSelected(position: Int) {
        progressBar.progress = position + 1
    }


    override fun onNext() {
        gotoNextChap()
    }

    override fun onPrevious() {
        gotoPreChap()
    }

    private fun toggleToolbars() {
        show = if (show) {
            hideToolbar()
            false
        } else {
            showToolbars()
            true
        }
    }

    private fun showToolbars() {
        toolbar_bottom.animate().translationY(dp2px(0))
        toolbar_top.animate().translationY(dp2px(0))
    }

    private fun hideToolbar() {
        toolbar_bottom.animate().translationY(dp2px(50))
        toolbar_top.animate().translationY(dp2px(-50))
    }

    private fun gotoNextChap() {
        if (startPosition > 0) {
            startPosition--
            loadPages(startPosition, true)
            showToolbars()
        } else {
            toast("No more next chapters")
        }
    }

    private fun gotoPreChap() {
        if (startPosition < chapters.size - 1) {
            startPosition++
            loadPages(startPosition, false)
            showToolbars()
        } else {
            toast("No more previous chapters")
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            btNextChap -> {
                gotoNextChap()
            }
            btPreChap -> {
                gotoPreChap()
            }
            btBrightness -> {
                changeBrightness()
            }
            btRotate -> {
                toggleRotate()
            }
        }
    }

    private fun toggleRotate() {
        portrait = if (portrait) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            false
        } else {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            true
        }
    }

    private fun changeBrightness() {
        val current = Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, 20)
        when (current) {
            25 -> {
                Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, 50)
            }
            50 -> {
                Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, 75)
            }
            75 -> {
                Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, 100)
            }
            100 -> {
                Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, 10)
            }
            else -> {
                Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, 25)
            }
        }
    }

    private fun loadPages(chapIndex: Int, isNext: Boolean) {
        showProgress()
        tvChapter.text = chapters[chapIndex].title
        VnSharing().getPages(chapters[chapIndex].href!!, object : GetDataCallback() {
            override fun onGetMangaPageFinished(pages: List<Page>) {
                imageUrls.clear()
                for (item in pages)
                    imageUrls.add(item.src!!)
                adapter.notifyDataSetChanged()
                progressBar.max = imageUrls.size
                viewPager.setCurrentItem(if (isNext) 0 else imageUrls.size - 1, false)
                hideProgress()
            }
        })
    }

    private fun hideProgress() {
        progressHolder.visibility = View.GONE
        viewPager.visibility = View.VISIBLE
    }

    private fun showProgress() {
        progressHolder.visibility = View.VISIBLE
        viewPager.visibility = View.GONE
    }

    //region unused methods


    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onProgress(percentage: Float) {

    }
    //endregion
}
