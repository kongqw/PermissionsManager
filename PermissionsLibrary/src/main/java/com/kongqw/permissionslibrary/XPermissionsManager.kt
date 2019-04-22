package com.kongqw.permissionslibrary

import android.app.Activity
import android.app.Application
import android.util.Log
import com.kongqw.permissionslibrary.bean.PermissionsResponseInfo
import com.kongqw.permissionslibrary.listener.OnRequestPermissionsListener
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

object XPermissionsManager {

    private var mApplication: Application? = null
    private var mOnRequestPermissionsListener: OnRequestPermissionsListener? = null

    /**
     * 初始化
     */
    fun init(application: Application) {
        mApplication = application
    }

    /**
     * 释放资源
     */
    fun release() {
        mApplication = null
        mOnRequestPermissionsListener = null
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    /**
     * 添加权限监听
     */
    fun addOnRequestPermissionsListener(listener: OnRequestPermissionsListener) {
        mOnRequestPermissionsListener = listener
    }

    /**
     * 移除权限监听
     */
    fun removeOnRequestPermissionsListener() {
        mOnRequestPermissionsListener = null
    }

    fun checkPermissions(requestCode: Int, permissions: ArrayList<String>) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        mApplication?.apply {
            RequestPermissionsActivity.startActivity(this, requestCode, permissions)
        }
    }

    fun checkPermissions(activity: Activity, requestCode: Int, permissions: ArrayList<String>) {
        RequestPermissionsActivity.startActivity(activity, requestCode, permissions)
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(permissionsResponseInfo: PermissionsResponseInfo?) {
        Log.i("kongqw666","permissionsResponseInfo = $permissionsResponseInfo")
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