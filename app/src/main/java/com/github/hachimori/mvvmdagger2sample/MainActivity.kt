package com.github.hachimori.mvvmdagger2sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // TODO: use in the future
//        val host: NavHostFragment = supportFragmentManager
//            .findFragmentById(R.id.main_nav_host_fragment) as NavHostFragment? ?: return
//        
//        val navController = host.navController
    }

}
