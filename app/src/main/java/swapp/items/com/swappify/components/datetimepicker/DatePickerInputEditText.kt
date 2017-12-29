package swapp.items.com.swappify.components.datetimepicker

import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.support.design.widget.TextInputEditText
import android.support.v4.app.FragmentManager
import android.text.InputType
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import swapp.items.com.swappify.R
import java.text.DateFormat
import java.util.*


class DatePickerInputEditText : TextInputEditText, OnFocusChangeListener, OnClickListener, OnDateSetListener {

    private var onFocusChangedListener: OnFocusChangeListener? = null

    private var manager: FragmentManager? = null

    private var focusCount: Int = 0
    private var themeId: Int? = null

    private var dateFormat: String? = null
    private var minDate: String? = null
    private var maxDate: String? = null

    private var textDateFormat: DateFormat? = null

    private var date: Calendar? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        handleAttributes(attrs)
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        handleAttributes(attrs)
        init()
    }

    private fun init() {
        inputType = InputType.TYPE_NULL
        onFocusChangeListener = this
        setOnClickListener(this)
    }

    private fun handleAttributes(attributeSet: AttributeSet) {
        try {
            val array = context.obtainStyledAttributes(attributeSet, R.styleable.DateTimePickerEditText)

            themeId = array.getResourceId(R.styleable.DateTimePickerEditText_theme, 0)

            dateFormat = array.getString(R.styleable.DateTimePickerEditText_dateFormat)
            minDate = array.getString(R.styleable.DateTimePickerEditText_minDate)
            maxDate = array.getString(R.styleable.DateTimePickerEditText_maxDate)

            array.recycle()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    override fun onFocusChange(view: View, isFocused: Boolean) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
        focusCount++

        if (isFocused && focusCount == 1) {
            showDatePicker()
        }

        if (null != onFocusChangedListener) {
            onFocusChangedListener!!.onFocusChange(view, isFocused)
        }
    }

    override fun onClick(v: View) {
        showDatePicker()
    }

    private fun showDatePicker() {
        val datePickerFragment = DatePickerFragment()

        datePickerFragment.date = date
        datePickerFragment.themeId = themeId
        datePickerFragment.minDate = minDate
        datePickerFragment.maxDate = maxDate
        datePickerFragment.onDateSetListener = this

        datePickerFragment.show(manager!!, TAG)
    }

    override fun onDateSet(datePicker: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance(Locale.getDefault())

        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, monthOfYear)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        if (textDateFormat != null) {
            setText(textDateFormat!!.format(calendar.time))
        } else {
            setText(DateUtils.toDate(calendar.time, dateFormat))
        }
        date = calendar
    }

    fun getManager(): FragmentManager? = manager

    fun setManager(manager: FragmentManager): DatePickerInputEditText {
        this.manager = manager
        return this
    }

    fun getDate(): Calendar? = date

    fun setDate(date: Calendar): DatePickerInputEditText {
        this.date = date
        return this
    }

    fun getOnFocusChangedListener(): OnFocusChangeListener? = onFocusChangedListener

    fun setOnFocusChangedListener(onFocusChangedListener: OnFocusChangeListener): DatePickerInputEditText {
        this.onFocusChangedListener = onFocusChangedListener
        return this
    }

    fun getDateFormat(): String? = dateFormat

    fun setDateFormat(dateFormat: String): DatePickerInputEditText {
        this.dateFormat = dateFormat
        return this
    }

    fun setDateFormat(format: DateFormat): DatePickerInputEditText {
        this.textDateFormat = format
        return this
    }

    fun getThemeId(): Int? = themeId

    fun setThemeId(themeId: Int?): DatePickerInputEditText {
        this.themeId = themeId
        return this
    }

    fun getMaxDate(): String? = maxDate

    fun setMaxDate(maxDate: String): DatePickerInputEditText {
        this.maxDate = maxDate
        return this
    }

    fun getMinDate(): String? = minDate

    fun setMinDate(minDate: String): DatePickerInputEditText {
        this.minDate = minDate
        return this
    }

    companion object {
        private val TAG = "DatePickerInputEditText"
    }
}