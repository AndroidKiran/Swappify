package swapp.items.com.swappify.components

import android.databinding.InverseBindingListener
import android.view.View
import android.widget.AdapterView
import android.widget.BaseAdapter


class SpinnerOnItemSelectedListener<T>(private var initialSelectedItem: T?, private val inverseBindingListener: InverseBindingListener?) : AdapterView.OnItemSelectedListener {

    override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
        if (initialSelectedItem != null) {
            val positionInAdapter: Int = getPositionInAdapter(adapterView.adapter as BaseAdapter, initialSelectedItem)
            if (positionInAdapter != -1) {
                adapterView.setSelection(positionInAdapter)
            }
            initialSelectedItem = null
        } else {
            inverseBindingListener?.onChange()
        }
    }

    override fun onNothingSelected(adapterView: AdapterView<*>) {}


    fun <D> getPositionInAdapter(adapter: BaseAdapter?, item: D?): Int {
        for (i in 0 until adapter!!.count) {
            val adapterItem: D? = adapter.getItem(i) as D
            if (adapterItem == item) {
                return i
            }
        }
        return -1
    }
}