package com.omega_r.bind.model

import android.app.Activity
import android.util.SparseArray
import android.view.View
import com.omega_r.bind.R
import com.omega_r.bind.model.binders.*

/**
 * Created by Anton Knyazev on 27.02.2019.
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
class BindModel<M>(private val list: List<Binder<*, in M>>) {

    companion object {

        inline fun <M> create(parentModel: BindModel<in M>? = null, block: Builder<M>.() -> Unit): BindModel<M> {
            return Builder(parentModel)
                .apply(block)
                .build()
        }

        inline fun <M> create(block: Builder<M>.() -> Unit): BindModel<M> {
            return create(null, block)
        }

        fun <M> create(model1: BindModel<in M>, model2: BindModel<in M>): BindModel<M> {
            return BindModel(model1, model2.list)
        }

    }

    constructor(parentModel: BindModel<in M>? = null, list: List<Binder<*, in M>>) : this(
        (parentModel?.list ?: emptyList<Binder<*, M>>()) + list
    )

    constructor(vararg binder: Binder<*, in M>) : this(binder.toList())

    fun onViewCreated(activity: Activity) {
        onViewCreated(activity.window.decorView)
    }

    fun onViewCreated(view: View) {
        val viewCache = SparseArray<View>()
        val array = SparseArray<MutableSet<Binder<*, *>>>()
        view.setTag(R.id.omega_autobind, viewCache)

        list.forEach { it.addViewId(array) }

        for (i in 0 until array.size()) {
            val id = array.keyAt(i)
            val binders = array.valueAt(i)
            val childView: View? = view.findViewById(id)
            if (childView == null) {
                binders.forEach {
                    if (!it.viewOptionally) {
                        throw IllegalStateException(
                            "View with R.id.${view.context.resources.getResourceEntryName(id)} not found"
                        )
                    }
                }
            } else {
                viewCache.put(id, childView)
                array[id].forEach { it.dispatchOnViewCreated(childView, viewCache) }
            }
        }

    }

    fun bind(activity: Activity, item: M) {
        bind(activity.window.decorView, item)
    }

    fun bind(view: View, item: M) {
        @Suppress("UNCHECKED_CAST")
        val viewCache = view.getTag(R.id.omega_autobind) as SparseArray<View>

        list.forEach { binder -> binder.dispatchBind(viewCache, item) }
    }

    open class Builder<M>(private val parentModel: BindModel<in M>? = null) {

        private val list: MutableList<Binder<*, M>> = mutableListOf()

        fun bindBinder(binder: Binder<*, M>) = binder.apply {
            list += binder
        }

        fun build() = BindModel(parentModel, list)

    }

}