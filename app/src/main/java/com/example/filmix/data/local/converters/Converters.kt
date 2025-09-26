package com.example.filmix.data.local.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        return if (value == null) null else {
            val listType = object : TypeToken<List<String>>() {}.type
            Gson().fromJson(value, listType)
        }
    }

    @TypeConverter
    fun fromIntList(value: List<Int>?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toIntList(value: String?): List<Int>? {
        return if (value == null) null else {
            val listType = object : TypeToken<List<Int>>() {}.type
            Gson().fromJson(value, listType)
        }
    }
}
