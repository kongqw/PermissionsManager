package com.kongqw.permissionslibrary

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import com.kongqw.permissionslibrary.bean.PermissionsResponseInfo
import org.greenrobot.eventbus.EventBus

class RequestPermissionsActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_REQUEST_CODE = "EXTRA_REQUEST_CODE"
        private const val EXTRA_PERMISSIONS = "EXTRA_PERMISSIONS"
        fun startActivity(context: Application, requestCode: Int, permissions: ArrayList<String>) {
            context.startActivity(Intent(context, RequestPermissionsActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                putExtra(EXTRA_REQUEST_CODE, requestCode)
                putStringArrayListExtra(EXTRA_PERMISSIONS, permissions)
            })
        }

        fun startActivity(activity: Activity, requestCode: Int, permissions: ArrayList<String>) {
            activity.startActivity(Intent(activity, RequestPermissionsActivity::class.java).apply {
                // lags = Intent.FLAG_ACTIVITY_NEW_TASK
                putExtra(EXTRA_REQUEST_CODE, requestCode)
                putStringArrayListExtra(EXTRA_PERMISSIONS, permissions)
            })
        }
    }

    private var mRequestCode: Int? = null
    private var mPermissions: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.statusBarColor = Color.TRANSPARENT
                // 底部控制栏颜色
                window.navigationBarColor = Color.TRANSPARENT
            }
        }
        super.onCreate(savedInstanceState)

        mPermissions = intent.getStringArrayListExtra(EXTRA_PERMISSIONS).apply {
            if (this.isNullOrEmpty()) {
                finish()
                return
            }
        }
        mRequestCode = intent.getIntExtra(EXTRA_REQUEST_CODE, 0)


        checkPermissions(mRequestCode!!, mPermissions!!)
    }

    /**
     * 检查权限
     *
     * @param requestCode 请求码
     * @param permissions 准备校验的权限
     */
    private fun checkPermissions(requestCode: Int, permissions: ArrayList<String>) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android 6.0 动态检查权限
            val lacks = java.util.ArrayList<String>()
            for (permission in permissions) {
                if (ContextCompat.checkSelfPermission(applicationContext, permission) == PackageManager.PERMISSION_DENIED) {
                    lacks.add(permission)
                }
            }

            if (lacks.isNotEmpty()) {
                // 有权限没有授权 申请CAMERA权限
                ActivityCompat.requestPermissions(this, lacks.toTypedArray(), requestCode)
            } else {
                // 授权
                EventBus.getDefault().post(PermissionsResponseInfo().apply {
                    isAuthorized = true
                    this.requestCode = requestCode
                    this.permissions = ArrayList<String>().apply {
                        addAll(permissions.toList())
                    }
                })
                finish()
                return
            }
        } else {
            // 6.0 以下版本不校验权限
            EventBus.getDefault().post(PermissionsResponseInfo().apply {
                isAuthorized = true
                this.requestCode = requestCode
                this.permissions = ArrayList<String>().apply {
                    addAll(permissions.toList())
                }
            })
            finish()
            return
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        recheckPermissions(requestCode, permissions, grantResults)
        finish()
    }

    private fun recheckPermissions(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        for (grantResult in grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                // 未授权
                EventBus.getDefault().post(PermissionsResponseInfo().apply {
                    isAuthorized = false
                    this.requestCode = requestCode
                    this.permissions = ArrayList<String>().apply {
                        addAll(permissions.toList())
                    }
                })
                return
            }
        }
        // 授权
        EventBus.getDefault().post(PermissionsResponseInfo().apply {
            isAuthorized = true
            this.requestCode = requestCode
            this.permissions = ArrayList<String>().apply {
                addAll(permissions.toList())
            }
        })
    }
}
