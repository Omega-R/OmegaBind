package com.omega_r.bind.model.binders

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import com.omega_r.bind.R

class BinderTextWatcher<E> : TextWatcher {

    companion object {

        fun <E> from(view: View): BinderTextWatcher<E> {
            return fromOrNull(view) ?: let {
                BinderTextWatcher<E>().also {
                    view.setTag(R.id.omega_text_watcher, it)
                }
            }
        }

        @Suppress("UNCHECKED_CAST")
        fun <E> fromOrNull(view: View): BinderTextWatcher<E>? {
            return view.getTag(R.id.omega_text_watcher) as? BinderTextWatcher<E>
        }

        inline fun runTextChangedTransaction(textView: TextView, block: () -> Unit) {
            val textWatcher = fromOrNull<Any>(textView)
            textWatcher?.enabled = false
            block()
            textWatcher?.enabled = true
        }

    }

    var item: E? = null
    val callbacks: MutableList<(E, String) -> Unit> = mutableListOf()

    var enabled: Boolean = true

    override fun afterTextChanged(s: Editable?) {
        // nothing
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // nothing
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (enabled) {
            item?.let { item ->
                callbacks.forEach { it(item, s.toString()) }
            }
        }
    }

}