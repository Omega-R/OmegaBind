package com.omega_r.bind.app

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import com.omega_r.bind.adapters.OmegaAutoAdapter
import com.omega_r.bind.delegates.OmegaBindable
import com.omega_r.bind.delegates.managers.BindersManager
import com.omega_r.bind.views.OmegaBindView
import com.omega_r.bind.model.BindModel
import com.omega_r.bind.model.binders.*

class MainActivity : AppCompatActivity(), OmegaBindable {

    override val bindersManager = BindersManager()

    private val bindModel = BindModel.create<String> {
        bindString(R.id.NO_DEBUG)
        bindVisible(R.id.NO_DEBUG) {
            true
        }
        bindMultiCustom(R.id.NO_DEBUG, R.id.NO_DEBUG) { sparseArray: SparseArray<View>, s ->
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

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        bindersManager.doAutoInit()
    }

    override fun getContext(): Context? = this


}