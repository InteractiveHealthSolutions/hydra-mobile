package ihsinformatics.com.hydra_mobile.data.services.manager

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import ihsinformatics.com.hydra_mobile.data.services.worker.WorkflowWorker


object SyncData {

    lateinit var context: Context

    fun instance(context: Context): SyncData {
        this.context = context
        return this
    }

    fun initWorkManage(){

        val workflowTask = OneTimeWorkRequest.Builder(WorkflowWorker::class.java).build()

        val workManager = WorkManager.getInstance(context)

        workManager.beginWith(workflowTask).enqueue()

    }

}