package com.kongqw.permissionslibrary.listener

import java.io.Serializable

interface OnRequestPermissionsListener : Serializable {

    /**
     * 权限通过
     */
    fun onPermissionsAuthorized(requestCode: Int, permissions: ArrayList<String>)

    /**
     * 权限被拒绝
     */
    fun onPermissionsNoAuthorization(requestCode: Int, lacksPermissions: ArrayList<String>)

    /**
     * 权限被拒绝并且不再提示
     */
    fun onPermissionsDeniedWithIgnore(requestCode: Int, lacksPermissions: ArrayList<String>)

}