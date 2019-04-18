package com.kongqw.permissionslibrary.listener

import java.io.Serializable

interface OnRequestPermissionsListener : Serializable {

    fun onPermissionsAuthorized(requestCode: Int, permissions: ArrayList<String>)

    fun onPermissionsNoAuthorization(requestCode: Int, lacksPermissions: ArrayList<String>)

}