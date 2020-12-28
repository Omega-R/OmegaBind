package com.omega_r.bind.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import com.omega_r.bind.OmegaAutoAdapter
import com.omega_r.bind.OmegaBindView
import com.omega_r.bind.model.BindModel
import com.omega_r.bind.model.binders.*

class MainActivity : AppCompatActivity() {

    private val bindModel = BindModel.create<String> {
        bindString(R.id.NO_DEBUG)
        bindVisible(R.id.NO_DEBUG) {
            true
        }
        bindMultiCustom(R.id.NO_DEBUG, R.id.NO_DEBUG) { sparseArray: SparseArray<View>, s: String ->
            sparseArray[R.id.NO_DEBUG]
        }
    }

    private val adapter = OmegaAutoAdapter.create(R.layout.activity_main, bindModel)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bindView = OmegaBindView.create<String>(this, R.layout.activity_main) {
            bindString(R.id.textview_hello)
        }


        bindView.bind("Bind: Hello World!")

        addContentView(bindView, ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT))
    }

}