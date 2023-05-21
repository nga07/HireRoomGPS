package com.example.finalapplication.screen.createpost

import android.app.ProgressDialog
import android.util.Log
import androidx.core.view.isVisible
import com.example.finalapplication.data.model.Post
import com.example.finalapplication.databinding.ActivityCreatePostBinding
import com.example.finalapplication.screen.createpostaddress.CPAddressFragment
import com.example.finalapplication.screen.createpostconfirm.CPConfirmFragment
import com.example.finalapplication.screen.createpostinfomation.CPInfomationFragment
import com.example.finalapplication.screen.createpostultility.CPUtilityFragment
import com.example.finalapplication.utils.Constant
import com.example.finalapplication.utils.base.BaseActivity
import com.example.finalapplication.utils.showMessage
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreatePostActivity :
    BaseActivity<ActivityCreatePostBinding>(ActivityCreatePostBinding::inflate) {

    private val createPostViewModel by viewModel<CreatePostViewModel>()
    private var state = 0
    private lateinit var loadingDialog: ProgressDialog


    override fun bindData() {
        createPostViewModel.isLoading.observe(this) { data ->
            if (data) loadingDialog.show()
            else loadingDialog.hide()
        }
        createPostViewModel.isSuccess.observe(this) { data ->
            if (data) {
                val msg = "Đã đăng"
                msg.showMessage(this)
                finish()
            }
        }
        createPostViewModel.isFirstState.observe(this) { data ->
            state = data
            when (data) {
                1 -> binding.buttonBack.isVisible = false
                2 -> {
                    supportFragmentManager.beginTransaction()
                        .add(binding.containerFrag.id, CPAddressFragment())
                        .addToBackStack("address").commit()
                    binding.buttonBack.isVisible = true
                }
                3 -> {
                    supportFragmentManager.beginTransaction()
                        .add(binding.containerFrag.id, CPUtilityFragment())
                        .addToBackStack("utility").commit()
                    binding.buttonBack.isVisible = true
                }
                4 -> {
                    supportFragmentManager.beginTransaction()
                        .add(binding.containerFrag.id, CPConfirmFragment())
                        .addToBackStack("confirm").commit()
                    binding.buttonBack.isVisible = true
                }
            }
        }
    }

    override fun handleEvent() {
        binding.apply {
            buttonBack.setOnClickListener {
                onBackPressed()
            }
            textCancel.setOnClickListener { finish() }
        }
    }

    override fun initData() {
        val post = intent.getSerializableExtra(Constant.INTENT_POST)
        if (post != null) createPostViewModel.setPost(post as Post)
        supportFragmentManager.beginTransaction()
            .add(binding.containerFrag.id, CPInfomationFragment())
            .commit()
        createPostViewModel.setState(1)
        loadingDialog = ProgressDialog(this)
        loadingDialog.setCancelable(false)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.fragments.size == 2) createPostViewModel.setState(1)
        if (supportFragmentManager.fragments.size == 1) finish()
        super.onBackPressed()
    }
}
