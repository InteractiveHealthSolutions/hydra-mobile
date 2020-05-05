package ihsinformatics.com.hydra_mobile.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ihsinformatics.com.hydra_mobile.data.local.dao.*
import ihsinformatics.com.hydra_mobile.data.local.dao.commonLab.LabTestTypeDao
import ihsinformatics.com.hydra_mobile.data.local.dao.workflow.*
import ihsinformatics.com.hydra_mobile.data.local.entities.AppSetting
import ihsinformatics.com.hydra_mobile.data.local.entities.Location
import ihsinformatics.com.hydra_mobile.data.local.entities.Person
import ihsinformatics.com.hydra_mobile.data.local.entities.User
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.*
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.LabTestAllType
import ihsinformatics.com.hydra_mobile.data.remote.model.patient.Patient
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.FormResultApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.PhaseComponentMap
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.WorkflowPhasesMap
import ihsinformatics.com.hydra_mobile.utils.Converters

/**
 * It represents the DB, it holds a connection to the actual SQLite DB.
 */

@Database(
    entities = [AppSetting::class, Patient::class, Person::class, User::class, Location::class, WorkFlow::class, FormResultApiResponse::class,
        Phases::class, Component::class, Forms::class, /*WorkFlowPhasesJoin::class ,*/ /*PhasesComponentJoin::class,*/ ComponentFormJoin::class,
        WorkflowPhasesMap::class, PhaseComponentMap::class, LabTestAllType::class],
    version = 15,
    exportSchema = false
)
@TypeConverters(Converters::class)
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
    abstract fun getFormsResult(): FormsResultDao
    //abstract fun getPhaseComponentJoin(): PhaseComponentJoinDao
    //abstract fun getWorkFlowPhaseJoin(): WorkFlowPhaseJoinDao
    abstract fun getLabTestTypeDao(): LabTestTypeDao
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