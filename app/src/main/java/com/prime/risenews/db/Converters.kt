package com.prime.risenews.db

import androidx.room.TypeConverter
import com.prime.risenews.models.Source

class Converters {

    @TypeConverter
    fun fromSource(source : Source) : String  = source.name

    @TypeConverter
    fun toSource(name:String) : Source = Source(name, name)
}