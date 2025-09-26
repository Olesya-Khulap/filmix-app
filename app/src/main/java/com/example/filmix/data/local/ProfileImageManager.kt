package com.example.filmix.data.local

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import com.google.firebase.auth.FirebaseAuth
import java.io.ByteArrayOutputStream
import java.io.InputStream

object ProfileImageManager {
    private const val PREFS_NAME = "filmix_profile_images"
    private const val KEY_PROFILE_IMAGE = "profile_image"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    private fun getCurrentUserId(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }

    private fun getUserKey(): String {
        val userId = getCurrentUserId() ?: "anonymous"
        return "${userId}_$KEY_PROFILE_IMAGE"
    }

    fun saveProfileImage(context: Context, imageUri: Uri): Boolean {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)
            val originalBitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            // resizing for better quality
            val resizedBitmap = resizeBitmapHighQuality(originalBitmap, 400, 400)

            // Saving as PNG for better quality
            val base64String = bitmapToBase64HighQuality(resizedBitmap)

            val prefs = getPrefs(context)
            prefs.edit().putString(getUserKey(), base64String).apply()

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun loadProfileImage(context: Context): Bitmap? {
        return try {
            val prefs = getPrefs(context)
            val base64String = prefs.getString(getUserKey(), null) ?: return null
            base64ToBitmap(base64String)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun clearProfileImage(context: Context) {
        val prefs = getPrefs(context)
        prefs.edit().remove(getUserKey()).apply()
    }

    fun hasProfileImage(context: Context): Boolean {
        val prefs = getPrefs(context)
        return prefs.contains(getUserKey())
    }

    // High-quality compression to PNG
    private fun bitmapToBase64HighQuality(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun base64ToBitmap(base64String: String): Bitmap {
        val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }

    private fun resizeBitmapHighQuality(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        val ratioBitmap = width.toFloat() / height.toFloat()
        val ratioMax = maxWidth.toFloat() / maxHeight.toFloat()

        var finalWidth = maxWidth
        var finalHeight = maxHeight

        if (ratioMax > ratioBitmap) {
            finalWidth = (maxHeight.toFloat() * ratioBitmap).toInt()
        } else {
            finalHeight = (maxWidth.toFloat() / ratioBitmap).toInt()
        }

        return Bitmap.createScaledBitmap(bitmap, finalWidth, finalHeight, true)
    }
}
