package com.omega_r.bind

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.omega_r.adapters.OmegaAdapter
import com.omega_r.adapters.OmegaIdentifiable
import com.omega_r.adapters.OmegaListAdapter
import com.omega_r.bind.model.BindModel
import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView
import kotlin.reflect.KClass

/**
 * Created by Anton Knyazev on 07.04.2019.
 */

@Suppress("unused")
open class OmegaAutoAdapter<M, VH>(
    private val viewHolderFactory: Factory<M, VH>
) : OmegaListAdapter<M, VH>() where VH : OmegaRecyclerView.ViewHolder, VH : OmegaListAdapter.ViewHolderBindable<M> {

    companion object {

        const val SWIPE_NO_ID = OmegaAdapter.SwipeViewHolder.NO_ID

        fun <M> create(
            @LayoutRes layoutRes: Int,
            callback: ((M) -> Unit)? = null,
            parentModel: BindModel<M>? = null,
            block: BindModel.Builder<M>.() -> Unit
        ): OmegaAutoAdapter<M, ViewHolder<M>> {
            val bindModel = BindModel.create(parentModel, block)
            return OmegaAutoAdapter(ViewHolderFactory(layoutRes, bindModel, callback))
        }

        fun <M> create(
            @LayoutRes layoutRes: Int,
            @LayoutRes swipeMenuLayoutRes: Int,
            callback: ((M) -> Unit)? = null,
            parentModel: BindModel<M>? = null,
            block: BindModel.Builder<M>.() -> Unit
        ): OmegaAutoAdapter<M, SwipeViewHolder<M>> {
            val bindModel = BindModel.create(parentModel, block)
            return OmegaAutoAdapter(SwipeViewHolderFactory(layoutRes, swipeMenuLayoutRes, bindModel, callback))
        }

        fun <M> create(
            @LayoutRes layoutRes: Int,
            bindModel: BindModel<M>,
            callback: ((M) -> Unit)? = null
        ): OmegaAutoAdapter<M, ViewHolder<M>> {
            return OmegaAutoAdapter(ViewHolderFactory(layoutRes, bindModel, callback))
        }

        fun <M> create(
            @LayoutRes layoutRes: Int,
            @LayoutRes swipeMenuLayoutRes: Int,
            bindModel: BindModel<M>,
            callback: ((M) -> Unit)? = null
        ): OmegaAutoAdapter<M, SwipeViewHolder<M>> {
            return OmegaAutoAdapter(SwipeViewHolderFactory(layoutRes, swipeMenuLayoutRes, bindModel, callback))
        }

    }

    @Suppress("UNCHECKED_CAST")
    val callback: ((M) -> Unit)?
        get() = (viewHolderFactory as? Callbackable<M>)?.callback

    public override var watcher: Watcher? = null

    override fun getItemId(position: Int): Long {
        return when (val item = list[position]) {
            is OmegaIdentifiable<*> -> item.idAsLong
            else -> super.getItemId(position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return viewHolderFactory.getItemViewType(position, this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return viewHolderFactory.createViewHolder(parent, this, viewType)
    }

    class ViewHolder<M>(
        viewGroup: ViewGroup,
        layoutRes: Int,
        private val bindModel: BindModel<M>,
        callback: ((M) -> Unit)? = null
    ) : OmegaListAdapter.ViewHolder<M>(viewGroup, layoutRes) {

        private var item: M? = null

        init {
            callback?.let {
                itemView.setClickListener {
                    item?.let {
                        callback(it)
                    }
                }
            }
            bindModel.onViewCreated(itemView)
        }

        override fun bind(item: M) {
            this.item = item
            bindModel.bind(itemView, item)
        }

    }

    class SwipeViewHolder<M>(
        parent: ViewGroup,
        @LayoutRes layoutRes: Int,
        @LayoutRes swipeMenuLayoutRes: Int,
        private val bindModel: BindModel<M>,
        callback: ((M) -> Unit)? = null
    ) : OmegaListAdapter.SwipeViewHolder<M>(
        parent,
        layoutRes,
        swipeMenuLayoutRes
    ) {

        private var item: M? = null

        init {
            itemView.id = R.id.omega_swipe_menu
            callback?.let {
                contentView.setClickListener {
                    item?.let {
                        callback(it)
                    }
                }
            }
            bindModel.onViewCreated(itemView)
        }

        override fun bind(item: M) {
            this.item = item
            bindModel.bind(itemView, item)
        }

    }

    interface Callbackable<M> {
        val callback: ((M) -> Unit)?
    }

    abstract class Factory<M, VH> where VH : OmegaRecyclerView.ViewHolder, VH : ViewHolderBindable<M> {


        open fun getItemViewType(
            position: Int,
            adapter: OmegaAutoAdapter<M, VH>
        ): Int = 0

        abstract fun createViewHolder(
            parent: ViewGroup,
            adapter: OmegaAutoAdapter<M, VH>,
            viewType: Int
        ): VH

    }

    open class ViewHolderFactory<M>(
        private val layoutRes: Int,
        private val bindModel: BindModel<M>,
        override val callback: ((M) -> Unit)? = null
    ) : Factory<M, ViewHolder<M>>(), Callbackable<M> {

        override fun createViewHolder(
            parent: ViewGroup,
            adapter: OmegaAutoAdapter<M, ViewHolder<M>>,
            viewType: Int
        ): ViewHolder<M> {
            return ViewHolder(parent, layoutRes, bindModel, callback)
        }

    }

    open class SwipeViewHolderFactory<M>(
        @LayoutRes private val layoutRes: Int,
        @LayoutRes private val swipeMenuLayoutRes: Int,
        private val bindModel: BindModel<M>,
        override val callback: ((M) -> Unit)? = null
    ) : Factory<M, SwipeViewHolder<M>>(), Callbackable<M> {

        override fun createViewHolder(
            parent: ViewGroup,
            adapter: OmegaAutoAdapter<M, SwipeViewHolder<M>>,
            viewType: Int
        ) = SwipeViewHolder(parent, layoutRes, swipeMenuLayoutRes, bindModel, callback)

    }

    open class MultiHolderFactory<M : Any, VH>(
        private val map: Map<KClass<M>, Factory<M, VH>>
    ) : Factory<M, VH>() where VH : OmegaRecyclerView.ViewHolder, VH : ViewHolderBindable<M> {

        override fun getItemViewType(
            position: Int,
            adapter: OmegaAutoAdapter<M, VH>
        ): Int {
            val item = adapter.list[position]
            map.keys.forEachIndexed { index: Int, kClass: KClass<*> ->
                if (kClass.isInstance(item)) {
                    return index
                }
            }

            throw IllegalStateException("Unknown class for item = $item")
        }

        override fun createViewHolder(
            parent: ViewGroup,
            adapter: OmegaAutoAdapter<M, VH>,
            viewType: Int
        ): VH {
            return map.getValue(map.keys.elementAt(viewType)).createViewHolder(parent, adapter, viewType)
        }
    }

    @Suppress("UNCHECKED_CAST")
    class MultiAutoAdapterBuilder<M : Any, VH>(
        private val parentModel: BindModel<M>? = null
    ) where VH : OmegaRecyclerView.ViewHolder, VH : ViewHolderBindable<M> {

        private val map = mutableMapOf<KClass<*>, Factory<*, *>>()

        fun <M2 : M> add(
            kClass: KClass<M2>,
            @LayoutRes layoutRes: Int,
            callback: ((M2) -> Unit)? = null,
            parentModel: BindModel<M2>? = this.parentModel as? BindModel<M2>,
            block: BindModel.Builder<M2>.() -> Unit
        ): MultiAutoAdapterBuilder<M, VH> = apply {
            map[kClass] = ViewHolderFactory(layoutRes, BindModel.create(parentModel, block), callback)
        }

        fun <M2 : M> add(
            kClass: KClass<M2>,
            @LayoutRes layoutRes: Int,
            model: BindModel<M2>,
            callback: ((M2) -> Unit)? = null,
            parentModel: BindModel<M2>? = this.parentModel as? BindModel<M2>
        ) = apply {
            val modifiedModel = if (parentModel != null) BindModel.create(parentModel, model) else model
            map[kClass] = ViewHolderFactory(layoutRes, modifiedModel, callback)
        }

        fun <M2 : M> add(
            kClass: KClass<M2>,
            @LayoutRes layoutRes: Int,
            @LayoutRes swipeMenuLayoutRes: Int,
            callback: ((M2) -> Unit)? = null,
            parentModel: BindModel<M2>? = this.parentModel as? BindModel<M2>,
            block: BindModel.Builder<M2>.() -> Unit
        ) = apply {
            map[kClass] = SwipeViewHolderFactory(
                layoutRes,
                swipeMenuLayoutRes,
                BindModel.create(parentModel, block),
                callback
            )
        }

        fun <M2 : M> add(
            kClass: KClass<M2>,
            @LayoutRes layoutRes: Int,
            @LayoutRes swipeMenuLayoutRes: Int,
            model: BindModel<M2>,
            callback: ((M2) -> Unit)? = null,
            parentModel: BindModel<M2>? = this.parentModel as? BindModel<M2>
        ) = apply {
            val modifiedModel = if (parentModel != null) BindModel.create(parentModel, model) else model

            map[kClass] = SwipeViewHolderFactory(layoutRes, swipeMenuLayoutRes, modifiedModel, callback)
        }

        fun <M2 : M> add(
            kClass: KClass<M2>,
            factory: Factory<M2, *>
        ) = apply {
            map[kClass] = factory
        }

        @Suppress("UNCHECKED_CAST")
        fun createFactory() = MultiHolderFactory(map as Map<KClass<M>, Factory<M, VH>>) as Factory<M, VH>

        fun build() = OmegaAutoAdapter(createFactory())

    }

}