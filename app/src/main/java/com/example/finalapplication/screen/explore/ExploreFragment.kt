package com.example.finalapplication.screen.explore

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Geocoder
import android.location.Location
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.finalapplication.R
import com.example.finalapplication.data.model.Post
import com.example.finalapplication.databinding.FragmentExploreBinding
import com.example.finalapplication.screen.postdetail.PostDetailActivity
import com.example.finalapplication.utils.Constant
import com.example.finalapplication.utils.ItemRecyclerViewListenner
import com.example.finalapplication.utils.base.BaseFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.koin.androidx.viewmodel.ext.android.viewModel

class ExploreFragment : BaseFragment<FragmentExploreBinding>(FragmentExploreBinding::inflate),
    OnMapReadyCallback, ItemRecyclerViewListenner<Post> {

    private var currentLocation: Location? = null
    private var markLocation: LatLng? = null
    private var currentMaker: Marker? = null
    private var fusedLocation: FusedLocationProviderClient? = null
    private var mapFragment: SupportMapFragment? = null
    private var mMap: GoogleMap? = null
    private val viewModel by viewModel<ExploreViewModel>()
    private val adapter = VicintyPostAdapter(this)
    private val postsVicinty = mutableListOf<Pair<Post, Double>>()

    @SuppressLint("PotentialBehaviorOverride")
    override fun bindData() {
        viewModel.apply {
            isLoading.observe(this@ExploreFragment) { data ->
                binding.containerLoading.isVisible = data
            }
            postVicinty.observe(this@ExploreFragment) { data ->
                postsVicinty.clear()
                postsVicinty.addAll(data)
                mMap?.clear()
                mMap?.addMarker(MarkerOptions().position(markLocation!!).title("Mark Location"))
                mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(markLocation!!, 13f))
                if (data.isEmpty()) binding.containerNoData.isVisible = true
                else {
                    binding.containerNoData.isVisible = false
                    adapter.setData(data)
                    for (post in data) {
                        val lat = LatLng(
                            post.first.address.latitude!!,
                            post.first.address.longitude!!
                        )
                        mMap?.addMarker(MarkerOptions().position(lat).icon(context?.let {
                            bitmapDescriptorFromVector(
                                it, R.drawable.ic_location_home
                            )
                        }).title(post.first.address.toString()))
                    }
                    mMap?.setOnMarkerClickListener(@SuppressLint("PotentialBehaviorOverride")
                    object : GoogleMap.OnMarkerClickListener{
                        override fun onMarkerClick(p0: Marker): Boolean {
                            val lat = p0.position
                            for(i in 0 until postsVicinty.size){
                                val address = postsVicinty[i].first.address
                                if(address.latitude == lat.latitude && address.longitude == lat.longitude){
                                    binding.recyclerRoom.scrollToPosition(i)
                                    Log.v("aaaaa", "scroll to $i")
                                    return false
                                }
                            }
                            return false
                        }

                    })
                }
            }
        }

    }

    override fun handleEvent() {
        binding.buttonMyLocation.setOnClickListener {
            val task = if (ActivityCompat.checkSelfPermission(
                    requireActivity().applicationContext,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                    requireActivity().applicationContext,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                fusedLocation?.lastLocation
            } else {
                return@setOnClickListener
            }
            task?.addOnSuccessListener { location ->
                if(location==null) return@addOnSuccessListener
                this.currentLocation = location
                val current = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
                mMap?.addMarker(
                    MarkerOptions().position(current).title("My Location")
                        .icon(context?.let { it1 ->
                            bitmapDescriptorFromVector(
                                it1, R.drawable.ic_dot_24
                            )
                        })
                )

                mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 13f))
            }
        }
        binding.buttonSearch.setOnClickListener {
            val geocoder = Geocoder(context!!)
            val address =
                geocoder.getFromLocation(markLocation!!.latitude, markLocation!!.longitude, 1)
            binding.textSearch.isVisible = true
            val ad = address?.first()
            binding.textSearch.text =
                "Kết quả tìm kiếm của : ${ad?.getAddressLine(0)?.replace(", Vietnam", "")}"
            currentMaker?.remove()
            currentMaker =
                mMap?.addMarker(MarkerOptions().position(markLocation!!).title("Mark Location"))
            viewModel.findPostVicinty(markLocation!!)
        }
    }

    override fun initData() {
        viewModel
        mapFragment =
            childFragmentManager.findFragmentById(binding.containerMap.id) as SupportMapFragment
        mapFragment?.getMapAsync(this)
        fusedLocation = context?.let { LocationServices.getFusedLocationProviderClient(it) }
        binding.recyclerRoom.adapter = adapter
    }

    override fun onMapReady(mMap: GoogleMap) {
        markLocation = LatLng(
            21.028511, 105.8
        )
        this.mMap = mMap
        currentMaker = mMap.addMarker(
            MarkerOptions()
                .position(markLocation!!)
                .title("Hà Nội")
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markLocation!!, 13f))
        mMap.setOnCameraIdleListener { markLocation = mMap.cameraPosition.target }
    }

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap =
                Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }

    override fun onItemClick(item: Post) {
        val intent = Intent(context, PostDetailActivity::class.java)
        intent.putExtra(Constant.INTENT_POST, item)
        intent.putExtra(Constant.INTENT_TYPE_USER, 1)
        startActivity(intent)
    }
}
