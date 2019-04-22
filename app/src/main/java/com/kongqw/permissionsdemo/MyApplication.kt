package com.kongqw.permissionsdemo

import android.app.Application
import com.kongqw.permissionslibrary.XPermissionsManager
import com.squareup.leakcanary.LeakCanary

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        LeakCanary.install(this)

        XPermissionsManager.init(this)
    }

}