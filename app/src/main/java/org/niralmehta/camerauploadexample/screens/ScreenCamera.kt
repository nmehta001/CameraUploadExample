package org.niralmehta.camerauploadexample.screens

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fab_menu.*
import kotlinx.android.synthetic.main.screen_camera.*
import org.niralmehta.camerauploadexample.R
import org.niralmehta.camerauploadexample.di.Injectable
import org.niralmehta.camerauploadexample.modules.upload.fragment.ScreenUpload
import org.niralmehta.camerauploadexample.utils.interfaces.OnBackPressedListener
import org.niralmehta.camerauploadexample.utils.view.*


class ScreenCamera : Fragment(), Injectable, OnBackPressedListener {

    private lateinit var cameraView: View
    private var photo: Bitmap? = null
    private var isFabOpen: Boolean = false
    private lateinit var uri: Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        cameraView = inflater.inflate(R.layout.screen_camera, container, false)
        return cameraView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)



        fab.setOnClickListener {
            setupFabMenu()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
                photo = MediaStore.Images.Media.getBitmap(context!!.contentResolver, uri)
                val roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(resources, photo)
                roundedBitmapDrawable.cornerRadius = 6f

                if (photo != null) {
                    camera_image.visibility = View.VISIBLE
                    camera_container_information.visibility = View.VISIBLE
                }

                camera_image.setImageDrawable(roundedBitmapDrawable)
                camera_details_height.text = "Height: ${photo?.height.toString()}px"
                camera_details_width.text = "Width: ${photo?.width.toString()}px"
            }
        }
    }

    private fun setupFabMenu() {
        fab1.setImageDrawable(getResDrawable(R.drawable.ic_camera))
        fab2.setImageDrawable(getResDrawable(R.drawable.ic_upload))

        fab1.setOnClickListener { launchCamera() }
        fab2.setOnClickListener { uploadImage() }

        if (isFabOpen) fabOpen() else fabClosed()
    }

    private fun launchCamera() {
        val contentValues = ContentValues()
        uri = context!!.contentResolver
            .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues) as Uri

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            .putExtra(MediaStore.EXTRA_OUTPUT, uri)

        this.startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
        isFabOpen = false
    }

    private fun uploadImage() {
        if (photo != null) {
            val uploadScreen = ScreenUpload()
            photo?.let { uploadScreen.photo = it }
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.container_main, uploadScreen, ScreenUpload.SCREEN_TAG)
                ?.addToBackStack(null)
                ?.commit()
        } else {
            view?.snack("Please take a photo first")
        }
    }

    private fun fabOpen() {
        context?.let {
            fab.animate(rotateBackwards(it))
            fab1.animate(fabClose(it))
            fab1.isClickable = true
            fab2.animate(fabClose(it))
            fab2.isClickable = false
            isFabOpen = false
        }
    }

    private fun fabClosed() {
        context?.let {
            fab.animate(rotateForwards(it))
            fab1.animate(fabOpen(it))
            fab1.isClickable = true
            fab2.animate(fabOpen(it))
            fab2.isClickable = true
            isFabOpen = true
        }
    }

    override fun onBackPressed() {
    }

    companion object {
        const val SCREEN_TAG = "screen_camera"
        const val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1888
    }
}