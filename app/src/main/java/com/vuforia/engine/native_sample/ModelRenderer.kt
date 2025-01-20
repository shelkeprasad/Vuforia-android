package com.vuforia.engine.native_sample

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class ModelRenderer(
    private val objPath: String,
    private val mtlPath: String,
    private val texturePath: String
) : GLSurfaceView.Renderer {

    init {
        System.loadLibrary("model_renderer")
    }

    external fun initRenderer(objPath: String, mtlPath: String, texturePath: String)
    external fun drawFrame()

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        initRenderer(objPath, mtlPath, texturePath)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        drawFrame()
    }
}
