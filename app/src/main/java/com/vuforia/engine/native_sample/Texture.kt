/*===============================================================================
Copyright (c) 2020 PTC Inc. All Rights Reserved.

Vuforia is a trademark of PTC Inc., registered in the United States and other
countries.
===============================================================================*/

package com.vuforia.engine.native_sample

import android.content.res.AssetManager
import android.graphics.BitmapFactory
import android.util.Log
import java.io.BufferedInputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder


/**
 * Utility class to read image data to be loaded into a texture.
 */
class Texture {

    /// The width of the texture.
    var width = 0
    /// The height of the texture.
    var height = 0
    /// The number of channels e.g. 4 for RGBA
    var channels = 4
    /// The pixel data.
    var data: ByteBuffer? = null


    companion object {

        @JvmStatic
        fun loadTextureFromApk(
            fileName: String,
            assets: AssetManager
        ): Texture? {
            val inputStream: InputStream
            return try {
                inputStream = assets.open(fileName, AssetManager.ACCESS_BUFFER)
                val bufferedStream = BufferedInputStream(
                    inputStream
                )
                val bitMap = BitmapFactory.decodeStream(bufferedStream)
                val data = IntArray(bitMap.width * bitMap.height)
                bitMap.getPixels(
                    data, 0, bitMap.width, 0, 0,
                    bitMap.width, bitMap.height
                )
                loadTextureFromIntBuffer(
                    data, bitMap.width,
                    bitMap.height
                )
            } catch (e: IOException) {
                Log.e("Texture", "Failed to load texture '$fileName' from APK")
                Log.d("Texture", e.message.toString())
                null
            }
        }


        @JvmStatic
        private fun loadTextureFromIntBuffer(
            data: IntArray, width: Int,
            height: Int
        ): Texture? {
            // Convert:
            val numPixels = width * height
            val dataBytes = ByteArray(numPixels * 4)
            for (p in 0 until numPixels) {
                val colour = data[p]
                dataBytes[p * 4] = (colour ushr 16).toByte() // R
                dataBytes[p * 4 + 1] = (colour ushr 8).toByte() // G
                dataBytes[p * 4 + 2] = colour.toByte() // B
                dataBytes[p * 4 + 3] = (colour ushr 24).toByte() // A
            }
            val texture: Texture = Texture()
            texture.width = width
            texture.height = height
            texture.data = ByteBuffer.allocateDirect(dataBytes.size).order(
                ByteOrder.nativeOrder()
            )
            val rowSize: Int = texture.width * texture.channels
            for (r in 0 until texture.height) texture.data?.put(
                dataBytes, rowSize * (texture.height - 1 - r),
                rowSize
            )
            texture.data?.rewind()
            return texture
        }

        /// new add


   /*     @JvmStatic
        fun loadTextureFromPath(
            filePath: String
        ): Texture? {
            return try {
                val file = File(filePath)
                if (!file.exists()) {
                    Log.e("Texture", "File does not exist at path: $filePath")
                    return null
                }

                val bitMap = BitmapFactory.decodeFile(filePath)
                if (bitMap == null) {
                    Log.e("Texture", "Failed to decode bitmap from file: $filePath")
                    return null
                }

                val data = IntArray(bitMap.width * bitMap.height)
                bitMap.getPixels(
                    data, 0, bitMap.width, 0, 0,
                    bitMap.width, bitMap.height
                )
                loadTextureFromIntBuffer(
                    data, bitMap.width,
                    bitMap.height
                )
            } catch (e: Exception) {
                Log.e("Texture", "Failed to load texture from file: $filePath")
                Log.d("Texture", e.message.toString())
                null
            }
        }

        @JvmStatic
        fun loadTextureFromApkOrPath(
            fileNameOrPath: String
        ): Texture? {
            return if (File(fileNameOrPath).exists()) {
                loadTextureFromPath(fileNameOrPath)
            }else {
                Log.e("Texture", "Invalid file name or path: $fileNameOrPath")
                null
            }
        }*/


        // decrease size

        @JvmStatic
        fun loadTextureFromPath(filePath: String): Texture? {
            return try {
                val file = File(filePath)
                if (!file.exists()) {
                    Log.e("Texture", "File does not exist at path: $filePath")
                    return null
                }

                // Step 1: Get image dimensions without loading full bitmap
                val options = BitmapFactory.Options().apply {
                    inJustDecodeBounds = true
                }
                BitmapFactory.decodeFile(filePath, options)

                // Step 2: Calculate inSampleSize to scale down the bitmap
                options.inSampleSize = calculateInSampleSize(options, 1024, 1024) // Target size
                options.inJustDecodeBounds = false

                // Step 3: Load the scaled-down bitmap
                val bitMap = BitmapFactory.decodeFile(filePath, options)
                if (bitMap == null) {
                    Log.e("Texture", "Failed to decode bitmap from file: $filePath")
                    return null
                }

                // Step 4: Convert the bitmap to a pixel array
                val data = IntArray(bitMap.width * bitMap.height)
                bitMap.getPixels(data, 0, bitMap.width, 0, 0, bitMap.width, bitMap.height)

                // Step 5: Create the texture
                loadTextureFromIntBuffer(data, bitMap.width, bitMap.height)
            } catch (e: Exception) {
                Log.e("Texture", "Failed to load texture from file: $filePath")
                Log.d("Texture", e.message.toString())
                null
            }
        }

        @JvmStatic
        fun loadTextureFromApkOrPath(fileNameOrPath: String): Texture? {
            return if (File(fileNameOrPath).exists()) {
                loadTextureFromPath(fileNameOrPath)
            } else {
                Log.e("Texture", "Invalid file name or path: $fileNameOrPath")
                null
            }
        }

        /**
         * Calculate inSampleSize to scale down the image
         */
        private fun calculateInSampleSize(
            options: BitmapFactory.Options,
            reqWidth: Int,
            reqHeight: Int
        ): Int {
            val (height: Int, width: Int) = options.run { outHeight to outWidth }
            var inSampleSize = 1

            if (height > reqHeight || width > reqWidth) {
                val halfHeight = height / 2
                val halfWidth = width / 2

                while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                    inSampleSize *= 2
                }
            }
            return inSampleSize
        }


    }
}
