package com.ecommerce.albeli

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.ecommerce.albeli.authentication.di.authenticationModule
import com.ecommerce.albeli.common.api.commonModule
import com.ecommerce.albeli.common.callback.LifeCycleDelegate
import com.ecommerce.albeli.common.utils.AppConstants
import com.ecommerce.albeli.common.utils.AppLifecycleHandler
import com.ecommerce.albeli.dashboard.di.dashboardModule
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication : Application(), LifeCycleDelegate {
    companion object {
        lateinit var context: MyApplication private set
        lateinit var sharedPreferencesEditor: SharedPreferences.Editor
        lateinit var sharedPreferences: SharedPreferences
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        provideSharedPreference()
        FirebaseApp.initializeApp(context)
        startKoin {
            FirebaseApp.initializeApp(context)
            androidContext(this@MyApplication)
            androidLogger(Level.NONE)
            modules(
                listOf(
                    authenticationModule,
                    dashboardModule,
                    commonModule,
                )
            )
        }

        when (MyApplication().preferenceGetInteger(
            AppConstants.SharedPrefKey.THEME_MODE,
            0
        )) {
            0 -> AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO
            )
            1 -> AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_YES
            )
        }

        val lifeCycleHandler = AppLifecycleHandler(this)
        registerLifecycleHandler(lifeCycleHandler)
    }

    private fun provideSharedPreference() {
        sharedPreferences = this@MyApplication.getSharedPreferences(
            this@MyApplication.packageName + "Preferences_",
            Context.MODE_PRIVATE
        )
        sharedPreferencesEditor = sharedPreferences.edit()
    }

    fun preferencePutInteger(key: String, value: Int) {
        sharedPreferencesEditor.putInt(key, value).apply()
    }

    fun preferenceGetInteger(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    fun preferencePutString(key: String, value: String) {
        sharedPreferencesEditor.putString(key, value).apply()
    }

    fun preferenceGetString(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue)!!
    }

    fun preferencePutBoolean(key: String, value: Boolean) {
        sharedPreferencesEditor.putBoolean(key, value).apply()
    }

    fun preferenceGetBoolean(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    fun preferenceRemoveKey(key: String?) {
        sharedPreferencesEditor.remove(key).apply()
    }

    fun provideGson(): Gson {
        return GsonBuilder().serializeNulls().create()
    }

    fun getContext(): Context {
        return context
    }

    fun clearData() {
        preferenceRemoveKey(AppConstants.SharedPrefKey.USER_INFO)
        preferenceRemoveKey(AppConstants.SharedPrefKey.COMPARE_PRODUCT_IDS)
        FirebaseAuth.getInstance().signOut();
    }

    override fun onAppBackgrounded() {
        val mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser
//        if (user != null)
//            FirebaseUtils.setOnlineStatus(MyApplication().getContext(), false)
    }

    override fun onAppForegrounded() {
        val mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser
//        if (user != null)
//            FirebaseUtils.setOnlineStatus(MyApplication().getContext(), true)
    }

    private fun registerLifecycleHandler(lifeCycleHandler: AppLifecycleHandler) {
        registerActivityLifecycleCallbacks(lifeCycleHandler)
        registerComponentCallbacks(lifeCycleHandler)
    }
}