package com.kongqw.permissionslibrary

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.view.View

class RequestPermissionsActivity : AppCompatActivity() {

    companion object {
        private val TAG = RequestPermissionsActivity::class.java.simpleName
        private const val EXTRA_REQUEST_CODE = "EXTRA_REQUEST_CODE"
        private const val EXTRA_PERMISSIONS = "EXTRA_PERMISSIONS"


        fun startActivity(context: Application, requestCode: Int, permissions: ArrayList<String>) {
            context.startActivity(Intent(context, RequestPermissionsActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                putExtra(EXTRA_REQUEST_CODE, requestCode)
                putStringArrayListExtra(EXTRA_PERMISSIONS, permissions)
            })
        }

//        fun startActivity2(context: Application, requestCode: Int, vararg permissions: String) {
//            context.startActivity(Intent(context, RequestPermissionsActivity::class.java).apply {
//                flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                putExtra(EXTRA_REQUEST_CODE, requestCode)
//                val list = ArrayList<String>(permissions.toList())
//                putStringArrayListExtra(EXTRA_PERMISSIONS, list)
//            })
//        }

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

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.TRANSPARENT
            // 底部控制栏颜色
            window.navigationBarColor = Color.TRANSPARENT
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
            val lacks = ArrayList<String>()
            for (permission in permissions) {
                if (ContextCompat.checkSelfPermission(applicationContext, permission) == PackageManager.PERMISSION_DENIED) {
                    lacks.add(permission)
                }
            }

            if (lacks.isNotEmpty()) {
                // 有权限没有授权 申请CAMERA权限
                ActivityCompat.requestPermissions(this, lacks.toTypedArray(), requestCode)
            } else {
                // 已全部授权
                XPermissionsManager.mOnRequestPermissionsListener?.onPermissionsAuthorized(requestCode, ArrayList<String>().apply {
                    addAll(permissions.toList())
                })
                XPermissionsManager.mOnRequestPermissionsListener = null
                finish()
                return
            }
        } else {
            // 6.0 以下版本不校验权限
            XPermissionsManager.mOnRequestPermissionsListener?.onPermissionsAuthorized(requestCode, ArrayList<String>().apply {
                addAll(permissions.toList())
            })
            XPermissionsManager.mOnRequestPermissionsListener = null
            finish()
            return
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.i(TAG, "onRequestPermissionsResult($requestCode, $permissions, $grantResults)")
        recheckPermissions(requestCode, permissions, grantResults)
        XPermissionsManager.mOnRequestPermissionsListener = null
        finish()
    }

    private fun recheckPermissions(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        for (grantResult in grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {

                val deniedWithIgnorePermissions = ArrayList<String>()
                permissions.forEach { permission ->
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                        // 已经设置过不再提醒
                        deniedWithIgnorePermissions.add(permission)
                    }
                }

                if (deniedWithIgnorePermissions.isNullOrEmpty()) {
                    // 未授权
                    XPermissionsManager.mOnRequestPermissionsListener?.onPermissionsNoAuthorization(requestCode, ArrayList<String>().apply {
                        addAll(permissions.toList())
                    })
                } else {
                    // 未授权
                    XPermissionsManager.mOnRequestPermissionsListener?.onPermissionsDeniedWithIgnore(requestCode, deniedWithIgnorePermissions)
                }
                return
            }
        }
        // 授权
        XPermissionsManager.mOnRequestPermissionsListener?.onPermissionsAuthorized(requestCode, ArrayList<String>().apply {
            addAll(permissions.toList())
        })
    }
}
