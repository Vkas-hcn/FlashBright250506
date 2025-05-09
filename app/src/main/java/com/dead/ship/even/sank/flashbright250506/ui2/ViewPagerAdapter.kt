package com.dead.ship.even.sank.flashbright250506.ui2


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dead.ship.even.sank.flashbright250506.ui.home.HomeFragment
import com.dead.ship.even.sank.flashbright250506.ui.launch.LaunchFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {


    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment() // 第一页是 HomeFragment
            1 -> LaunchFragment() // 第二页是 LaunchFragment
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }}
