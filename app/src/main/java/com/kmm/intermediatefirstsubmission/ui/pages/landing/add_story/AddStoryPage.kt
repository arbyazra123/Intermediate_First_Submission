package com.kmm.intermediatefirstsubmission.ui.pages.landing.add_story

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.kmm.intermediatefirstsubmission.R
import com.kmm.intermediatefirstsubmission.data.core.StateHandler
import com.kmm.intermediatefirstsubmission.data.story.view_model.StoryViewModel
import com.kmm.intermediatefirstsubmission.databinding.FragmentAddStoryPageBinding
import com.kmm.intermediatefirstsubmission.utility.CommonFunction
import com.kmm.intermediatefirstsubmission.utility.createCustomTempFile
import com.kmm.intermediatefirstsubmission.utility.reduceFileImage
import com.kmm.intermediatefirstsubmission.utility.uriToFile
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.io.File

class AddStoryPage : Fragment(), View.OnClickListener, OnMapReadyCallback {
    private lateinit var binding: FragmentAddStoryPageBinding
    private var photo: File? = null
    private val storyViewModel by sharedViewModel<StoryViewModel>()
    private lateinit var mMap: GoogleMap
    private var myLocation: Location? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activity?.closeOptionsMenu()
        binding = FragmentAddStoryPageBinding.inflate(layoutInflater)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        val mapManager =
                childFragmentManager.findFragmentById(R.id.map_add_story) as SupportMapFragment?

        mapManager?.getMapAsync(this)
        askPermission()
        binding.btnCamera.setOnClickListener {
            startTakePhoto()
        }
        binding.storyImageView.clipToOutline = true
        binding.btnGallery.setOnClickListener {
            startGallery()

        }
        binding.btnPost.setOnClickListener(this)

        storyViewModel.storyPostState.observe(viewLifecycleOwner) {

            when (it) {
                is StateHandler.Loading -> {
                    showLoading()
                }
                is StateHandler.Success -> {
                    hideLoading()
                    CommonFunction.showSnackBar(
                        binding.root,
                        requireContext(),
                        getString(R.string.upload_success),
                    )
                    storyViewModel.getStories("1")
                    findNavController().popBackStack()
                    storyViewModel.resetPostStory()

                }
                is StateHandler.Error -> {
                    hideLoading()
                    CommonFunction.showSnackBar(
                        binding.root,
                        requireContext(),
                        message = getString(R.string.upload_failed) + it.message + "!",
                        error = true
                    )


                }
                else -> {

                }
            }
        }
        return binding.root
    }

    override fun onMapReady(map: GoogleMap) {
        mMap = map
        CommonFunction.setMapStyle(mMap, requireContext())
        getMyLocation()
        mMap.uiSettings.apply {
            binding.addMapFrame?.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.rounded_outline)
            binding.addMapFrame?.clipToOutline = true
            setAllGesturesEnabled(true)
            isZoomControlsEnabled = true
        }
        if (mMap.isMyLocationEnabled) {
            mMap.setOnMyLocationClickListener {
                myLocation = it
                mMap.addMarker(
                    MarkerOptions().position(LatLng(it.latitude, it.longitude)).title("My Location")
                )
            }
        }
    }

    private val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    getMyLocation()
                }
            }

    @SuppressLint("MissingPermission")
    private fun getMyLocation() {
        if (!CommonFunction.allPermissionsGranted(requireContext())) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            mMap.isMyLocationEnabled = true
            fusedLocationClient.lastLocation.addOnSuccessListener {
                myLocation = it
                val latLng = LatLng(it.latitude, it.longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
                mMap.addMarker(MarkerOptions().position(latLng))
            }
        }
    }

    private fun askPermission() {
        if (!CommonFunction.allPermissionsGranted(requireContext())) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                CommonFunction.REQUIRED_PERMISSIONS,
                CommonFunction.REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnPost.isEnabled = false
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
        binding.btnPost.isEnabled = true
    }

    private lateinit var photoURI: Uri
    private lateinit var currentPhotoPath: String
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            photo = File(currentPhotoPath)
            photo?.let { photo ->
                val result = BitmapFactory.decodeFile(photo.path)

                binding.storyImageView.setImageBitmap(result)
                binding.storyImageView.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.rounded_outline)
                binding.storyImageView.clipToOutline = true
            }


        } else {
            CommonFunction.showSnackBar(
                binding.root,
                requireContext(),
                getString(R.string.failed_to_get_image),
                true
            )
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            binding.storyImageView.setImageURI(selectedImg)
            binding.storyImageView.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.rounded_outline)
            binding.storyImageView.clipToOutline = true
            photo = uriToFile(selectedImg, requireContext())
        } else {
            CommonFunction.showSnackBar(
                binding.root,
                requireContext(),
                getString(R.string.failed_to_get_image),
                true
            )
        }
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(requireActivity().packageManager)
        createCustomTempFile(requireContext()).also {
            photoURI = FileProvider.getUriForFile(
                requireContext(),
                getString(R.string.package_name),
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }


    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.choose_picture))
        launcherIntentGallery.launch(chooser)
    }

    override fun onClick(p0: View?) {
        if (photo == null) {
            CommonFunction.showSnackBar(
                binding.root,
                requireContext(),
                getString(R.string.photo_empty_warning),
                true
            )
            return
        }
        photo?.let {
            try {
                reduceFileImage(it).let { reduced ->
                    if (binding.etDescription.text?.isEmpty() == true) {
                        binding.etDescription.error = getString(R.string.description_empty_warning)
                        return
                    }

                    storyViewModel.postStory(
                        binding.etDescription.text.toString(),
                        reduced,
                        myLocation
                    )
                }
            } catch (e: Exception) {
                CommonFunction.showSnackBar(
                    binding.root,
                    requireContext(),
                    getString(R.string.file_failed_to_convert),
                    true
                )
            }

        }

    }


}