package ihsinformatics.com.hydra_mobile.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import ihsinformatics.com.hydra_mobile.data.local.dao.*
import ihsinformatics.com.hydra_mobile.data.local.dao.workflow.*
import ihsinformatics.com.hydra_mobile.data.local.entities.*
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.*
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.PhaseComponentMap
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.WorkflowPhasesMap

/**
 * It represents the DB, it holds a connection to the actual SQLite DB.
 */

@Database(
    entities = [AppSetting::class, Patient::class, Person::class, User::class,Location::class,WorkFlow::class,
        Phases::class, Component::class, Forms::class, /*WorkFlowPhasesJoin::class ,*/ /*PhasesComponentJoin::class,*/ ComponentFormJoin::class,
        WorkflowPhasesMap::class, PhaseComponentMap::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appSettingDao(): AppSettingDao
    abstract fun getPatientDao(): PatientDao
    abstract fun getPersonDao(): PersonDao
    abstract fun getLocationDao(): LocationDao
    abstract fun getUserDao(): UserDao
    abstract fun getPhaseDao(): PhasesDao
    abstract fun getWorkFlowDao(): WorkFlowDao
    abstract fun getComponent(): ComponentDao
    abstract fun getForm(): FormDao
    abstract fun getComponentForm(): ComponentFormDao
    //abstract fun getPhaseComponentJoin(): PhaseComponentJoinDao
    //abstract fun getWorkFlowPhaseJoin(): WorkFlowPhaseJoinDao
    abstract fun getComponentFormJoin(): ComponentFormJoinDao
    abstract fun getWorkflowPhases(): WorkflowPhasesMapDao
    abstract fun getPhaseComponentMap(): PhaseComponentMapDao

    companion object {
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (instance == null) {
                synchronized(AppDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java, "hydra_database"
                    ).allowMainThreadQueries()
                        .fallbackToDestructiveMigration() // when version increments, it migrates (deletes db and creates new) - else it crashes
                        .build()
                }
            }
            return instance
        }

        fun destroyInstance() {
            instance = null
        }
    }

}