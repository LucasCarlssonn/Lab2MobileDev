package com.ebookfrenzy.lab2

import androidx.fragment.app.*
import androidx.viewpager2.adapter.FragmentStateAdapter

class TabPagerAdapter(fa: FragmentActivity, private var tabCount: Int): FragmentStateAdapter(fa)  {

    private val fragments = mutableListOf<Fragment>()

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
    override fun getItemCount(): Int {
        return tabCount
    }

    fun addFragment(fragment: Fragment) {
        fragments.add(fragment)
    }
}