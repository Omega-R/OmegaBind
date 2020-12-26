package com.omega_r.bind.model.binders

import android.util.SparseArray
import android.view.View

abstract class MultiBinder<V : View, M>(final override val id: Int, vararg ids: Int) : Binder<V, M>() {

    val ids = listOf(id, *ids.toTypedArray())


    @Suppress("UNCHECKED_CAST")
    override fun dispatchBind(viewCache: SparseArray<View>, item: M) {
        bind(viewCache[id] as V, item)
        val array = SparseArray<V>(ids.size)
        ids.forEach { array.put(it, viewCache[it] as V) }
        bind(array, item)
    }

    override fun bind(itemView: V, item: M) {
        // nothing
    }

    abstract fun bind(views: SparseArray<V>, item: M)

    override fun addViewId(array: SparseArray<MutableSet<Binder<*, *>>>) {
        ids.forEach { id -> array.getSet(id) += this }
    }

}