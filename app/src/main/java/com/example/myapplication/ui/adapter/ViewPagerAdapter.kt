package com.example.myapplication.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myapplication.ui.fragment.ChatFragment
import com.example.myapplication.ui.fragment.DataFragment
import com.example.myapplication.ui.fragment.DiscoverFragment
import com.example.myapplication.ui.fragment.ProfileFragment

class ViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ChatFragment()
            1 -> DataFragment()
            2 -> DiscoverFragment()
            else -> ProfileFragment()
        }
    }
}