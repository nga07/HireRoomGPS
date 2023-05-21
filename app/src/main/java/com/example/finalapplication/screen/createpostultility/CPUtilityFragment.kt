package com.example.finalapplication.screen.createpostultility

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.example.finalapplication.data.model.Utility
import com.example.finalapplication.data.model.UtilityPost
import com.example.finalapplication.databinding.FragmentCreatePostUltilityBinding
import com.example.finalapplication.screen.createpost.CreatePostViewModel
import com.example.finalapplication.utils.Constant
import com.example.finalapplication.utils.ItemRecyclerViewListenner
import com.example.finalapplication.utils.base.BaseFragment
import com.example.finalapplication.utils.getUtilities
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class CPUtilityFragment :
    BaseFragment<FragmentCreatePostUltilityBinding>(FragmentCreatePostUltilityBinding::inflate),
    ItemRecyclerViewListenner<Utility> {

    private val createPostViewModel by sharedViewModel<CreatePostViewModel>()
    private val imageAdapter = ImageAdapter()
    private val utilityAdapter = UtilityAdapter(this)
    private val utilities = mutableListOf<Utility>()
    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                Log.d("anh", "Tra data ve" + result.data)
                val intent = result.data
                val datas = intent?.clipData
                Log.d("anh", "Tra data ve 2 " + datas)
                val images = mutableListOf<String>()
                images.add(intent?.data.toString())
                imageAdapter.addImage(images)
                Log.d("anh", "link" + intent?.data.toString())
                if (datas != null) {
                    val images = mutableListOf<String>()
                    val count = datas?.itemCount?.minus(1)
                    for (i in 0..count!!) {
                        images.add(datas.getItemAt(i).uri.toString())
                        Log.d("anh", "link" + datas.getItemAt(i).uri.toString())
                    }
                    imageAdapter.addImage(images)
                }
            }
        }

    override fun bindData() {
        // TODO("Not yet implemented")
    }

    override fun handleEvent() {
        binding.apply {
            buttonNext.setOnClickListener {
                val utilityPosts = mutableListOf<UtilityPost>()
                for (uti in utilities) {
                    utilityPosts.add(UtilityPost(uti, -1))
                }
                val images = imageAdapter.getImages()
                if (images.size < 4) {
                    errorImage.isVisible = true
                    return@setOnClickListener
                }
                val post = createPostViewModel.getPost()
                post.images = images
                post.utilities?.let { it1 -> utilityPosts.addAll(it1) }
                post.utilities = utilityPosts
                createPostViewModel.setPost(post)
                createPostViewModel.setState(4)
            }
            buttonUploadImage.setOnClickListener {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                intent.type = Constant.TYPE_IMAGE
                activityResultLauncher.launch(Intent.createChooser(intent, "Select picture"))
            }
        }
    }

    override fun initData() {
        binding.recyclerImage.adapter = imageAdapter
        var manager = GridLayoutManager(context, 5)
        binding.recyclerImage.layoutManager = manager
        binding.recyclerUtility.adapter = utilityAdapter
        manager = GridLayoutManager(context, 2)
        binding.recyclerUtility.layoutManager = manager
        utilityAdapter.setData(getUtilities())
        val post = createPostViewModel.getPost()
        if (post.images != null) {
            imageAdapter.addImage(post.images as List<String>)
            val utiPost = post.utilities
            val utis = mutableListOf<Utility>()
            if (utiPost != null) {
                for (item in utiPost) {
                    item.utility?.let { utis.add(it) }
                }
            }
            utilityAdapter.addData(utis)
        }

    }

    override fun onItemClick(item: Utility) {
        if (utilities.filter { uti -> uti.id == item.id }.isEmpty()) utilities.add(item)
        else utilities.removeIf { uti -> uti.id == item.id }
    }
}
