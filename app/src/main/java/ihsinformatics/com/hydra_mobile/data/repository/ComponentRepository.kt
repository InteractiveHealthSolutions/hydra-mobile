package ihsinformatics.com.hydra_mobile.data.repository

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import ihsinformatics.com.hydra_mobile.data.local.AppDatabase
import ihsinformatics.com.hydra_mobile.data.local.dao.workflow.ComponentDao
import ihsinformatics.com.hydra_mobile.data.local.dao.workflow.ComponentFormDao
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.Component
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.ComponentForm
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.jetbrains.anko.doAsync

class ComponentRepository(context: Context) {

    private var componentDao: ComponentDao
    private var componentFormDao: ComponentFormDao
    private var context: Context

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

}