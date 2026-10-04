package com.example.data.local

import androidx.room.TypeConverter
import com.example.data.model.SentimentCategory

class Converters {
    @TypeConverter
    fun fromSentimentCategory(value: SentimentCategory): String = value.name

    @TypeConverter
    fun toSentimentCategory(value: String): SentimentCategory {
        return try {
            SentimentCategory.valueOf(value)
        } catch (e: Exception) {
            SentimentCategory.HARMONIOUS
        }
    }
}
