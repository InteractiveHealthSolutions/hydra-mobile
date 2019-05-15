package ihsinformatics.com.hydra_mobile

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import ihsinformatics.com.hydra_mobile.view.LoginActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class LoginInstrumentedTest {

    @Rule
    @JvmField
    val rule:ActivityTestRule<LoginActivity> = ActivityTestRule(LoginActivity::class.java);

    @Test
    fun  testUsername(){
        Espresso.onView(ViewMatchers.withId(R.id.edt_username))
            .check(ViewAssertions.matches(ViewMatchers.withHint("Enter Username")));
        // onView(withId(R.id.edt_username)).perform(typeText("shujaat.ali"), closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.edt_username))
            .perform(ViewActions.typeText("shujaat.ali"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.edt_username))
            .check(ViewAssertions.matches(ViewMatchers.withText("shujaat.ali")));

    }
    @Test
    fun  testPassword(){
        Espresso.onView(ViewMatchers.withId(R.id.edt_password)).perform(ViewActions.typeText("shujaat123"))
    }

    @Test
    fun testButtonClick(){
        //onView(withId(R.id.btn_login)).perform(typeText("LOGIN"), closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withText("LOGIN")).perform(ViewActions.click())

        //check the hint visibility  after  the text is cleared
        // onView(withId(R.id.edt_username)).check(matches(withHint("Enter Username")));
    }

    @Test
    fun testSettingImageClick(){
        //onView(withId(R.id.btn_login)).perform(typeText("LOGIN"), closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.img_setting)).perform(ViewActions.click())

        //check the hint visibility  after  the text is cleared
        // onView(withId(R.id.edt_username)).check(matches(withHint("Enter Username")));
    }

}