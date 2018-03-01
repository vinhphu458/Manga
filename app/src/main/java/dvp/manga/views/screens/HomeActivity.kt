package dvp.manga.views.screens

import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import dvp.manga.BaseActivity
import dvp.manga.R
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : BaseActivity(), ViewPager.OnPageChangeListener {

    private var viewType: ViewType = ViewType.LAST_UPDATE

    enum class ViewType {
        LAST_UPDATE, GENRE
    }

    private val adapter: ViewPagerAdapter = ViewPagerAdapter()

    override fun getLayout(): Int {
        return R.layout.activity_home
    }

    private fun setToolbarView(type: ViewType, url: String? = null) {
        this.viewType = type
        if (url.isNullOrEmpty())
            toolbar.title = getString(R.string.app_name)
        else
            toolbar.title = Regex("(?<=::)[^]]+").find(url!!)?.value

        invalidateOptionsMenu()
        viewPager.post {
            adapter.updateFragment(viewPager.currentItem, url)
        }

        if(searchView.isVisible)
            searchView.hideSearch()
    }

    override fun setUpViews() {
        setSupportActionBar(toolbar)
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(this)
        viewPager.setCurrentItem(-1, false)
        viewPager.post {
            viewPager.setCurrentItem(0, false)
            onPageSelected(0)
        }
        setToolbarView(ViewType.LAST_UPDATE)
    }

    inner class ViewPagerAdapter : FragmentPagerAdapter(supportFragmentManager) {
        private val mFragmentList = mutableListOf<Fragment>()

        init {
            mFragmentList.add(MangaListFragment())
        }

        override fun getItem(position: Int): Fragment {
            return mFragmentList.get(position)
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun updateFragment(position: Int, url: String?) {
            if (position == 0) {
                (getItem(0) as MangaListFragment).updateAdapter(url)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 999 && resultCode == HomeActivity.ViewType.GENRE.ordinal) {
            val url = data?.getStringExtra("genreUrl")
            setToolbarView(HomeActivity.ViewType.GENRE, url)
        }
    }

    //region search view
    override fun onBackPressed() {
        if(viewType == ViewType.GENRE){
            setToolbarView(ViewType.LAST_UPDATE)
        }
        if (searchView.isVisible) {
            searchView.hideSearch()
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(if (viewType == ViewType.LAST_UPDATE) R.menu.main else R.menu.close, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_search -> searchView.showSearch()
            R.id.action_close_genre -> setToolbarView(ViewType.LAST_UPDATE)
        }
        return true
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        searchView.applyTouchEvent(ev!!)
        return super.dispatchTouchEvent(ev)
    }

    //endregion

    //region not implemented


    override fun loadData() {
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
    }
    //endregion
}
