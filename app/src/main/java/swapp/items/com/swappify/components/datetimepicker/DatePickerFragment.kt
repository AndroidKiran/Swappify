package swapp.items.com.swappify.components.datetimepicker

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v7.app.AppCompatDialogFragment
import java.util.*


class DatePickerFragment : AppCompatDialogFragment() {

    var onDateSetListener: DatePickerDialog.OnDateSetListener? = null
        set

    var date: Calendar? = null
        set

    var minDate: String? = null
        set

    var maxDate: String? = null
        set

    var themeId: Int? = null
        set

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val year: Int; val month: Int; val day: Int

        if (null != date) {
            year = date!!.get(Calendar.YEAR)
            month = date!!.get(Calendar.MONTH)
            day = date!!.get(Calendar.DAY_OF_MONTH)
        } else {
            val c = Calendar.getInstance()

            year = c.get(Calendar.YEAR)
            month = c.get(Calendar.MONTH)
            day = c.get(Calendar.DAY_OF_MONTH)
        }

        val datePickerDialog: DatePickerDialog

        if (null != themeId && themeId != 0) {
            datePickerDialog = DatePickerDialog(activity, themeId!!, onDateSetListener, year, month, day)
        } else {
            datePickerDialog = DatePickerDialog(activity, onDateSetListener, year, month, day)
        }

        val minDateStr = if (null != minDate) minDate else DEFAULT_MIN_DATE
        val maxDateStr = if (null != maxDate) maxDate else DEFAULT_MAX_DATE

        val min = DateUtils.parse(minDateStr, DEFAULT_TEMPLATE)!!.time
        val max = DateUtils.parse(maxDateStr, DEFAULT_TEMPLATE)!!.time

        datePickerDialog.datePicker.minDate = min
        datePickerDialog.datePicker.maxDate = max

        return datePickerDialog
    }

    fun setOnDateSetListener(onDateSetListener: DatePickerDialog.OnDateSetListener): DatePickerFragment {
        this.onDateSetListener = onDateSetListener
        return this
    }

    fun setDate(date: Calendar): DatePickerFragment {
        this.date = date
        return this
    }

    fun setThemeId(themeId: Int?): DatePickerFragment {
        this.themeId = themeId
        return this
    }

    fun setMaxDate(maxDate: String): DatePickerFragment {
        this.maxDate = maxDate
        return this
    }

    fun setMinDate(minDate: String): DatePickerFragment {
        this.minDate = minDate
        return this
    }

    companion object {
        private val DEFAULT_TEMPLATE = "dd/MM/yyyy"
        private val DEFAULT_MIN_DATE = "01/01/1980"
        private val DEFAULT_MAX_DATE = "01/01/2100"
    }
}