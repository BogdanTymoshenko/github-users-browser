package com.amicablesoft.ghusersbrowser.android

import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.Menu

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar) as Toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_actions, menu)
        val searchItem = menu.findItem(R.id.main_action__search)
        DrawableCompat.setTint(searchItem.icon, ResourcesCompat.getColor(resources, R.color.icons, theme))
        val searchView = MenuItemCompat.getActionView(searchItem) as SearchView

        return super.onCreateOptionsMenu(menu)
    }
}
