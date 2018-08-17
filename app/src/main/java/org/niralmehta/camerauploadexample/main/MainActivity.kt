package org.niralmehta.camerauploadexample.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.toolbar_main.*
import org.niralmehta.camerauploadexample.R
import org.niralmehta.camerauploadexample.screens.ScreenCamera
import org.niralmehta.camerauploadexample.utils.interfaces.OnBackPressedListener
import org.niralmehta.camerauploadexample.utils.view.snack
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>
    private var doubleBackToExitPressedOnce: Boolean = false
    private lateinit var mainView: View


    override fun supportFragmentInjector(): AndroidInjector<Fragment> = dispatchingAndroidInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupToolbar()

        val permission = ContextCompat
            .checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        mainView = (this.findViewById(R.id.content) as ViewGroup).getChildAt(0)

        if (permission != PackageManager.PERMISSION_GRANTED) requestPermissions() else setupScreen()
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            val fragments = supportFragmentManager.fragments
            fragments.filter { it is OnBackPressedListener }
                .map {
                    (it as OnBackPressedListener).onBackPressed()
                }
            finish()
            return
        }

        this.doubleBackToExitPressedOnce = true
        mainView.snack("Press back twice to exit")

        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            ACCESS_WRITE_EXTERNAL_STORAGE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    setupScreen()
                }
            }
        }
    }

    private fun requestPermissions() {
        // Here, thisActivity is the current activity

        val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE

        if (ContextCompat.checkSelfPermission(
                this,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                ACCESS_WRITE_EXTERNAL_STORAGE
            )
        }
    }

    private fun setupToolbar() = setSupportActionBar(toolbar_main)

    private fun setupScreen() = supportFragmentManager.beginTransaction()
        .replace(R.id.container_main, ScreenCamera(), ScreenCamera.SCREEN_TAG)
        .commit()


    companion object {
        const val ACCESS_WRITE_EXTERNAL_STORAGE = 1
    }
}
