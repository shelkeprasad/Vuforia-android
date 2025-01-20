/*===============================================================================
Copyright (c) 2024 PTC Inc. and/or Its Subsidiary Companies. All Rights Reserved.

Vuforia is a trademark of PTC Inc., registered in the United States and other
countries.
===============================================================================*/

package com.vuforia.engine.native_sample

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.vuforia.engine.native_sample.databinding.ActivityMainBinding
import java.io.File
import android.Manifest


/**
 * The MainActivity presents a simple choice for the user to select Image Targets or Model Targets.
 */
class MainActivity : AppCompatActivity() {
    private val REQUEST_CODE_READ_EXTERNAL_STORAGE = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

           if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
         != PackageManager.PERMISSION_GRANTED
     ) {
         ActivityCompat.requestPermissions(
             this,
             arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
             REQUEST_CODE_READ_EXTERNAL_STORAGE
         )
         Toast.makeText(this, "Permission granted ", Toast.LENGTH_SHORT).show()
     } else {
         Toast.makeText(this, "Permission already granted ", Toast.LENGTH_SHORT).show()
     }

     val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "finalimage.jpg")
     val filePath = file.absolutePath
     Log.d("FilePath", "Image path: $filePath")
    }
      override fun onRequestPermissionsResult(
           requestCode: Int,
           permissions: Array<out String>,
           grantResults: IntArray
       ) {
           super.onRequestPermissionsResult(requestCode, permissions, grantResults)

           if (requestCode == REQUEST_CODE_READ_EXTERNAL_STORAGE) {
               if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                   Toast.makeText(this, "Permission granted ", Toast.LENGTH_SHORT).show()
               } else {
                   Toast.makeText(this, "Permission denied. Unable to access external storage.", Toast.LENGTH_SHORT).show()
               }
           }
       }

    fun goToActivity(view: View) {
        if (view.id == binding.btnImageTarget.id || view.id == binding.btnModelTarget.id) {

            val intent = Intent(
                this@MainActivity,
                MainActivity2::class.java
            )
           /* if (view.id == binding.btnImageTarget.id) {
                intent.putExtra("Target", VuforiaActivity.getImageTargetId())
            } else {
                intent.putExtra("Target", VuforiaActivity.getModelTargetId())
            }*/

            if (view.id == binding.btnImageTarget.id) {
                intent.putExtra("Target", VuforiaActivity.getImageTargetId())
            } else {
                intent.putExtra("Target", VuforiaActivity.getModelTargetId())
            }

            startActivity(intent)
        }
    }


    companion object {

        // Used to load the 'VuforiaSample' library on application startup.
        init {
            System.loadLibrary("VuforiaSample")
        }
    }

    private lateinit var binding: ActivityMainBinding
}
