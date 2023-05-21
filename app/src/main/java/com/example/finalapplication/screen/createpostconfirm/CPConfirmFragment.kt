package com.example.finalapplication.screen.createpostconfirm

import android.app.TimePickerDialog
import android.location.Address
import android.location.Geocoder
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.finalapplication.databinding.FragmentCreatePostConfirmBinding
import com.example.finalapplication.screen.createpost.CreatePostViewModel
import com.example.finalapplication.utils.base.BaseFragment
import com.example.finalapplication.utils.getNewid
import com.google.android.gms.maps.model.LatLng
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CPConfirmFragment :
    BaseFragment<FragmentCreatePostConfirmBinding>(FragmentCreatePostConfirmBinding::inflate) {

    private val createPostViewModel by sharedViewModel<CreatePostViewModel>()
    override fun bindData() {
        // TODO("Not yet implemented")
    }

    override fun handleEvent() {
        binding.apply {
            buttonPost.setOnClickListener {
                val title = textTitle.text.toString()
                val description = textDescription.text.toString()
                val open = textOpenTime.text.toString()
                val close = textCloseTime.text.toString()
                errorTitle.isVisible = title.isNullOrEmpty()
                errorDescription.isVisible = description.isNullOrEmpty()
                if (title.isNullOrEmpty() || description.isNullOrEmpty()) return@setOnClickListener
                else {
                    val post = createPostViewModel.getPost()
                    if(post.id == null) post.id = getNewid().toString()
                    post.title = title
                    post.description = description
                    post.close = close
                    post.open = open
                    if (checkboxRequireVerify.isChecked) {
                        post.requestVerify = true
                        post.requestTime = System.currentTimeMillis()
                    }
                    val geocoder = Geocoder(context!!)
                    val list = geocoder.getFromLocationName(post.address.toString(), 1)
                    if (list!!.isNotEmpty()) {
                        post.address.latitude = list.first().latitude
                        post.address.longitude = list.first().longitude
                    }
                    if (buttonPost.text == "Update") createPostViewModel.upDatePost(post)
                    else createPostViewModel.createPost(post)
                }
            }
            textOpenTime.setOnClickListener {
                val dialog = TimePickerDialog(
                    context,
                    { p0, p1, p2 ->
                        if (p2 < 10) textOpenTime.text = "$p1:0$p2"
                        else textOpenTime.text = "$p1:$p2"
                    }, 0, 0, true
                )
                dialog.show()
            }
            textCloseTime.setOnClickListener {
                val dialog = TimePickerDialog(
                    context,
                    { p0, p1, p2 ->
                        if (p2 < 10) textCloseTime.text = "$p1:0$p2"
                        else textCloseTime.text = "$p1:$p2"
                    }, 0, 0, true
                )
                dialog.show()
            }
        }
    }

    override fun initData() {
        val post = createPostViewModel.getPost()
        if (post.title != null) {
            binding.apply {
                (textTitle as TextView).text = post.title.toString()
                (textDescription as TextView).text = post.description
                (textOpenTime as TextView).text = post.open
                textCloseTime.text = post.close
                buttonPost.text = "Update"
                textCloseTime.isFocusable = false
                textOpenTime.isFocusable = false
            }
        }
    }
}
