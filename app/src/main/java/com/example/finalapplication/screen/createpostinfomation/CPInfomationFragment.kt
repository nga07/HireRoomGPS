package com.example.finalapplication.screen.createpostinfomation

import android.widget.TextView
import androidx.core.view.isVisible
import com.example.finalapplication.data.model.Post
import com.example.finalapplication.data.model.UtilityPost
import com.example.finalapplication.databinding.FragmentCreatePostInformationBinding
import com.example.finalapplication.screen.createpost.CreatePostViewModel
import com.example.finalapplication.utils.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.w3c.dom.Text

class CPInfomationFragment :
    BaseFragment<FragmentCreatePostInformationBinding>(FragmentCreatePostInformationBinding::inflate) {

    private val createPostViewModel by sharedViewModel<CreatePostViewModel>()
    private val informationViewModel by viewModel<CPInformationViewModel>()

    override fun bindData() {
        binding.apply {
            informationViewModel.price.observe(this@CPInfomationFragment) { data ->
                errorPrice.isVisible = !data
                if (!data)
                    edittextCost.requestFocus()
            }
            informationViewModel.deposit.observe(this@CPInfomationFragment) { data ->
                errorDeposit.isVisible = !data
                if (!data)
                    edittextDeposit.requestFocus()
            }
            informationViewModel.squad.observe(this@CPInfomationFragment) { data ->
                errorSquad.isVisible = !data
                if (!data)
                    edittextSpread.requestFocus()
            }
            informationViewModel.personPerRoom.observe(this@CPInfomationFragment) { data ->
                errorCapacity.isVisible = !data
                if (!data)
                    edittextCapacity.requestFocus()
            }
            informationViewModel.numberOfRoom.observe(this@CPInfomationFragment) { data ->
                errorRoom.isVisible = !data
                if (!data)
                    edittextNumberOfRoom.requestFocus()
            }
            informationViewModel.available.observe(this@CPInfomationFragment) { data ->
                errorAvailable.isVisible = !data
                if (!data)
                    edittextAvailable.requestFocus()
            }
        }
    }

    override fun handleEvent() {
        binding.apply {
            buttonNext.setOnClickListener {
                val post = createPostViewModel.getPost()
                if (radioNha.isChecked) post.postType = Post.nhaChoThue
                if (radioPhongOGhep.isChecked) post.postType = Post.phongOGhep
                if (radioPhongChoThue.isChecked) post.postType = Post.phongChoThue
                post.squad = edittextSpread.text.toString().toInt()
                post.deposit = edittextDeposit.text.toString().toLong()
                post.maxOfPeople = edittextCapacity.text.toString().toInt()
                post.numberOfRoom = edittextNumberOfRoom.text.toString().toInt()
                post.peopleAvailable = edittextAvailable.text.toString().toInt()
                post.price = edittextCost.text.toString().toLong()
                if (radioAll.isChecked) post.requireGender = 0
                if (radioFemale.isChecked) post.requireGender = 2
                if (radioMale.isChecked) post.requireGender = 1
                val water = UtilityPost()
                water.price = edittextWaterCost.text.toString().toLong()
                water.utility?.name = "Nước"
                water.utility?.id = 15
                val electric = UtilityPost()
                electric.price = edittextElectricCost.text.toString().toLong()
                electric.utility?.name = "Điện"
                electric.utility?.id = 16
                val internet = UtilityPost()
                internet.price = edittextInternetCost.text.toString().toLong()
                internet.utility?.name = "Mạng Internet/Truyền hình cáp"
                internet?.utility?.id = 17
                val listUtis = mutableListOf<UtilityPost>()
                if (post.utilities != null) {
                    for (item in post.utilities!!) {
                        if (item.utility?.id != 15 && item.utility?.id != 16 && item.utility?.id != 17 && item.utility?.id != 18)
                            listUtis.add(item)
                    }
                }

                listUtis.addAll(listOf(water, electric, internet))
                if (checkboxParking.isChecked) {
                    val parking = UtilityPost()
                    parking.price = edittextParkingCost.text.toString().toLong()
                    parking.utility?.name = "Chỗ để xe"
                    parking.utility?.id = 18
                    listUtis.add(parking)
                }
                post.utilities = listUtis
                if (informationViewModel.validateInput(post)) {
                    createPostViewModel.setPost(post)
                    createPostViewModel.setState(2)
                }
            }
            radioNha.setOnClickListener {
                containerAvailable.isVisible = false
                containerNumberOfRoom.isVisible = true
            }
            radioPhongOGhep.setOnClickListener {
                containerNumberOfRoom.isVisible = false
                containerAvailable.isVisible = true
            }
            radioPhongChoThue.setOnClickListener {
                containerNumberOfRoom.isVisible = false
                containerAvailable.isVisible = false
            }
            checkboxParking.setOnClickListener {
                containerParkingCost.isVisible = checkboxParking.isChecked
            }
        }
    }

    override fun initData() {
        val post = createPostViewModel.getPost()
        if (post.postType != null) {
            binding.apply {
                when (post.postType) {
                    Post.phongChoThue -> {
                        radioPhongChoThue.isChecked = true
                        containerNumberOfRoom.isVisible = false
                        containerAvailable.isVisible = false
                    }
                    Post.phongOGhep -> {
                        radioPhongOGhep.isChecked = true
                        containerNumberOfRoom.isVisible = false
                        containerAvailable.isVisible = true
                    }
                    else -> {
                        radioNha.isChecked = true
                        containerAvailable.isVisible = false
                        containerNumberOfRoom.isVisible = true
                    }
                }
                (edittextAvailable as TextView).text = post.peopleAvailable.toString()
                (edittextCapacity as TextView).text = post.maxOfPeople.toString()
                (edittextNumberOfRoom as TextView).text = post.numberOfRoom.toString()
                when (post.requireGender) {
                    0 -> radioAll.isChecked = true
                    1 -> radioMale.isChecked = true
                    else -> radioFemale.isChecked = true
                }

                (edittextSpread as TextView).text = post.squad.toString()
                (edittextCost as TextView).text = post.price.toString()
                (edittextDeposit as TextView).text = post.deposit.toString()
                if (post.utilities != null) {
                    for (item in post.utilities!!) {
                        if (item.utility?.id == 15) {
                            (edittextWaterCost as TextView).text = item.price.toString()
                        } else if (item.utility?.id == 16) {
                            (edittextElectricCost as TextView).text = item.price.toString()
                        } else if (item.utility?.id == 17) {
                            (edittextInternetCost as TextView).text = item.price.toString()
                        } else if (item.utility?.id == 18) {
                            checkboxParking.isChecked
                        }
                    }
                }
            }
        }
    }
}
