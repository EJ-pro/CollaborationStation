package com.example.collaborationstation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_item1 -> {
                    replaceFragment(Fragment_Team_Matching())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_item2 -> {
                    replaceFragment(Fragment_Message())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_item3 -> {
                    replaceFragment(Fragment_Portfolio_Management())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_item4 -> {
                    replaceFragment(Fragment_My_Page())
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        navView.selectedItemId = R.id.navigation_item1

    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
