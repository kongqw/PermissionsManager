package com.kongqw.permissionsdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.kongqw.permissionsdemo.databinding.ActivitySystemSettingBinding
import com.kongqw.permissionslibrary.SystemSettingUtils

class SystemSettingActivity : AppCompatActivity() {

    private lateinit var mViewBinding: ActivitySystemSettingBinding

    private val mSystemSettingUtils = SystemSettingUtils(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding = ActivitySystemSettingBinding.inflate(layoutInflater)
        setContentView(mViewBinding.root)

        mViewBinding.btnCheckLocation.setOnClickListener {
            mSystemSettingUtils.checkLocationEnabled {
                Toast.makeText(applicationContext, "定位权限 = $it", Toast.LENGTH_SHORT).show()
            }
        }
    }
}