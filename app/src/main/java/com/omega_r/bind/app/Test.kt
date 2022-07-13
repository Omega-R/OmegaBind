package com.omega_r.bind.app

import android.content.Context
import android.view.View
import com.omega_r.bind.delegates.OmegaBindable
import com.omega_r.bind.delegates.managers.BindersManager
import com.omega_r.bind.model.binders.bindEnabled

class Test: OmegaBindable {

    private var visible: Boolean by bindEnabled(R.id.mtrl_card_checked_layer_id)

    override val bindersManager: BindersManager
        get() = TODO("Not yet implemented")


    override fun getContext(): Context? {
        TODO("Not yet implemented")
    }

    override fun <T : View> findViewById(id: Int): T? {
        TODO("Not yet implemented")
    }
}