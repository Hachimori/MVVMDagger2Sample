package com.github.hachimori.mvvmdagger2sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.hachimori.mvvmdagger2sample.ui.input_form.InputFormFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, InputFormFragment.newInstance())
                .commitNow()
        }
    }

}
