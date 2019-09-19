package ihsinformatics.com.hydra_mobile.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import ihsinformatics.com.hydra_mobile.data.local.dao.*
import ihsinformatics.com.hydra_mobile.data.local.dao.workflow.*
import ihsinformatics.com.hydra_mobile.data.local.entities.*
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.*
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.WorkflowPhasesMap

/**
 * It represents the DB, it holds a connection to the actual SQLite DB.
 */

@Database(
    entities = [AppSetting::class, Patient::class, Person::class, Address::class, Concept::class,
        Encounter::class, Location::class, Obs::class, Order::class, Permission::class, User::class,
        Phases::class, Component::class, Forms::class, PhasesComponentJoin::class, ComponentFormJoin::class,
        WorkflowPhasesMap::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appSettingDao(): AppSettingDao
    abstract fun getPatientDao(): PatientDao
    abstract fun getPersonDao(): PersonDao
    abstract fun getAddressDao(): AddressDao
    abstract fun getConceptDao(): ConceptDao
    abstract fun getEncounterDao(): EncounterDao
    abstract fun getLocationDao(): LocationDao
    abstract fun getObsDao(): ObsDao
    abstract fun getOrderDao(): OrderDao
    abstract fun getPermissionDao(): PermissionDao
    abstract fun getUserDao(): UserDao
    abstract fun getPhaseDao(): PhasesDao
    abstract fun getPhaseComponent(): PhaseComponentDao
    abstract fun getComponent(): ComponentDao
    abstract fun getForm(): FormDao
    abstract fun getComponentForm(): ComponentFormDao
    abstract fun getPhaseComponentJoin(): PhaseComponentJoinDao
    abstract fun getComponentFormJoin(): ComponentFormJoinDao
    abstract fun getWorkflowPhases(): WorkflowPhasesMapDao


    companion object {
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (instance == null) {
                synchronized(AppDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java, "hydra_database"
                    )
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