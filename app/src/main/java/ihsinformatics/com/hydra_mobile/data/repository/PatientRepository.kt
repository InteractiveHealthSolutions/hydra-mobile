package ihsinformatics.com.hydra_mobile.data.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import ihsinformatics.com.hydra_mobile.common.Constant
import ihsinformatics.com.hydra_mobile.data.local.AppDatabase
import ihsinformatics.com.hydra_mobile.data.local.dao.PatientDao
import ihsinformatics.com.hydra_mobile.data.local.entities.Patient
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.Component
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.ComponentForm
import ihsinformatics.com.hydra_mobile.data.remote.manager.RequestManager
import ihsinformatics.com.hydra_mobile.data.remote.model.RESTCallback
import ihsinformatics.com.hydra_mobile.data.remote.model.patient.PatientApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.model.patient.Result
import ihsinformatics.com.hydra_mobile.utils.SessionManager
import org.jetbrains.anko.doAsync
import timber.log.Timber


class PatientRepository(context: Context) {

    private var context: Context
    private val sessionManager = SessionManager(context)
    private val patientDao: PatientDao

    init {
        val database: AppDatabase = AppDatabase.getInstance(
            context.applicationContext
        )!!
        this.context = context
        patientDao = database.getPatientDao()

    }

    fun searchPatientByIdentifier(identifier: String) {
        RequestManager(
            context, sessionManager.getUsername(),
            sessionManager.getPassword()
        ).searchPatient(Constant.REPRESENTATION, identifier,
            object :
                RESTCallback {
                override fun <T> onSuccess(o: T) {
                    try {
                         val response = (o as PatientApiResponse)
                         getPatient(response.patientList)
                    } catch (e: Exception) {
                        Timber.e(e.localizedMessage)
                    }
                }

                override fun onFailure(t: Throwable) {
                    Timber.e(t.localizedMessage)
                }
            })

    }

    fun getPatient(patients: List<Result>): Patient {

        var patient: Patient = Patient()

        for (i in patients.indices) {

            var externalId = ""
            var identifier = ""
            var enrs = ""

            for (j in patients[i].identifiers.indices) {
                val keyIdentity = patients[i].identifiers[j].display.split("=")
                val key = keyIdentity[0]
                val value = keyIdentity[1]

                when (key) {
                    "External ID" -> externalId = value
                    "Patient ID" -> identifier = value
                    "ENRS" -> enrs = enrs
                }
            }

            patient = Patient(
                givenName = patients[i].display,
                uuid = patients[i].uuid,
                externalId = externalId,
                identifier = identifier,
                enrs = enrs,
                age = patients[i].person.age,
                gender = patients[i].person.gender,
                voided = patients[i].voided
            )
        }

        return patient
    }

    fun insertPatient(patient: Patient) {
        doAsync {
            patientDao.insertPatient(patient)
        }
    }

    fun updatePatient(patient: Patient) {
        doAsync {
            patientDao.updatePatient(patient)
        }
    }

    fun getAllPatient(): LiveData<List<Patient>> {
        return patientDao.getAllPatient()
    }

}