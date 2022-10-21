package com.omega_r.bind.model.binders

import android.util.SparseArray
import android.view.View
import androidx.core.util.getOrElse
import kotlin.properties.Delegates
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class Binder<V : View, M, R>: ReadWriteProperty<Any, R> {

    private companion object {
        private val NULL = Binder::class
    }

    abstract val id: Int

    var viewOptionally: Boolean = false

    internal var viewLazy: Lazy<View>? = null

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
    override operator fun getValue(thisRef: Any, property: KProperty<*>): R = value as R

    @Suppress("UNCHECKED_CAST")
    override operator fun setValue(thisRef: Any, property: KProperty<*>, value: R) {
        this.value = value
        bind(viewLazy?.value as V, value as M)
        onBindListener?.invoke(viewLazy?.value as V, value as M)
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
    protected fun <T> Any?.findValue(item: Any?, properties: Array<out KProperty<*>>): T? {
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