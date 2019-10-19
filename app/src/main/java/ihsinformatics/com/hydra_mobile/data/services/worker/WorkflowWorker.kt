package ihsinformatics.com.hydra_mobile.data.services.worker

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import ihsinformatics.com.hydra_mobile.data.repository.WorkflowPhasesRepository
import ihsinformatics.com.hydra_mobile.utils.SessionManager

class WorkflowWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    val context = context
    lateinit var workflowPhasesRepository: WorkflowPhasesRepository

    override fun doWork(): Result = try {

        workflowPhasesRepository = WorkflowPhasesRepository(context)
        //workflowPhasesRepository.deleteAllWorkflowPhases()
        workflowPhasesRepository.getRemoteWorkflowData()

        Log.e(LOG_TAG, "Work Complete")

        Result.success()
    } catch (e: Throwable) {
        Log.e(LOG_TAG, "Error executing work: " + e.message, e)
        Result.failure()
    }

    companion object {
        private const val LOG_TAG = "WorkflowWorker"
    }
}