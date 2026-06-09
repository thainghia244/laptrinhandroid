package com.example.myapplication

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class CalculatorPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val calculatorEngine: CalculatorEngine
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> BasicCalculatorFragment.newInstance(calculatorEngine)
            1 -> ScientificCalculatorFragment.newInstance(calculatorEngine)
            2 -> ProgrammingCalculatorFragment.newInstance(calculatorEngine)
            else -> BasicCalculatorFragment.newInstance(calculatorEngine)
        }
    }
}
