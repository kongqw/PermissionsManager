package com.kongqw.permissionsdemo;

import android.Manifest;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.kongqw.permissionslibrary.PermissionsManager;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.CAMERA
    };
    static final String[] PERMISSIONS1 = new String[]{
            Manifest.permission.RECORD_AUDIO
    };
    static final String[] PERMISSIONS2 = new String[]{
            Manifest.permission.CAMERA
    };

    // 权限管理器
    private PermissionsManager mPermissionsManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化
        mPermissionsManager = new PermissionsManager(this) {
            @Override
            public void authorized(int requestCode) {
                Toast.makeText(getApplicationContext(), requestCode + " ： 全部权限通过", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void noAuthorization(int requestCode, String[] lacksPermissions) {
                Toast.makeText(getApplicationContext(), requestCode + " ： 有权限没有通过！需要授权", Toast.LENGTH_SHORT).show();
                for (String permission : lacksPermissions) {
                    Log.i(TAG, "noAuthorization: " + permission);
                }
                showMissingPermissionDialog();
            }

            @Override
            public void ignore() {
                // Android 6.0 以下系统不校验
                Toast.makeText(getApplicationContext(), "Android 6.0 以下系统无需动态校验权限！自行检查！", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private static final int REQUEST_CODE = 0;

    public void check(View view) {
        // 要校验的权限
        String[] PERMISSIONS = new String[]{
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA
        };
        // 检查权限
        mPermissionsManager.checkPermissions(0, PERMISSIONS);
    }

    public void check1(View view) {
        // 检查权限
        mPermissionsManager.checkPermissions(1, PERMISSIONS1);
    }

    public void check2(View view) {
        // 检查权限
        mPermissionsManager.checkPermissions(2, PERMISSIONS2);
    }

    public void settings(View view) {
        PermissionsManager.startAppSettings(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // 用户做出选择以后复查权限，判断是否通过了权限申请
        mPermissionsManager.recheckPermissions(requestCode, permissions, grantResults);
    }


    // 显示缺失权限提示
    private void showMissingPermissionDialog() {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("缺少必要权限")
                .setNegativeButton("取消", null)
                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PermissionsManager.startAppSettings(getApplicationContext());
                    }
                })
                .create().show();
    }

}
