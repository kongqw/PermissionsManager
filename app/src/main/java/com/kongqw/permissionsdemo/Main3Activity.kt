package com.kongqw.permissionsdemo

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.kongqw.permissionslibrary.XPermissionsManager
import com.kongqw.permissionslibrary.listener.OnRequestPermissionsListener
import kotlinx.android.synthetic.main.activity_main3.*

class Main3Activity : AppCompatActivity(), OnRequestPermissionsListener {
    override fun onPermissionsAuthorized(requestCode: Int, permissions: ArrayList<String>) {
        Toast.makeText(applicationContext, "权限通过 : requestCode = $requestCode", Toast.LENGTH_SHORT).show()
    }

    override fun onPermissionsNoAuthorization(requestCode: Int, lacksPermissions: ArrayList<String>) {
        Toast.makeText(applicationContext, "权限拒绝 : requestCode = $requestCode", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        btn_1.setOnClickListener {
            val p1 = ArrayList<String>().apply {
                add(Manifest.permission.MODIFY_AUDIO_SETTINGS)
                add(Manifest.permission.CAMERA)
            }
            XPermissionsManager.checkPermissions(0, p1, this)
        }
        btn_2.setOnClickListener {
            val p2 = ArrayList<String>().apply {
                add(Manifest.permission.RECORD_AUDIO)
            }
            XPermissionsManager.checkPermissions(1, p2, this)
        }
        btn_3.setOnClickListener {
            val p3 = ArrayList<String>().apply {
                add(Manifest.permission.ACCESS_FINE_LOCATION)
                add(Manifest.permission.ACCESS_COARSE_LOCATION)
            }
            XPermissionsManager.checkPermissions(this, 2, p3, this)
        }
    }

}
