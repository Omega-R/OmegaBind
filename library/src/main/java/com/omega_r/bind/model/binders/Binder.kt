package com.omega_r.bind.model.binders

import android.util.SparseArray
import android.view.View
import kotlin.reflect.KProperty

abstract class Binder<V : View, M> {

    abstract val id: Int

    var viewOptionally: Boolean = false

    open fun dispatchOnCreateView(view: View, viewCache: SparseArray<View>) {
        @Suppress("UNCHECKED_CAST")
        onCreateView(view as V)
    }

    protected open fun onCreateView(itemView: V) {
        // nothing
    }

    @Suppress("UNCHECKED_CAST")
    open fun dispatchBind(viewCache: SparseArray<View>, item: M) {
        viewCache[id]?.let { view ->
            bind(view as V, item)
        }
    }

    abstract fun bind(itemView: V, item: M)

    protected fun SparseArray<MutableSet<Binder<*, *>>>.getSet(id: Int) = this[id] ?: let {
        HashSet<Binder<*, *>>().also {
            put(id, it)
        }
    }

    open fun addViewId(array: SparseArray<MutableSet<Binder<*, *>>>) {
        array.getSet(id) += this
    }

    @Suppress("UNCHECKED_CAST")
    protected fun <T> Any?.findValue(item: Any?, properties: Array<out KProperty<*>>): T? {
        var obj: Any? = item
        for (property in properties) {
            obj = property.call(obj)
            if (obj == null) {
                break
            }
        }
        return obj?.let { it as T }
    }

    fun optionally() {
        viewOptionally = true
    }


}