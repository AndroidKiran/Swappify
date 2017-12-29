package swapp.items.com.swappify.common.bindutil

import android.databinding.BindingAdapter
import android.databinding.InverseBindingAdapter
import android.databinding.InverseBindingListener
import android.support.v7.widget.AppCompatSpinner
import android.widget.BaseAdapter
import swapp.items.com.swappify.components.SpinnerOnItemSelectedListener


class SpinnerBindingUtils {

    companion object {

        @JvmStatic
        @BindingAdapter(value = *arrayOf("bind:selectedItem", "bind:selectedItemAttrChanged"), requireAll = false)
        fun <D> bindItemSelected(spinner: AppCompatSpinner, itemSetByViewModel: D?, inverseBindingListener: InverseBindingListener) {

            val initialSelectedPlanet: D? = itemSetByViewModel
            val spinnerOnItemSelectedListener: SpinnerOnItemSelectedListener<D>? = SpinnerOnItemSelectedListener<D>(initialSelectedPlanet, inverseBindingListener)
            spinner.onItemSelectedListener = spinnerOnItemSelectedListener

            if (itemSetByViewModel!! == spinner.selectedItem) {
                val positionInAdapter: Int = spinnerOnItemSelectedListener!!.getPositionInAdapter(spinner.adapter as BaseAdapter, itemSetByViewModel)
                spinner.setSelection(positionInAdapter)
            }
        }

        @JvmStatic
        @InverseBindingAdapter(attribute = "bind:selectedItem", event = "bind:selectedItemAttrChanged")
        fun <D> captureSelectedItem(spinner: AppCompatSpinner): D = spinner.selectedItem as D
    }
}