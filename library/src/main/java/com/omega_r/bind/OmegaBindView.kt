package com.omega_r.bind

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import com.omega_r.bind.model.BindModel

class OmegaBindView<M> @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {

    companion object {

        fun <M> create(context: Context, @LayoutRes layoutRes: Int, bindModel: BindModel<M>): OmegaBindView<M> {
            return OmegaBindView<M>(context).apply {
                setBindModel(layoutRes, bindModel)
            }
        }

        fun <M> create(
            context: Context,
            @LayoutRes layoutRes: Int,
            parentBindModel: BindModel<M>? = null,
            builder: BindModel.Builder<M>.() -> Unit,
        ): OmegaBindView<M> {
            val bindModel = BindModel.create(parentBindModel, builder)
            return create(context, layoutRes, bindModel)
        }

    }

    private var bindModel: BindModel<M>? = null

    fun setBindModel(@LayoutRes layoutRes: Int, bindModel: BindModel<M>) {
        removeAllViews()
        inflate(context, layoutRes, this)
        bindModel.onViewCreated(this)
        this.bindModel = bindModel
    }

    fun bind(item: M) {
        bindModel?.bind(this, item)
    }

}