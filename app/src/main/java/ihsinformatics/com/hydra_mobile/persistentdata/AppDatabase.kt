package ihsinformatics.com.hydra_mobile.persistentdata

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import ihsinformatics.com.hydra_mobile.persistentdata.dao.AppSettingDao
import ihsinformatics.com.hydra_mobile.persistentdata.entities.AppSetting

/**
 * It represents the DB, it holds a connection to the actual SQLite DB.
 */

@Database(entities = [AppSetting::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appSettingDao(): AppSettingDao

    companion object {
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (instance == null) {
                synchronized(AppDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java, "hydra_database"
                    ).build()
                }
            }
            return instance
        }

        fun destroyInstance() {
            instance = null
        }
    }

}