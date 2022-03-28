package com.omega_r.bind.model.binders

import android.view.View
import androidx.annotation.IdRes
import com.omega_r.bind.model.BindModel
import kotlin.reflect.KProperty

open class VisibleBinder<M>(
    override val id: Int,
    private val trueVisibility: Int = View.VISIBLE,
    private val falseVisibility: Int = View.GONE,
    private val nullVisibility: Int = View.GONE,
    private vararg val properties: KProperty<*>
) : Binder<View, M>() {

    override fun bind(itemView: View, item: M) {
        val obj: Any? = item.findValue(item, properties)
        itemView.visibility = when (obj) {
            true -> trueVisibility
            false -> falseVisibility
            null -> nullVisibility
            else -> trueVisibility
        }
    }
}

@Suppress("RemoveRedundantSpreadOperator")
fun <M> BindModel.Builder<M>.bindVisible(
    @IdRes id: Int,
    property: KProperty<Boolean?>,
    trueVisibility: Int = View.VISIBLE,
    falseVisibility: Int = View.GONE,
    nullVisibility: Int = View.GONE
) = bindVisible(id, trueVisibility, falseVisibility, nullVisibility, *arrayOf(property))

@Suppress("RemoveRedundantSpreadOperator")
fun <M> BindModel.Builder<M>.bindNonNullVisible(
    @IdRes id: Int,
    trueVisibility: Int = View.VISIBLE,
    falseVisibility: Int = View.GONE,
    nullVisibility: Int = View.GONE,
    property: KProperty<*>
) = bindVisible(id, trueVisibility, falseVisibility, nullVisibility, *arrayOf(property))


fun <M> BindModel.Builder<M>.bindVisible(
    @IdRes id: Int,
    trueVisibility: Int = View.VISIBLE,
    falseVisibility: Int = View.GONE,
    nullVisibility: Int = View.GONE,
    vararg properties: KProperty<*>
) = bindBinder(
    VisibleBinder(id, trueVisibility, falseVisibility, nullVisibility, *properties)
)

fun <M> BindModel.Builder<M>.bindGone(
    @IdRes id: Int,
    trueVisibility: Int = View.GONE,
    falseVisibility: Int = View.VISIBLE,
    nullVisibility: Int = View.VISIBLE,
    vararg properties: KProperty<*>
) = bindBinder(
    VisibleBinder(id, trueVisibility, falseVisibility, nullVisibility, *properties)
)

fun <M> BindModel.Builder<M>.bindInvisible(
    @IdRes id: Int,
    trueVisibility: Int = View.INVISIBLE,
    falseVisibility: Int = View.VISIBLE,
    nullVisibility: Int = View.VISIBLE,
    vararg properties: KProperty<*>
) = bindBinder(
    VisibleBinder(id, trueVisibility, falseVisibility, nullVisibility, *properties)
)
