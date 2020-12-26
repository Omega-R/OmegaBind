package com.omega_r.bind.model.binders

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import com.omega_r.adapters.OmegaSpinnerAdapter
import com.omega_r.bind.model.BindModel
import kotlin.reflect.KProperty

open class SpinnerListBinder<M, SM>(
    override val id: Int,
    private val layoutRes: Int,
    private vararg val properties: KProperty<*>,
    private val nonSelectedItem: SM? = null,
    private val callback: ((M, SM?, Int) -> Unit)? = null,
    private val selector: (M) -> SM?,
    private val converter: (Context, SM, isDropDown: Boolean) -> CharSequence
) : Binder<Spinner, M>() {

    override fun onCreateView(itemView: Spinner) {
        itemView.adapter = OmegaSpinnerAdapter.CustomAdapter(itemView.context, layoutRes, converter).also {
            it.nonSelectedItem = nonSelectedItem
        }
    }

    @Suppress("UNCHECKED_CAST", "PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun bind(spinner: Spinner, item: M) {
        val list: List<SM>? = item.findValue(item, properties)

        val adapter = spinner.adapter as OmegaSpinnerAdapter.CustomAdapter<SM>

        adapter.list = list ?: emptyList()

        if (callback != null) {
            spinner.onItemSelectedListener = null
        }

        adapter.setSelection(spinner, selector(item))

        if (callback != null) {

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // nothing
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    callback.invoke(item, adapter.getSelection(spinner), adapter.getSelectionPosition(spinner))
                }

            }
        }
    }
}


fun <SM, M> BindModel.Builder<M>.bindList(
    id: Int,
    layoutRes: Int = android.R.layout.simple_spinner_item,
    vararg properties: KProperty<*>,
    nonSelectedItem: SM? = null,
    callback: ((M, SM?, Int) -> Unit)? = null,
    selector: (M) -> SM?,
    converter: (Context, SM, isDropDown: Boolean) -> CharSequence
) = bindBinder(
    SpinnerListBinder(
        id,
        layoutRes,
        *properties,
        nonSelectedItem = nonSelectedItem,
        callback = callback,
        selector = selector,
        converter = converter
    )
)