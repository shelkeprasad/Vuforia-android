package com.vuforia.engine.native_sample

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ModelItem(
    val name: String,
    val objPath: String,
    val mtlPath: String,
    val texturePath: String
) : Parcelable

