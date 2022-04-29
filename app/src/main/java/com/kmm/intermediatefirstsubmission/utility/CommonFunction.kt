package com.kmm.intermediatefirstsubmission.utility

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.material.snackbar.Snackbar
import com.kmm.intermediatefirstsubmission.R
import com.kmm.intermediatefirstsubmission.ui.pages.map_story.MapStoryPage

class CommonFunction {
    companion object {
        val REQUIRED_PERMISSIONS =
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
        const val REQUEST_CODE_PERMISSIONS = 10
        fun showSnackBar(
            view: View,
            context: Context,
            message: String,
            error: Boolean? = null
        ) {
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(
                    ContextCompat.getColor(
                        context,
                        R.color.gray_dark
                    )
                )
                .setTextColor(
                    ContextCompat.getColor(
                        context,
                        if (error == true) R.color.pink_light else R.color.cream
                    )
                )
                .show()
        }


        fun allPermissionsGranted(context: Context) = REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

        fun setMapStyle(map: GoogleMap, context: Context) {
            try {
                val success =
                        map.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                context,
                                R.raw.map_style
                            )
                        )
                if (!success) {
                    Log.e(context.applicationContext.javaClass.simpleName, "Style parsing failed.")
                }
            } catch (exception: Resources.NotFoundException) {
                Log.e(
                    context.applicationContext.javaClass.simpleName,
                    "Can't find style. Error: ",
                    exception
                )
            }
        }

    }
}