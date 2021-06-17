package com.tcc.soundidentifier

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Declare actionBar
        var actionBar = getSupportActionBar()

        // Showing the back button in action bar
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }
}