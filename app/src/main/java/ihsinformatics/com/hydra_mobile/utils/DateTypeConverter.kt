package ihsinformatics.com.hydra_mobile.utils

import androidx.room.TypeConverter
import java.sql.Date

/**
 * Type Converters are used when we declare a property which
 * Room and SQL don’t know how to serialize
 */
class DateTypeConverter {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}