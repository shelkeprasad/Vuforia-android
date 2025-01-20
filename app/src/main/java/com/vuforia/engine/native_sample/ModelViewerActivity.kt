package com.vuforia.engine.native_sample

import android.opengl.GLSurfaceView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import java.io.File
import java.nio.ByteBuffer

class ModelViewerActivity : AppCompatActivity() {
    private lateinit var glSurfaceView: GLSurfaceView
    private var renderer: ModelRenderer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_model_viewer)
        glSurfaceView = findViewById(R.id.gl_surface_view)
        val model: ModelItem? = intent.getParcelableExtra("model_data")

        if (model != null) {
            val name = model.name
            Toast.makeText(this, "Loaded: $name", Toast.LENGTH_SHORT).show()
           // loadAndRenderModel(objPath, mtlPath, texturePath)

            renderer = ModelRenderer(model.objPath, model.mtlPath, model.texturePath)

        } else {
            Toast.makeText(this, "Failed to load model", Toast.LENGTH_SHORT).show()
            finish()
        }

        glSurfaceView.setEGLContextClientVersion(3) // OpenGL ES 3.0
        glSurfaceView.setRenderer(renderer)
    }



    override fun onPause() {
        super.onPause()
        glSurfaceView.onPause()
    }

    override fun onResume() {
        super.onResume()
        glSurfaceView.onResume()
    }






    private fun loadAndRenderModel(objPath: String, mtlPath: String, texturePath: String) {
        // Load the texture
        val texture = Texture.loadTextureFromApkOrPath(texturePath)
        if (texture != null) {
            setTextures(
                texture.width, texture.height, texture.data!!,
                texture.width, texture.height, texture.data!!
            )
        } else {
            Log.e("ModelViewer", "Failed to load texture: $texturePath")
        }
    }



    private external fun initRendering()

    private external fun setTextures(astronautWidth: Int, astronautHeight: Int, astronautBytes: ByteBuffer,
                                     landerWidth: Int, landerHeight: Int, landerBytes: ByteBuffer)


}