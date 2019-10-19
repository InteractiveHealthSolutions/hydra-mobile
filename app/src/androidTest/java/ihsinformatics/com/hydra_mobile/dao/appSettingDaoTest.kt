package ihsinformatics.com.hydra_mobile.dao

import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import ihsinformatics.com.hydra_mobile.data.local.AppDatabase
import ihsinformatics.com.hydra_mobile.data.local.entities.AppSetting
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class appSettingDaoTest {

    lateinit var appDatabase: AppDatabase
    lateinit var ipAddress: String
    lateinit var portNumber: String
    var enableSsl: Boolean = false
    @Before
    fun init() {
        appDatabase =
            Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), AppDatabase::class.java).build()
        ipAddress = "mrs.ghd.ihn.org.pk";
        portNumber = "445"
        enableSsl = true
    }

    @After
    fun closeDbConnection() {
        appDatabase.close()
    }

    @Test
    fun saveAppSettingTest() {
        val appSetting = AppSetting(ipAddress, portNumber, enableSsl)
        val appSettingDao = appDatabase.appSettingDao()
        appSettingDao.insertSetting(appSetting)
        //get the list
        val appSettingList: List<AppSetting> = appDatabase.appSettingDao().getAllSetting()
        assertNotNull(appSettingList)
    }

    @Test
    fun updateAppSettingTest() {
        ipAddress = "192.168.172.5"
        val appSetting = AppSetting(ipAddress, portNumber, enableSsl)
        val appSettingDao = appDatabase.appSettingDao()
        appSettingDao.updateSetting(appSetting)
        //get the list
        val appSettingList: List<AppSetting> = appDatabase.appSettingDao().getAllSetting()
        assertEquals(appSettingList.get(0).ip ,ipAddress)
    }


}