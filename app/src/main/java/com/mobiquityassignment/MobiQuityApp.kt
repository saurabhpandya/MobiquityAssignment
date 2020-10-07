package com.mobiquityassignment

import android.app.Application
import android.content.Context
import com.mobiquityassignment.utility.MobiQuityPreference
import java.lang.ref.WeakReference

class MobiQuityApp : Application(){
    private val TAG = this::class.java.canonicalName

    override fun onCreate() {
        super.onCreate()
        wApp!!.clear()
        wApp = WeakReference(this@MobiQuityApp)
        MobiQuityPreference.init(this)
    }

    companion object {
        private var wApp: WeakReference<MobiQuityApp>? = WeakReference<MobiQuityApp>(null)!!
        val instance: MobiQuityApp get() = wApp?.get()!!

        val context: Context
            get() {
                val app = if (null != wApp) wApp!!.get() else MobiQuityApp()
                return if (app != null) app.applicationContext else MobiQuityApp().applicationContext
            }

//        var cacheSize = 2 * 1024 * 1024.toLong() // size of each cache file.

//        var exoCreator: ExoCreator? = null
    }

}