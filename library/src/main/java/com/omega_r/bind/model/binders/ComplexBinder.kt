package com.omega_r.bind.model.binders

import android.util.SparseArray
import android.view.View
import com.omega_r.bind.model.BindModel
import kotlin.reflect.KProperty

class ComplexBinder<V : View, M, M2, R>(
    private val binders: List<Binder<out View, in M2, out R>>,
    private val properties: Array<out KProperty<*>> = emptyArray(),
    ) : MultiBinder<V, M, R>(binders.getOrNull(0)?.id ?: -1, *(binders.drop(0).map { it.id }.toIntArray())) {

    override fun dispatchOnViewCreated(view: View, viewCache: SparseArray<View>) {
        super.dispatchOnViewCreated(view, viewCache)
        binders.forEach { binder ->
            val binderView = viewCache.get(binder.id)
            binder.dispatchOnViewCreated(binderView, viewCache)
        }
    }

    override fun dispatchBind(viewCache: SparseArray<View>, item: M) {
        val obj = item.findValue<M2>(item, properties) as M2

        binders.forEach { binder ->
            binder.dispatchBind(viewCache, obj)
        }
    }

    override fun bind(views: SparseArray<V>, item: M) {
        // nothing
    }

}

inline fun <M, M2: Any> BindModel.Builder<M>.bindComplex(
    parent: BindModel<M2>? = null,
    vararg properties: KProperty<*>,
    block: BindModel.Builder<M2>.() -> Unit
): Binder<View, M, M2> {
    val bindModel = BindModel.create(parent, block)
    val list: List<Binder<*, in M2, M2>> = bindModel.list as List<Binder<*, in M2, M2>>
    return bindBinder(ComplexBinder(list, properties))
}

inline fun <M, M2: Any> BindModel.Builder<M>.bindComplex(
    parent: BindModel<M2>? = null,
    property: KProperty<M2>,
    block: BindModel.Builder<M2>.() -> Unit
): Binder<View, M, M2> = bindComplex(parent, properties = *arrayOf(property), block)
