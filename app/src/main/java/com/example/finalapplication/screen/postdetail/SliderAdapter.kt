package com.example.finalapplication.screen.postdetail

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.example.finalapplication.databinding.ItemImageBinding
import com.example.finalapplication.databinding.ItemImageSliderBinding
import com.example.finalapplication.utils.loadImageByUrl

class SliderAdapter : PagerAdapter() {

    private val images = mutableListOf<String>()

    fun setData(list: List<String>) {
        images.clear()
        images.addAll(list)
        Log.v("aaaaa", "have image : ${images.size}")
    }


    override fun getCount(): Int {
        return images.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater = LayoutInflater.from(container.context)
        val binding = ItemImageSliderBinding.inflate(layoutInflater, container,false)

        binding.imageRoom.loadImageByUrl(images[position], binding.root.context)
        container.addView(binding.root)
        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}