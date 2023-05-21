package com.example.finalapplication.screen.mainstaff

import android.util.Log
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.finalapplication.R
import com.example.finalapplication.databinding.ActivityMainStaffBinding
import com.example.finalapplication.databinding.FragmentHistoryContactBinding
import com.example.finalapplication.screen.historycontact.HistoryContactFragment
import com.example.finalapplication.screen.homestaff.HomeStaffFragment
import com.example.finalapplication.screen.statisticstaff.StatisticStaffFragment
import com.example.finalapplication.utils.base.BaseActivity

class MainStaffActivity :
    BaseActivity<ActivityMainStaffBinding>(ActivityMainStaffBinding::inflate) {

    private val fragmentMessage = HistoryContactFragment()
    private val fragmentHome = HomeStaffFragment()
    private val fragmentStatistic = StatisticStaffFragment()
    override fun bindData() {

    }

    override fun handleEvent() {
        binding.apply {
            navStaff.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.home -> {
                        replaceFragment(fragmentHome)
                    }
                    R.id.staff_statis -> {
                        replaceFragment(fragmentStatistic)
                    }
                    else -> {

                        replaceFragment(fragmentMessage)
                    }

                }
                return@setOnItemSelectedListener true
            }
        }
    }

    override fun initData() {
        supportFragmentManager.beginTransaction()
            .replace(binding.containerFrag.id, HomeStaffFragment())
            .commit()
    }

    fun addNewFragment(frag: Fragment) {
        supportFragmentManager.beginTransaction()
            .add(binding.containerFrag.id, frag)
            .addToBackStack(null)
            .commit()
    }


    private fun replaceFragment(frag: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.containerFrag.id, frag)
            .commit()
    }

    fun setVisibleNavBar(status: Boolean) {
        binding.navStaff.isVisible = status
    }

    override fun onBackPressed() {
        setVisibleNavBar(true)
        super.onBackPressed()
    }

    override fun onResume() {
        if (supportFragmentManager.fragments.size <= 2) setVisibleNavBar(true)
        else setVisibleNavBar(false)
        super.onResume()
    }
}
