package com.omega_r.bind.model.binders

import android.os.Build
import android.widget.SeekBar
import androidx.annotation.IdRes
import com.omega_r.bind.model.BindModel
import kotlin.reflect.KProperty

open class SeekBarBinder<M>(
    override val id: Int,
    private val properties: Array<out KProperty<*>>,
    private val min: Int? = null,
    private val max: Int? = null,
    private val onChanged: ((view: SeekBar, progress: Int) -> Unit)? = null
) : Binder<SeekBar, M>() {

    override fun onViewCreated(itemView: SeekBar) {
        super.onViewCreated(itemView)
        max?.let { itemView.max = max }
        min?.let {
            itemView.max = itemView.max - min
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                itemView.min = 0
            }
        }
        if (onChanged != null) {
            itemView.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        onChanged.invoke(seekBar, progress + (min ?: 0))
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {
                    // nothing
                }

                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    // nothing
                }

            })
        }
    }

    override fun bind(itemView: SeekBar, item: M) {
        val progress: Int? = item.findValue(item, properties)
        progress?.let {
            itemView.progress = progress - (min ?: 0)
        }
    }
}

fun <M> BindModel.Builder<M>.bindSeek(
    @IdRes id: Int,
    vararg properties: KProperty<*>,
    min: Int? = null,
    max: Int? = null,
    onChanged: ((view: SeekBar, progress: Int) -> Unit)? = null
) = bindBinder(SeekBarBinder(id, properties = properties, min, max, onChanged))