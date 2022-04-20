package com.kongqw.permissionslibrary

import android.app.Activity
import android.app.Application
import com.kongqw.permissionslibrary.listener.OnRequestPermissionsListener

object XPermissionsManager {

    private var mApplication: Application? = null
    var mOnRequestPermissionsListener: OnRequestPermissionsListener? = null

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
    }

    fun checkPermissions(requestCode: Int, permissions: ArrayList<String>, listener: OnRequestPermissionsListener) {
        mApplication?.apply {
            mOnRequestPermissionsListener = listener
            RequestPermissionsActivity.startActivity(this, requestCode, permissions)
        }
    }

    fun checkPermissions(requestCode: Int, listener: OnRequestPermissionsListener, vararg permissions: String) {
        mApplication?.apply {
            mOnRequestPermissionsListener = listener
            RequestPermissionsActivity.startActivity(this, requestCode, ArrayList(permissions.toList()))
        }
    }

    fun checkPermissions(requestCode: Int, permissions: Array<String>, listener: OnRequestPermissionsListener) {
        mApplication?.apply {
            mOnRequestPermissionsListener = listener
            permissions.toList()
            RequestPermissionsActivity.startActivity(this, requestCode, ArrayList(permissions.toList()))
        }
    }

    fun checkPermissions(activity: Activity, requestCode: Int, permissions: ArrayList<String>, listener: OnRequestPermissionsListener) {
        mOnRequestPermissionsListener = listener
        RequestPermissionsActivity.startActivity(activity, requestCode, permissions)
    }

    fun checkPermissions(activity: Activity, requestCode: Int, listener: OnRequestPermissionsListener, vararg permissions: String) {
        mOnRequestPermissionsListener = listener
        RequestPermissionsActivity.startActivity(activity, requestCode, ArrayList(permissions.toList()))
    }

    fun checkPermissions(activity: Activity, requestCode: Int, permissions: Array<String>, listener: OnRequestPermissionsListener) {
        mOnRequestPermissionsListener = listener
        RequestPermissionsActivity.startActivity(activity, requestCode, ArrayList(permissions.toList()))
    }
}