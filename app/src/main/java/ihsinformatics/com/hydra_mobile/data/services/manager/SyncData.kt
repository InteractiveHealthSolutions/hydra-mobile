package ihsinformatics.com.hydra_mobile.data.services.manager

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import ihsinformatics.com.hydra_mobile.data.services.worker.DemoWorker
import ihsinformatics.com.hydra_mobile.data.services.worker.WorkflowWorker


object SyncData {

    lateinit var context: Context
    lateinit var workManager:WorkManager
    val workflowTask = OneTimeWorkRequest.Builder(WorkflowWorker::class.java).build()
    val demoTask = OneTimeWorkRequest.Builder(DemoWorker::class.java).build()

    fun instance(context: Context): SyncData {
        this.context = context
        workManager = WorkManager.getInstance(context)
        return this
    }


    fun getWorkManagerInstance():WorkManager
    {
        return workManager
    }

    fun getWorkFlowTaskRequest():OneTimeWorkRequest
    {
        return workflowTask
    }

    fun getDemoTaskRequest():OneTimeWorkRequest
    {
        return demoTask
    }
}