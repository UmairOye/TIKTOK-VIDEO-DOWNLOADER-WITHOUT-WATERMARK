package com.example.tiktokvideodownloaderwithoutwatermark.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.tiktokvideodownloaderwithoutwatermark.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navController = this.findNavController(R.id.nav_host_fragment)
        val navView: BottomNavigationView = findViewById(R.id.bottom_nav_view)
        navView.setupWithNavController(navController)
    }
}