package com.kmm.intermediatefirstsubmission.ui.pages.landing.story

import android.content.res.Resources
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.kmm.intermediatefirstsubmission.R
import com.kmm.intermediatefirstsubmission.data.story.model.ListStoryResponseItem

class StoryLocation(private val story: ListStoryResponseItem) : Fragment() {
    private lateinit var mMap: GoogleMap
    private val callback = OnMapReadyCallback {
        mMap = it
        setMapStyle()
        if (story.lat != null && story.lon != null) {
            moveToStoryLocation(story)
        }
    }

    private fun moveToStoryLocation(story: ListStoryResponseItem) {
        val currentLocation = LatLng(story.lat!!, story.lon!!)
        mMap.addMarker(MarkerOptions().position(currentLocation).title(story.name))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_story_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }


    private fun setMapStyle() {
        try {
            val success =
                    mMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                            requireContext(),
                            R.raw.map_style
                        )
                    )
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }

    companion object {
        private val TAG = StoryLocation::class.java.simpleName
    }
}