package ihsinformatics.com.hydra_mobile.utils

import android.content.Context
import android.content.Intent
import ihsinformatics.com.hydra_mobile.ui.activity.LoginActivity


import android.content.SharedPreferences
import io.reactivex.internal.operators.flowable.FlowableThrottleFirstTimed


class SessionManager(context: Context) {


    // Shared Preferences
    lateinit var pref: SharedPreferences

    // Editor for Shared preferences
    lateinit var editor: SharedPreferences.Editor

    // Context
    lateinit var _context: Context

    // Shared pref mode
    var PRIVATE_MODE = 0
    private val PREF_NAME = "AainaPref"

    private val IS_LOGIN = "IsLoggedIn"
    private val IS_FIRST_TIME = "IsFirstTime"

    val KEY_NAME = "productName"
    val KEY_PASS = "password"
    val KEY_PROVIDER = "provider"

    init {
        this._context = context
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }

    /**
     * Create login session
     */
    fun createLoginSession(name: String, password: String,provider:String) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true)

        // Storing name in pref
        editor.putString(KEY_NAME, name)

        // Storing email in pref
        editor.putString(KEY_PASS, password)
        editor.putString(KEY_PROVIDER, provider)


        // commit changes
        editor.commit()
    }

    fun checkFirstTimeInstall(firstTimed: Boolean) {
        editor.putBoolean(IS_FIRST_TIME, firstTimed)
        editor.commit()
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    fun checkLogin() {
        // Check login status
        if (!isLoggedIn()) {
            // user is not logged in redirect him to Login Activity
            val i = Intent(_context, LoginActivity::class.java)
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            // Add new Flag to start new Activity
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK

            // Staring Login Activity
            _context.startActivity(i)
        }

    }

    fun getUsername(): String {
        return pref.getString(KEY_NAME, "")
    }

    fun getPassword(): String {
        return pref.getString(KEY_PASS, "")
    }

    fun getProviderUUID():String{
        return  pref.getString(KEY_PROVIDER,"")
    }


    /**
     * Get stored session data
     */
    fun getUserDetails(): HashMap<String, String> {
        val user = HashMap<String, String>()
        // user name
        user[KEY_NAME] = pref.getString(KEY_NAME, null)

        // user email id
        user[KEY_PASS] = pref.getString(KEY_PASS, null)

        // return user
        return user
    }

    /**
     * Clear session details
     */
    fun logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear()
        editor.commit()

        // After logout redirect user to Loing Activity
        val i = Intent(_context, LoginActivity::class.java)
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        // Add new Flag to start new Activity
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        // Staring Login Activity
        _context.startActivity(i)
    }

    /**
     * Quick check for login
     */
    // Get Login State
    fun isLoggedIn(): Boolean {
        return pref.getBoolean(IS_LOGIN, false)
    }

    fun isFirstTime(): Boolean {
        return pref.getBoolean(IS_FIRST_TIME, true)
    }

}