package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var calculatorEngine: CalculatorEngine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Khởi tạo CalculatorEngine
        calculatorEngine = CalculatorEngine()

        // Khởi tạo TabLayout và ViewPager
        tabLayout = findViewById(R.id.tab_layout)
        viewPager = findViewById(R.id.view_pager)

        // Cấu hình adapter
        val adapter = CalculatorPagerAdapter(this, calculatorEngine)
        viewPager.adapter = adapter

        // Kết nối TabLayout với ViewPager
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Cơ bản"
                1 -> "Khoa học"
                2 -> "Lập trình"
                else -> ""
            }
        }.attach()
    }
}
