package com.example.photo_management_app

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AllImages : AppCompatActivity() {

    private val REQUEST_IMAGE_CAPTURE: Int=1
    private val REQUEST_IMAGE_Permission: Int=101

    lateinit var imagePaths : ArrayList<Image>
    lateinit var recyclerView: RecyclerView
    lateinit var myAdapter : MyAdapter

    
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_images)

        recyclerView = findViewById(R.id.recyclerView)
        imagePaths = ArrayList<Image>()

        myAdapter = MyAdapter(this, imagePaths)
        recyclerView.adapter = myAdapter
        recyclerView.layoutManager = GridLayoutManager(this, 4)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_IMAGES
                ) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.READ_MEDIA_IMAGES,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                    ), REQUEST_IMAGE_Permission
                );

            }
            else {
                getallimages()
            }
        }
        else{
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA

                    ), REQUEST_IMAGE_Permission
                );

            }
            else {
                getallimages()
            }
        }

        myAdapter = MyAdapter(this, imagePaths)
        recyclerView.adapter = myAdapter
        recyclerView.layoutManager = GridLayoutManager(this, 4)
        
    }

    private fun getallimages() {
        val projection = arrayOf(MediaStore.Images.Media._ID)
        val selection = "${MediaStore.Images.Media.DATA} like ? "
        val selectionArgs = arrayOf("%/storage/emulated/0/%")

        val cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, null)
        imagePaths.clear()
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                imagePaths.add(Image(id))
            }
            cursor.close()
        }
        if(imagePaths.isEmpty()){
            Toast.makeText(this,"there no image please take image",Toast.LENGTH_LONG).show()
        }
        else{
            imagePaths.reverse()
            myAdapter.submitList(imagePaths)
        }
    }
    private fun dispatchTakePictureIntent() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent,REQUEST_IMAGE_CAPTURE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "${SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())}")
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
            }
            val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            val outputStream = contentResolver.openOutputStream(uri!!)
            outputStream?.use {
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            }
            getallimages()
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==101){
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getallimages()
            }
            else{
                Toast.makeText(this,"cant load images",Toast.LENGTH_LONG).show()
            }
        }
    }
}