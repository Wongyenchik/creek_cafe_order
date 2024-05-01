package com.example.creekcafe

import OrderFragmentAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout

class OrderHistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_history)
        val btnBack = findViewById<Button>(R.id.btnBack)
        btnBack.setOnClickListener{
            finish()
        }
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val OrderViewPager = findViewById<ViewPager2>(R.id.OrderViewPager)
        val adapter = OrderFragmentAdapter(this@OrderHistoryActivity, lifecycle)
        OrderViewPager.adapter = adapter

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    OrderViewPager.currentItem = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Handle tab unselected event
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Handle tab reselected event
            }
        })
        val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                // Your implementation when the page is selected
                // For example, you can update UI components or perform other actions
                tabLayout.getTabAt(position)?.select()
            }
        }

        OrderViewPager.registerOnPageChangeCallback(onPageChangeCallback)

    }
}