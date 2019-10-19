package ihsinformatics.com.hydra_mobile.common

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*


@RunWith(JUnit4::class)
class AppConstantUnitTest {

    @Test
    fun testTimeFormater() {
        val currentDate = Constant.Companion.DATE_FORMAT.format(Date()) //Constant.TIME_FORMAT.format(Date())
        Assert.assertNotNull(currentDate)
    }

}