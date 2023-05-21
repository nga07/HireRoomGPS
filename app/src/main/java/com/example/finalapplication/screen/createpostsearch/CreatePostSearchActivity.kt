package com.example.finalapplication.screen.createpostsearch

import android.R
import android.app.ProgressDialog
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.finalapplication.data.model.Address
import com.example.finalapplication.data.model.District
import com.example.finalapplication.data.model.Post
import com.example.finalapplication.data.model.Province
import com.example.finalapplication.databinding.ActivityCreatePostSearchBinding
import com.example.finalapplication.screen.createpost.CreatePostViewModel
import com.example.finalapplication.screen.createpostaddress.CPAddressViewModel
import com.example.finalapplication.utils.Constant
import com.example.finalapplication.utils.base.BaseActivity
import com.example.finalapplication.utils.getNewid
import com.example.finalapplication.utils.showMessage
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreatePostSearchActivity :
    BaseActivity<ActivityCreatePostSearchBinding>(ActivityCreatePostSearchBinding::inflate) {

    private val addressViewModel by viewModel<CPAddressViewModel>()
    private val createPostViewModel by viewModel<CreatePostViewModel>()
    private val provinces = mutableListOf<Province>()
    private val districts = mutableListOf<District>()
    private lateinit var loadingDialog: ProgressDialog

    override fun bindData() {
        createPostViewModel.isLoading.observe(this) { data ->
            if (data) loadingDialog.show()
            else {
                loadingDialog.hide()
            }
        }
        createPostViewModel.isSuccess.observe(this) { data ->

            var msg = "Đã đăng"

            if (data) {
                if (binding.buttonPost.text == "Update") msg = "Đã cập nhật"
                msg.showMessage(this)
                finish()
            }
        }
        addressViewModel.provinces.observe(this) { data ->
            provinces.clear()
            provinces.addAll(data)
            val names = mutableListOf<String>()
            for (i in data) {
                names.add(i.name.toString())
            }
            val cityAdapter =
                ArrayAdapter(applicationContext, R.layout.simple_spinner_item, names)
            binding.spinnerCity.adapter = cityAdapter
        }
        addressViewModel.districts.observe(this) { data ->
            districts.clear()
            districts.addAll(data)
            districts.removeFirst()
            districts.removeFirst()
            val names = mutableListOf<String>()
            for (i in data) {
                names.add(i.name.toString())
            }
            names.removeFirst()
            names.removeFirst()
            val districtAdapter =
                ArrayAdapter(applicationContext, R.layout.simple_spinner_item, names)
            binding.spinnerDistrisct.adapter = districtAdapter
        }
    }

    override fun handleEvent() {
        binding.apply {
            buttonBack.setOnClickListener { onBackPressed() }
            spinnerCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    addressViewModel.getDistricts(provinces[p2])
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    // TODO("Not yet implemented")
                }
            }
            buttonPost.setOnClickListener {
                val from = edittextCostFrom.text.toString()
                val to = edittextCostTo.text.toString()
                val description = textRequied.text.toString()
                val city = provinces[spinnerCity.selectedItemPosition].name.toString()
                val district = districts[spinnerDistrisct.selectedItemPosition].name.toString()
                errorFrom.isVisible = from.isNullOrEmpty()
                errorTo.isVisible = to.isNullOrEmpty()
                errorRequire.isVisible = description.isNullOrEmpty()
                if (from.isNullOrEmpty() || to.isNullOrEmpty() || description.isNullOrEmpty()) return@setOnClickListener
                val post = createPostViewModel.getPost()
                if(post.id == null) post.id = getNewid().toString()
                post.postType = Post.timPhongO
                post.address = Address(city, district, null, null, null)
                post.minPrice = from.toLong()
                post.maxPrice = to.toLong()
                post.description = description
                if (buttonPost.text == "Update") createPostViewModel.upDatePost(post)
                else createPostViewModel.createPost(post)
            }
        }
    }

    override fun initData() {
        addressViewModel.getProvinces()
        loadingDialog = ProgressDialog(this)
        loadingDialog.setCancelable(false)
        val post = intent.getSerializableExtra(Constant.INTENT_POST)
        if (post != null) {
            createPostViewModel.setPost(post as Post)
            binding.apply {
                (edittextCostFrom as TextView).text = post.minPrice.toString()
                (edittextCostTo as TextView).text = post.maxPrice.toString()
                (textRequied as TextView).text = post.description.toString()
                buttonPost.text = "Update"
            }
        }
    }
}
