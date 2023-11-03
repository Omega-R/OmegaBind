package com.omega_r.bind.model.binders

import android.util.SparseArray
import android.view.View
import androidx.core.util.getOrElse
import com.omega_r.bind.delegates.OmegaBindable
import kotlin.reflect.KProperty

abstract class Binder<V : View, M, R> {

    private companion object {
        private val NULL = Binder::class
    }

    abstract val id: Int

    var viewOptionally: Boolean = false

    internal var viewCache: Lazy<SparseArray<View>>? = null

    private var value: Any? = NULL

    private var onViewCreateListener: ((itemView: V) -> Unit)? = null

    private var onBindListener: ((itemView: V, item: M) -> Unit)? = null

    internal open fun dispatchOnViewCreated(view: View, viewCache: SparseArray<View>) {
        @Suppress("UNCHECKED_CAST")
        onViewCreated(view as V)
        onViewCreateListener?.invoke(view)
    }

    protected open fun onViewCreated(itemView: V) {
        // nothing
    }

    @Suppress("UNCHECKED_CAST")
    open fun dispatchBind(viewCache: SparseArray<View>, item: M) {
        viewCache[id]?.let { view ->
            bind(view as V, item)
            onBindListener?.invoke(view as V, item)
        }
    }

    @Suppress("UNCHECKED_CAST")
    operator fun getValue(thisRef: OmegaBindable, property: KProperty<*>): R = value.takeIf { it != NULL } as R

    @Suppress("UNCHECKED_CAST")
    operator fun setValue(thisRef: OmegaBindable, property: KProperty<*>, value: R) {
        this.value = value
        dispatchBind(viewCache!!.value, value as M)
        onBindListener?.invoke(viewCache!!.value.get(id) as V, value as M)
    }

    abstract fun bind(itemView: V, item: M)

    protected fun SparseArray<MutableSet<Binder<*, *, *>>>.getSet(id: Int) = getOrElse(id) {
        HashSet<Binder<*, *, *>>().also {
            put(id, it)
        }
    }

    open fun addViewId(array: SparseArray<MutableSet<Binder<*, *, *>>>) {
        array.getSet(id) += this
    }

    @Suppress("UNCHECKED_CAST")
    protected fun <T: Any?> Any?.findValue(item: Any?, properties: Array<out KProperty<*>>): T? {
        var obj: Any? = item
        if (obj == null || obj::class.java === Any::class.java) {
            return null
        }
        for (property in properties) {
            obj = property.call(obj)
            if (obj == null) {
                break
            } else if (obj::class.java === Any::class.java) {
                obj = null
                break
            }
        }
        return obj?.let { it as T }
    }

    fun optionally() {
        viewOptionally = true
    }

    @Suppress("UNCHECKED_CAST")
    fun asNullType(): Binder<V, M, R?> = this as Binder<V, M, R?>

    fun onCreateView(listener: (itemView: V) -> Unit) = apply {
        onViewCreateListener = listener
    }

    fun onBindView(listener: (itemView: V, item: M) -> Unit) = apply {
        onBindListener = listener
    }

}