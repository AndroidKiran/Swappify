package swapp.items.com.swappify.components.search

import android.animation.LayoutTransition
import android.annotation.TargetApi
import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.speech.RecognizerIntent
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.support.annotation.NonNull
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.TypedValue
import android.view.*
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.InputMethodManager
import android.widget.Filterable
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.search_view.view.*
import swapp.items.com.swappify.R


class SearchView : FrameLayout, View.OnClickListener {

    companion object Constants {
        const val VERSION_TOOLBAR_ICON = 1000
        const val LAYOUT_TRANSITION_DURATION = 200
        const val ANIMATION_DURATION = 300
        const val VERSION_TOOLBAR = 1000
        const val VERSION_MENU_ITEM = 1002
        const val VERSION_MARGINS_TOOLBAR_SMALL = 2000
        const val VERSION_MARGINS_TOOLBAR_BIG = 2001
        const val VERSION_MARGINS_MENU_ITEM = 2002
        const val THEME_LIGHT = 3000
        const val THEME_DARK = 3001
        const val SPEECH_REQUEST_CODE = 4000
    }


    private var onQueryChangeListener: ISearchOnQueryChangeListener? = null
    private var onClickListener: ISearchOnClickListener? = null;
//    private var savedState: SavedState? = null
    private var animationDuration = ANIMATION_DURATION
    private var isSearchArrowHamburgerState = DrawerArrowDrawable.STATE_HAMBURGER
    private var isSearchArrowSearchState = ASearchArrowDrawable.STATE_SEARCH

    private var voice: Boolean = true
    private var textColor: Int = 0
    private var textHighlightColor: Int = 0
    private var textStyle: Int = Typeface.NORMAL
    private var textFont = Typeface.DEFAULT

    private var userQuery: CharSequence? = null
    private var oldQueryText: CharSequence? = null

    private var isSearchOpen: Boolean = false
    private var drawerArrow: DrawerArrowDrawable? = null
    private var searchArrow: ASearchArrowDrawable? = null

    private var menuItemView: View? = null
    private var menuItemCx = -1
    private var shouldClearOnClose = true
    private var shouldClearOnOpen = true
    private var shouldHideOnKeyboardClose = true
    private var version: Int = 0
    private var shadow = true

    private var adapter: RecyclerView.Adapter<*>? = null
    private var activity: Activity? = null
    private var fragment: Fragment? = null
    private var supportFragment: android.support.v4.app.Fragment? = null


    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
        initStyle(attrs, defStyleAttr)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context)
        initStyle(attrs, defStyleAttr)
    }

    private var iconColor: Int = 0

    fun getIconColor(): Int = iconColor

    private fun setIconColor(@ColorInt color: Int) {
        iconColor = color

        val colorFilter = PorterDuffColorFilter(iconColor, PorterDuff.Mode.SRC_IN)
        imageViewArrowBack.colorFilter = colorFilter
        imageViewMic.colorFilter = colorFilter
        imageViewClear.colorFilter = colorFilter

        if (drawerArrow != null) {
            drawerArrow?.colorFilter = colorFilter
        }

        if(searchArrow != null) {
            searchArrow?.colorFilter = colorFilter
        }

    }


    fun getTextColor(): Int = textColor

    fun setTextColor(@ColorInt color: Int) {
        textColor = color
        searchEditText.setTextColor(textColor)
    }


    fun getTextHighlightColor(): Int = textHighlightColor

    private fun setTextHighlightColor(@ColorInt color: Int) {
        textHighlightColor = color
    }

    fun getTextFont(): Typeface = textFont

    fun setTextFont(font: Typeface) {
        textFont = font
        searchEditText.typeface = Typeface.create(textFont, textStyle)
    }


    fun getTextStyle(): Int = textStyle

    fun setTextStyle(style: Int) {
        textStyle = style
        searchEditText.setTypeface(Typeface.create(textFont, textStyle))
    }



    private fun init(@NonNull context: Context) {
        val layoutInflater = LayoutInflater.from(context)
        layoutInflater.inflate(R.layout.search_view, this, true)

        viewDivider.visibility = View.GONE

        shadowView.setOnClickListener(this)
        shadowView.visibility = View.GONE

        imageViewArrowBack.setOnClickListener(this)
        imageViewMic.setOnClickListener(this)
        imageViewClear.setOnClickListener(this)

        imageViewClear.visibility = View.GONE

        initRecyclerView()

        searchEditText.setSearchView(this@SearchView)

        searchEditText.setOnEditorActionListener({ textView, actionId, event ->
            onSubmitQuery()
            true
        })

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                this@SearchView.onTextChanged(s)
            }

            override fun afterTextChanged(s: Editable) {}
        })

        searchEditText.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                addFocus()
            } else {
                removeFocus()
            }
        }

        setVersion(VERSION_TOOLBAR)
        setVersionMargins(VERSION_MARGINS_TOOLBAR_SMALL)
        setTheme(THEME_LIGHT)
    }

    private fun initStyle(attrs: AttributeSet?, defStyleAttr: Int) {
        val attr = context.obtainStyledAttributes(attrs, R.styleable.SearchView, defStyleAttr, 0)
        if (attr != null) {
            if (attr.hasValue(R.styleable.SearchView_search_version)) {
                setVersion(attr.getInt(R.styleable.SearchView_search_version, VERSION_TOOLBAR))
            }
            if (attr.hasValue(R.styleable.SearchView_search_version_margins)) {
                setVersionMargins(attr.getInt(R.styleable.SearchView_search_version_margins, VERSION_MARGINS_TOOLBAR_SMALL))
            }
            if (attr.hasValue(R.styleable.SearchView_search_theme)) {
                setTheme(attr.getInt(R.styleable.SearchView_search_theme, THEME_LIGHT))
            }
            if (attr.hasValue(R.styleable.SearchView_search_navigation_icon)) {
                setNavigationIcon(attr.getResourceId(R.styleable.SearchView_search_navigation_icon, 0))
            }
            if (attr.hasValue(R.styleable.SearchView_search_icon_color)) {
                setIconColor(attr.getColor(R.styleable.SearchView_search_icon_color, 0))
            }
            if (attr.hasValue(R.styleable.SearchView_search_background_color)) {
                setBackgroundColor(attr.getColor(R.styleable.SearchView_search_background_color, 0))
            }
            if (attr.hasValue(R.styleable.SearchView_search_text_input)) {
                setTextInput(attr.getString(R.styleable.SearchView_search_text_input))
            }
            if (attr.hasValue(R.styleable.SearchView_search_text_color)) {
                setTextColor(attr.getColor(R.styleable.SearchView_search_text_color, 0))
            }
            if (attr.hasValue(R.styleable.SearchView_search_text_highlight_color)) {
                setTextHighlightColor(attr.getColor(R.styleable.SearchView_search_text_highlight_color, 0))
            }
            if (attr.hasValue(R.styleable.SearchView_search_text_size)) {
                setTextSize(attr.getDimension(R.styleable.SearchView_search_text_size, 0f))
            }
            if (attr.hasValue(R.styleable.SearchView_search_text_style)) {
                setTextStyle(attr.getInt(R.styleable.SearchView_search_text_style, 0))
            }
            if (attr.hasValue(R.styleable.SearchView_search_hint)) {
                setHint(attr.getString(R.styleable.SearchView_search_hint))
            }
            if (attr.hasValue(R.styleable.SearchView_search_hint_color)) {
                setHintColor(attr.getColor(R.styleable.SearchView_search_hint_color, 0))
            }
            if (attr.hasValue(R.styleable.SearchView_search_divider)) {
                setDivider(attr.getBoolean(R.styleable.SearchView_search_divider, false))
            }
            if (attr.hasValue(R.styleable.SearchView_search_voice)) {
                setVoice(attr.getBoolean(R.styleable.SearchView_search_voice, false))
            }
            if (attr.hasValue(R.styleable.SearchView_search_voice_text)) {
                setVoiceText(attr.getString(R.styleable.SearchView_search_voice_text))
            }
            if (attr.hasValue(R.styleable.SearchView_search_animation_duration)) {
                setAnimationDuration(attr.getInt(R.styleable.SearchView_search_animation_duration, animationDuration))
            }
            if (attr.hasValue(R.styleable.SearchView_search_shadow)) {
                setShadow(attr.getBoolean(R.styleable.SearchView_search_shadow, false))
            }
            if (attr.hasValue(R.styleable.SearchView_search_shadow_color)) {
                setShadowColor(attr.getColor(R.styleable.SearchView_search_shadow_color, 0))
            }
            if (attr.hasValue(R.styleable.SearchView_search_elevation)) {
                elevation = attr.getDimensionPixelSize(R.styleable.SearchView_search_elevation, 0).toFloat()
            }
            if (attr.hasValue(R.styleable.SearchView_search_clear_on_close)) {
                setShouldClearOnClose(attr.getBoolean(R.styleable.SearchView_search_clear_on_close, true))
            }
            if (attr.hasValue(R.styleable.SearchView_search_clear_on_open)) {
                setShouldClearOnOpen(attr.getBoolean(R.styleable.SearchView_search_clear_on_open, true))
            }
            if (attr.hasValue(R.styleable.SearchView_search_hide_on_keyboard_close)) {
                setShouldHideOnKeyboardClose(attr.getBoolean(R.styleable.SearchView_search_hide_on_keyboard_close, true))
            }
            attr.recycle()
        }
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.itemAnimator = null
        recyclerView.layoutTransition = getRecyclerViewLayoutTransition()
        recyclerView.visibility = View.GONE
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    recyclerView?.layoutTransition = null
                    hideKeyboard()
                } else {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        recyclerView?.layoutTransition = getRecyclerViewLayoutTransition()
                    }
                }
            }
        })
    }

    private fun setTheme(theme: Int) {
        setTheme(theme, true)
    }

    private fun setTheme(theme: Int, tint: Boolean) {
        if (theme == THEME_LIGHT) {
            setBackgroundColor(ContextCompat.getColor(context, R.color.search_light_background))
            if (tint) {
                setIconColor(ContextCompat.getColor(context, R.color.search_light_icon))
                setHintColor(ContextCompat.getColor(context, R.color.search_light_hint))
                setTextColor(ContextCompat.getColor(context, R.color.search_light_text))
                setTextHighlightColor(ContextCompat.getColor(context, R.color.search_light_text_highlight))
            }
        }

        if (theme == THEME_DARK) {
            setBackgroundColor(ContextCompat.getColor(context, R.color.search_dark_background))
            if (tint) {
                setIconColor(ContextCompat.getColor(context, R.color.search_dark_icon))
                setHintColor(ContextCompat.getColor(context, R.color.search_dark_hint))
                setTextColor(ContextCompat.getColor(context, R.color.search_dark_text))
                setTextHighlightColor(ContextCompat.getColor(context, R.color.search_dark_text_highlight))
            }
        }
    }

    private fun setNavigationIcon(@DrawableRes resource: Int) {
        imageViewArrowBack.setImageResource(resource)
    }

    private fun setNavigationIcon(drawable: Drawable?) = if (drawable == null) {
        imageViewArrowBack.visibility = View.GONE
    } else {
        imageViewArrowBack.setImageDrawable(drawable)
    }

    fun setNavigationIconArrowHamburger() {
        drawerArrow = DrawerArrowDrawable(context)
        imageViewArrowBack.setImageDrawable(drawerArrow)
    }

    fun setNavigationIconArrowSearch() {
        searchArrow = ASearchArrowDrawable(context)
        imageViewArrowBack.setImageDrawable(searchArrow)
    }

    //@IntR
    override fun setBackgroundColor(@ColorInt color: Int) = searchCard.setCardBackgroundColor(color)

    fun setTextInput(text: CharSequence?) = searchEditText.setText(text)

    private fun setTextInput(@StringRes text: Int) = searchEditText.setText(text)

    private fun setTextSize(size: Float) =
            searchEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)

    private fun setHint(hint: CharSequence?) {
        searchEditText.hint = hint
    }

    fun setHint(@StringRes hint: Int) = searchEditText.setHint(hint)

    private fun setHintColor(@ColorInt color: Int) = searchEditText.setHintTextColor(color)

    private fun setDivider(divider: Boolean) {
        if (divider) {
            recyclerView.addItemDecoration(SearchDivider(context))
        } else {
            recyclerView.removeItemDecoration(SearchDivider(context))
        }
    }

    private fun setVoice(voice: Boolean) {
        this.voice = voice
        imageViewMic.visibility = if (voice && isVoiceAvailable()) View.VISIBLE else View.GONE
    }

    fun setVoice(voice: Boolean, context: Activity) {
        activity = context
        setVoice(voice)
    }


    fun setVoice(voice: Boolean, context: Fragment) {
        fragment = context
        setVoice(voice)
    }

    fun setVoice(voice: Boolean, context: android.support.v4.app.Fragment) {
        supportFragment = context
        setVoice(voice)
    }

    private var voiceSearchText: String? = null

    fun setVoiceText(text: String?) {
        voiceSearchText = text
    }

    fun setAnimationDuration(animationDuration: Int) {
        this.animationDuration = animationDuration
    }

    fun setShadow(shadow: Boolean) {
        if (shadow) {
            shadowView.visibility = View.VISIBLE
        } else {
            shadowView.visibility = View.GONE
        }
        this.shadow = shadow
    }

    fun setShadowColor(@ColorInt color: Int) {
        shadowView.setBackgroundColor(color)
    }

    override fun setElevation(elevation: Float) {
        searchCard.maxCardElevation = elevation
        searchCard.cardElevation = elevation
        invalidate()
    }

    fun getShouldClearOnClose(): Boolean = shouldClearOnClose

    fun setShouldClearOnClose(shouldClearOnClose: Boolean) {
        this.shouldClearOnClose = shouldClearOnClose
    }

    fun getShouldClearOnOpen(): Boolean = shouldClearOnOpen

    fun setShouldClearOnOpen(shouldClearOnOpen: Boolean) {
        this.shouldClearOnOpen = shouldClearOnOpen
    }

    fun getShouldHideOnKeyboardClose(): Boolean = shouldHideOnKeyboardClose

    fun setShouldHideOnKeyboardClose(shouldHideOnKeyboardClose: Boolean) {
        this.shouldHideOnKeyboardClose = shouldHideOnKeyboardClose
    }

    /* Use setTextInput. */
    @Deprecated("")
    fun setText(text: CharSequence) {
        setTextInput(text)
    }

    /* Use setTextInput. */
    @Deprecated("")
    fun setText(@StringRes text: Int) {
        setTextInput(text)
    }


    private fun getRecyclerViewLayoutTransition(): LayoutTransition {
        val layoutTransition = LayoutTransition()
        layoutTransition.setDuration(LAYOUT_TRANSITION_DURATION.toLong())
        return layoutTransition
    }

    private fun onSubmitQuery() {
        val query = searchEditText.text
        if (query != null && TextUtils.getTrimmedLength(query) > 0) {
            onQueryChangeListener!!.onQueryTextSubmit(query.toString())
        }
    }


    private fun onTextChanged(newText: CharSequence) {
        val text = searchEditText.text
        userQuery = text
        if (adapter != null && adapter is Filterable) {
            (adapter as Filterable).filter.filter(text)
        }
        if (onQueryChangeListener != null && !TextUtils.equals(newText, oldQueryText)) {
            onQueryChangeListener?.onQueryTextChange(newText.toString())
        }
        oldQueryText = newText.toString()

        if (!TextUtils.isEmpty(newText)) {
            showClearTextIcon()
        } else {
            hideClearTextIcon()
        }
    }

    private fun checkVoiceStatus(status: Boolean) {
        imageViewMic.visibility = if (voice && status && isVoiceAvailable()) View.VISIBLE else View.GONE
    }


    private fun addFocus() {
        isSearchOpen = true
        setDrawerArrow()
        setSearchArrow()
        showSuggestions()
        if (shadow) {
            SearchAnimator.fadeIn(shadowView, animationDuration)
        }
        showKeyboard()
        showClearTextIcon()
        if (version != VERSION_MENU_ITEM) {
            postDelayed({
                if (onClickListener != null) {
                    onClickListener?.onOpen()
                }
            }, animationDuration.toLong())
        }
    }

    private fun removeFocus() {
        isSearchOpen = false
        setDrawerHamburger()
        setSearchIcon()
        if (shadow) {
            SearchAnimator.fadeOut(shadowView, animationDuration)
        }
        hideSuggestions()
        hideKeyboard()
        hideClearTextIcon()
        if (version != VERSION_MENU_ITEM) {
            postDelayed({
                if (onClickListener != null) {
                    onClickListener?.onClose()
                }
            }, animationDuration.toLong())
        }
    }

    fun isSearchOpen(): Boolean = isSearchOpen

    fun getVersion(): Int = version

    private fun setVersion(version: Int) {
        this.version = version
        if (version == VERSION_TOOLBAR) {
            this@SearchView.visibility = View.VISIBLE
            searchEditText.clearFocus()
        }

        if (version == VERSION_MENU_ITEM) {
            this@SearchView.visibility = View.GONE
        }
    }

    private fun setVersionMargins(version: Int) {

        val cardViewParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
        )

        when (version) {
            VERSION_MARGINS_TOOLBAR_SMALL -> {
                val top = context.resources.getDimensionPixelSize(R.dimen.dimen_5)
                val leftRight = context.resources.getDimensionPixelSize(R.dimen.dimen_8)
                val bottom = 0

                cardViewParams.setMargins(leftRight, top, leftRight, bottom)
            }

            VERSION_MARGINS_TOOLBAR_BIG -> {
                val top = context.resources.getDimensionPixelSize(R.dimen.dimen_5)
                val leftRight = context.resources.getDimensionPixelSize(R.dimen.dimen_8)
                val bottom = 0

                cardViewParams.setMargins(leftRight, top, leftRight, bottom)

            }

            VERSION_MARGINS_MENU_ITEM -> {
                val top = context.resources.getDimensionPixelSize(R.dimen.dimen_1)
                val leftRight = context.resources.getDimensionPixelSize(R.dimen.dimen_2)
                val bottom = context.resources.getDimensionPixelSize(R.dimen.dimen_1)

                cardViewParams.setMargins(leftRight, top, leftRight, bottom)

            }
            else -> cardViewParams.setMargins(0, 0, 0, 0)
        }

        searchCard.layoutParams = cardViewParams
    }

    private fun showKeyboard() {
        if (!isInEditMode) {
            val imm = searchEditText.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(searchEditText, 0)
            imm.showSoftInput(this, 0)
        }
    }

    fun hideKeyboard() {
        if (!isInEditMode) {
            val imm = searchEditText.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0)
        }
    }

    fun setOnQueryTextListener(onQueryChangelistener: ISearchOnQueryChangeListener) {
       this.onQueryChangeListener = onQueryChangelistener
    }

    fun setOnClickListeners(onClickListener: ISearchOnClickListener) {
        this.onClickListener = onClickListener
    }

//    override fun onSaveInstanceState(): Parcelable {
//        val superState = super.onSaveInstanceState()
//        savedState = SavedState(superState)
//        savedState?.query = if (userQuery != null) userQuery.toString() else null
//        savedState?.isSearchOpen = isSearchOpen
//        return savedState!!
//    }

//    override fun onRestoreInstanceState(state: Parcelable?) {
//        if (state !is SavedState) {
//            super.onRestoreInstanceState(state)
//            return
//        }
//        savedState = state
//        if (savedState!!.isSearchOpen) {
//            open(true)
//            setQueryWithoutSubmitting(savedState?.query)
//        }
//        super.onRestoreInstanceState(savedState?.superState)
//    }

    fun getAdapter(): RecyclerView.Adapter<*> = recyclerView.adapter


    fun setAdapter(adapter: RecyclerView.Adapter<*>) {
        this.adapter = adapter
        recyclerView.adapter = adapter

//        if (adapter is SearchAdapter)
//            (adapter as SearchAdapter).addOnItemClickListener(object : SearchAdapter.OnItemClickListener() {
//                fun onItemClick(view: View, position: Int) {
//                    dispatchFilters()
//                }
//            }, 0)
    }

    private fun open(animate: Boolean) {
        open(animate, null)
    }

    private fun open(animate: Boolean, menuItem: MenuItem?) {
        if (version == VERSION_MENU_ITEM) {
            visibility = View.VISIBLE

            if (animate) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (menuItem != null)
                        getMenuItemPosition(menuItem.itemId)
                    reveal()
                } else {
                    SearchAnimator.fadeOpen(searchCard, animationDuration, searchEditText, shouldClearOnOpen, onClickListener)
                }
            } else {
                searchCard.visibility = View.VISIBLE
                if (shouldClearOnOpen && searchEditText.length() > 0) {
                    searchEditText.text.clear()
                }
                searchCard.requestFocus()
                if (onClickListener != null) {
                    onClickListener?.onOpen()
                }
            }
        }
        if (version == VERSION_TOOLBAR) {
            if (shouldClearOnOpen && searchEditText.length() > 0) {
                searchEditText.text.clear()
            }
            searchEditText.requestFocus()
        }
    }

    fun close(animate: Boolean) {
        if (version == VERSION_MENU_ITEM) {
            if (animate) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    SearchAnimator.revealClose(searchCard, menuItemCx, animationDuration, context, searchEditText, shouldClearOnClose, this, onClickListener)
                } else {
                    SearchAnimator.fadeClose(searchCard, animationDuration, searchEditText, shouldClearOnClose, this, onClickListener)
                }
            } else {
                if (shouldClearOnClose && searchEditText.length() > 0) {
                    searchEditText.text.clear()
                }
                searchEditText.clearFocus()
                searchCard.visibility = View.GONE
                visibility = View.GONE
                if (onClickListener != null) {
                    onClickListener?.onClose()
                }
            }
        }
        if (version == VERSION_TOOLBAR) {
            if (shouldClearOnClose && searchEditText.length() > 0) {
                searchEditText.text.clear()
            }
            searchEditText.clearFocus()
        }
    }

    private fun setQueryWithoutSubmitting(query: CharSequence?) {
        searchEditText.setText(query)
        if (query != null && query.isNotEmpty()) {
            searchEditText.setSelection(searchEditText.length())
            userQuery = query
        } else {
            searchEditText.text.clear()
        }
    }

    private fun showSuggestions() {
        if (recyclerView.visibility == View.GONE) {
            recyclerView.visibility = View.VISIBLE
            viewDivider.visibility = View.VISIBLE
            SearchAnimator.fadeIn(recyclerView, animationDuration)
        }
    }

    private fun hideSuggestions() {
        if (recyclerView.visibility == View.VISIBLE) {
            viewDivider.visibility = View.GONE
            recyclerView.visibility = View.GONE
            SearchAnimator.fadeOut(recyclerView, animationDuration)
        }
    }


    private fun showClearTextIcon() {
        if (!userQuery.isNullOrEmpty()) {
            imageViewClear.visibility = View.VISIBLE
            checkVoiceStatus(false)
        }
    }

    private fun hideClearTextIcon() {
        if (userQuery.isNullOrEmpty()) {
            imageViewClear.visibility = View.GONE
            checkVoiceStatus(true)
        }
    }


    private fun getMenuItemPosition(menuItemId: Int) {
        if (menuItemView != null) {
            menuItemCx = getCenterX(menuItemView!!)
        }
        var viewParent: ViewParent? = parent
        while (viewParent != null && viewParent is View) {
            val parent = viewParent as View?
            val view = parent?.findViewById<View>(menuItemId)
            if (view != null) {
                menuItemView = view
                menuItemCx = getCenterX(menuItemView!!)
                break
            }
            viewParent = viewParent!!.parent
        }
    }

    private fun getCenterX(view: View): Int {
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        return location[0] + view.width / 2
    }


    private fun isVoiceAvailable(): Boolean {
        if (isInEditMode) {
            return true
        }
        val pm = context.packageManager
        val activities = pm.queryIntentActivities(Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0)
        return activities.size != 0
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun reveal() {
        searchCard.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                searchCard.viewTreeObserver.removeOnGlobalLayoutListener(this)
                SearchAnimator.revealOpen(searchCard, menuItemCx, animationDuration, context, searchEditText, shouldClearOnOpen, onClickListener)
            }
        })
    }

    override fun onClick(view: View?) {

        when(view) {
            imageViewArrowBack -> {
                if (drawerArrow != null && isSearchArrowHamburgerState == DrawerArrowDrawable.STATE_ARROW
                        || searchArrow != null && isSearchArrowSearchState == ASearchArrowDrawable.STATE_ARROW ) {
                    close(true)
                } else {
                    if (onClickListener != null) {
                        onClickListener?.onMenuClick()
                    } else {
                        close(true)
                    }
                }
            }

            imageViewMic -> {
                onVoiceClicked()
            }

            imageViewClear -> {
                if (searchEditText.length() > 0) {
                    searchEditText.text.clear()
                }
            }

            else -> {
                close(true)
            }
        }
    }

    private fun onVoiceClicked() {
        if (onClickListener != null) {
            onClickListener?.onVoiceClick()
        }
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, voiceSearchText)
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)

        if (activity != null) {
            activity?.startActivityForResult(intent, SPEECH_REQUEST_CODE)
        } else if (fragment != null) {
            fragment?.startActivityForResult(intent, SPEECH_REQUEST_CODE)
        } else if (supportFragment != null) {
            supportFragment?.startActivityForResult(intent, SPEECH_REQUEST_CODE)
        } else {
            if (context is Activity) {
                (context as Activity).startActivityForResult(intent, SPEECH_REQUEST_CODE)
            }
        }
    }

    private fun setDrawerArrow() {
        if (drawerArrow != null) {
            drawerArrow?.setVerticalMirror(false)
            drawerArrow?.animate(DrawerArrowDrawable.STATE_ARROW, animationDuration)
            isSearchArrowHamburgerState = DrawerArrowDrawable.STATE_ARROW
        }
    }

    private fun setSearchArrow() {
        if (searchArrow != null) {
            searchArrow?.verticalMirror = false
            searchArrow?.animate(ASearchArrowDrawable.STATE_ARROW, animationDuration)
            isSearchArrowSearchState = ASearchArrowDrawable.STATE_ARROW
        }
    }



    private fun setDrawerHamburger() {
        if (drawerArrow != null) {
            drawerArrow?.setVerticalMirror(true)
            drawerArrow?.animate(DrawerArrowDrawable.STATE_HAMBURGER, animationDuration)
            isSearchArrowHamburgerState = DrawerArrowDrawable.STATE_HAMBURGER
        }
    }

    private fun setSearchIcon() {
        if (searchArrow != null) {
            searchArrow?.verticalMirror = true
            searchArrow?.animate(ASearchArrowDrawable.STATE_SEARCH, animationDuration)
            isSearchArrowSearchState = ASearchArrowDrawable.STATE_SEARCH
        }
    }

//    private class SavedState : View.BaseSavedState {
//
//        internal var query: String? = null
//        internal var isSearchOpen: Boolean = false
//
//        internal constructor(superState: Parcelable) : super(superState)
//
//        internal constructor(parcel: Parcel) : super(parcel) {
//            this.query = parcel.readString()
//            this.isSearchOpen = parcel.readInt() == 1
//        }
//
//        override fun writeToParcel(out: Parcel, flags: Int) {
//            super.writeToParcel(out, flags)
//            out.writeString(query)
//            out.writeInt(if (isSearchOpen) 1 else 0)
//        }
//
//        companion object {
//            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
//                override fun createFromParcel(Parcel: Parcel): SavedState = SavedState(Parcel)
//
//                override fun newArray(size: Int): Array<SavedState?> = arrayOfNulls(size)
//            }
//        }
//
//    }
}
