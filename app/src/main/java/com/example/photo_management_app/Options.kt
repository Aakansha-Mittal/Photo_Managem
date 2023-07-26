package com.example.photo_management_app

import android.Manifest.permission.CAMERA
import android.Manifest.permission_group.CAMERA
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.SensorPrivacyManager.Sensors.CAMERA
import android.media.MediaRecorder.VideoSource.CAMERA
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.Manifest
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class Options : AppCompatActivity() {

    var cameraRequest = 1888

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options)

        val camera = findViewById<Button>(R.id.camera)
        val allImg = findViewById<Button>(R.id.allImg)

        camera.setOnClickListener {
            if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED)
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), cameraRequest)
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, cameraRequest)
        }

        allImg.setOnClickListener {
            val intent = Intent(this, allImg::class.java)
            startActivity(intent)
        }
    }
}