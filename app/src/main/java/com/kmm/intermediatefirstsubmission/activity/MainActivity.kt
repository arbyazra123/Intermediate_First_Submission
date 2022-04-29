package com.kmm.intermediatefirstsubmission.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.navigation.findNavController
import com.kmm.intermediatefirstsubmission.R
import com.kmm.intermediatefirstsubmission.data.auth.viewmodel.SessionViewModel
import com.kmm.intermediatefirstsubmission.utility.CommonFunction
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val sessionViewModel by viewModel<SessionViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        sessionViewModel.getRealtimeToken().observe(this) {
            findNavController(R.id.fragmentContainerView).currentDestination?.let { nav ->
                if (nav.id == R.id.splash) {
                    if (it.isEmpty()) {
                        supportActionBar?.hide()
                        findNavController(R.id.fragmentContainerView).navigate(R.id.action_splash_to_loginPage)
                    } else {
                        supportActionBar?.show()
                        findNavController(R.id.fragmentContainerView).navigate(R.id.action_splash_to_storyPage)

                    }

                } else {
                    if (it.isEmpty()) {
                        supportActionBar?.hide()
                        findNavController(R.id.fragmentContainerView).navigate(R.id.action_storyPage_to_loginPage)
                    } else {
                        if (nav.id != R.id.addStoryPage) {
                            supportActionBar?.show()
                            findNavController(R.id.fragmentContainerView).navigate(R.id.action_loginPage_to_storyPage)
                        }
                    }
                }


            }

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CommonFunction.REQUEST_CODE_PERMISSIONS) {

            if (!CommonFunction.allPermissionsGranted(applicationContext)) {
                Toast.makeText(
                    findNavController(R.id.fragmentContainerView).context,
                    getString(R.string.failed_to_get_permissions),
                    Toast.LENGTH_LONG
                ).show()
                findNavController(R.id.fragmentContainerView).popBackStack()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_logout -> {

                findNavController(R.id.fragmentContainerView).currentDestination?.let {
                    when (it.id) {
                        R.id.addStoryPage -> {
                            item.isVisible = false
                            findNavController(R.id.fragmentContainerView).popBackStack()
                        }
                        R.id.mapStoryPage -> {
                            findNavController(R.id.fragmentContainerView).popBackStack()
                        }
                        else -> {}
                    }
                }
                sessionViewModel.removeToken()
            }
            R.id.show_as_map -> {
                item.isVisible = false
                findNavController(R.id.fragmentContainerView).navigate(R.id.action_storyPage_to_mapStoryPage)

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        menu.findItem(R.id.show_as_map).isVisible = true
        menu.findItem(R.id.menu_logout).isVisible = true
        return super.onCreateOptionsMenu(menu)
    }

}