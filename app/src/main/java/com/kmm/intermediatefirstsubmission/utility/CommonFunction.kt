package com.kmm.intermediatefirstsubmission.utility

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.kmm.intermediatefirstsubmission.R

class CommonFunction {
    companion object {
        val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
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
    }
}