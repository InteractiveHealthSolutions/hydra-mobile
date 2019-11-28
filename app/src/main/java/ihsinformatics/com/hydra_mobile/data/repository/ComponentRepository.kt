package ihsinformatics.com.hydra_mobile.data.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import ihsinformatics.com.hydra_mobile.common.Constant
import ihsinformatics.com.hydra_mobile.data.local.AppDatabase
import ihsinformatics.com.hydra_mobile.data.local.dao.workflow.ComponentDao
import ihsinformatics.com.hydra_mobile.data.local.dao.workflow.ComponentFormDao
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.Component
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.ComponentForm
import ihsinformatics.com.hydra_mobile.data.remote.manager.RequestManager
import ihsinformatics.com.hydra_mobile.data.remote.model.RESTCallback
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.ComponentApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.PhaseApiResponse
import ihsinformatics.com.hydra_mobile.utils.SessionManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.jetbrains.anko.doAsync

class ComponentRepository(context: Context) {

    private var componentDao: ComponentDao
    private var componentFormDao: ComponentFormDao
    private var context: Context
    private val sessionManager = SessionManager(context)

    init {

        val database: AppDatabase = AppDatabase.getInstance(
            context.applicationContext
        )!!
        componentDao = database.getComponent()
        componentFormDao = database.getComponentForm()
        this.context = context
    }


    fun insertComponent(component: Component) {
        doAsync {
            componentDao.insertComponent(component)
        }
    }

    fun updateComponent(component: Component) {
        doAsync {
            componentDao.updateComponent(component)
        }
    }

    fun getAllComponent(): LiveData<List<Component>> {
        return componentDao.getAllComponent()
    }

    suspend fun getAllComponentForm(): List<ComponentForm> {
        var formList = GlobalScope.async {
            componentFormDao.getComponentFormList()
        }
        return  formList.await()
    }


    fun getRemoteComponentData(){
        RequestManager(
            context, sessionManager.getUsername(),
            sessionManager.getPassword()
        ).getComponents(
            Constant.REPRESENTATION,
            object :
                RESTCallback {
                override fun <T> onSuccess(o: T) {

                    try {
                        val response = (o as ComponentApiResponse)
                        for (i in response.component.indices) {
                            //insert into local database
                            insertComponent(response.component[i])
                        }
                        Log.e("ComponentLoading", "completed")
                    } catch (e: Exception) {

                    }
                }

                override fun onFailure(t: Throwable) {

                }
            })
    }


}