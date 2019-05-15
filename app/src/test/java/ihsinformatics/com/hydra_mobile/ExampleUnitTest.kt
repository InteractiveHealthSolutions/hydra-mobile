package ihsinformatics.com.hydra_mobile

import ihsinformatics.com.hydra_mobile.view.LoginActivity
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import  androidx.test.rule.ActivityTestRule

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @get:Rule
    val activityRule = ActivityTestRule(LoginActivity::class.java)

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

}
