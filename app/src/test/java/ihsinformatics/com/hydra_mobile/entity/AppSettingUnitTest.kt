package ihsinformatics.com.hydra_mobile.entity

import ihsinformatics.com.hydra_mobile.data.local.entities.AppSetting
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class AppSettingUnitTest {

    lateinit var appSetting: AppSetting

    @Test
    fun testIpAddress() {
        appSetting = AppSetting("12.15.25.6","20",false)
        assertEquals(appSetting.ip, "12.15.25.6")
    }

    @Test
    fun testPort() {
        appSetting = AppSetting("12.15.25.6","300",false)
        assertEquals(appSetting.port, "300")
    }


    @Test
    fun testEnableSsl() {
        appSetting = AppSetting("12.15.25.6","300",false)
        assertEquals(appSetting.ssl, false)
    }

}