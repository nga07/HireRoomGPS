package com.example.finalapplication.screen.mypost

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AlertDialog
import com.example.finalapplication.R
import com.example.finalapplication.data.model.Post
import com.example.finalapplication.data.model.User
import com.example.finalapplication.databinding.ActivityMyPostBinding
import com.example.finalapplication.databinding.BottomSheetBinding
import com.example.finalapplication.databinding.FragmentStatusPostBinding
import com.example.finalapplication.screen.createpost.CreatePostActivity
import com.example.finalapplication.screen.createpostsearch.CreatePostSearchActivity
import com.example.finalapplication.screen.detailpostsearch.PostSearchDetailActivity
import com.example.finalapplication.screen.postdetail.PostDetailActivity
import com.example.finalapplication.utils.Constant
import com.example.finalapplication.utils.ItemOptionClickListenner
import com.example.finalapplication.utils.base.BaseActivity
import com.example.finalapplication.utils.showMessage
import org.koin.androidx.viewmodel.ext.android.viewModel


class MyPostActivity : BaseActivity<ActivityMyPostBinding>(ActivityMyPostBinding::inflate),
    ItemOptionClickListenner {

    private val viewModel by viewModel<MyPostViewModel>()

    private val adapter = MyPostAdapter(this)
    private var currentUser: User? = null

    override fun bindData() {

        viewModel.apply {
            myPosts.observe(this@MyPostActivity) { data ->
                adapter.setData(data)
            }
            user.observe(this@MyPostActivity) { data ->
                adapter.setUser(data)
                currentUser = data
            }
            errorMessage.observe(this@MyPostActivity) { data ->
                Log.v("error", data)
                data.showMessage(this@MyPostActivity)
            }
        }

    }

    override fun handleEvent() {

    }

    override fun initData() {
        binding.apply {
            recyclerMyPost.adapter = adapter
        }
    }

    override fun onClick(item: Any) {
        item as Post
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val bindingDialog = BottomSheetBinding.inflate(layoutInflater)
        dialog.setContentView(bindingDialog.root)
        dialog.show()
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.attributes?.windowAnimations = R.style.BottonSheetAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        bindingDialog.apply {
            containerDelete.setOnClickListener {
                dialog.hide()

                val builder: AlertDialog.Builder = AlertDialog.Builder(this@MyPostActivity)
                builder.setMessage("Bạn có chắc chắn muốn xoá bài đăng này?")
                    .setTitle("Attention")
                    .setPositiveButton(
                        "Xoá"
                    ) { dialog, _ ->
                        viewModel.deletePost(item)
                        dialog.cancel()
                    }
                    .setNegativeButton(
                        "Huỷ bỏ"
                    ) { dialog, _ ->
                        dialog.cancel()
                    }
                builder.create().show()
            }
            containerVerify.setOnClickListener {
                dialog.hide()
                if (item.verify == true) {
                    "Bài đăng đã được xác thực".showMessage(
                        this@MyPostActivity
                    )
                    return@setOnClickListener
                }
                if (item.verify == false) {
                    "Bài đăng này đã bị từ chối xác thực do không liên lạc được với " +
                            "chủ bài viết hoặc thông tin bài viết chưa đúng với thực tế".showMessage(
                                this@MyPostActivity
                            )
                    return@setOnClickListener
                }
                if (item.postType == Post.timPhongO) {
                    "Không thể yêu cầu xác thực bài viết đối với các bài đăng tìm phòng".showMessage(
                        this@MyPostActivity
                    )
                    return@setOnClickListener
                }
                if (item.requestVerify) {
                    "Bài đăng này đã gửi yêu cầu xác thực".showMessage(
                        this@MyPostActivity
                    )
                    return@setOnClickListener
                }

                val builder: AlertDialog.Builder = AlertDialog.Builder(this@MyPostActivity)
                builder.setMessage("Bạn có chắc chắn muốn gửi yêu cầu xác thực?")
                    .setTitle("Attention")
                    .setPositiveButton(
                        "Gủi"
                    ) { dialog, _ ->
                        viewModel.requestVerify(item)
                        dialog.cancel()
                    }
                    .setNegativeButton(
                        "Huỷ bỏ"
                    ) { dialog, _ ->
                        dialog.cancel()
                    }
                builder.create().show()
            }
            containerStatus.setOnClickListener {
                dialog.hide()
                val dialog = Dialog(this@MyPostActivity)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                val bindingDialog = FragmentStatusPostBinding.inflate(layoutInflater)
                dialog.setContentView(bindingDialog.root)
                dialog.show()
                dialog.window?.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                dialog.window?.attributes?.windowAnimations = R.style.BottonSheetAnimation
                dialog.window?.setGravity(Gravity.BOTTOM)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                bindingDialog.apply {
                    if (item.available) radioAvailable.isChecked = true
                    else radioUnAvailable.isChecked = true
                    butonSave.setOnClickListener {
                        dialog.hide()
                        if (radioAvailable.isChecked && item.available) return@setOnClickListener
                        if (radioUnAvailable.isChecked && !item.available) return@setOnClickListener
                        if (radioAvailable.isChecked) {
                            item.available = true
                            item.timeHired = System.currentTimeMillis()
                            viewModel.changeActivityPost(item)
                            return@setOnClickListener
                        }
                        if (radioUnAvailable.isChecked) {
                            item.available = false
                            viewModel.changeActivityPost(item)
                            return@setOnClickListener
                        }
                    }
                    buttonCancel.setOnClickListener {
                        dialog.cancel()
                    }
                }
            }

            containerEdit.setOnClickListener {
                if (item.verify == true) {
                    "Không thể sửa đổi bài viết đã được xác thực".showMessage(this@MyPostActivity)
                } else {
                    dialog.hide()
                    if (item.postType == Post.timPhongO) {
                        val intent =
                            Intent(this@MyPostActivity, CreatePostSearchActivity::class.java)
                        intent.putExtra(Constant.INTENT_POST, item)
                        startActivity(intent)
                    } else {
                        val intent = Intent(this@MyPostActivity, CreatePostActivity::class.java)
                        intent.putExtra(Constant.INTENT_POST, item)
                        startActivity(intent)
                    }
                }
            }
            containerView.setOnClickListener {
                dialog.hide()
                if (item.postType == Post.timPhongO) {
                    val intent = Intent(this@MyPostActivity, PostSearchDetailActivity::class.java)
                    intent.putExtra(Constant.INTENT_POST, item)
                    intent.putExtra(Constant.INTENT_USER, currentUser)
                    startActivity(intent)

                } else {
                    val intent = Intent(this@MyPostActivity, PostDetailActivity::class.java)
                    intent.putExtra(Constant.INTENT_POST, item)
                    startActivity(intent)
                }
            }
        }
    }
}
