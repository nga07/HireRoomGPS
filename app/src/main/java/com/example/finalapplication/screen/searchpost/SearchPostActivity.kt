package com.example.finalapplication.screen.searchpost

import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.finalapplication.databinding.ActivitySearchPostBinding
import com.example.finalapplication.utils.Constant
import com.example.finalapplication.utils.ItemRecyclerViewListenner
import com.example.finalapplication.utils.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchPostActivity :
    BaseActivity<ActivitySearchPostBinding>(ActivitySearchPostBinding::inflate),
    ItemRecyclerViewListenner<String> {

    private val viewModel by viewModel<SearchPostViewModel>()
    private val adapter = HistoryAdapter(this)

    override fun bindData() {
        viewModel.dataSearch.observe(this) { data ->
            val list = mutableListOf<String>()
            list.addAll(data)
            val searchAdapter = ArrayAdapter(
                this,
                android.R.layout.simple_dropdown_item_1line,
                list
            )
            binding.textSearch.setAdapter(searchAdapter)
        }
        viewModel.history.observe(this){data ->
            adapter.setData(data)
        }
    }

    override fun handleEvent() {
        binding.apply {
            buttonFilter.setOnClickListener {
                supportFragmentManager.beginTransaction()
                    .add(binding.containerResultSearch.id, FilterSearchFragment())
                    .addToBackStack(null)
                    .commit()
                buttonFilter.visibility = View.INVISIBLE
                containerSearchview.visibility = View.INVISIBLE
            }
            buttonBack.setOnClickListener {
                onBackPressed()
            }
            buttonSearch.setOnClickListener {
                viewModel.clearData()
                buttonFilter.visibility = View.VISIBLE
                containerHistory.isVisible = false
                containerResultSearch.isVisible = true
                viewModel.addHistory(textSearch.text.toString())
                viewModel.setNewStatePage()
                viewModel.getResultSearch(textSearch.text.toString())
            }
            textSearch.setOnClickListener {
                buttonFilter.visibility = View.INVISIBLE
                containerHistory.isVisible = true
                containerResultSearch.isVisible = false
            }
            textSearch.onFocusChangeListener = View.OnFocusChangeListener { p0, focus ->
                if(focus){
                    containerHistory.isVisible = true
                    containerResultSearch.isVisible = false
                } else{
                    containerHistory.isVisible = false
                    containerResultSearch.isVisible = true
                }
            }
        }

    }

    override fun initData() {
        binding.buttonFilter.visibility = View.INVISIBLE
        viewModel.getHistory()
        supportFragmentManager.beginTransaction()
            .add(binding.containerResultSearch.id, ResultSearchFragment())
            .commit()
        val query = intent.getStringExtra(Constant.INTENT_QUERY)
        if (!query.isNullOrEmpty()) {
            binding.apply {
                (textSearch as TextView).text = query
                viewModel.setNewStatePage()
                viewModel.getResultSearch(textSearch.text.toString())
                textSearch.dismissDropDown()
                buttonFilter.visibility = View.VISIBLE
            }

        }
        binding.recyclerHistory.adapter = adapter
    }

    override fun onItemClick(item: String) {
        binding.apply {
            buttonFilter.visibility = View.VISIBLE
            containerHistory.isVisible = false
            containerResultSearch.isVisible = true
            (textSearch as TextView).text = item
            viewModel.setNewStatePage()
            viewModel.getResultSearch(textSearch.text.toString())
            textSearch.dismissDropDown()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        binding.buttonFilter.visibility = View.VISIBLE
        binding.containerSearchview.visibility = View.VISIBLE
    }

}
