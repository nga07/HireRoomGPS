package com.example.finalapplication.screen.searchpost

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.finalapplication.data.model.District
import com.example.finalapplication.data.model.HistorySearch
import com.example.finalapplication.data.model.Post
import com.example.finalapplication.data.model.Province
import com.example.finalapplication.data.model.Utility
import com.example.finalapplication.data.model.Ward
import com.example.finalapplication.data.repository.AddressRepository
import com.example.finalapplication.data.repository.HistoryRepository
import com.example.finalapplication.data.repository.PostRepository
import com.example.finalapplication.data.repository.resource.Listenner
import com.example.finalapplication.utils.Constant
import com.example.finalapplication.utils.base.BaseViewModel


class SearchPostViewModel(
    val historyRepository: HistoryRepository,
    val postRepository: PostRepository,
    val addressRepository: AddressRepository,
    val sharedPreferences: SharedPreferences
) : BaseViewModel() {
    private val _history = MutableLiveData<List<String>>()
    val history: LiveData<List<String>>
        get() = _history
    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>>
        get() = _posts
    private val _hideFilter = MutableLiveData<Boolean>(false)
    val hideFilter: LiveData<Boolean>
        get() = _hideFilter
    private val _dataSearch = MutableLiveData<Set<String>>()
    val dataSearch: LiveData<Set<String>>
        get() = _dataSearch
    var filterPost = mutableListOf<Post>()
    val storePosts = mutableListOf<Post>()
    private val _isPageEnd = MutableLiveData<Boolean>(false)
    val isPageEnd: LiveData<Boolean>
        get() = _isPageEnd
    var query = ""
    var min = ""
    var max = ""
    var type = 0
    var person = ""
    var sortType = 0
    var utility = mutableListOf<Utility>()

    init {
        val listDataSearch = sharedPreferences.getStringSet(Constant.DATA_SEARCH, mutableSetOf())
        if (listDataSearch.isNullOrEmpty()) {
            launchTask<Any>(onRequest = {
                addressRepository.getProvinces(object : Listenner<List<Province>> {
                    override fun onSuccess(province: List<Province>) {
                        listDataSearch?.add(province[0].name.toString())
                        addressRepository.getDistricts(
                            province[0],
                            object : Listenner<List<District>> {
                                override fun onSuccess(districts: List<District>) {
                                    for (district in districts) {
                                        listDataSearch?.add("${district.name}, ${province[0].name}")
                                        addressRepository.getWards(
                                            district,
                                            object : Listenner<List<Ward>> {
                                                override fun onSuccess(wards: List<Ward>) {
                                                    for (ward in wards) {
                                                        listDataSearch?.add("${ward.name}, ${district.name}, ${province[0].name}")
                                                        if (ward == wards.last() && district == districts.last()) {
                                                            val editer = sharedPreferences.edit()
                                                            editer.putStringSet(
                                                                Constant.DATA_SEARCH,
                                                                listDataSearch
                                                            )
                                                            editer.commit()
                                                            _dataSearch.postValue(listDataSearch)
                                                        }
                                                    }
                                                }

                                                override fun onError(msg: String) {
                                                    message.value = msg
                                                }

                                            })
                                    }
                                }

                                override fun onError(msg: String) {
                                    message.value = msg
                                }

                            })
                    }

                    override fun onError(msg: String) {
                        message.value = msg
                    }

                })
            }, isShowLoading = false)
        } else
            _dataSearch.postValue(listDataSearch)
    }

    fun clearData() {
        query = ""
        min = ""
        max = ""
        type = 0
        person = ""
        sortType = 0
        utility = mutableListOf<Utility>()
    }

    fun filter(
        min: String,
        max: String,
        type: Int,
        person: String,
        sortType: Int,
        utility: List<Utility>
    ) {
        this.min = min
        this.max = max
        this.type = type
        this.person = person
        this.sortType = sortType
        this.utility.clear()
        this.utility.addAll(utility)
        _hideFilter.value = true
        startFilter()
    }

    private fun startFilter() {
        filterPost = storePosts
        when (type) {
            1 -> filterPost = filterType(Post.nhaChoThue)
            2 -> filterPost = filterType(Post.phongChoThue)
            3 -> filterPost = filterType(Post.phongOGhep)
            else -> {}
        }
        if (!min.isNullOrEmpty() && !max.isNullOrEmpty()) filterPost =
            filterPrice(min.toLong(), max.toLong())
        if (!person.isNullOrEmpty()) filterPost = filterPerson(person.toInt())
        if (utility.isNotEmpty()) filterPost = filterUtility()
        when (sortType) {
            0 -> {
                filterPost.sortByDescending { it.time }
            }
            1 -> {
                filterPost.sortBy { it.price }
            }
            2 -> {
                filterPost.sortByDescending { it.price }
            }
        }
        _posts.postValue(filterPost)
    }

    private fun filterUtility(): MutableList<Post> {
        val list = mutableListOf<Post>()
        for (post in filterPost) {
            if (checkUtility(post)) list.add(post)
        }
        return list
    }

    private fun checkUtility(post: Post): Boolean {
        for (uti in utility) {
            var count = 0
            for (item in post.utilities!!) {
                if (item.utility?.id == uti.id) count = 1
            }

            if (count == 0) {
                return false
            }
        }
        return true
    }

    private fun filterPrice(min: Long, max: Long): MutableList<Post> {
        val list = mutableListOf<Post>()
        for (post in filterPost) {
            if (post.price in min..max) list.add(post)
        }
        return list
    }

    private fun filterPerson(num: Int): MutableList<Post> {
        val list = mutableListOf<Post>()
        for (post in filterPost) {
            if (post.postType == Post.phongChoThue && post.maxOfPeople == num) list.add(post)
            if (post.postType == Post.nhaChoThue && post.maxOfPeople * post.numberOfRoom == num) list.add(
                post
            )
            if (post.postType == Post.phongOGhep && post.peopleAvailable == num) list.add(post)
        }
        return list
    }

    private fun filterType(type: String): MutableList<Post> {
        val list = mutableListOf<Post>()
        for (post in filterPost) {
            if (post.postType == type) list.add(post)
        }
        return list
    }

    fun setNewStatePage() {
        _isPageEnd.value = false
        filterPost.clear()
        storePosts.clear()
    }

    fun getResultSearch(query: String) {
        this.query = query
        val datas = query.split(",")
        var province = ""
        var district = ""
        var ward = ""
        if (_dataSearch.value?.contains(query) == true) {
            when (datas.size) {
                1 -> {
                    province = datas.first()
                }
                2 -> {
                    district = datas.first()
                    province = datas.last()
                }
                3 -> {
                    ward = datas.first()
                    district = datas[1]
                    province = datas.last()
                }
            }
            var lastIndex = Long.MAX_VALUE
            if (storePosts.isNotEmpty()) lastIndex = storePosts.last().time
            launchTask<Any>(onRequest = {
                postRepository.getResultSearch(
                    lastIndex,
                    ward,
                    district,
                    province,
                    object : Listenner<List<Post>> {
                        override fun onSuccess(data: List<Post>) {
                            storePosts.addAll(data)
                            startFilter()
                            if (data.size < 20) _isPageEnd.value = true
                            hideLoading(true)
                        }

                        override fun onError(msg: String) {
                            message.value = msg
                            Log.v("aaaaaa", msg)
                            hideLoading(true)
                        }

                    })
            }, isShowLoading = true)

        } else _posts.postValue(mutableListOf())
    }

    fun addHistory(query: String) {
        launchTask<Any>(onRequest = {
            historyRepository.addHistory(query)
        }, isShowLoading = false)
    }

    fun getHistory() {
        launchTask<Any>(onRequest = {
            historyRepository.getHistory(object : Listenner<List<HistorySearch>> {
                override fun onSuccess(data: List<HistorySearch>) {
                    val list = mutableListOf<String>()
                    for (item in data) {
                        list.add(item.query.toString())
                    }
                    _history.value = list
                }

                override fun onError(msg: String) {
                    Log.v("aaaaa", msg)
                }

            })
        })
    }

}
