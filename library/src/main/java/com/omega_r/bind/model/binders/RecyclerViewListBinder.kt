package com.omega_r.bind.model.binders

import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import com.omega_r.bind.adapters.OmegaAutoAdapter
import com.omega_r.bind.model.BindModel
import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView
import kotlin.reflect.KProperty

open class RecyclerViewListBinder<M, SM>(
    override val id: Int,
    private vararg val properties: KProperty<*>,
    private val layoutRes: Int,
    private val callback: ((M, SM) -> Unit)? = null,
    private val parentModel: BindModel<SM>? = null,
    private val block: BindModel.Builder<SM>.() -> Unit
) : Binder<RecyclerView, M>() {

    override fun onViewCreated(itemView: RecyclerView) {
        itemView.adapter = OmegaAutoAdapter.create(
            layoutRes = layoutRes,
            callback = callback?.let { Callback(callback) },
            parentModel = parentModel,
            block = block
        )
    }

    @Suppress("UNCHECKED_CAST")
    override fun bind(itemView: RecyclerView, item: M) {
        val list: List<SM>? = item.findValue(item, properties)
        getAdapter(itemView).also {
            it.list = list ?: emptyList()
            (it.callback as? Callback<M, SM>)?.apply {
                model = item
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    protected fun getAdapter(itemView: RecyclerView): OmegaAutoAdapter<SM, *> {
        val adapter = when (itemView) {
            is OmegaRecyclerView -> itemView.realAdapter
            else -> itemView.adapter
        }
        return adapter as OmegaAutoAdapter<SM, *>
    }

    class Callback <M, SM>(private val block: (M, SM) -> Unit) : (SM) -> Unit {

        var model: M? = null

        override fun invoke(subModel: SM) {
            block(model ?: return, subModel)
        }

    }
}

@Suppress("CHANGING_ARGUMENTS_EXECUTION_ORDER_FOR_NAMED_VARARGS",
    "REDUNDANT_SPREAD_OPERATOR_IN_NAMED_FORM_IN_FUNCTION"
)
fun <SM, M> BindModel.Builder<M>.bindList(
    @IdRes id: Int,
    layoutRes: Int,
    property: KProperty<List<SM>>,
    callback: ((M, SM) -> Unit)? = null,
    block: BindModel.Builder<SM>.() -> Unit
) = bindList(id, layoutRes, properties = *arrayOf(property), block = block, callback = callback)


fun <SM, M> BindModel.Builder<M>.bindList(
    @IdRes id: Int,
    layoutRes: Int,
    vararg properties: KProperty<*>,
    callback: ((M, SM) -> Unit)? = null,
    parentModel: BindModel<SM>? = null,
    block: BindModel.Builder<SM>.() -> Unit
) = bindBinder(
    RecyclerViewListBinder(
        id,
        *properties,
        layoutRes = layoutRes,
        block = block,
        parentModel = parentModel,
        callback = callback
    )
)