package com.vuforia.engine.native_sample

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
private var mTarget = 0
class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        mTarget = intent.getIntExtra("Target", 0)


        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val basePath = "/storage/emulated/0/Download"
        val models = listOf(
            ModelItem(
                "Model Cat",
                basePath+"/Cat13.obj",
                basePath+"/Cat.mtl",
                basePath+"/Cat_diffuse.jpg"
            ),
            ModelItem(
                "Model Machinery",
                basePath+"/Machinery2.obj",
                basePath+"/base.mtl",
                basePath+"/texture_diffuse.png"
            ),
            ModelItem(
                "Model Generator",
                basePath +"/Generator.obj",
                basePath +"/Generator.mtl",
                "/storage/emulated/0/Download/4K textures/Generator4K_color.png"
            )
           /* ModelItem(
                "Model Machinery1",
                basePath +"/Machinerybody1.obj",
                basePath +"/Bodie-3.mtl",
                basePath +"/Bodie-3_material_1.jpg"
            )*/
        )

        val adapter = ModelAdapter(models) { model ->
            val sharedPreferences: SharedPreferences = this.getSharedPreferences("ModelPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("model_obj_path", model.objPath)
            editor.putString("model_mtl_path", model.mtlPath)
            editor.putString("model_texture_path", model.texturePath)
            editor.apply()

            passModelDataToNative(model.objPath, model.mtlPath)

            val intent = Intent(this, VuforiaActivity::class.java)
            intent.putExtra("Target", mTarget)

            startActivity(intent)
            Toast.makeText(this, "Clicked on: ${model.name}", Toast.LENGTH_SHORT).show()
        }

        recyclerView.adapter = adapter
    }

    external fun passModelDataToNative(objPath: String, mtlPath: String)
}