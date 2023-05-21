package com.example.finalapplication.screen.main

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.finalapplication.R
import com.example.finalapplication.databinding.ActivityMainBinding
import com.example.finalapplication.screen.explore.ExploreFragment
import com.example.finalapplication.screen.historycontact.HistoryContactFragment
import com.example.finalapplication.screen.home.HomeFragment
import com.example.finalapplication.screen.profile.ProfileFragment
import com.example.finalapplication.screen.statistics.StatisticsFragment
import com.example.finalapplication.utils.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private val mainViewModel by viewModel<MainViewModel>()

    override fun handleEvent() {
        binding.apply {
            botomnavigationMain.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.home -> pagerMain.setCurrentItem(0, false)
                    R.id.explore -> pagerMain.setCurrentItem(1, false)
                    R.id.message ->
                        pagerMain.setCurrentItem(2, false)
                    R.id.statistic -> pagerMain.setCurrentItem(3, false)
                    else ->
                        pagerMain.setCurrentItem(4, false)
                }
                Log.e("aaaaa", pagerMain.currentItem.toString())
                return@setOnItemSelectedListener true
            }
            pagerMain.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    when (position) {
                        0 -> botomnavigationMain.menu.findItem(R.id.home).isChecked = true
                        1 -> botomnavigationMain.menu.findItem(R.id.explore).isChecked = true
                        2 -> botomnavigationMain.menu.findItem(R.id.message).isChecked = true
                        3 -> botomnavigationMain.menu.findItem(R.id.statistic).isChecked = true
                        4 -> botomnavigationMain.menu.findItem(R.id.account).isChecked = true
                        else -> {}
                    }
                }
            })
        }
    }

    override fun initData() {
        if (!checkPermission()) requestPermission()
        binding.apply {
            val mainPagerAdapter = MainViewPagerAdapter(this@MainActivity)
            mainPagerAdapter.setFragment(
                listOf(
                    HomeFragment(),
                    ExploreFragment(),
                    HistoryContactFragment(),
                    StatisticsFragment(),
                    ProfileFragment()
                )
            )
            pagerMain.adapter = mainPagerAdapter
        }
        binding.pagerMain.isUserInputEnabled = false
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            REQUEST_CODE_PERMISSION
        )
    }

    private fun checkPermission(): Boolean {
        val readStorge = ActivityCompat
            .checkSelfPermission(
                applicationContext,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        val manageStorge = ActivityCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.MANAGE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        val camera = ActivityCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        val call = ActivityCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED
        val location = ActivityCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        return readStorge && manageStorge && camera && call && location
    }

    override fun bindData() {
        // TODO  no-ip
    }

    override fun onResume() {
        mainViewModel.updateStatus(true)
        super.onResume()
    }

    override fun onBackPressed() {
        mainViewModel.updateStatus(false)
        super.onBackPressed()
    }

    companion object {
        private const val REQUEST_CODE_PERMISSION = 101
    }
}
