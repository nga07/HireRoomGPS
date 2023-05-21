package com.example.finalapplication.screen.postdetail

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.example.finalapplication.R
import com.example.finalapplication.data.model.Post
import com.example.finalapplication.data.model.User
import com.example.finalapplication.data.model.Utility
import com.example.finalapplication.databinding.ActivityPostDetailBinding
import com.example.finalapplication.screen.chatroom.ChatRoomActivity
import com.example.finalapplication.utils.Constant
import com.example.finalapplication.utils.base.BaseActivity
import com.example.finalapplication.utils.loadImageByUrl
import com.example.finalapplication.utils.showMessage
import com.example.finalapplication.utils.toDateString
import org.koin.androidx.viewmodel.ext.android.viewModel

class PostDetailActivity :
    BaseActivity<ActivityPostDetailBinding>(ActivityPostDetailBinding::inflate) {

    private lateinit var post: Post
    private var user: User? = null
    private val sliderAdapter = SliderAdapter()
    private val utilityAdapter = PDUtilityAdapter()
    private val viewModel by viewModel<PostDetailViewModel>()
    private val postFavorite = mutableListOf<Post>()
    private lateinit var loading: ProgressDialog


    override fun bindData() {
        binding.apply {
            viewModel.user.observe(this@PostDetailActivity) { data ->
                user = data
                imageAvatar.loadImageByUrl(data.avatar, this@PostDetailActivity)
                textName.text = data.name
                textPhone.text = data.phoneNumber
            }
            viewModel.postFavorite.observe(this@PostDetailActivity) { data ->
                postFavorite.clear()
                postFavorite.addAll(data)
                if (isFavorite()) {
                    buttonFavorite.setImageResource(R.drawable.icon_favotite_24)
                } else buttonFavorite.setImageResource(R.drawable.ic_un_favorite)
            }
            viewModel.updateSuccess.observe(this@PostDetailActivity) { data ->
                if (data) {
                    "Đã cập nhật".showMessage(applicationContext)
                    finish()
                }
            }
            viewModel.isLoading.observe(this@PostDetailActivity) { data ->
                if (data) loading.show()
                else loading.dismiss()
            }
        }
    }

    override fun handleEvent() {
        binding.apply {
            buttonBack.setOnClickListener { finish() }

            buttonChat.setOnClickListener {
                val intent = Intent(this@PostDetailActivity, ChatRoomActivity::class.java)
                intent.putExtra(Constant.RECIVER, user?.id)
                startActivity(intent)
            }
            buttonCall.setOnClickListener {
                val intent = Intent(Intent.ACTION_CALL)
                val phoneNumber = user?.phoneNumber.toString()
                if (phoneNumber.isNullOrEmpty()) {
                    "Không thể thực hiện cuộc gọi với người dùng này".showMessage(this@PostDetailActivity)
                } else {
                    intent.data = Uri.parse("tel:$phoneNumber")
                    startActivity(intent)
                }
            }
            buttonFavorite.setOnClickListener {
                if (isFavorite()) {
                    viewModel.unFavoritePost(post)
                    buttonFavorite.setImageResource(R.drawable.ic_un_favorite)
                } else {
                    viewModel.addPostFavorite(post)
                    buttonFavorite.setImageResource(R.drawable.icon_favotite_24)
                }
            }
            buttonAccept.setOnClickListener {
                post.verifying = true
                viewModel.updatePost(post)
            }
            buttonVerified.setOnClickListener {
                post.verify = true
                viewModel.updatePost(post)
            }
            buttonDeny.setOnClickListener {
                post.verify = false
                viewModel.updatePost(post)
            }
        }
    }

    override fun initData() {
        loading = ProgressDialog(this@PostDetailActivity)
        loading.setCancelable(false)
        val type = intent.getIntExtra(Constant.INTENT_TYPE_USER, 0)
        binding.apply {
            when (type) {
                1 -> {
                    buttonAccept.isVisible = false
                    buttonDeny.isVisible = false
                    buttonVerified.isVisible = false
                    buttonChat.isVisible = true
                    buttonCall.isVisible = true
                }
                2 -> {
                    buttonFavorite.isVisible = false
                    buttonAccept.isVisible = true
                    buttonDeny.isVisible = true
                    buttonVerified.isVisible = false
                    buttonChat.isVisible = true
                    buttonCall.isVisible = false
                }
                3 -> {
                    buttonFavorite.isVisible = false
                    buttonAccept.isVisible = false
                    buttonDeny.isVisible = true
                    buttonVerified.isVisible = true
                    buttonChat.isVisible = true
                    buttonCall.isVisible = false
                }
                else -> {
                    buttonFavorite.isVisible = false
                    buttonAccept.isVisible = false
                    buttonDeny.isVisible = false
                    buttonVerified.isVisible = false
                    buttonChat.isVisible = false
                    buttonCall.isVisible = false
                    buttonFavorite.isVisible = false
                }
            }
            post = intent.getSerializableExtra(Constant.INTENT_POST) as Post
            post.images?.let { sliderAdapter.setData(it) }
            viewPager.adapter = sliderAdapter
            slider.setViewPager(viewPager)
            sliderAdapter.registerDataSetObserver(slider.dataSetObserver)
            textTitle.text = post.title
            textPrice.text = "Giá phòng : ${post.price / 1000}k"
            textAvailable.text = if (post.available) "Còn" else "Đã hết"
            textSpread.text = "${post.squad}m2"
            textDeposit.text = "${post.deposit / 1000}k"
            textType.text = post.postType?.toUpperCase()
            val utilities = mutableListOf<Utility>()
            for (item in post.utilities!!) {
                if (item.price == -1L) utilities.add(item.utility!!)
                when (item.utility?.id) {
                    15 -> {
                        textWaterCost.text = "${item.price?.div(1000)}K"
                    }
                    16 -> {
                        textElectricCost.text =
                            "${item.price?.div(1000)}.${item.price?.rem(1000)?.div(100)}K"
                    }
                    17 -> {
                        textInternetCost.text = "${item.price?.div(1000)}K"
                    }
                    18 -> {
                        containerParking.isVisible = true
                        textParkingCost.text = "${item.price?.div(1000)}K"
                    }
                }
            }
            utilityAdapter.setData(utilities)
            recyclerUtility.layoutManager = GridLayoutManager(this@PostDetailActivity, 2)
            recyclerUtility.adapter = utilityAdapter
            textCapacity.text = post.maxOfPeople.toString() + "người/phòng"
            textDescription.text = post.description
            textAddress.text = post.address.toString()
            when (post.requireGender) {
                0 -> textGender.text = "Tất cả"
                1 -> textGender.text = "Nam"
                2 -> textGender.text = "Nữ"
                else -> textGender.text = "Tất cả"
            }
            if (post.postType == Post.phongOGhep) {
                containerAvailable.isVisible = true
                textAvailablePerson.text = post.peopleAvailable.toString()
            } else containerAvailable.isVisible = false

            if (post.postType == Post.nhaChoThue) {
                containerNumberOfRoom.isVisible = true
                textNumberOfRoom.text = post.numberOfRoom.toString()
            } else containerNumberOfRoom.isVisible = false
            viewModel.getUser(post.uid.toString())
            textDate.text = post.time.toDateString()
        }
    }

    private fun isFavorite(): Boolean {
        for (item in postFavorite) {
            if (item.id == post.id) return true
        }
        return false
    }
}
