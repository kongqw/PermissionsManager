package com.kongqw.permissionslibrary.bean

class PermissionsResponseInfo {
    var isAuthorized: Boolean = false
    var requestCode: Int = 0
    var permissions: ArrayList<String> = ArrayList()
}