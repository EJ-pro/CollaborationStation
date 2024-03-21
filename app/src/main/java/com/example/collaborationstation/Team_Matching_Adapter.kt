package com.example.collaborationstation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class Team_Matching_Adapter (fm: FragmentManager, private val tabCount: Int) : FragmentPagerAdapter(fm) {

    override fun getCount(): Int {

        return tabCount
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> Fragment_Team_Matching_Tab1()
            1 -> Fragment_Team_Matching_Tab2()
            2 -> Fragment_Team_Matching_Tab3()
            else -> throw IllegalStateException("Invalid tab position")
        }
    }

}
