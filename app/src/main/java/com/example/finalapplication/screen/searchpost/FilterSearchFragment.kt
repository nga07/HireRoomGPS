package com.example.finalapplication.screen.searchpost

import android.R
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.finalapplication.data.model.Post
import com.example.finalapplication.data.model.Utility
import com.example.finalapplication.data.model.UtilityPost
import com.example.finalapplication.databinding.FragmentFilterSearchBinding
import com.example.finalapplication.screen.createpostultility.UtilityAdapter
import com.example.finalapplication.utils.Constant
import com.example.finalapplication.utils.ItemRecyclerViewListenner
import com.example.finalapplication.utils.base.BaseFragment
import com.example.finalapplication.utils.getUtilities
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FilterSearchFragment :
    BaseFragment<FragmentFilterSearchBinding>(FragmentFilterSearchBinding::inflate),
    ItemRecyclerViewListenner<Utility> {

    private val utilityAdapter = UtilityAdapter(this)
    private val utilities = mutableListOf<Utility>()
    private val types =
        mutableListOf(Constant.SP_ALL, Post.nhaChoThue, Post.phongChoThue, Post.phongOGhep)
    private val sorts = mutableListOf(
        Constant.SP_NEW,
        Constant.SP_PRICE_INCREMENT,
        Constant.SP_PRICE_DECREMENT
    )
    private val viewModel by sharedViewModel<SearchPostViewModel>()

    override fun bindData() {

    }

    override fun handleEvent() {
        binding.apply {
            buttonSubmit.setOnClickListener {
                clickSubmit()
            }
        }
    }

    private fun clickSubmit() {
        binding.apply {
            val minPrice = textMinPrice.text.toString()
            val maxPrice = textMaxPrice.text.toString()
            val type = spinnerTypeRoom.selectedItemPosition
            val person = textPerson.text.toString()
            val sortType = spinnerSort.selectedItemPosition
            viewModel.filter(minPrice, maxPrice, type, person, sortType, utilities)
            activity?.onBackPressed()
        }
    }

    override fun initData() {

        binding.apply {
            recyclerUtility.adapter = utilityAdapter
            recyclerUtility.layoutManager = GridLayoutManager(context, 2)
            utilityAdapter.setData(getUtilities())
            val typesAdapter =
                context?.let { ArrayAdapter(it, R.layout.simple_spinner_item, types) }
            spinnerTypeRoom.adapter = typesAdapter
            val sortsAdapter =
                context?.let { ArrayAdapter(it, R.layout.simple_spinner_item, sorts) }
            spinnerSort.adapter = sortsAdapter
            (textMinPrice as TextView).text = viewModel.min
            (textMaxPrice as TextView).text = viewModel.max
            (textPerson as TextView).text = viewModel.person
            spinnerTypeRoom.setSelection(viewModel.type)
            spinnerSort.setSelection(viewModel.sortType)
            utilityAdapter.addData(viewModel.utility)
            utilities.addAll(viewModel.utility)
        }
    }

    override fun onItemClick(item: Utility) {
        if(utilities.filter { uti -> uti.id == item.id }.isEmpty()) utilities.add(item)
        else utilities.removeIf { uti -> uti.id == item.id }
    }
}
