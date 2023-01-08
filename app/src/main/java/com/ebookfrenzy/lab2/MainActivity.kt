package com.ebookfrenzy.lab2

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.ebookfrenzy.lab2.databinding.ActivityMainBinding
import com.ebookfrenzy.lab2.tabFragments.AnalyzesFragment
import com.ebookfrenzy.lab2.tabFragments.HomeFragment
import com.ebookfrenzy.lab2.tabFragments.QuestionsFragment
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity(),
    HomeFragment.OnFragmentInteractionListener,
    QuestionsFragment.OnFragmentInteractionListener,
    AnalyzesFragment.OnFragmentInteractionListener{

    private lateinit var binding: ActivityMainBinding
    private var layoutManager: RecyclerView.LayoutManager? = null
    lateinit var adapter: TabPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        configureTabLayout()

    }

    private fun configureTabLayout() {
        val tabNames = arrayOf("Home", "Questions", "Analyzes")

        repeat (tabNames.size) {
            binding.tabLayout.addTab(binding.tabLayout.newTab())
        }
        adapter = TabPagerAdapter(this,
            binding.tabLayout.tabCount)
        adapter.addFragment(HomeFragment())
        adapter.addFragment(QuestionsFragment())
        adapter.addFragment(AnalyzesFragment())
        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) {
                tab, position ->
            tab.text = tabNames[position]
        }.attach()
    }
    override fun onFragmentInteraction(uri: Uri) {
    }


}