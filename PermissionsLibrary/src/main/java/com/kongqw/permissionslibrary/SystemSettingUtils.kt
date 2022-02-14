package com.kongqw.permissionslibrary

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle

@MainThread
class SystemSettingUtils(private val activity: FragmentActivity?) {

    constructor(fragment: Fragment) : this(fragment.activity)

    private var isLocationEnabledListener: ((isEnabled: Boolean) -> Unit)? = null

    private val isNotStarted = (false == activity?.lifecycle?.currentState?.isAtLeast(Lifecycle.State.STARTED))

    // 开启定位权限的返回监听
    private val mRequestLocationEnabledRegisterForActivityResult = if (isNotStarted) {
        activity?.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            isLocationEnabledListener?.invoke(isLocationEnabled(activity.applicationContext))
        }
    } else null

    /**
     * 检查定位功能是否开启
     */
    fun checkLocationEnabled(listener: (isEnabled: Boolean) -> Unit) {
        if (isLocationEnabled(activity)) {
            listener(true)
            return
        }
        isLocationEnabledListener = listener
        mRequestLocationEnabledRegisterForActivityResult?.launch(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
    }

    /**
     * 是否开启了定位功能
     */
    fun isLocationEnabled(context: Context?): Boolean {
        return try {
            val locationMode = Settings.Secure.getInt(context?.contentResolver, Settings.Secure.LOCATION_MODE)
            locationMode != Settings.Secure.LOCATION_MODE_OFF
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}