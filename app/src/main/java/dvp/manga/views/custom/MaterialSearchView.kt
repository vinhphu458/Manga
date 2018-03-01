package dvp.manga.views.custom

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewAnimationUtils
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.vicpin.krealmextensions.queryAsync
import dvp.manga.GetDataCallback
import dvp.manga.R
import dvp.manga.adapters.SearchAdapter
import dvp.manga.common.DelayTextChanged
import dvp.manga.extensions.getColorFromRes
import dvp.manga.model.Manga
import dvp.manga.model.SearchItem
import dvp.manga.model.VnSharing
import io.realm.Case
import io.realm.RealmQuery

/**
 * @author Zero on 2/26/2018.
 */

class MaterialSearchView : CardView {

    private var searchMenuPosition: Int = 0
    private var searchHint: String? = null
    private var searchTextColor: Int = 0
    private var searchIconColor: Int? = null
    private var mOldQueryText: CharSequence? = null
    private var mUserQuery: CharSequence? = null
    private var hideSearch = false

    private var listenerQuery: OnQueryTextListener? = null

    private var btBack: ImageView? = null
    private var btClear: ImageView? = null
    private var editText: EditText? = null
    private var recycler: RecyclerView? = null
    private var adapter: SearchAdapter? = null
    private var progressBar: ProgressBar? = null
    private var tvNoResult: TextView? = null

    private val mOnClickListener = OnClickListener { v ->
        if (v === btClear) {
            editText!!.text = null
        } else if (v === btBack) {
            hideSearch()
        }
    }

    /**
     * Callback to watch the text field for empty/non-empty
     */
    private val mTextWatcher = object : DelayTextChanged() {
        override fun textWasChanged(s: String) {
            if (s.trim().length < 2) return
            getDataOffline(s)
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s.isNullOrEmpty()) {
                showHistoryRecycler()
                btClear!!.visibility = View.GONE
            } else {
                btClear!!.visibility = View.VISIBLE
                submitText(s.toString())
            }
        }
    }

    /**
     * Returns the IME options set on the query text field.
     */
    /**
     * Sets the IME options on the query text field.
     */
    private var imeOptions: Int
        get() = editText!!.imeOptions
        set(imeOptions) {
            editText!!.imeOptions = imeOptions
        }

    /**
     * Returns the input type set on the query text field.
     */
    /**
     * Sets the input type on the query text field.
     */
    private var inputType: Int
        get() = editText!!.inputType
        set(inputType) {
            editText!!.inputType = inputType
        }

    // text color
    var textColor: Int
        get() = searchTextColor
        set(textColor) {
            this.searchTextColor = textColor
            invalidate()
            requestFocus()
        }

    /*
    TODO not correct but close
    Need to do correct measure
     */
    private val centerX: Int
        get() {
            val icons = (width - convertDpToPixel((21 * (1 + searchMenuPosition)).toFloat())).toInt()
            val padding = convertDpToPixel((searchMenuPosition * 21).toFloat()).toInt()
            return icons - padding
        }

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.MaterialSearchView, 0, 0)

        val inflater = LayoutInflater.from(context)
        val layout = inflater.inflate(R.layout.search_layout, this)

        btBack = layout.findViewById(R.id.imgBack)
        btClear = layout.findViewById(R.id.imgClear)
        editText = layout.findViewById(R.id.editText)
        recycler = layout.findViewById(R.id.recycler)
        progressBar = layout.findViewById(R.id.progressBar)
        tvNoResult = layout.findViewById(R.id.tvNoResult)

        searchMenuPosition = a.getInteger(R.styleable.MaterialSearchView_search_menu_position, 0)
        searchHint = a.getString(R.styleable.MaterialSearchView_search_hint)
        searchTextColor = a.getColor(R.styleable.MaterialSearchView_search_text_color, context.getColorFromRes(android.R.color.black))
        searchIconColor = a.getColor(R.styleable.MaterialSearchView_search_icon_color, context.getColorFromRes(android.R.color.black))

        val imeOptions = a.getInt(R.styleable.MaterialSearchView_search_ime_options, -1)
        if (imeOptions != -1) {
            this.imeOptions = imeOptions
        }

        val inputType = a.getInt(R.styleable.MaterialSearchView_search_input_type, -1)
        if (inputType != -1) {
            this.inputType = inputType
        }

        val focusable: Boolean
        focusable = a.getBoolean(R.styleable.MaterialSearchView_search_focusable, true)
        editText!!.isFocusable = focusable

        a.recycle()

        setUpViews()
    }

    private fun setUpViews() {
        btBack!!.setOnClickListener(mOnClickListener)
        btClear!!.setOnClickListener(mOnClickListener)
        editText!!.addTextChangedListener(mTextWatcher)
        editText!!.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                onSubmitQuery()
            }
            true
        }
        editText!!.hint = getSearchHint()
        editText!!.setTextColor(textColor)
        setDrawableTint(btBack!!.drawable, searchIconColor!!)
        setDrawableTint(btClear!!.drawable, searchIconColor!!)

        adapter = SearchAdapter(context)
        adapter!!.setOnHistoryClicked {
            editText?.removeTextChangedListener(mTextWatcher)
            editText?.setText(it.title)
            btClear!!.visibility = View.VISIBLE
            getDataOnline(it.title!!)
        }
        recycler!!.adapter = adapter
    }


    private fun submitText(s: CharSequence) {
        mUserQuery = editText!!.text
        if (listenerQuery != null && !TextUtils.equals(s, mOldQueryText)) {
            listenerQuery!!.onQueryTextChange(s.toString())
        }
        mOldQueryText = s.toString()
        adapter?.clear()
        tvNoResult!!.visibility = View.GONE
        progressBar!!.visibility = View.GONE
    }

    private fun onSubmitQuery() {
        val query = editText!!.text.toString()
        if (listenerQuery != null) {
            listenerQuery!!.onQueryTextSubmit(query)
        }
        getDataOnline(query)
        adapter!!.saveToHistory(SearchItem(query))
    }

    private fun setDataCallback(list: List<Manga>?) {
        if (list == null || list.isEmpty()) {
            setNoDataCallback()
        } else {
            tvNoResult!!.visibility = View.GONE
            progressBar!!.visibility = View.GONE
            adapter!!.setFilteredList(list)
        }
    }

    private fun getDataOffline(q: String) {
        Manga().queryAsync(object : (RealmQuery<Manga>) -> Unit() {
            override fun invoke(p1: RealmQuery<Manga>) {
                p1.contains("title", q, Case.INSENSITIVE)
            }
        }, {
            adapter?.setFilteredList(it)
        })
    }

    private fun getDataOnline(q: String) {
        progressBar!!.visibility = View.VISIBLE
        adapter?.clear()
        editText?.addTextChangedListener(mTextWatcher)
        VnSharing().getSearchedMangas(q, 1, object : GetDataCallback() {
            override fun onSearchMangaFinish(mangas: List<Manga>?) {
                setDataCallback(mangas)
            }
        })
    }

    private fun setNoDataCallback() {
        progressBar!!.visibility = View.GONE
        tvNoResult!!.visibility = View.VISIBLE
    }

    private fun showHistoryRecycler() {
        adapter?.showHistory()
    }

    private fun showKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, 0)
    }

    private fun hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText!!.windowToken, 0)
    }

    fun showSearch() {
        editText!!.requestFocus()
        hideSearch = false
        this.visibility = View.VISIBLE
        showKeyboard()
        val animatorShow = ViewAnimationUtils.createCircularReveal(
                this, // view
                centerX, // center x
                convertDpToPixel(23f).toInt(), // center y
                0f, // start radius
                Math.hypot(width.toDouble(), height.toDouble()).toFloat() // end radius
        )
        animatorShow.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                showHistoryRecycler()
            }
        })
        animatorShow.start()
    }

    fun hideSearch() {
        hideSearch = true
        hideKeyboard()
        val animatorHide = ViewAnimationUtils.createCircularReveal(
                this, // View
                centerX, // center x
                convertDpToPixel(23f).toInt(), // center y
                Math.hypot(width.toDouble(), height.toDouble()).toFloat(), // start radius
                0f// end radius
        )
        animatorHide.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                editText!!.clearFocus()
                editText!!.text.clear()
                visibility = View.GONE
            }
        })
        animatorHide.start()
    }

    fun setMenuPosition(menuPosition: Int) {
        this.searchMenuPosition = menuPosition
        invalidate()
        requestFocus()
    }

    // search searchHint
    private fun getSearchHint(): String {
        return if (TextUtils.isEmpty(searchHint)) {
            "Search"
        } else searchHint!!
    }

    /**
     * Interface
     */
    fun addQueryTextListener(listenerQuery: OnQueryTextListener) {
        this.listenerQuery = listenerQuery
    }

    interface OnQueryTextListener {
        fun onQueryTextSubmit(query: String)

        fun onQueryTextChange(newText: String)
    }


    /**
     * Helpers
     */
    private fun setDrawableTint(resDrawable: Drawable, resColor: Int) {
        resDrawable.colorFilter = PorterDuffColorFilter(resColor, PorterDuff.Mode.SRC_ATOP)
        resDrawable.mutate()

    }

    private fun convertDpToPixel(dp: Float): Float {
        return dp * (context.resources.displayMetrics.densityDpi / 160f)
    }

    val isVisible: Boolean get() = visibility == View.VISIBLE

    fun applyTouchEvent(event: MotionEvent) {
        if (event.action == MotionEvent.ACTION_DOWN && isVisible) {
            val outRect = Rect()
            editText?.getGlobalVisibleRect(outRect)
            if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                hideKeyboard()
            }
        }
    }
}
