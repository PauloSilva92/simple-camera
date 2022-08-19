package com.example.simplecamera.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.simplecamera.R

class PictureFragment : Fragment(R.layout.fragment_picture) {


    private val pictureFragmentArgs: PictureFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pictureView = view.findViewById<AppCompatImageView>(R.id.picture)
        pictureView.setImageURI(pictureFragmentArgs.uri)
    }
}