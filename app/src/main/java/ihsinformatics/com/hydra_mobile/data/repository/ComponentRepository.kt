package ihsinformatics.com.hydra_mobile.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import ihsinformatics.com.hydra_mobile.data.local.AppDatabase
import ihsinformatics.com.hydra_mobile.data.local.dao.workflow.ComponentDao
import ihsinformatics.com.hydra_mobile.data.local.dao.workflow.ComponentFormDao
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.Component
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.ComponentForm
import org.jetbrains.anko.doAsync

class ComponentRepository(application: Application) {

    private var componentDao: ComponentDao
    private var componentFormDao: ComponentFormDao
    private var application: Application

    init {

        val database: AppDatabase = AppDatabase.getInstance(
            application.applicationContext
        )!!
        componentDao = database.getComponent()
        componentFormDao = database.getComponentForm()
        this.application = application
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

    fun getAllComponentForm(): LiveData<List<ComponentForm>> {
        return componentFormDao.getComponentFormList()
    }

}