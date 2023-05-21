package com.example.finalapplication.screen.createpostaddress

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.finalapplication.data.model.Address
import com.example.finalapplication.data.model.District
import com.example.finalapplication.data.model.Province
import com.example.finalapplication.data.model.Ward
import com.example.finalapplication.databinding.FragmentCreatePostAddressBinding
import com.example.finalapplication.screen.createpost.CreatePostViewModel
import com.example.finalapplication.utils.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CPAddressFragment :
    BaseFragment<FragmentCreatePostAddressBinding>(FragmentCreatePostAddressBinding::inflate){

    private val createPostViewModel by sharedViewModel<CreatePostViewModel>()
    private val addressViewModel by viewModel<CPAddressViewModel>()
    private val provinces = mutableListOf<Province>()
    private val districts = mutableListOf<District>()
    private val wards = mutableListOf<Ward>()

    override fun bindData() {
        addressViewModel.provinces.observe(this) { data ->
            provinces.clear()
            provinces.addAll(data)
            val names = mutableListOf<String>()
            for (i in data) {
                names.add(i.name.toString())
            }
            val cityAdapter =
                context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, names) }
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
                context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, names) }
            binding.spinnerDistrisct.adapter = districtAdapter
        }

        addressViewModel.ward.observe(this) { data ->
            wards.clear()
            wards.addAll(data)
            val names = mutableListOf<String>()
            for (i in data) {
                names.add(i.name.toString())
            }
            val wardAdapter =
                context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, names) }
            binding.spinnerWards.adapter = wardAdapter
        }
        addressViewModel.street.observe(this) { data ->
            binding.errorStreet.isVisible = !data
            if (!data) binding.edittextStreet.requestFocus()
        }
        addressViewModel.apartmentNumber.observe(this) { data ->
            binding.errorNumber.isVisible = !data
            if (!data) binding.edittextHouseNumber.requestFocus()
        }
    }

    override fun handleEvent() {
        binding.apply {
            buttonNext.setOnClickListener {
                val post = createPostViewModel.getPost()
                val city = provinces[spinnerCity.selectedItemPosition].name.toString()
                val district = districts[spinnerDistrisct.selectedItemPosition].name.toString()
                val ward = wards[spinnerWards.selectedItemPosition].name.toString()
                val street = edittextStreet.text.toString()
                val apartmentNumber = edittextHouseNumber.text.toString()
                val address = Address(city, district, ward, street, apartmentNumber)
                if (addressViewModel.validateInput(address)) {
                    post.address = address
                    createPostViewModel.setPost(post)
                    createPostViewModel.setState(3)
                }
            }
            spinnerCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    addressViewModel.getDistricts(provinces[p2])
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    // TODO("Not yet implemented")
                }
            }
            spinnerDistrisct.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    addressViewModel.getWards(districts[p2])
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    // TODO("Not yet implemented")
                }
            }
        }
    }

    override fun initData() {
        addressViewModel.getProvinces()
        val post = createPostViewModel.getPost()
        binding.apply {
            if(post.id == null) return
            (edittextStreet as TextView).text = post.address.street.toString()
            (edittextHouseNumber as TextView).text = post.address.apartmentNumber.toString()
        }
    }
}
