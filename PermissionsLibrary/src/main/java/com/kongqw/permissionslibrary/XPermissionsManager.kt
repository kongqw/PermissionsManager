package com.kongqw.permissionslibrary

import android.app.Activity
import android.app.Application
import com.kongqw.permissionslibrary.bean.PermissionsResponseInfo
import com.kongqw.permissionslibrary.listener.OnRequestPermissionsListener
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

object XPermissionsManager {

    private var mApplication: Application? = null

    fun init(application: Application) {
        mApplication = application
    }

    private var mOnRequestPermissionsListener: OnRequestPermissionsListener? = null

    fun addOnRequestPermissionsListener(listener: OnRequestPermissionsListener) {
        EventBus.getDefault().register(this)
        mOnRequestPermissionsListener = listener
    }

    fun removeOnRequestPermissionsListener() {
        EventBus.getDefault().unregister(this)
        mOnRequestPermissionsListener = null
    }

    fun checkPermissions(requestCode: Int, permissions: ArrayList<String>) {
        mApplication?.apply {
            RequestPermissionsActivity.startActivity(this, requestCode, permissions)
        }
    }

    fun checkPermissions(activity: Activity, requestCode: Int, permissions: ArrayList<String>) {
        RequestPermissionsActivity.startActivity(activity, requestCode, permissions)
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(permissionsResponseInfo: PermissionsResponseInfo?) {
        if (null == permissionsResponseInfo) {
            return
        }
        if (permissionsResponseInfo.isAuthorized) {
            // 授权通过
            mOnRequestPermissionsListener?.onPermissionsAuthorized(permissionsResponseInfo.requestCode, permissionsResponseInfo.permissions)
        } else {
            // 未授权
            mOnRequestPermissionsListener?.onPermissionsNoAuthorization(permissionsResponseInfo.requestCode, permissionsResponseInfo.permissions)
        }
    }
}