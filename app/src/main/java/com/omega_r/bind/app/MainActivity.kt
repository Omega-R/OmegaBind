package com.omega_r.bind.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.omega_r.bind.model.BindModel
import com.omega_r.bind.model.binders.bindString

class MainActivity : AppCompatActivity() {

    private val bindModel = BindModel.create<String> {
        bindString(R.id.NO_DEBUG)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}