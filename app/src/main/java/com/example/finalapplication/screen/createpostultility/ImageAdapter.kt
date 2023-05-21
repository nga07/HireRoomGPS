package com.example.finalapplication.screen.createpostultility

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finalapplication.databinding.ItemImageBinding
import com.example.finalapplication.utils.base.BaseViewHolder
import com.example.finalapplication.utils.loadImageByUrl

class ImageAdapter : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>(){

    private val images  = mutableListOf<String>()

    fun addImage(list: List<String>){
        Log.v("aaaaa", "have ${list.size}")
        images.addAll(list)
        notifyDataSetChanged()
    }

    fun getImages() = images

    inner class ImageViewHolder(val binding : ItemImageBinding) : BaseViewHolder<String>(binding){
        override fun bind(item: String) {
            binding.image.loadImageByUrl(item, binding.root.context)
            binding.buttonDelete.setOnClickListener {
                images.removeAt(adapterPosition)
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemImageBinding.inflate(layoutInflater, parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount(): Int {
        return images.size
    }
}