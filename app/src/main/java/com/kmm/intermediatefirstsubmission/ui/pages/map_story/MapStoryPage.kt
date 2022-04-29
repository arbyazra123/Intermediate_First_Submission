package com.kmm.intermediatefirstsubmission.ui.pages.map_story

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.kmm.intermediatefirstsubmission.R
import com.kmm.intermediatefirstsubmission.data.core.StateHandler
import com.kmm.intermediatefirstsubmission.data.story.view_model.StoryViewModel
import com.kmm.intermediatefirstsubmission.databinding.FragmentMapStoryPageBinding
import com.kmm.intermediatefirstsubmission.utility.CommonFunction
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MapStoryPage : Fragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentMapStoryPageBinding
    private val storyViewModel by sharedViewModel<StoryViewModel>()
    private lateinit var mMap: GoogleMap
    private var bounds: ArrayList<LatLng> = arrayListOf()
    private var boundsBuilder = LatLngBounds.Builder()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapStoryPageBinding.inflate(layoutInflater)
        val mapFragment =
                childFragmentManager.findFragmentById(R.id.map_story_page) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        storyViewModel.getStories("1")
        return binding.root
    }


    override fun onMapReady(map: GoogleMap) {
        mMap = map
        mMap.uiSettings.apply {
            setAllGesturesEnabled(true)
            isZoomControlsEnabled = true
            isZoomGesturesEnabled = true
        }
        CommonFunction.setMapStyle(mMap, requireContext())

        storyViewModel.storyViewState.observe(viewLifecycleOwner) {

            when (it) {
                is StateHandler.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is StateHandler.Success -> {
//                    binding.swipe.isRefreshing = false
                    binding.progressBar.visibility = View.GONE
                    it.data?.listStory?.let { stories ->
                        stories.forEach { story ->
                            if (story.lat != null && story.lon != null) {
                                val location = LatLng(story.lat, story.lon)
                                mMap.addMarker(
                                    MarkerOptions()
                                        .position(location)
                                        .snippet(story.description)
                                        .title(
                                            story.name
                                        )
                                )
                                bounds.add(location)
                                boundsBuilder.include(location)
                            }


                        }
                        if (bounds.isNotEmpty()) {
                            val bounds: LatLngBounds = boundsBuilder.build()
                            mMap.setOnMapLoadedCallback {
                                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 64))
                            }

                        }

                    }

                }
                is StateHandler.Error -> {
//                    binding.swipe.isRefreshing = false
                    binding.progressBar.visibility = View.GONE

                }
                else -> {

                }
            }
        }
    }


}